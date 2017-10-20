package org.atlaslabs.speedrun.network.responses;

import org.atlaslabs.speedrun.models.Run;

public class RunsResponse {
    Run[] data;

    public Run[] getRuns(){
        return data;
    }
}
