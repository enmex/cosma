package com.imit.cosma.gui.animation;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

public class ContentAnimation {
    private CompoundAnimation compoundAnimation;

    public ContentAnimation(CompoundAnimation compoundAnimation){
        this.compoundAnimation = compoundAnimation;
    }
    public ContentAnimation(){
        this(null);
    }

    public void init(CompoundAnimation compoundAnimation, Path boardPath, Path screenPath){
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

        compoundAnimation.init(boardPath, originCenterScreenPath);
        this.compoundAnimation = compoundAnimation;

    }

    public void init(CompoundAnimation compoundAnimation, IntegerPoint boardPoint, IntegerPoint screenPoint) {
        IntegerPoint originBasedScreenPoint = new IntegerPoint(
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

    public boolean isAnimatedObject(IntegerPoint objectLocation){
        return compoundAnimation != null && compoundAnimation.isAnimatedObject(objectLocation);
    }
}