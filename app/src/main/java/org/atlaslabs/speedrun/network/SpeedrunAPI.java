package org.atlaslabs.speedrun.network;

import org.atlaslabs.speedrun.network.responses.CategoryResponse;
import org.atlaslabs.speedrun.network.responses.GamesResponse;
import org.atlaslabs.speedrun.network.responses.PlatformsResponse;
import org.atlaslabs.speedrun.network.responses.RunsResponse;
import org.atlaslabs.speedrun.network.responses.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpeedrunAPI {
    String BASE_URL = "http://www.speedrun.com/api/v1/";

    @GET("categories/{id}")
    Observable<CategoryResponse> getCategory(
            @Path("id") String id);

    @GET("games?_bulk=yes&max=1000")
    Observable<GamesResponse> getGamesBulk(
            @Query("offset") int offset);

    @GET("platforms?_bulk=yes&max=1000")
    Observable<PlatformsResponse> getPlatformsBulk();

    @GET("runs?status=verified&orderby=verify-date&direction=desc")
    Observable<RunsResponse> getRunsRecent();

    @GET("users/{id}")
    Observable<UserResponse> getUser(
            @Path("id") String id);
}
