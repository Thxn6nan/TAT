package tat;

import java.awt.Color;

public class Barrier extends BoardEvent {
    public Barrier(Board board, int col, int row){
        super(board, col, row);
        sumBarrier();
    }

    public void sumBarrier(){
        board.setTileColor(col, row, Color.YELLOW);
    }
}
