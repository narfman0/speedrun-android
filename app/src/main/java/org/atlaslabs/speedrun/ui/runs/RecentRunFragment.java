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
import org.atlaslabs.speedrun.ui.run.RunFragment;
import org.atlaslabs.speedrun.ui.util.RecyclerItemClickListener;

import java.util.List;

import io.realm.Realm;

public class RecentRunFragment extends Fragment {
    private Realm realm;
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

        realm = Realm.getDefaultInstance();
        final List<Run> runs = Run.getByDate(realm, 20);
        recentRunsList.setAdapter(new RecentRunsListAdapter(runs));
        recentRunsList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recentRunsList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Run run = runs.get(position);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .addToBackStack(RecentRunFragment.class.getSimpleName())
                                .replace(R.id.content, RunFragment.newInstance(run))
                                .commit();
                    }
                    @Override public void onLongItemClick(View view, int position) {}
                })
        );
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(realm != null)
            realm.close();
    }
}