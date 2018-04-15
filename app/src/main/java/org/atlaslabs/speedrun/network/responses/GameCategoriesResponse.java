package org.atlaslabs.speedrun.network.responses;

import org.atlaslabs.speedrun.models.Category;

public class GameCategoriesResponse {
    private Category[] data;

    public Category[] getCategories() {
        return data;
    }
}
