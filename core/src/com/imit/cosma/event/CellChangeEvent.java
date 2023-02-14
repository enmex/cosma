package com.imit.cosma.event;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.board.content.Content;

public class CellChangeEvent extends Event {
    private final Cell cell;

    public CellChangeEvent(Cell cell) {
        this.cell = cell;
    }

    public Content getContent() {
        return cell.getContent();
    }
}
