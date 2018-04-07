package org.atlaslabs.speedrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.atlaslabs.speedrun.services.GamesLoadReceiver;
import org.atlaslabs.speedrun.services.GamesLoadService;
import org.atlaslabs.speedrun.services.PlatformsReceiver;
import org.atlaslabs.speedrun.services.PlatformsService;
import org.atlaslabs.speedrun.services.RecentRunReceiver;
import org.atlaslabs.speedrun.services.RecentRunService;
import org.atlaslabs.speedrun.ui.runs.RecentRunFragment;

public class MainActivity extends AppCompatActivity implements GamesLoadReceiver.IGamesLoadedHandler,
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        gamesLoadReceiver = new GamesLoadReceiver(this, this);
        platformsReceiver = new PlatformsReceiver(this, this);
        recentRunReceiver = new RecentRunReceiver(this, this);
        startService(new Intent(this, GamesLoadService.class));
        startService(new Intent(this, PlatformsService.class));
        startService(new Intent(this, RecentRunService.class));
    }

    private void handleDataLoaded() {
        if (!runsLoaded || !gamesLoaded || !platformsLoaded)
            return;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, RecentRunFragment.newInstance())
                .commit();
        progressBar.setVisibility(View.GONE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
