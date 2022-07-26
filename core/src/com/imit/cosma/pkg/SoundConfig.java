package com.imit.cosma.pkg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundConfig {
    public final Map<SoundType , Sound> soundBank;

    private static SoundConfig soundConfig;

    private SoundConfig() {
        soundBank = new HashMap<>();
        for(SoundType soundType : SoundType.values()) {
            soundBank.put(soundType, Gdx.audio.newSound(Gdx.files.internal(soundType.getPath())));
        }
    }

    public static SoundConfig getInstance() {
        if (soundConfig == null) {
            soundConfig = new SoundConfig();
        }

        return soundConfig;
    }
}
