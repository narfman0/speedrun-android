package org.atlaslabs.speedrun.network.models;

import java.util.List;

public class Run {
    public String id;
    public String game;
    public String category;
    public String submitted;
    public String comment;
    public Times times;
    public System system;
    public List<User> players;
    public Videos videos;
}
