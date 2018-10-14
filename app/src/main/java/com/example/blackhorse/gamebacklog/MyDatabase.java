package com.example.blackhorse.gamebacklog;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Game.class},version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract GameDAO GameDAO();
    private final static String NAME_DATABASE = "game_db";
    private static MyDatabase sInstance;

    public static MyDatabase getInstance(Context context){
        if(sInstance == null) {
            sInstance = Room.databaseBuilder(context,
                    MyDatabase.class, NAME_DATABASE).build(); }
        return sInstance;
    }
}
