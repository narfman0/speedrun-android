package org.atlaslabs.speedrun.ui.runs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.ui.decorations.VerticalSpaceItemDecoration;

import io.realm.Realm;

public class RecentRunFragment extends Fragment {
    private RecyclerView recentRunsList;

    public static RecentRunFragment newInstance(){
        return new RecentRunFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_runs, container, false);
        recentRunsList = (RecyclerView) view.findViewById(R.id.recentRunsList);
        recentRunsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recentRunsList.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.run_list_divider_height)));

        Realm realm = Realm.getDefaultInstance();
        try {
            recentRunsList.setAdapter(new RecentRunsListAdapter(Run.getByDate(realm, 20)));
        } finally {
            realm.close();
        }
        return view;
    }
}