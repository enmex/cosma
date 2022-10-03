package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

public class LootSpawnAnimation extends AnimationType {
    private final String lootSpawnAnimationPath;
    private IntegerPoint spawnBoardPoint;

    public LootSpawnAnimation(Loot loot) {
        this.lootSpawnAnimationPath = loot.getLootType().getSpawnAnimationPath();
    }

    @Override
    public void init(IntegerPoint boardPoint, IntegerPoint screenPoint) {
        this.spawnBoardPoint = boardPoint;

        AnimationData spawnAnimation = new AnimationData();
        spawnAnimation.currentPhase = 0;
        spawnAnimation.path = new Path(screenPoint, screenPoint);
        spawnAnimation.rotation = 0;
        spawnAnimation.phases = new Array<>(1);
        spawnAnimation.phases.add(
                new IdleAnimation(lootSpawnAnimationPath, Animation.PlayMode.NORMAL, screenPoint, 0)
        );
        spawnAnimation.getCurrentPhase().setAnimated();

        datas.add(spawnAnimation);
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return datas.size != 0 && objectLocation.equals(spawnBoardPoint);
    }

    @Override
    public boolean isAnimated() {
        return datas.size != 0;
    }
}
