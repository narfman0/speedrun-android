package org.atlaslabs.speedrun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Favorite;
import org.atlaslabs.speedrun.services.GamesLoadReceiver;
import org.atlaslabs.speedrun.services.GamesLoadService;
import org.atlaslabs.speedrun.services.PlatformsReceiver;
import org.atlaslabs.speedrun.services.PlatformsService;
import org.atlaslabs.speedrun.services.RecentRunReceiver;
import org.atlaslabs.speedrun.services.RecentRunService;
import org.atlaslabs.speedrun.ui.favorite.FavoriteActivity;
import org.atlaslabs.speedrun.ui.runs.RecentRunActivity;
import org.atlaslabs.speedrun.ui.util.AbstractActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AbstractActivity implements GamesLoadReceiver.IGamesLoadedHandler,
        RecentRunReceiver.IRecentRunsLoadedHandler, PlatformsReceiver.IPlatformsHandler {
    private GamesLoadReceiver gamesLoadReceiver;
    private RecentRunReceiver recentRunReceiver;
    private PlatformsReceiver platformsReceiver;
    private boolean gamesLoaded, runsLoaded, platformsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();
        boolean startFavorite = !realm.where(Favorite.class).findAll().isEmpty();
        realm.close();
        if (startFavorite) {
            startActivity(new Intent(this, FavoriteActivity.class));
            finish();
        } else {
            gamesLoadReceiver = new GamesLoadReceiver(this, this);
            platformsReceiver = new PlatformsReceiver(this, this);
            recentRunReceiver = new RecentRunReceiver(this, this);
            startService(new Intent(this, GamesLoadService.class));
            startService(new Intent(this, PlatformsService.class));
            startService(new Intent(this, RecentRunService.class));
        }
    }

    private void handleDataLoaded() {
        if (!runsLoaded || !gamesLoaded || !platformsLoaded || !getWindow().getDecorView().isShown())
            return;
        startActivity(new Intent(this, RecentRunActivity.class));
        finish();
    }

    public void gamesLoaded() {
        gamesLoaded = true;
        handleDataLoaded();
    }

    public void recentRunsLoaded() {
        runsLoaded = true;
        handleDataLoaded();
    }

    public void platformsLoaded() {
        platformsLoaded = true;
        handleDataLoaded();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gamesLoadReceiver != null)
            gamesLoadReceiver.clean();
        if (recentRunReceiver != null)
            recentRunReceiver.clean();
        if (platformsReceiver != null)
            platformsReceiver.clean();
    }
}
