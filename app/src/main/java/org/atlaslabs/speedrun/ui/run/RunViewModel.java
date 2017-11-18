package org.atlaslabs.speedrun.ui.run;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Platform;
import org.atlaslabs.speedrun.models.User;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

public class RunViewModel extends ViewModel {
    private final static String TAG = RunViewModel.class.getSimpleName();
    private String id, game, platform, category, comment, videos;
    private float time;
    private List<String> userIDs;
    public final MutableLiveData<String>
            gameName = new MutableLiveData<>(),
            platformName = new MutableLiveData<>(),
            categoryName = new MutableLiveData<>();
    public final MutableLiveData<User> user = new MutableLiveData<>();

    /**
     * Load data asynchronously. Expected that observers are attached to names of things
     */
    public void load() {
        Realm realm = Realm.getDefaultInstance();
        loadGameName(realm);
        loadUserNames(realm);
        loadPlatform(realm);
        loadCategory(realm);
        realm.close();
    }

    private void loadCategory(Realm realm) {
        if (category != null)
            Category.getOrFetch(realm, category)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((category) -> categoryName.setValue(category.getName()));
        else
            Log.i(TAG, "No category given for run: " + id);
    }

    private void loadPlatform(Realm realm) {
        if (platform != null)
            Platform.getOrFetch(realm, platform)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((platform) -> platformName.setValue(platform.getName()));
        else
            Log.i(TAG, "No platform given for run: " + id);
    }

    private void loadUserNames(Realm realm) {
        if (userIDs != null)
            for (String userID : userIDs) {
                User.getOrFetch(realm, userID)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((user) -> {
                            if (user != null && user.getId() != null && user.getNamePretty() != null)
                                RunViewModel.this.user.setValue(user);
                        });
                break;
            }
        else
            Log.i(TAG, "No userIDs given for run: " + id);
    }

    private void loadGameName(Realm realm) {
        if (game != null)
            Game.getOrFetch(realm, game)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((game) -> gameName.setValue(game.getNames().getInternational()));
        else
            Log.i(TAG, "No game given for run: " + id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }
}