package org.atlaslabs.speedrun.network;

import org.atlaslabs.speedrun.network.responses.CategoryResponse;
import org.atlaslabs.speedrun.network.responses.GameCategoriesResponse;
import org.atlaslabs.speedrun.network.responses.GameResponse;
import org.atlaslabs.speedrun.network.responses.GamesResponse;
import org.atlaslabs.speedrun.network.responses.PlatformResponse;
import org.atlaslabs.speedrun.network.responses.PlatformsResponse;
import org.atlaslabs.speedrun.network.responses.RunsResponse;
import org.atlaslabs.speedrun.network.responses.UserResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpeedrunAPI {
    String BASE_URL = "http://www.speedrun.com/api/v1/";

    @GET("categories/{id}")
    Single<CategoryResponse> getCategory(
            @Path("id") String id);

    @GET("games/{id}")
    Single<GameResponse> getGame(
            @Path("id") String id);

    @GET("games?_bulk=yes&max=1000")
    Single<GamesResponse> getGamesBulk(
            @Query("offset") int offset);

    @GET("games/{id}/categories")
    Single<GameCategoriesResponse> getGameCategories(
            @Path("id") String id);

    @GET("platforms?_bulk=yes&max=1000")
    Single<PlatformsResponse> getPlatformsBulk();

    @GET("platforms/{id}")
    Single<PlatformResponse> getPlatform(
            @Path("id") String id);

    @GET("runs?status=verified&orderby=verify-date&direction=desc")
    Single<RunsResponse> getRunsRecent();

    @GET("users/{id}")
    Single<UserResponse> getUser(
            @Path("id") String id);
}
