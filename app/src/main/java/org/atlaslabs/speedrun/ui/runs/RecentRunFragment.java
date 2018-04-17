package org.atlaslabs.speedrun.ui.runs;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.FragmentRecentRunsBinding;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.ui.util.VerticalSpaceItemDecoration;
import org.atlaslabs.speedrun.ui.run.RunActivity;
import org.atlaslabs.speedrun.ui.util.RecyclerItemClickListener;

import java.util.List;

import io.realm.Realm;

public class RecentRunFragment extends Fragment {
    private Realm realm;

    public static RecentRunFragment newInstance() {
        return new RecentRunFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRecentRunsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recent_runs, container, false);
        binding.recentRunsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recentRunsList.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.run_list_divider_height)));

        realm = Realm.getDefaultInstance();
        final List<Run> runs = Run.getByDate(realm, 100);
        binding.recentRunsList.setAdapter(new RecentRunsListAdapter(runs));
        binding.recentRunsList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), binding.recentRunsList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Run run = runs.get(position);
                                Intent intent = new Intent(getActivity(), RunActivity.class);
                                intent.putExtras(RunActivity.buildBundle(new Bundle(), run));
                                getActivity().startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }
                        })
        );
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null)
            realm.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }
}