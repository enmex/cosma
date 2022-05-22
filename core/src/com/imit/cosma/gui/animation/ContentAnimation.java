package com.imit.cosma.gui.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationData;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ContentAnimation {
    private final TextureRegion SPACESHIP_ATLAS = new TextureRegion(new Texture(Config.getInstance().SPACESHIP_PATH));

    private Animation<TextureRegion> spriteAnimation;
    private Array<TextureRegion> frames;

    private AnimationType animationType;
    private SpriteBatch batch;
    private Sprite sprite;

    private int cellWidth, cellHeight;

    public ContentAnimation(AnimationType animationType){
        batch = new SpriteBatch();
        sprite = new Sprite(SPACESHIP_ATLAS);

        this.animationType = animationType;
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

    private void updateSpriteAnimation(AnimationData data){
        int framesAmount = data.getFramesAmount();
        this.frames = new Array<>(framesAmount);
        for(int i = 1; i < framesAmount; i++){
            int atlasX  =data.getAtlasCoords().x + data.getSpriteSize() * i;
            int atlasY = data.getAtlasCoords().y;
            this.frames.add(new TextureRegion(SPACESHIP_ATLAS,
                    data.getAtlasCoords().x + data.getSpriteSize() * i, data.getAtlasCoords().y, data.getSpriteSize(), data.getSpriteSize()));
        }
        spriteAnimation = new Animation<>(Config.getInstance().ANIMATION_DURATION, this.frames);
        spriteAnimation.setPlayMode(data.getPlayMode());
    }

    public void render(){
        for(AnimationData data : animationType.getDatas()) {
            if(data.getCurrentPhase().isAnimated()) {
                updateSpriteAnimation(data);
                batch.begin();

                sprite.setRegion(spriteAnimation.getKeyFrame(data.getElapsedTime(),
                        data.getPlayMode() != Animation.PlayMode.NORMAL));
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
    public boolean isAnimatedObject(int x, int y){
        return animationType != null && animationType.isAnimated(x, y);
    }
}
