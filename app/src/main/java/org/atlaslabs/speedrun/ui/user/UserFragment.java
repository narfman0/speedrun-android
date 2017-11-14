package org.atlaslabs.speedrun.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.databinding.FragmentUserBinding;
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
        bundle.putString(BUNDLE_KEY_NAME, user.getNamePretty());
        bundle.putString(BUNDLE_KEY_YOUTUBE, user.getYoutubePretty());
        bundle.putString(BUNDLE_KEY_TWITCH, user.getTwitchPretty());
        bundle.putString(BUNDLE_KEY_WEBLINK, user.getWeblink());
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
        FragmentUserBinding binding = FragmentUserBinding.inflate(inflater);

        if(!TextUtils.isEmpty(name))
            binding.userName.setText(name);
        else{
            binding.userName.setVisibility(View.GONE);
            binding.userNameText.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(twitch))
            binding.userTwitch.setText(twitch);
        else{
            binding.userTwitchText.setVisibility(View.GONE);
            binding.userTwitch.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(youtube))
            binding.userYoutube.setText(youtube);
        else{
            binding.userYoutubeText.setVisibility(View.GONE);
            binding.userYoutube.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(weblink))
            binding.userWeblink.setText(weblink);
        else{
            binding.userWeblinkText.setVisibility(View.GONE);
            binding.userWeblink.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }
}
