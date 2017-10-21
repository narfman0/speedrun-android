package org.atlaslabs.speedrun.network;

import org.atlaslabs.speedrun.network.responses.GamesResponse;
import org.atlaslabs.speedrun.network.responses.RunsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpeedrunAPI {
    String BASE_URL = "http://www.speedrun.com/api/v1/";

    @GET("runs?status=verified&orderby=verify-date&direction=desc")
    Observable<RunsResponse> getRecentRuns();

    @GET("games?_bulk=yes&max=1000")
    Observable<GamesResponse> getGamesBulk(
            @Query("offset") String offset);
}
