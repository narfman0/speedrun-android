package org.atlaslabs.speedrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.network.RestUtil;
import org.atlaslabs.speedrun.runs.RecentRunsListAdapter;
import org.atlaslabs.speedrun.services.GamesLoadReceiver;
import org.atlaslabs.speedrun.services.GamesLoadService;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements GamesLoadReceiver.IGamesLoadedHandler{
    private RecyclerView recentRunsList;
    private ProgressBar progressBar;
    private GamesLoadReceiver gamesLoadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        recentRunsList = (RecyclerView)findViewById(R.id.recentRunsList);
        recentRunsList.setLayoutManager(new LinearLayoutManager(this));
        gamesLoadReceiver = new GamesLoadReceiver(this, this);
        startService(new Intent(this, GamesLoadService.class));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        gamesLoadReceiver.clean();
    }

    public void gamesLoaded(){
        populateRecentRuns();
    }

    private void populateRecentRuns(){
        RestUtil.createAPI().getRecentRuns()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((item) -> {
                    progressBar.setVisibility(View.GONE);
                    List<Run> runs = Arrays.asList(item.getRuns());
                    recentRunsList.setAdapter(new RecentRunsListAdapter(runs));
                });
    }
}
