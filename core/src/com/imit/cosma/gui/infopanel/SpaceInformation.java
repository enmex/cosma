package com.imit.cosma.gui.infopanel;

import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.spaceship.Spaceship;

public class SpaceInformation extends ContentInformation{

    private SelectedCellDetails parent;

    public SpaceInformation(SelectedCellDetails parent){
        this.parent = parent;
    }

    @Override
    public void init(int panelLeft, int panelBottom, int panelWidth, int panelHeight) { }

    @Override
    public void show() {}

    @Override
    public void update(Content content) {
        if(content.isShip()){
            parent.setContentInformation(new SpaceshipInformation(parent, (Spaceship) content));
        }
    }
}
