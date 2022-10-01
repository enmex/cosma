package com.imit.cosma.pkg.soundtrack.sound;

import com.badlogic.gdx.audio.Sound;

import static com.imit.cosma.config.Config.*;

public class SoundEffect {
    private long soundId;
    private Sound sound;

    public SoundEffect(SoundType soundType) {
        sound = null;//SoundConfig.getInstance().soundBank.get(soundType);
    }

    public SoundEffect() {}

    public void playLoop() {
        if (sound != null && getInstance().SOUNDS_ON) {
            soundId = sound.loop();
        }
    }

    public void play() {
        if (sound != null && getInstance().SOUNDS_ON) {
            soundId = sound.play();
        }
    }

    public void stop() {
        if (sound != null && getInstance().SOUNDS_ON) {
            sound.stop(soundId);
        }
    }
}
