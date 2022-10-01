package com.imit.cosma.pkg.soundtrack.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;
import com.imit.cosma.config.Config;

public class CosmaMusic {
    private final Music music;

    public CosmaMusic(MusicType musicType) {
        this.music = MusicConfig.getInstance().musicBank.get(musicType);
    }

    public void play() {
        music.play();
    }

    public void stop() {
        music.stop();
    }

    public void stopSmoothFading(float fadeInterval) {
        final float fadeStep = Config.getInstance().MUSIC_FADE_STEP;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (music.getVolume() >= fadeStep) {
                    music.setVolume(music.getVolume() - fadeStep);
                } else {
                    music.stop();
                    this.cancel();
                }
            }
        }, 0f, fadeInterval);
    }

}
