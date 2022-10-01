package com.imit.cosma.pkg.soundtrack.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.HashMap;
import java.util.Map;

public class MusicConfig {
    public final Map<MusicType, Music> musicBank;

    private static MusicConfig instance;

    private MusicConfig() {
        musicBank = new HashMap<>();
        for (MusicType musicType : MusicType.values()) {
            musicBank.put(musicType, Gdx.audio.newMusic(Gdx.files.internal(musicType.getMusicPath())));
        }
    }

    public static MusicConfig getInstance() {
        if (instance == null) {
            instance = new MusicConfig();
        }

        return instance;
    }
}
