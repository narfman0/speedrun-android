package org.atlaslabs.speedrun.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SpeedrunDAO {
    @Insert(onConflict = REPLACE)
    void addGame(Game game);

    @Insert(onConflict = REPLACE)
    void addRun(Run run);

    @Insert(onConflict = REPLACE)
    void addUser(User user);

    @Query("select * from Run order by contact_id")
    LiveData<List<Run>> getRecentRuns();

    @Query("select * from Game where id = :id")
    User getGameById(String id);

    @Query("select * from Run where id = :id")
    Run getRunById(String id);

    @Query("select * from User where id = :id")
    User getUserById(String id);
}
