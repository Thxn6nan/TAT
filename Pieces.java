package tat;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pieces {
    public int col, row;
    protected int coordinate_x, coordinate_y;

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

    public Pieces(Board board, int sumCost, int attackRange, boolean isFirst) { 
        this.board = board;
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

    public void setCoordinate(int x, int y) {
        this.coordinate_x = x;
        this.coordinate_y = y;
    }

    public int[] getCoordinate() {
        return new int[]{coordinate_x, coordinate_y};
    }

    public void setRangeCost(int addRange) {
        this.addRange = addRange;
        attackRangeCost = attackRange + Math.max(0, addRange);
    }

    public int getRangeCost() {
        return attackRangeCost;
    }

    public int addPieceRange(){
        return addRange;
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