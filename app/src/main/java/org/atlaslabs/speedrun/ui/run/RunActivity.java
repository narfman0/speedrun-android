package org.atlaslabs.speedrun.ui.run;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityRunBinding;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.util.Utils;

import java.util.Arrays;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RunViewModel model = ViewModelProviders.of(this).get(RunViewModel.class);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            model.setId(b.getString(BUNDLE_KEY_ID));
            model.setGame(b.getString(BUNDLE_KEY_GAME));
            model.setUserIDs(Arrays.asList(b.getString(BUNDLE_KEY_USER).split(",")));
            model.setPlatform(b.getString(BUNDLE_KEY_PLATFORM));
            model.setCategory(b.getString(BUNDLE_KEY_CATEGORY));
            model.setComment(b.getString(BUNDLE_KEY_COMMENT));
            model.setVideos(b.getString(BUNDLE_KEY_VIDEOS));
            model.setTime(b.getFloat(BUNDLE_KEY_TIME));
        }
        ActivityRunBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_run);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        model.gameName.observe(this, (name) -> {
            binding.runGame.setText(name);
            binding.runGame.setVisibility(View.VISIBLE);
        });
        model.userName.observe(this, (name) -> {
            binding.runUser.setVisibility(View.VISIBLE);
            binding.runUser.setText(name);
        });
        model.platformName.observe(this, (name) -> {
            binding.runPlatform.setText(name);
            binding.runPlatform.setVisibility(View.VISIBLE);
        });
        model.categoryName.observe(this, (name) -> {
            binding.runCategory.setText(name);
            binding.runCategory.setVisibility(View.VISIBLE);
        });
        model.load();
        binding.runTime.setText(Utils.timePretty(model.getTime()));
        binding.runComment.setText(model.getComment());
        binding.runComment.setVisibility(TextUtils.isEmpty(model.getComment()) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(model.getVideos())) {
            binding.runVideos.setText(Html.fromHtml(model.getVideos()));
            binding.runVideos.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            binding.runVideos.setVisibility(View.GONE);
            binding.runVideosText.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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