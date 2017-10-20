package org.atlaslabs.speedrun.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpeedrunAPI {
    String BASE_URL = "http://www.speedrun.com/api/v1/";

    @GET("runs?status=verified&orderby=verify-date&direction=desc")
    Call<RunsResponse> getRecentRuns();
}
