package org.atlaslabs.speedrun.ui.game;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityGameBinding;
import org.atlaslabs.speedrun.models.Favorite;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.ui.category.CategoryActivity;
import org.atlaslabs.speedrun.ui.util.AbstractActivity;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

public class GameActivity extends AbstractActivity {
    private static final String TAG = GameActivity.class.getSimpleName(),
            BUNDLE_KEY_ID = "BUNDLE_KEY_ID";
    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            Realm realm = Realm.getDefaultInstance();
            game = Game.get(realm, b.getString(BUNDLE_KEY_ID));
            realm.close();
        }
        ActivityGameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.gameName.setText(game.getNames().getInternational());
        disposable.add(Game.fetchCategories(game.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    CategoriesAdapter adapter = new CategoriesAdapter(Arrays.asList(categories));
                    binding.gameCategories.setAdapter(adapter);
                    adapter.getClickedCategories().subscribe(c -> {
                        Intent intent = new Intent(GameActivity.this, CategoryActivity.class);
                        intent.putExtras(CategoryActivity.buildBundle(new Bundle(), game.getId(), c.getId()));
                        startActivity(intent);
                    });
                }));
        binding.gameFavorite.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            Favorite favorite = Favorite.get(realm, game.getId(), Favorite.FavoriteType.GAME);
            if (favorite == null)
                Favorite.insert(realm, game.getId(), Favorite.FavoriteType.GAME);
            realm.close();
            Toast.makeText(GameActivity.this, "Game favorited!", Toast.LENGTH_SHORT)
                    .show();
        });
        binding.gameCategories.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Bundle buildBundle(Bundle bundle, String gameID) {
        bundle.putString(BUNDLE_KEY_ID, gameID);
        return bundle;
    }
}
