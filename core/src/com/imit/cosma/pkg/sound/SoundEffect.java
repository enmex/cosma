package com.imit.cosma.pkg.sound;

import com.badlogic.gdx.audio.Sound;

public class SoundEffect {
    private long soundId;
    private final Sound sound;

    public SoundEffect(SoundType soundType) {
        sound = SoundConfig.getInstance().soundBank.get(soundType);
    }

    public void playLoop() {
        soundId = sound.loop();
    }

    public void play() {
        soundId = sound.play();
    }

    public void stop() {
        sound.stop(soundId);
    }
}
