package org.atlaslabs.speedrun.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.network.RestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Download all games from speedrun.com and add to the database
 */
public class GamesLoadService extends IntentService{
    public static String INTENT_GAMES_LOAD_COMPLETE = "INTENT_GAMES_LOAD_COMPLETE";

    public GamesLoadService(){
        super(GamesLoadService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Realm realm = Realm.getDefaultInstance();
        try {
            long totalGames = realm.where(Game.class).count();
            if(totalGames == 0)
                populateGameData(realm);
        } finally {
            realm.close();
        }

        // Broadcast that we are done populating game data
        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(INTENT_GAMES_LOAD_COMPLETE));
    }

    /**
     * We want to populate "most" games into the database. It's fine if we miss some -
     * we will grab game detail for each individual missing game.
     */
    private void populateGameData(Realm realm){
        ConcurrentLinkedQueue<Boolean> workInProgress = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<List<Game>> work = new ConcurrentLinkedQueue<>();

        // initial games seed. we want to grab most, but we can leave off some
        // until they are needed. we get detailed game info later, as well.
        for(int i=0; i<11; i++) {
            workInProgress.add(true);
            RestUtil.createAPI().getGamesBulk(i * 1000)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe((item) -> {
                        work.add(Arrays.asList(item.getGames()));
                        workInProgress.remove();
                    });
        }

        // wait for threads to converge. this is essentially `join`.
        while(!workInProgress.isEmpty());

        // batch add all games at once
        realm.beginTransaction();
        for(List<Game> games : work)
            realm.copyToRealmOrUpdate(games);
        realm.commitTransaction();
    }
}
