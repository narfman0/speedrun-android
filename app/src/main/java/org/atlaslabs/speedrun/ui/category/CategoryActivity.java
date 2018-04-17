package org.atlaslabs.speedrun.ui.category;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityCategoryBinding;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Favorite;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Leaderboard;
import org.atlaslabs.speedrun.ui.game.GameActivity;
import org.atlaslabs.speedrun.ui.run.RunActivity;
import org.atlaslabs.speedrun.ui.util.AbstractActivity;
import org.atlaslabs.speedrun.ui.util.VerticalSpaceItemDecoration;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

import static org.atlaslabs.speedrun.models.Favorite.FavoriteType.CATEGORY;

public class CategoryActivity extends AbstractActivity {
    private static final String TAG = CategoryActivity.class.getSimpleName(),
            BUNDLE_KEY_ID = "BUNDLE_KEY_ID",
            BUNDLE_KEY_GAME_ID = "BUNDLE_KEY_GAME_ID";
    private Category category;
    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            Realm realm = Realm.getDefaultInstance();
            category = Category.get(realm, b.getString(BUNDLE_KEY_ID));
            game = Game.get(realm, b.getString(BUNDLE_KEY_GAME_ID));
            realm.close();
        }
        ActivityCategoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.gameName.setText(game.getNames().getInternational());
        binding.gameName.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, GameActivity.class);
            intent.putExtras(GameActivity.buildBundle(new Bundle(), game.getId()));
            startActivity(intent);
        });
        binding.categoryName.setText(category.getName());
        disposable.add(Leaderboard.fetch(game.getId(), category.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    RecordAdapter adapter = new RecordAdapter(Arrays.asList(l.getRuns()));
                    binding.categoryRuns.setAdapter(adapter);
                    adapter.getClickedRecords().subscribe(r -> {
                        Intent intent = new Intent(CategoryActivity.this, RunActivity.class);
                        intent.putExtras(RunActivity.buildBundle(new Bundle(), r.getRun()));
                        startActivity(intent);
                    });
                }, e -> Log.e(TAG, "Error getting leaderboard for game: " + game.getId() +
                        " category: " + category.getId() + " error: " + e)));
        binding.favoriteName.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            Favorite favorite = Favorite.get(realm, category.getId(), game.getId(), CATEGORY);
            if (favorite == null) {
                binding.favoriteName.setImageResource(R.drawable.ic_favorite);
                Favorite.insert(realm, category.getId(), game.getId(), CATEGORY);
                Toast.makeText(CategoryActivity.this, "Category favorited!", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Favorite.remove(realm, new Favorite(category.getId(), CATEGORY).setId2(game.getId()));
                binding.favoriteName.setImageResource(R.drawable.ic_favorite_border);
                Toast.makeText(CategoryActivity.this, "Favorite removed", Toast.LENGTH_SHORT)
                        .show();
            }
            realm.close();
        });
        Realm realm = Realm.getDefaultInstance();
        if (Favorite.get(realm, category.getId(), game.getId(), CATEGORY) != null)
            binding.favoriteName.setImageResource(R.drawable.ic_favorite);
        realm.close();

        binding.categoryRuns.setLayoutManager(new LinearLayoutManager(this));
        binding.categoryRuns.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.run_list_divider_height)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Bundle buildBundle(Bundle bundle, String game, String category) {
        bundle.putString(BUNDLE_KEY_ID, category);
        bundle.putString(BUNDLE_KEY_GAME_ID, game);
        return bundle;
    }
}
