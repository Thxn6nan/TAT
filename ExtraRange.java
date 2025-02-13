package tat;

import java.awt.Color;

public class ExtraRange extends BoardEvent {
    public ExtraRange(Board board, int col, int row){
        super(board, col, row);
        sumExtraRange();
    }

    public void sumExtraRange(){
        board.setTileColor(col, row, Color.GREEN);
    }
}
