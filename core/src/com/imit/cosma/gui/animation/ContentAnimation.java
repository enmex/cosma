package com.imit.cosma.gui.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AttackAnimation;
import com.imit.cosma.util.Path;
//может анимировать что-то одно
public class ContentAnimation {
    private final float ANIMATION_DURATION = 0.1f;
    private final TextureRegion SPACESHIP_ATLAS = new TextureRegion(new Texture(Config.getInstance().SPACESHIP_PATH));

    private Animation<TextureRegion> spriteAnimation;
    private Array<TextureRegion> frames;

    private AnimationType animationType;
    private SpriteBatch batch;
    private Sprite sprite;

    private float elapsedTime;

    public ContentAnimation(AnimationType animationType){
        batch = new SpriteBatch();
        sprite = new Sprite(SPACESHIP_ATLAS);
        elapsedTime = 0f;

        this.animationType = animationType;
    }
    public ContentAnimation(){
        this(null);
    }

    private void setRegion(AnimationType animationType){
        int framesAmount = animationType.getFramesAmount();
        this.frames = new Array<>(framesAmount);
        for(int i = 0; i < framesAmount; i++){
            this.frames.add(new TextureRegion(SPACESHIP_ATLAS,
                    animationType.getAtlas().x + animationType.getSpriteSize() * (i+1), animationType.getAtlas().y, animationType.getSpriteSize(), animationType.getSpriteSize()));
        }
        spriteAnimation = new Animation<>(ANIMATION_DURATION, this.frames);
        spriteAnimation.setPlayMode(animationType.getPlayMode());
        this.animationType = animationType;
    }

    public void init(AnimationType animationType, Path path, int cellWidth, int cellHeight, int boardY){
        animationType.init(path.getDestination().x, path.getDestination().y, path.getDeparture().x * cellWidth,
                path.getDeparture().y * cellHeight + boardY
                , path.getDestination().x * cellWidth, path.getDestination().y * cellHeight + boardY);
        setRegion(animationType);
    }

    public void render(int cellWidth, int cellHeight){
        elapsedTime += Gdx.graphics.getDeltaTime();

        batch.begin();
        sprite.setRegion(spriteAnimation.getKeyFrame(elapsedTime, true));
        sprite.setBounds(animationType.path.getDeparture().x + animationType.offset.getX(),
                animationType.path.getDeparture().y + animationType.offset.getY(), cellWidth, cellHeight);
        sprite.setOrigin((float) cellWidth / 2, (float) cellHeight / 2);
        sprite.setRotation(animationType.getCurrentRotation());
        sprite.draw(batch);
        batch.end();

        setRegion(animationType);
        animationType.render();
    }

    public boolean isAnimated(){
        return animationType != null && animationType.isAnimated();
    }
    public boolean isAnimatedObject(int x, int y){
        return animationType != null && animationType.isAnimated(x, y);
    }
}
