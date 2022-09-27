package com.imit.cosma.pkg.sound;

import com.badlogic.gdx.audio.Sound;

import static com.imit.cosma.config.Config.*;

public class SoundEffect {
    private long soundId;
    private Sound sound;

    public SoundEffect(SoundType soundType) {
        sound = SoundConfig.getInstance().soundBank.get(soundType);
    }

    public SoundEffect() {}

    public void playLoop() {
        System.out.println(getInstance().SOUNDS_ON);
        if (sound != null && getInstance().SOUNDS_ON) {
            soundId = sound.loop();
        }
    }

    public void play() {
        System.out.println(getInstance().SOUNDS_ON);
        if (sound != null && getInstance().SOUNDS_ON) {
            soundId = sound.play();
        }
    }

    public void stop() {
        System.out.println(getInstance().SOUNDS_ON);
        if (sound != null && getInstance().SOUNDS_ON) {
            sound.stop(soundId);
        }
    }
}
