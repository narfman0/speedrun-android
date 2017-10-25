package org.atlaslabs.speedrun.ui.run;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Platform;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.models.User;
import org.atlaslabs.speedrun.util.Utils;

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
    private String id, game, user, platform, category, comment, videos;
    private float time;
    private Realm realm;

    public static RunFragment newInstance(Run run){
        RunFragment fragment = new RunFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_GAME, run.getGame());
        if(!run.getPlayers().isEmpty())
            bundle.putString(BUNDLE_KEY_USER, run.getPlayers().get(0).getId());
        bundle.putString(BUNDLE_KEY_PLATFORM, run.getSystem().getPlatform());
        bundle.putString(BUNDLE_KEY_CATEGORY, run.getCategory());
        bundle.putFloat(BUNDLE_KEY_TIME, run.getTimes().getPrimaryTime());
        bundle.putString(BUNDLE_KEY_COMMENT, run.getComment());
        bundle.putString(BUNDLE_KEY_ID, run.getID());
        if(run.getVideos() != null && run.getVideos().getLinks() != null)
            bundle.putString(BUNDLE_KEY_VIDEOS, Utils.buildVideoLinks(run.getVideos().getLinks()));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            id = getArguments().getString(BUNDLE_KEY_ID);
            game = getArguments().getString(BUNDLE_KEY_GAME);
            user = getArguments().getString(BUNDLE_KEY_USER);
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
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        TextView gameNameView = (TextView) view.findViewById(R.id.run_game);
        TextView userNameView = (TextView) view.findViewById(R.id.run_user);
        TextView platformView = (TextView) view.findViewById(R.id.run_platform);
        TextView categoryView = (TextView) view.findViewById(R.id.run_category);
        TextView timeView = (TextView) view.findViewById(R.id.run_time);
        TextView commentView = (TextView) view.findViewById(R.id.run_comment);
        TextView videosView = (TextView) view.findViewById(R.id.run_videos);
        TextView videosTextView = (TextView) view.findViewById(R.id.run_videos_text);

        realm = Realm.getDefaultInstance();
        Game.getOrFetch(realm, game)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((game) -> gameNameView.setText(game.getNames().getInternational()));
        User.getOrFetch(realm, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((user) -> {
                    if(user != null && user.getId() != null && user.getNames() != null)
                        userNameView.setText(user.getNames().getInternational());
                    else {
                        Log.w(TAG, "User name inaccessible for run: " + id);
                        userNameView.setVisibility(View.GONE);
                    }
                });
        Platform.getOrFetch(realm, platform)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( (platform) -> platformView.setText(platform.getName()));
        Category.getOrFetch(realm, category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((category) -> categoryView.setText(category.getName()));
        timeView.setText(Utils.timePretty(time));
        commentView.setText(comment);
        commentView.setVisibility(TextUtils.isEmpty(comment) ? View.GONE : View.VISIBLE);
        if(!TextUtils.isEmpty(videos)) {
            videosView.setText(Html.fromHtml(videos));
            videosView.setMovementMethod(LinkMovementMethod.getInstance());
        }else{
            videosView.setVisibility(View.GONE);
            videosTextView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(realm != null)
            realm.close();
    }
}