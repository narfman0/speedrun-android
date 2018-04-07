package org.atlaslabs.speedrun.network.responses;

import org.atlaslabs.speedrun.network.models.Run;

public class RunsResponse {
    private Run[] data;

    public Run[] getRuns() {
        return data;
    }
}
