package com.example.blackhorse.gamebacklog;


import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mGames;
    private final GameClickListener mGameClickListener;
    private final Resources mResources;

    public GameAdapter(List<Game> mGames, GameClickListener mGameClickListener, Resources mResources) {
        this.mGames = mGames;
        this.mGameClickListener = mGameClickListener;
        this.mResources = mResources;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.game_cardview, viewGroup, false); // KAN MISSCHIEN ANDERS
        // Return a new holder instance
//        GameAdapter.ViewHolder viewHolder = new GameAdapter.ViewHolder(view);
//        return viewHolder;
        return new ViewHolder(view, mGameClickListener);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Game game = mGames.get(i);

        viewHolder.titleTextView.setText(game.getTitle());
        viewHolder.platformTextView.setText(game.getPlatform());
        viewHolder.dateTextView.setText(game.getDate());
        viewHolder.statusTextView.setText(mResources.getStringArray(R.array.status_list)[game.getStatus()]);
    }

    public int getItemCount() {
        return mGames.size();
    }

    public void swapList(List<Game> newList) {
        mGames = newList;
        if (newList != null) {
            this.notifyDataSetChanged();
        }
    }

    public interface GameClickListener {
        void gameOnClick(int i);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView titleTextView;
        public final TextView platformTextView;
        public final TextView statusTextView;
        public final TextView dateTextView;
        public final GameAdapter.GameClickListener gameClickListener;

        public ViewHolder(View itemView, GameAdapter.GameClickListener mPortalClickListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleText);
            platformTextView = itemView.findViewById(R.id.platformText);
            statusTextView = itemView.findViewById(R.id.statusText);
            dateTextView = itemView.findViewById(R.id.dateText);
            this.gameClickListener = mPortalClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            gameClickListener.gameOnClick(clickedPosition);
        }
    }

}


