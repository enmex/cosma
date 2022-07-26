package com.imit.cosma.pkg;

import com.badlogic.gdx.audio.Sound;

public class SoundEffect {
    private long soundId;
    private final Sound sound;

    public SoundEffect(SoundType soundType) {
        sound = SoundConfig.getInstance().soundBank.get(soundType);
    }

    public void play() {
        soundId = sound.loop();
    }

    public void stop() {
        sound.stop(soundId);
    }
}
