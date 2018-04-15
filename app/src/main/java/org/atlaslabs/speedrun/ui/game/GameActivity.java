package org.atlaslabs.speedrun.ui.game;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityGameBinding;
import org.atlaslabs.speedrun.models.Game;

import io.realm.Realm;

public class GameActivity extends AppCompatActivity {
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Bundle buildBundle(Bundle bundle, Game game) {
        bundle.putString(BUNDLE_KEY_ID, game.getId());
        return bundle;
    }
}
