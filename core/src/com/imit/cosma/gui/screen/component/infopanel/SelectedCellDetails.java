package com.imit.cosma.gui.screen.component.infopanel;

import com.imit.cosma.model.board.content.Content;

public class SelectedCellDetails {

    private ContentInformation contentInformation;

    public SelectedCellDetails(){
        contentInformation = new SpaceInformation(this);
    }

    public void setContentInformation(ContentInformation contentInformation){
        this.contentInformation = contentInformation;
    }

    public void init(int componentLeft, int componentBottom, int componentWidth, int componentHeight){
        contentInformation.init(componentLeft, componentBottom, componentWidth, componentHeight);
    }

    public void render(){
        contentInformation.render();
    }

    public void update(Content content){
        contentInformation.update(content);
    }
}
