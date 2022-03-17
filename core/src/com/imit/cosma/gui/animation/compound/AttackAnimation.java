package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.gui.animation.AnimationType;
import com.imit.cosma.gui.animation.simple.Idle;
import com.imit.cosma.gui.animation.simple.Movement;
import com.imit.cosma.gui.animation.simple.Rotation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.util.Point;

import java.util.List;

import static com.imit.cosma.config.Config.*;

public class AttackAnimation extends AnimationType {
    private List<Weapon> weaponList;
    private Point shipAtlas;

    private SimpleAnimation shipRotation, shipRotationToDefault;

    private Point sourceBoardCell;

    public AttackAnimation(Content content){
        super(((Spaceship)content).getWeapons().size() + 2, content.getSide().getDefaultRotation());
        this.weaponList = ((Spaceship)content).getWeapons();
        shipAtlas = content.getSprite();
    }

    @Override
    public void init(int selectedBoardX, int selectedBoardY, int fromX, int fromY, int toX, int toY) {
        super.init(selectedBoardX, selectedBoardY, fromX, fromY, toX, toY);

        sourceBoardCell = new Point(selectedBoardX * fromX / toX, selectedBoardY * fromY / toY);

        rotation *= Math.signum(rotation - defaultRotation);

        shipRotation = new Rotation(shipAtlas, getInstance().SHIP_SPRITE_SIZE, defaultRotation, defaultRotation + rotation * Math.signum(fromX - toX));
        shipRotationToDefault = new Rotation(shipAtlas, getInstance().SHIP_SPRITE_SIZE,  defaultRotation + rotation * Math.signum(fromX - toX), defaultRotation);

        shipRotation.init(fromX, fromY, toX, toY, rotation);
        shipRotationToDefault.init(fromX, fromY, toX, toY, rotation);

        phase.add(shipRotation);

        for(Weapon weapon : weaponList){
            Movement movement = new Movement(weapon.getShotSprite(), getInstance().SHOT_SPRITE_SIZE);
            Idle idle = new Idle(weapon.getExplosionSprite(), getInstance().SHOT_SPRITE_SIZE, toX - fromX, toY - fromY);

            movement.init(fromX, fromY, toX, toY, defaultRotation + rotation * Math.signum(fromX - toX));

            phase.add(movement);
            phase.add(idle);
        }

        phase.add(shipRotationToDefault);
        currentPhase = 0;
        phase.get(0).setAnimated();
    }

    @Override
    public void render() {
        super.render();
        if(currentPhase < phase.size()) {
            offset = phase.get(currentPhase).getOffset();
        }
    }

    @Override
    public boolean isAnimated(int x, int y) {
        return sourceBoardCell.x == x && sourceBoardCell.y == y;
    }
}
