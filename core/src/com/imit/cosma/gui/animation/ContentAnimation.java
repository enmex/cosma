package com.imit.cosma.gui.animation;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ContentAnimation {
    private CompoundAnimation compoundAnimation;

    public ContentAnimation(CompoundAnimation compoundAnimation){
        this.compoundAnimation = compoundAnimation;
    }
    public ContentAnimation(){
        this(null);
    }

    public void init(CompoundAnimation compoundAnimation, Path<Integer> boardPath, Path<Float> screenPath){
        Path<Float> originCenterScreenPath = new Path<>(
                new Point<>(
                        screenPath.getSource().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        screenPath.getSource().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                ),
                new Point<>(
                        screenPath.getTarget().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        screenPath.getTarget().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                )
        );

        compoundAnimation.init(boardPath, originCenterScreenPath);
        this.compoundAnimation = compoundAnimation;

    }

    public void init(CompoundAnimation compoundAnimation, Point<Integer> boardPoint, Point<Float> screenPoint) {
        Point<Float> originBasedScreenPoint = new Point<>(
                screenPoint.x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                screenPoint.y + + Config.getInstance().BOARD_CELL_HEIGHT / 2
        );
        compoundAnimation.init(boardPoint, originBasedScreenPoint);
        this.compoundAnimation = compoundAnimation;

    }

    public void init(CompoundAnimation compoundAnimation) {
        compoundAnimation.init();
        this.compoundAnimation = compoundAnimation;
    }

    public void render(float delta){
        compoundAnimation.render(delta);
    }

    public boolean isAnimated(){
        return compoundAnimation != null && compoundAnimation.isAnimated();
    }

    public boolean isAnimatedObject(Point<Integer> objectLocation){
        return compoundAnimation != null && compoundAnimation.isAnimatedObject(objectLocation);
    }
}