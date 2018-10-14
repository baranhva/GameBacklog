package com.example.blackhorse.gamebacklog;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;

    private static final int EDIT_REQUEST_CODE = 1337;
    private static final int ADD_REQUEST_CODE = 7331;

    public static final String EXTRA_GAME = "GAME";

    private static MyDatabase sDatabase;

    private List<Game> mGames = new ArrayList<>();
    private GameAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sDatabase = MyDatabase.getInstance(this);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        mAdapter = new GameAdapter(mGames, new GameAdapter.GameClickListener() {
            @Override
            public void gameOnClick(int i) {
                Intent intent = new Intent(MainActivity.this, GameForm.class);
                intent.putExtra(EXTRA_GAME, mGames.get(i));
                startActivityForResult(intent,EDIT_REQUEST_CODE);
            }
        }, getResources());

        recyclerView.setAdapter(mAdapter);

        new GameAsyncTask(TASK_GET_ALL_GAMES).execute();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        Game game = mGames.get(position);
                        new GameAsyncTask(TASK_DELETE_GAME).execute(game);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,GameForm.class),ADD_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Game game = data.getParcelableExtra(MainActivity.EXTRA_GAME);
                new GameAsyncTask(TASK_INSERT_GAME).execute(game);
            }
        }else if(requestCode == EDIT_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                Game game = data.getParcelableExtra(MainActivity.EXTRA_GAME);
                new GameAsyncTask(TASK_UPDATE_GAME).execute(game);
            }
        }
    }

    public void onGameDbUpdated(List list) {
        mGames = list;
        updateUI();
    }

    public void updateUI(){
        mAdapter.swapList(mGames);
    }

    public class GameAsyncTask extends AsyncTask<Game, Void, List> {
        private int taskCode;
        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode){
                case TASK_DELETE_GAME:
                    sDatabase.GameDAO().deleteGames(games[0]);
                    break;
                case TASK_UPDATE_GAME:
                    sDatabase.GameDAO().updateGames(games[0]);
                    break;
                case TASK_INSERT_GAME:
                    sDatabase.GameDAO().insertGames(games[0]);
                    break;
            }
            //To return a new list with the updated data, we get all the data from the database again.
            return sDatabase.GameDAO().getAllGames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }
    }
}
