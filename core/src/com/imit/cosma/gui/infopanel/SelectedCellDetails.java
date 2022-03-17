package com.imit.cosma.gui.infopanel;

import com.imit.cosma.model.board.Content;

public class SelectedCellDetails {

    private ContentInformation contentInformation;

    public SelectedCellDetails(){
        contentInformation = new SpaceInformation(this);
    }

    public void setContentInformation(ContentInformation contentInformation){
        this.contentInformation = contentInformation;
    }

    public void init(int panelLeft, int panelBottom, int panelWidth, int panelHeight){
        contentInformation.init(panelLeft, panelBottom, panelWidth, panelHeight);
    }

    public void show(){
        contentInformation.show();
    }

    public void update(Content content){
        contentInformation.update(content);
    }

}
