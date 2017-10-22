package org.atlaslabs.speedrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.runs.RecentRunsListAdapter;
import org.atlaslabs.speedrun.services.GamesLoadReceiver;
import org.atlaslabs.speedrun.services.GamesLoadService;
import org.atlaslabs.speedrun.services.RecentRunReceiver;
import org.atlaslabs.speedrun.services.RecentRunService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements GamesLoadReceiver.IGamesLoadedHandler,
    RecentRunReceiver.IRecentRunsLoadedHandler{
    private RecyclerView recentRunsList;
    private ProgressBar progressBar;
    private GamesLoadReceiver gamesLoadReceiver;
    private RecentRunReceiver recentRunReceiver;
    private boolean gamesLoaded, runsLoaded;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        recentRunsList = (RecyclerView)findViewById(R.id.recentRunsList);
        recentRunsList.setLayoutManager(new LinearLayoutManager(this));

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        gamesLoadReceiver = new GamesLoadReceiver(this, this);
        recentRunReceiver = new RecentRunReceiver(this, this);
        startService(new Intent(this, GamesLoadService.class));
        startService(new Intent(this, RecentRunService.class));
    }

    private void handleDataLoaded(){
        progressBar.setVisibility(View.GONE);
        realm = Realm.getDefaultInstance();
        recentRunsList.setAdapter(new RecentRunsListAdapter(Run.getByDate(realm, 20)));
    }

    public void gamesLoaded(){
        gamesLoaded = true;
        if(runsLoaded && gamesLoaded)
            handleDataLoaded();
    }

    public void recentRunsLoaded(){
        runsLoaded = true;
        if(runsLoaded && gamesLoaded)
            handleDataLoaded();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(gamesLoadReceiver != null)
            gamesLoadReceiver.clean();
        if(recentRunReceiver != null)
            recentRunReceiver.clean();
        if(realm != null)
            realm.close();
    }
}
