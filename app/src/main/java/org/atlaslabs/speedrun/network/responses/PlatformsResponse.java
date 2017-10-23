package org.atlaslabs.speedrun.network.responses;

import org.atlaslabs.speedrun.models.Platform;

public class PlatformsResponse {
    private Platform[] data;

    public Platform[] getPlatforms(){
        return data;
    }
}
