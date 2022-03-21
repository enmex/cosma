package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.*;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.Idle;
import com.imit.cosma.gui.animation.simple.Movement;
import com.imit.cosma.gui.animation.simple.Rotation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

import java.util.List;

public class AttackAnimation extends AnimationType {
    private final List<Weapon> weaponList;
    private final Point shipAtlas;
    private final int mainAnimationIndex;
    private final int standingShipAnimationIndex;

    private Point sourceBoardCell;

    public AttackAnimation(Content content){
        super(((Spaceship)content).getWeapons().size() + 2, content.getSide().getDefaultRotation());
        this.weaponList = ((Spaceship)content).getWeapons();
        shipAtlas = content.getSprite();
        mainAnimationIndex = 0;
        standingShipAnimationIndex = 1;
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        super.init(boardPath, screenPath);

        this.sourceBoardCell = boardPath.getDeparture();

        //init main animation
        datas.get(mainAnimationIndex).rotation *= Math.signum(datas.get(0).rotation - defaultRotation);

        SimpleAnimation shipRotation = new Rotation(shipAtlas, getInstance().SHIP_SPRITE_SIZE, defaultRotation, defaultRotation + datas.get(mainAnimationIndex).rotation * Math.signum(screenPath.getDeparture().x - screenPath.getDestination().x));
        SimpleAnimation shipRotationToDefault = new Rotation(shipAtlas, getInstance().SHIP_SPRITE_SIZE, defaultRotation + datas.get(mainAnimationIndex).rotation * Math.signum(screenPath.getDeparture().x - screenPath.getDestination().x), defaultRotation);
        shipRotation.init(screenPath.getDeparture().x, screenPath.getDeparture().y, screenPath.getDestination().x, screenPath.getDestination().y, datas.get(mainAnimationIndex).rotation);
        shipRotationToDefault.init(screenPath.getDeparture().x, screenPath.getDeparture().y, screenPath.getDestination().x, screenPath.getDestination().y, datas.get(mainAnimationIndex).rotation);

        datas.get(mainAnimationIndex).phase.add(shipRotation);

        for(Weapon weapon : weaponList){
            Movement movement = new Movement(weapon.getShotSprite(), getInstance().SHOT_SPRITE_SIZE);
            Idle idle = new Idle(weapon.getExplosionSprite(), getInstance().SHOT_SPRITE_SIZE, screenPath.getDestination().x - screenPath.getDeparture().x, screenPath.getDestination().y - screenPath.getDeparture().y);

            movement.init(screenPath.getDeparture().x, screenPath.getDeparture().y, screenPath.getDestination().x, screenPath.getDestination().y, defaultRotation + datas.get(mainAnimationIndex).rotation * Math.signum(screenPath.getDeparture().x - screenPath.getDestination().x));

            phase.add(movement);
            phase.add(idle);
        }

        datas.get(mainAnimationIndex).phase.add(shipRotationToDefault);
        datas.get(mainAnimationIndex).phase.get(0).setAnimated();

        //init staticShip
        AnimationData staticShip = new AnimationData();
        Idle standing = new Idle(shipAtlas, getInstance().SHIP_SPRITE_SIZE, 0, 0,
                defaultRotation + datas.get(mainAnimationIndex).rotation * Math.signum(screenPath.getDeparture().x - screenPath.getDestination().x), getInstance().FRAMES_AMOUNT_SHIPS);
        staticShip.phase = new Array<>(1);
        staticShip.phase.add(standing);
        staticShip.offset = new Vector();
        staticShip.path = new Path(screenPath.getDeparture(), screenPath.getDeparture());
        staticShip.currentPhase = 0;

        datas.add(staticShip);
    }

    @Override
    public void render() {
        AnimationData data = datas.get(mainAnimationIndex);
        int currentPhase = data.currentPhase;

        //render standing ship between first and last phases
        AnimationData standingShipData = datas.get(standingShipAnimationIndex);

        if(currentPhase > 0 && currentPhase < data.phase.size - 1){
            standingShipData.getCurrentPhase().setAnimated();
        }
        else{
            standingShipData.getCurrentPhase().setNotAnimated();
        }

        datas.get(mainAnimationIndex).offset = datas.get(mainAnimationIndex).phase.get(currentPhase).getOffset();

        //render shots from second to last-1 phases
        data.phase.get(data.currentPhase).render();

        if (!data.phase.get(data.currentPhase).isAnimated()) {
            data.currentPhase++;
            if(data.currentPhase >= data.phase.size){
                clear();
            }
            else {
                data.phase.get(data.currentPhase).setAnimated();
            }
        }
    }

    @Override
    public boolean isAnimated(int x, int y) {
        return sourceBoardCell.x == x && sourceBoardCell.y == y;
    }
}
