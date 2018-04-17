package org.atlaslabs.speedrun.ui.user;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.databinding.ActivityUserBinding;
import org.atlaslabs.speedrun.models.Favorite;
import org.atlaslabs.speedrun.models.User;
import org.atlaslabs.speedrun.ui.category.CategoryActivity;
import org.atlaslabs.speedrun.ui.util.AbstractActivity;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

public class UserActivity extends AbstractActivity {
    private static final String TAG = UserActivity.class.getSimpleName(),
            BUNDLE_KEY_ID = "BUNDLE_KEY_ID",
            BUNDLE_KEY_NAME = "BUNDLE_KEY_NAME",
            BUNDLE_KEY_YOUTUBE = "BUNDLE_KEY_YOUTUBE",
            BUNDLE_KEY_TWITCH = "BUNDLE_KEY_TWITCH",
            BUNDLE_KEY_WEBLINK = "BUNDLE_KEY_WEBLINK";
    private PersonalBestAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            model.setID(b.getString(BUNDLE_KEY_ID));
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
        binding.userFavorite.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            Favorite favorite = Favorite.get(realm, model.getID(), Favorite.FavoriteType.USER);
            if (favorite == null)
                Favorite.insert(realm, model.getID(), Favorite.FavoriteType.USER);
            realm.close();
            Toast.makeText(UserActivity.this, "User favorited!", Toast.LENGTH_SHORT)
                    .show();
        });

        // TODO back this off to VM
        disposable.add(User.fetchPersonalBests(model.getID())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(records -> {
                    adapter = new PersonalBestAdapter(Arrays.asList(records));
                    adapter.getClickedRecords().subscribe(c -> {
                        Intent intent = new Intent(UserActivity.this, CategoryActivity.class);
                        intent.putExtras(CategoryActivity.buildBundle(new Bundle(),
                                c.getRun().getGame(), c.getRun().getCategory()));
                        startActivity(intent);
                    });
                    binding.userPersonalBests.setAdapter(adapter);
                }));
        binding.userPersonalBests.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Bundle buildBundle(Bundle bundle, User user) {
        bundle.putString(BUNDLE_KEY_ID, user.getId());
        bundle.putString(BUNDLE_KEY_NAME, user.getNamePretty());
        bundle.putString(BUNDLE_KEY_YOUTUBE, user.getYoutubePretty());
        bundle.putString(BUNDLE_KEY_TWITCH, user.getTwitchPretty());
        bundle.putString(BUNDLE_KEY_WEBLINK, user.getWeblink());
        return bundle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null)
            adapter.destroy();
    }
}
