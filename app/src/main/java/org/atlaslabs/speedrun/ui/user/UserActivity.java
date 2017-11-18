package org.atlaslabs.speedrun.ui.user;

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
    public static final String TAG = UserActivity.class.getSimpleName(),
            BUNDLE_KEY_NAME = "BUNDLE_KEY_NAME",
            BUNDLE_KEY_YOUTUBE = "BUNDLE_KEY_YOUTUBE",
            BUNDLE_KEY_TWITCH = "BUNDLE_KEY_TWITCH",
            BUNDLE_KEY_WEBLINK = "BUNDLE_KEY_WEBLINK";
    private String name, youtube, twitch, weblink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            name = b.getString(BUNDLE_KEY_NAME);
            youtube = b.getString(BUNDLE_KEY_YOUTUBE);
            twitch = b.getString(BUNDLE_KEY_TWITCH);
            weblink = b.getString(BUNDLE_KEY_WEBLINK);
        }
        ActivityUserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!TextUtils.isEmpty(name))
            binding.userName.setText(name);
        else {
            binding.userName.setVisibility(View.GONE);
            binding.userNameText.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(twitch))
            binding.userTwitch.setText(twitch);
        else {
            binding.userTwitchText.setVisibility(View.GONE);
            binding.userTwitch.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(youtube))
            binding.userYoutube.setText(youtube);
        else {
            binding.userYoutubeText.setVisibility(View.GONE);
            binding.userYoutube.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(weblink))
            binding.userWeblink.setText(weblink);
        else {
            binding.userWeblinkText.setVisibility(View.GONE);
            binding.userWeblink.setVisibility(View.GONE);
        }
    }

    public static Bundle buildBundle(Bundle bundle, User user) {
        bundle.putString(BUNDLE_KEY_NAME, user.getNamePretty());
        bundle.putString(BUNDLE_KEY_YOUTUBE, user.getYoutubePretty());
        bundle.putString(BUNDLE_KEY_TWITCH, user.getTwitchPretty());
        bundle.putString(BUNDLE_KEY_WEBLINK, user.getWeblink());
        return bundle;
    }
}
