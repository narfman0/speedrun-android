package org.atlaslabs.speedrun.ui.user;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityUserBinding;
import org.atlaslabs.speedrun.models.User;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = UserActivity.class.getSimpleName(),
            BUNDLE_KEY_NAME = "BUNDLE_KEY_NAME",
            BUNDLE_KEY_YOUTUBE = "BUNDLE_KEY_YOUTUBE",
            BUNDLE_KEY_TWITCH = "BUNDLE_KEY_TWITCH",
            BUNDLE_KEY_WEBLINK = "BUNDLE_KEY_WEBLINK";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            model.setName(b.getString(BUNDLE_KEY_NAME));
            model.setYoutube(b.getString(BUNDLE_KEY_YOUTUBE));
            model.setTwitch(b.getString(BUNDLE_KEY_TWITCH));
            model.setWeblink(b.getString(BUNDLE_KEY_WEBLINK));
        }
        ActivityUserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (!TextUtils.isEmpty(model.getName()))
            binding.userName.setText(model.getName());
        else {
            binding.userName.setVisibility(View.GONE);
            binding.userNameText.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(model.getTwitch()))
            binding.userTwitch.setText(model.getTwitch());
        else {
            binding.userTwitchText.setVisibility(View.GONE);
            binding.userTwitch.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(model.getYoutube()))
            binding.userYoutube.setText(model.getYoutube());
        else {
            binding.userYoutubeText.setVisibility(View.GONE);
            binding.userYoutube.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(model.getWeblink()))
            binding.userWeblink.setText(model.getWeblink());
        else {
            binding.userWeblinkText.setVisibility(View.GONE);
            binding.userWeblink.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Bundle buildBundle(Bundle bundle, User user) {
        bundle.putString(BUNDLE_KEY_NAME, user.getNamePretty());
        bundle.putString(BUNDLE_KEY_YOUTUBE, user.getYoutubePretty());
        bundle.putString(BUNDLE_KEY_TWITCH, user.getTwitchPretty());
        bundle.putString(BUNDLE_KEY_WEBLINK, user.getWeblink());
        return bundle;
    }
}