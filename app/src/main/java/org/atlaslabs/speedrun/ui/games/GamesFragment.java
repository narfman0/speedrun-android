package org.atlaslabs.speedrun.ui.games;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.FragmentGamesBinding;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.ui.decorations.VerticalSpaceItemDecoration;
import org.atlaslabs.speedrun.ui.game.GameActivity;
import org.atlaslabs.speedrun.ui.util.RecyclerItemClickListener;

import java.util.List;

import io.realm.Realm;

public class GamesFragment extends Fragment {
    private Realm realm;

    public static GamesFragment newInstance() {
        return new GamesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentGamesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_games,
                container, false);
        binding.gamesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.gamesList.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.run_list_divider_height)));

        binding.gamesFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                List<Game> games = Game.getFiltered(realm, s.toString());
                binding.gamesList.setAdapter(new GamesListAdapter(games));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        realm = Realm.getDefaultInstance();
        binding.gamesList.setAdapter(new GamesListAdapter(Game.get(realm)));
        binding.gamesList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), binding.gamesList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (!isAdded() || getActivity() == null)
                                    return;
                                GamesListAdapter adapter = (GamesListAdapter) binding.gamesList.getAdapter();
                                Game game = adapter.getItem(position);
                                Intent intent = new Intent(getActivity(), GameActivity.class);
                                intent.putExtras(GameActivity.buildBundle(new Bundle(), game));
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
}