package tat;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pieces {
    public int col, row; // for piece
    protected int coordinate_x, coordinate_y; // for graphic

    public String name;
    public int value;
    public boolean isFirst;
    public boolean invul = false;
    
    protected int attackRange;
    protected int sumCost;
    protected int attackRangeCost;
    protected int addRange = 1;
    
    protected Board board;
    protected BoardEvent event;
    BufferedImage sheet;
    BufferedImage sprite;

    public Pieces(Board board, int col, int row, int sumCost, int attackRange, boolean isFirst) { 
        this.board = board;
        this.col = col;
        this.row = row;
        this.sumCost = sumCost;
        this.attackRange = attackRange;
        this.isFirst = isFirst;
        
        try {
            sheet = ImageIO.read(getClass().getResource((isFirst == true) ? "/res/pawn.png" : "/res/pawn2.png")); // แสดงตัวหมากแต่ละฝั่ง
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void paint(Graphics2D g2d) {
        if (sprite != null) {
            g2d.drawImage(sprite, coordinate_x, coordinate_y, null);
        }
    }

    public String getName(){
        return name;
    }

    public void setRangeCost(int addRange) {
        this.addRange = addRange;
        attackRangeCost = attackRange + Math.max(0, addRange);
    }

    public int getRangeCost() {
        return attackRangeCost;
    }

    protected int addPieceRange(){
        return addRange;
    }

    public int getCol(){
        return col;
    }

    public int getRow(){
        return row;
    }

    public ArrayList<int[]> getAttackRange() {
        return new ArrayList<>();
    }

    public void setInvul(boolean invul){
        this.invul = invul;
    }

    public boolean isInvul(){
        return invul;
    }
}