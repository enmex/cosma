package com.imit.cosma.pkg.soundtrack.music;

import com.imit.cosma.config.Config;

public enum MusicType {
    MAIN_MENU_OST("");

    private final String musicPath;

    MusicType(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getMusicPath() {
        return musicPath;
    }
}
