package tat;

import java.awt.Color;

public class Wall extends BoardEvent {
    public Wall(Board board, int col, int row){
        super(board, col, row);
        sumWall();
    }

    public void sumWall(){
        board.setTileColor(col, row, Color.RED); // เปลี่ยนช่องเป็นสีแดง
    }
}
