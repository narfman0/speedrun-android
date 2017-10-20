package org.atlaslabs.speedrun;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.atlaslabs.speedrun.network.RestUtil;
import org.atlaslabs.speedrun.network.responses.RunsResponse;
import org.atlaslabs.speedrun.runs.RecentRunsListAdapter;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recentRunsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recentRunsList = (RecyclerView)findViewById(R.id.recent_runs_list);
        recentRunsList.setLayoutManager(new LinearLayoutManager(this));
        // async grab this data
        new RecentRunsNetworkCall().execute(RestUtil.createAPI().getRecentRuns());
    }

    public class RecentRunsNetworkCall extends AsyncTask<Call<RunsResponse>, Void, RunsResponse> {
        @Override
        protected RunsResponse doInBackground(Call<RunsResponse>... params) {
            try {
                Call<RunsResponse> call = params[0];
                Response<RunsResponse> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(RunsResponse runs) {
            recentRunsList.setAdapter(new RecentRunsListAdapter(Arrays.asList(runs.getRuns())));
        }
    }
}
