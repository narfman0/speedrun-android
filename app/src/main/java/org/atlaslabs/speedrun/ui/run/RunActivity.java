package org.atlaslabs.speedrun.ui.run;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityRunBinding;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Platform;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.models.User;
import org.atlaslabs.speedrun.ui.user.UserActivity;
import org.atlaslabs.speedrun.util.Utils;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

public class RunActivity extends AppCompatActivity {
    private static final String TAG = RunActivity.class.getSimpleName(),
            BUNDLE_KEY_GAME = "BUNDLE_KEY_GAME",
            BUNDLE_KEY_USER = "BUNDLE_KEY_USER",
            BUNDLE_KEY_PLATFORM = "BUNDLE_KEY_PLATFORM",
            BUNDLE_KEY_CATEGORY = "BUNDLE_KEY_CATEGORY",
            BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME",
            BUNDLE_KEY_COMMENT = "BUNDLE_KEY_COMMENT",
            BUNDLE_KEY_ID = "BUNDLE_KEY_ID",
            BUNDLE_KEY_VIDEOS = "BUNDLE_KEY_VIDEOS";
    private String id, game, platform, category, comment, videos;
    private List<String> userIDs;
    private float time;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getString(BUNDLE_KEY_ID);
            game = b.getString(BUNDLE_KEY_GAME);
            userIDs = Arrays.asList(b.getString(BUNDLE_KEY_USER).split(","));
            platform = b.getString(BUNDLE_KEY_PLATFORM);
            category = b.getString(BUNDLE_KEY_CATEGORY);
            comment = b.getString(BUNDLE_KEY_COMMENT);
            videos = b.getString(BUNDLE_KEY_VIDEOS);
            time = b.getFloat(BUNDLE_KEY_TIME);
        }
        ActivityRunBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_run);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();
        Game.getOrFetch(realm, game)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((game) -> binding.runGame.setText(game.getNames().getInternational()));
        for (String userID : userIDs) {
            User.getOrFetch(realm, userID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((user) -> {
                        if (user != null && user.getId() != null && user.getNames() != null) {
                            binding.runUser.setText(user.getNames().getInternational());
                            binding.runUser.setOnClickListener((c) -> {
                                        Intent intent = new Intent(RunActivity.this, UserActivity.class);
                                        intent.putExtras(UserActivity.buildBundle(new Bundle(), user));
                                        startActivity(intent);
                                    }
                            );
                        } else {
                            Log.w(TAG, "User name inaccessible for run: " + id);
                            binding.runUser.setVisibility(View.GONE);
                        }
                    });
            break;
        }
        if (platform != null)
            Platform.getOrFetch(realm, platform)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((platform) -> binding.runPlatform.setText(platform.getName()));
        else
            Log.i(TAG, "No platform given for run: " + id);
        if (category != null)
            Category.getOrFetch(realm, category)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((category) -> binding.runCategory.setText(category.getName()));
        else
            Log.i(TAG, "No category given for run: " + id);
        binding.runTime.setText(Utils.timePretty(time));
        binding.runComment.setText(comment);
        binding.runComment.setVisibility(TextUtils.isEmpty(comment) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(videos)) {
            binding.runVideos.setText(Html.fromHtml(videos));
            binding.runVideos.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            binding.runVideos.setVisibility(View.GONE);
            binding.runVideosText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null)
            realm.close();
    }

    public static Bundle buildBundle(Bundle bundle, Run run) {
        bundle.putString(BUNDLE_KEY_GAME, run.getGame());
        if (!run.getPlayersIDs().isEmpty())
            bundle.putString(BUNDLE_KEY_USER, Utils.join(run.getPlayersIDs().iterator(), ","));
        bundle.putString(BUNDLE_KEY_PLATFORM, run.getSystem().getPlatform());
        bundle.putString(BUNDLE_KEY_CATEGORY, run.getCategory());
        bundle.putFloat(BUNDLE_KEY_TIME, run.getTimes().getPrimaryTime());
        bundle.putString(BUNDLE_KEY_COMMENT, run.getComment());
        bundle.putString(BUNDLE_KEY_ID, run.getID());
        if (run.getVideos() != null && run.getVideos().getLinks() != null)
            bundle.putString(BUNDLE_KEY_VIDEOS, Utils.buildVideoLinks(run.getVideos().getLinks()));
        return bundle;
    }
}