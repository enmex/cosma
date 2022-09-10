package com.imit.cosma.gui.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationData;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ContentAnimation {

    private Animation<TextureRegion> spriteAnimation;

    private AnimationType animationType;
    private final SpriteBatch batch;
    private final Sprite sprite;

    private int cellWidth, cellHeight;

    public ContentAnimation(AnimationType animationType){
        batch = new SpriteBatch();
        this.animationType = animationType;
        sprite = new Sprite();
    }
    public ContentAnimation(){
        this(null);
    }

    private void setAnimationType(AnimationType animationType){
        this.animationType = animationType;
    }

    public void init(AnimationType animationType, Path boardPath, int cellWidth, int cellHeight, int boardY){
        Path screenPath = new Path(new Point(boardPath.getSource().x * cellWidth,
                boardPath.getSource().y * cellHeight + boardY),
                new Point(boardPath.getTarget().x * cellWidth,
                        boardPath.getTarget().y * cellHeight + boardY));

        animationType.init(boardPath, screenPath);
        setAnimationType(animationType);

        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
    }

    public void init(AnimationType animationType, Point boardPoint, int cellWidth, int cellHeight, int boardY) {
        Point screenPoint = new Point(boardPoint.x * cellWidth,
                boardPoint.y * cellHeight + boardY);
        animationType.init(boardPoint, screenPoint);
        setAnimationType(animationType);

        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
    }

    private void updateSpriteAnimation(AnimationData data){
        String atlasPath = data.getAtlasPath();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        spriteAnimation = new Animation<TextureRegion>(Config.getInstance().ANIMATION_DURATION, atlas.findRegions(data.getRegionName()));
        spriteAnimation.setPlayMode(data.getPlayMode());
    }

    public void render(){
        for(AnimationData data : animationType.getDatas()) {
            if(data.getCurrentPhase().isAnimated()) {
                updateSpriteAnimation(data);
                batch.begin();

                TextureRegion currentFrame = spriteAnimation.getKeyFrame(data.getElapsedTime(),
                        data.getPlayMode() != Animation.PlayMode.NORMAL);

                sprite.setRegion(currentFrame);

                sprite.setBounds(data.getPath().getSource().x + data.getOffset().getX(),
                        data.getPath().getSource().y + data.getOffset().getY(), cellWidth, cellHeight);
                sprite.setOrigin((float) cellWidth / 2, (float) cellHeight / 2);
                sprite.setRotation(data.getCurrentRotation());
                sprite.draw(batch);

                batch.end();
            }
        }
        setAnimationType(animationType);
        if(isAnimated()) {
            animationType.render();
        }
    }

    public boolean isAnimated(){
        return animationType != null && animationType.isAnimated();
    }

    public boolean isAnimatedObject(Point objectLocation){
        return animationType != null && animationType.isAnimated(objectLocation);
    }
}