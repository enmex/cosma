package com.imit.cosma.pkg.sound;

import com.badlogic.gdx.audio.Sound;

public class SoundEffect {
    private long soundId;
    private Sound sound;

    public SoundEffect(SoundType soundType) {
        sound = SoundConfig.getInstance().soundBank.get(soundType);
    }

    public SoundEffect() {}

    public void playLoop() {
        if (sound != null) {
            soundId = sound.loop();
        }
    }

    public void play() {
        if (sound != null) {
            soundId = sound.play();
        }
    }

    public void stop() {
        if (sound != null) {
            sound.stop(soundId);
        }
    }
}
