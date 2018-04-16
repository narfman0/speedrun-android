package org.atlaslabs.speedrun.ui.category;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityCategoryBinding;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Leaderboard;
import org.atlaslabs.speedrun.ui.decorations.VerticalSpaceItemDecoration;
import org.atlaslabs.speedrun.ui.run.RunActivity;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

public class CategoryActivity extends AppCompatActivity {
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
        binding.categoryName.setText(category.getName());
        Leaderboard.fetch(game.getId(), category.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    RecordAdapter adapter = new RecordAdapter(Arrays.asList(l.getRuns()));
                    binding.categoryRuns.setAdapter(adapter);
                    adapter.getClickedRecords().subscribe(r -> {
                        Intent intent = new Intent(CategoryActivity.this, RunActivity.class);
                        intent.putExtras(RunActivity.buildBundle(new Bundle(), r.getRun()));
                        startActivity(intent);
                    });
                });
        binding.categoryRuns.setLayoutManager(new LinearLayoutManager(this));
        binding.categoryRuns.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) getResources().getDimension(R.dimen.run_list_divider_height)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Bundle buildBundle(Bundle bundle, Game game, Category category) {
        bundle.putString(BUNDLE_KEY_ID, category.getId());
        bundle.putString(BUNDLE_KEY_GAME_ID, game.getId());
        return bundle;
    }
}
