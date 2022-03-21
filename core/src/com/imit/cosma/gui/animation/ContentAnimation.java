package com.imit.cosma.gui.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationData;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

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
        this.animationType = animationType;
    }

    public void init(AnimationType animationType, Path boardPath, int cellWidth, int cellHeight, int boardY){
        Path screenPath = new Path(new Point(boardPath.getDeparture().x * cellWidth, boardPath.getDeparture().y * cellHeight + boardY), new Point(boardPath.getDestination().x * cellWidth, boardPath.getDestination().y * cellHeight + boardY));
        animationType.init(boardPath, screenPath);
        setRegion(animationType);
    }

    private void updateSpriteAnimation(AnimationData data){
        int framesAmount = data.getFramesAmount();
        this.frames = new Array<>(framesAmount);
        for(int i = 0; i < framesAmount; i++){
            this.frames.add(new TextureRegion(SPACESHIP_ATLAS,
                    data.getAtlas().x + data.getSpriteSize() * (i+1), data.getAtlas().y, data.getSpriteSize(), data.getSpriteSize()));
        }
        spriteAnimation = new Animation<>(ANIMATION_DURATION, this.frames);
        spriteAnimation.setPlayMode(data.getPlayMode());
    }

    public void render(int cellWidth, int cellHeight){
        elapsedTime += Gdx.graphics.getDeltaTime();

        for(AnimationData data : animationType.getDatas()) {
            if(data.getCurrentPhase().isAnimated()) {
                updateSpriteAnimation(data);
                batch.begin();

                sprite.setRegion(spriteAnimation.getKeyFrame(elapsedTime, true));
                sprite.setBounds(data.getPath().getDeparture().x + data.getOffset().getX(),
                        data.getPath().getDeparture().y + data.getOffset().getY(), cellWidth, cellHeight);
                sprite.setOrigin((float) cellWidth / 2, (float) cellHeight / 2);
                sprite.setRotation(data.getCurrentRotation());
                sprite.draw(batch);

                batch.end();
            }
        }

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
