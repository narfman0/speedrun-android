package org.atlaslabs.speedrun.ui.games;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityGamesBinding;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.ui.game.GameActivity;
import org.atlaslabs.speedrun.ui.util.AbstractActivity;
import org.atlaslabs.speedrun.ui.util.RecyclerItemClickListener;
import org.atlaslabs.speedrun.ui.util.VerticalSpaceItemDecoration;

import java.util.List;

import io.realm.Realm;

public class GamesActivity extends AbstractActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGamesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_games);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.gamesList.setLayoutManager(new LinearLayoutManager(this));
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
                new RecyclerItemClickListener(this, binding.gamesList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                GamesListAdapter adapter = (GamesListAdapter) binding.gamesList.getAdapter();
                                Game game = adapter.getItem(position);
                                Intent intent = new Intent(GamesActivity.this, GameActivity.class);
                                intent.putExtras(GameActivity.buildBundle(new Bundle(), game.getId()));
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