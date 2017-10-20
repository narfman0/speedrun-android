package org.atlaslabs.speedrun.network.responses;

import org.atlaslabs.speedrun.models.Game;

public class GamesResponse {
    Game[] data;

    public Game[] getGames(){
        return data;
    }
}
