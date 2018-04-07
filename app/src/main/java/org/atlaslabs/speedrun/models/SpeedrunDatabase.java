package org.atlaslabs.speedrun.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Category.class, Game.class, Platform.class, Run.class, System.class, User.class, Videos.class}, version = 1)
public abstract class SpeedrunDatabase extends RoomDatabase {
    private static SpeedrunDatabase INSTANCE;

    public synchronized static SpeedrunDatabase getDatabase(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    SpeedrunDatabase.class, "speedrun_db").build();
        return INSTANCE;
    }

    public abstract SpeedrunDAO itemAndPersonModel();
}
