package org.atlaslabs.speedrun.ui.run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.databinding.FragmentRunBinding;
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

public class RunFragment extends Fragment {
    private static final String TAG = RunFragment.class.getSimpleName(),
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

    public static RunFragment newInstance(Run run) {
        RunFragment fragment = new RunFragment();
        Bundle bundle = new Bundle();
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
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(BUNDLE_KEY_ID);
            game = getArguments().getString(BUNDLE_KEY_GAME);
            userIDs = Arrays.asList(getArguments().getString(BUNDLE_KEY_USER).split(","));
            platform = getArguments().getString(BUNDLE_KEY_PLATFORM);
            category = getArguments().getString(BUNDLE_KEY_CATEGORY);
            comment = getArguments().getString(BUNDLE_KEY_COMMENT);
            videos = getArguments().getString(BUNDLE_KEY_VIDEOS);
            time = getArguments().getFloat(BUNDLE_KEY_TIME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRunBinding binding = FragmentRunBinding.inflate(inflater);
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
                                        Intent intent = new Intent(getActivity(), UserActivity.class);
                                        intent.putExtras(UserActivity.buildBundle(new Bundle(), user));
                                        getActivity().startActivity(intent);
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
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null)
            realm.close();
    }
}