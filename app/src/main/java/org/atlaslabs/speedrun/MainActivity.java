package org.atlaslabs.speedrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.network.RestUtil;
import org.atlaslabs.speedrun.runs.RecentRunsListAdapter;

import java.util.Arrays;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recentRunsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recentRunsList = (RecyclerView)findViewById(R.id.recent_runs_list);
        recentRunsList.setLayoutManager(new LinearLayoutManager(this));
        // async grab this data
        RestUtil.createAPI().getRecentRuns()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((item) -> {
                    List<Run> runs = Arrays.asList(item.getRuns());
                    recentRunsList.setAdapter(new RecentRunsListAdapter(runs));
                });
    }
}
