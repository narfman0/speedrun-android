package org.atlaslabs.speedrun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.services.GamesLoadReceiver;
import org.atlaslabs.speedrun.services.GamesLoadService;
import org.atlaslabs.speedrun.services.PlatformsReceiver;
import org.atlaslabs.speedrun.services.PlatformsService;
import org.atlaslabs.speedrun.services.RecentRunReceiver;
import org.atlaslabs.speedrun.services.RecentRunService;
import org.atlaslabs.speedrun.ui.favorite.FavoriteActivity;
import org.atlaslabs.speedrun.ui.games.GamesActivity;
import org.atlaslabs.speedrun.ui.runs.RecentRunActivity;
import org.atlaslabs.speedrun.ui.util.DisposableActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends DisposableActivity implements GamesLoadReceiver.IGamesLoadedHandler,
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
        gamesLoadReceiver = new GamesLoadReceiver(this, this);
        platformsReceiver = new PlatformsReceiver(this, this);
        recentRunReceiver = new RecentRunReceiver(this, this);
        startService(new Intent(this, GamesLoadService.class));
        startService(new Intent(this, PlatformsService.class));
        startService(new Intent(this, RecentRunService.class));
    }

    private void handleDataLoaded() {
        if (!runsLoaded || !gamesLoaded || !platformsLoaded || !getWindow().getDecorView().isShown())
            return;
        navigateRecent();
    }

    private void navigateGames() {
        startActivity(new Intent(this, GamesActivity.class));
    }

    private void navigateRecent() {
        startActivity(new Intent(this, RecentRunActivity.class));
    }

    private void navigateFavorites() {
        startActivity(new Intent(this, FavoriteActivity.class));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorites:
                navigateFavorites();
                return true;
            case R.id.menu_recent:
                navigateRecent();
                return true;
            case R.id.menu_games:
                navigateGames();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
