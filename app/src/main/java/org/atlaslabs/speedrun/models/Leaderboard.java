package org.atlaslabs.speedrun.models;

import android.support.annotation.NonNull;
import android.util.Log;

import org.atlaslabs.speedrun.network.RestUtil;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class Leaderboard {
    private static final String TAG = Leaderboard.class.getSimpleName();
    private String weblink, game, category, timing;
    private Record[] runs;

    public Record[] getRuns() {
        return runs;
    }

    public void setRuns(Record[] runs) {
        this.runs = runs;
    }

    public static Single<Leaderboard> fetch(@NonNull String gameID, @NonNull String categoryID) {
        return RestUtil.createAPI().getCategoryLeaderboard(gameID, categoryID)
                .subscribeOn(Schedulers.newThread())
                .doOnError((e) ->
                        Log.e(TAG, "fetch error: " + e)
                )
                .flatMap(item -> Single.just(item.data));
    }
}
