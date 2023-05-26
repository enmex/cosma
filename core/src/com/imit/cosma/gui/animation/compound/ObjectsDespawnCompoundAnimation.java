package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.imit.cosma.gui.animation.simple.StaticAnimation;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ObjectsDespawnCompoundAnimation extends CompoundAnimation {
    public ObjectsDespawnCompoundAnimation(MutualLinkedMap<GameObject, Point<Float>> objectsToLocations) {
        super();
        for (GameObject gameObject : objectsToLocations.keySet()) {
            Point<Float> objectLocation = objectsToLocations.getValue(gameObject);
            Point<Float> originObjectLocation = CoordinateConverter.toOriginCenter(objectLocation);
            SequentialObjectAnimation objectDespawnAnimation = new SequentialObjectAnimation(0, new Path<Float>(originObjectLocation, originObjectLocation));
            objectDespawnAnimation.phases.add(new StaticAnimation(
                    gameObject.getDespawnAnimationPath(),
                    Animation.PlayMode.NORMAL,
                    originObjectLocation,
                    0
            ));
            animatedObjectsLocations.add(objectLocation);
            objectsAnimations.add(objectDespawnAnimation);
        }
    }

    @Override
    public void start() {
        for (SequentialObjectAnimation sequentialObjectAnimation : objectsAnimations) {
            sequentialObjectAnimation.start();
        }
    }
}
