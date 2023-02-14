package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class LootSpawnAnimation extends CompoundAnimation {
    private final String lootSpawnAnimationPath;
    private Point<Integer> spawnBoardPoint;

    public LootSpawnAnimation(Loot loot) {
        this.lootSpawnAnimationPath = loot.getLootType().getSpawnAnimationPath();
    }

    @Override
    public void init(Point<Integer> boardPoint, Point<Float> screenPoint) {
        this.spawnBoardPoint = boardPoint;

        SequentialObjectAnimation spawnAnimation = new SequentialObjectAnimation();
        spawnAnimation.currentPhase = 0;
        spawnAnimation.path = new Path<>(screenPoint, screenPoint);
        spawnAnimation.rotation = 0;
        spawnAnimation.phases = new Array<>(1);
        spawnAnimation.phases.add(
                new IdleAnimation(
                        lootSpawnAnimationPath,
                        Animation.PlayMode.NORMAL,
                        screenPoint,
                        0
                )
        );
        spawnAnimation.start();
        objectsAnimations.add(spawnAnimation);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public boolean isAnimatedObject(Point<Integer> objectLocation) {
        return isAnimated() && objectLocation.equals(spawnBoardPoint);
    }
}
