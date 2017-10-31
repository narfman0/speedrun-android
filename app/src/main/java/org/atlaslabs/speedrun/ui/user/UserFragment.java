package org.atlaslabs.speedrun.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.User;

public class UserFragment extends Fragment{
    private static final String TAG = UserFragment.class.getSimpleName(),
            BUNDLE_KEY_NAME = "BUNDLE_KEY_NAME",
            BUNDLE_KEY_YOUTUBE = "BUNDLE_KEY_YOUTUBE",
            BUNDLE_KEY_TWITCH = "BUNDLE_KEY_TWITCH",
            BUNDLE_KEY_WEBLINK = "BUNDLE_KEY_WEBLINK";
    private String name, youtube, twitch, weblink;

    public static UserFragment newInstance(User user){
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();

        String name = null;
        if(user.getNames() != null){
            if(!TextUtils.isEmpty(user.getNames().getInternational()))
                name = user.getNames().getInternational();
            else if(!TextUtils.isEmpty(user.getNames().getJapanese()))
                name = user.getNames().getJapanese();
        }

        String youtube = null;
        if(user.getYoutube() != null)
            youtube = user.getYoutube().getUri();

        String twitch = null;
        if(user.getTwitch() != null)
            youtube = user.getTwitch().getUri();

        String weblink = user.getWeblink();

        if(!TextUtils.isEmpty(name))
            bundle.putString(BUNDLE_KEY_NAME, name);
        if(!TextUtils.isEmpty(youtube))
            bundle.putString(BUNDLE_KEY_YOUTUBE, youtube);
        if(!TextUtils.isEmpty(twitch))
            bundle.putString(BUNDLE_KEY_TWITCH, twitch);
        if(!TextUtils.isEmpty(weblink))
            bundle.putString(BUNDLE_KEY_WEBLINK, weblink);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            name = getArguments().getString(BUNDLE_KEY_NAME);
            youtube = getArguments().getString(BUNDLE_KEY_YOUTUBE);
            twitch = getArguments().getString(BUNDLE_KEY_TWITCH);
            weblink = getArguments().getString(BUNDLE_KEY_WEBLINK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        TextView nameView = view.findViewById(R.id.user_name);
        TextView nameTextView = view.findViewById(R.id.user_name_text);
        TextView twitchView = view.findViewById(R.id.user_twitch);
        TextView twitchTextView = view.findViewById(R.id.user_twitch_text);
        TextView youtubeView = view.findViewById(R.id.user_youtube);
        TextView youtubeTextView = view.findViewById(R.id.user_youtube_text);
        TextView weblinkView = view.findViewById(R.id.user_weblink);
        TextView weblinkTextView = view.findViewById(R.id.user_weblink_text);

        if(!TextUtils.isEmpty(name))
            nameView.setText(name);
        else{
            nameView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(twitch))
            twitchView.setText(twitch);
        else{
            twitchTextView.setVisibility(View.GONE);
            twitchView.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(youtube))
            youtubeView.setText(youtube);
        else{
            youtubeTextView.setVisibility(View.GONE);
            youtubeView.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(weblink))
            weblinkView.setText(weblink);
        else{
            weblinkTextView.setVisibility(View.GONE);
            weblinkView.setVisibility(View.GONE);
        }
        return view;
    }
}
