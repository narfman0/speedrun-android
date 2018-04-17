package org.atlaslabs.speedrun.ui.runs;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityRecentRunsBinding;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.ui.run.RunActivity;
import org.atlaslabs.speedrun.ui.util.DisposableActivity;
import org.atlaslabs.speedrun.ui.util.RecyclerItemClickListener;
import org.atlaslabs.speedrun.ui.util.VerticalSpaceItemDecoration;

import java.util.List;

import io.realm.Realm;

public class RecentRunActivity extends DisposableActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRecentRunsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recent_runs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.recentRunsList.setLayoutManager(new LinearLayoutManager(this));
        binding.recentRunsList.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.run_list_divider_height)));

        realm = Realm.getDefaultInstance();
        final List<Run> runs = Run.getByDate(realm, 100);
        binding.recentRunsList.setAdapter(new RecentRunsListAdapter(runs));
        binding.recentRunsList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, binding.recentRunsList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Run run = runs.get(position);
                                Intent intent = new Intent(RecentRunActivity.this, RunActivity.class);
                                intent.putExtras(RunActivity.buildBundle(new Bundle(), run));
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }
                        })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null)
            realm.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }
}