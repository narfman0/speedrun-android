package org.atlaslabs.speedrun.ui.games;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.ui.decorations.VerticalSpaceItemDecoration;

import java.util.List;

import io.realm.Realm;

public class GamesFragment extends Fragment {
    private Realm realm;

    public static GamesFragment newInstance() {
        return new GamesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        RecyclerView gamesList = view.findViewById(R.id.gamesList);
        gamesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        gamesList.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.run_list_divider_height)));

        realm = Realm.getDefaultInstance();
        final List<Game> games = Game.get(realm);
        gamesList.setAdapter(new GamesListAdapter(games));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null)
            realm.close();
    }
}