package com.imit.cosma.fabric;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackAnimation;
import com.imit.cosma.gui.animation.compound.MovementAnimation;
import com.imit.cosma.model.board.BoardState;
import com.imit.cosma.model.board.Content;

public class AnimationFabric {

    public static AnimationType getAnimation(BoardState state, Content content){
        switch (state){
            case SHIP_MOVING:
                return new MovementAnimation(content);
            case SHIP_ATTACKING:
               // return new AttackAnimation(content);
            default:
                return null;
        }
    }
}
