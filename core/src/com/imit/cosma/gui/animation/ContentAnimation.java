package com.imit.cosma.gui.animation;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ContentAnimation {
    private AnimationType animationType;

    public ContentAnimation(AnimationType animationType){
        this.animationType = animationType;
    }
    public ContentAnimation(){
        this(null);
    }

    private void setAnimationType(AnimationType animationType){
        this.animationType = animationType;
    }

    public void init(AnimationType animationType, Path boardPath, Path screenPath){
        Path originCenterScreenPath = new Path(
                new Point(
                        screenPath.getSource().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        screenPath.getSource().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                ),
                new Point(
                        screenPath.getTarget().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        screenPath.getTarget().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                )
        );

        animationType.init(boardPath, originCenterScreenPath);
        setAnimationType(animationType);

    }

    public void init(AnimationType animationType, Point boardPoint, Point screenPoint) {
        Point originBasedScreenPoint = new Point(
                screenPoint.x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                screenPoint.y + + Config.getInstance().BOARD_CELL_HEIGHT / 2
        );
        animationType.init(boardPoint, originBasedScreenPoint);
        setAnimationType(animationType);

    }

    public void render(float delta){
        animationType.render(delta);
    }

    public boolean isAnimated(){
        return animationType != null && animationType.isAnimated();
    }

    public boolean isAnimatedObject(Point objectLocation){
        return animationType != null && animationType.isAnimated(objectLocation);
    }
}