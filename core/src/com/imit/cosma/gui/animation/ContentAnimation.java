package com.imit.cosma.gui.animation;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

import java.util.ArrayList;
import java.util.List;

public class ContentAnimation {
    private AnimationType animationType;

    public ContentAnimation(AnimationType animationType){
        this.animationType = animationType;
    }
    public ContentAnimation(){
        this(null);
    }

    public void init(AnimationType animationType, Path boardPath, Path screenPath){
        Path originCenterScreenPath = new Path(
                new IntegerPoint(
                        screenPath.getSource().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        screenPath.getSource().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                ),
                new IntegerPoint(
                        screenPath.getTarget().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        screenPath.getTarget().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                )
        );

        animationType.init(boardPath, originCenterScreenPath);
        this.animationType = animationType;

    }

    public void init(AnimationType animationType, IntegerPoint boardPoint, IntegerPoint screenPoint) {
        IntegerPoint originBasedScreenPoint = new IntegerPoint(
                screenPoint.x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                screenPoint.y + + Config.getInstance().BOARD_CELL_HEIGHT / 2
        );
        animationType.init(boardPoint, originBasedScreenPoint);
        this.animationType = animationType;

    }

    public void init(AnimationType animationType) {
        animationType.init();
        this.animationType = animationType;
    }

    public void render(float delta){
        animationType.render(delta);
    }

    public boolean isAnimated(){
        return animationType != null && animationType.isAnimated();
    }

    public boolean isAnimatedObject(IntegerPoint objectLocation){
        return animationType != null && animationType.isAnimated(objectLocation);
    }
}