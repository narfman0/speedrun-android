package org.atlaslabs.speedrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.atlaslabs.speedrun.services.GamesLoadReceiver;
import org.atlaslabs.speedrun.services.GamesLoadService;
import org.atlaslabs.speedrun.services.PlatformsReceiver;
import org.atlaslabs.speedrun.services.PlatformsService;
import org.atlaslabs.speedrun.services.RecentRunReceiver;
import org.atlaslabs.speedrun.services.RecentRunService;
import org.atlaslabs.speedrun.ui.favorite.FavoriteActivity;
import org.atlaslabs.speedrun.ui.games.GamesFragment;
import org.atlaslabs.speedrun.ui.runs.RecentRunFragment;
import org.atlaslabs.speedrun.ui.util.DisposableActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends DisposableActivity implements GamesLoadReceiver.IGamesLoadedHandler,
        RecentRunReceiver.IRecentRunsLoadedHandler, PlatformsReceiver.IPlatformsHandler {
    private View progressBar;
    private GamesLoadReceiver gamesLoadReceiver;
    private RecentRunReceiver recentRunReceiver;
    private PlatformsReceiver platformsReceiver;
    private boolean gamesLoaded, runsLoaded, platformsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
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
        progressBar.setVisibility(View.GONE);
    }

    private void navigateGames() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, GamesFragment.newInstance())
                .commit();
    }

    private void navigateRecent() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, RecentRunFragment.newInstance())
                .commit();
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
        progressBar.setVisibility(View.GONE);
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
