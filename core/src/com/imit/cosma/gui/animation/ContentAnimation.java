package com.imit.cosma.gui.animation;

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

    public void init(AnimationType animationType, Path boardPath, int cellWidth, int cellHeight, int boardY){
        Path screenPath = new Path(
                new Point(
                        boardPath.getSource().x * cellWidth,
                        boardPath.getSource().y * cellHeight + boardY
                ),
                new Point(
                        boardPath.getTarget().x * cellWidth,
                        boardPath.getTarget().y * cellHeight + boardY
                )
        );

        animationType.init(boardPath, screenPath);
        setAnimationType(animationType);

    }

    public void init(AnimationType animationType, Point boardPoint, int cellWidth, int cellHeight, int boardY) {
        Point screenPoint = new Point(boardPoint.x * cellWidth,
                boardPoint.y * cellHeight + boardY);
        animationType.init(boardPoint, screenPoint);
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