package org.atlaslabs.speedrun.network.responses;

import org.atlaslabs.speedrun.network.models.Platform;

public class PlatformsResponse {
    private Platform[] data;

    public Platform[] getPlatforms() {
        return data;
    }
}
