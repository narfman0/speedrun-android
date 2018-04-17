package org.atlaslabs.speedrun.ui.favorite;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityFavoriteBinding;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Favorite;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.User;
import org.atlaslabs.speedrun.ui.category.CategoryActivity;
import org.atlaslabs.speedrun.ui.game.GameActivity;
import org.atlaslabs.speedrun.ui.user.UserActivity;
import org.atlaslabs.speedrun.ui.util.AbstractActivity;

import java.util.List;

import io.realm.Realm;

public class FavoriteActivity extends AbstractActivity {
    private static final String TAG = FavoriteActivity.class.getSimpleName();
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        ActivityFavoriteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        List<Favorite> favorites = Favorite.get(realm);
        FavoriteAdapter adapter = new FavoriteAdapter(favorites);
        binding.favorites.setAdapter(adapter);
        disposable.add(adapter.getClicked().subscribe(f -> {
            switch (f.getType()) {
                case CATEGORY: {
                    Category category = Category.get(realm, f.getId());
                    Game game = Game.get(realm, f.getId2());
                    Intent intent = new Intent(FavoriteActivity.this, CategoryActivity.class);
                    intent.putExtras(CategoryActivity.buildBundle(new Bundle(), game.getId(), category.getId()));
                    startActivity(intent);
                    break;
                }
                case GAME: {
                    Intent intent = new Intent(FavoriteActivity.this, GameActivity.class);
                    intent.putExtras(GameActivity.buildBundle(new Bundle(), f.getId()));
                    startActivity(intent);
                    break;
                }
                case USER: {
                    User user = User.get(realm, f.getId());
                    Intent intent = new Intent(FavoriteActivity.this, UserActivity.class);
                    intent.putExtras(UserActivity.buildBundle(new Bundle(), user));
                    startActivity(intent);
                    break;
                }
            }
        }));
        disposable.add(adapter.getRemoved().subscribe(f -> Favorite.remove(realm, f)));
        binding.favorites.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null)
            realm.close();
    }
}
