package tat;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.awt.*;
import javax.swing.*;


public class BoardEvent {
    public int col, row;
    protected int coordinate_x, coordinate_y;
    private int turn = 5; // อาจลบออก หากเซตใน main
    protected Board board;

    private static final Random random = new Random();

    // ตัวแปรเก็บตำแหน่งของอีเวนต์ที่เกิดขึ้น
    private final Set<Point> eventPositions = new HashSet<>();

    public BoardEvent(Board board, int col, int row) {
        this.board = board;
        this.col = col;
        this.row = row;
    }

    public void setCoordinate(int x, int y) {
        this.coordinate_x = x;
        this.coordinate_y = y;
    }

    public void setTurn(int turn) { // main
        this.turn = turn;
    }

    public void sumEvent(int piece_col, int piece_row) {
        if (turn > 4 && turn % 4 != 0) {
            int c = random.nextInt(9);
            int r = random.nextInt(8);

            // ถ้าทับตัวหมากหรืออีเวนต์ที่มีอยู่แล้ว สุ่มใหม่
            while (c == piece_col && r == piece_row || eventPositions.contains(new Point(c, r))) {
                c = random.nextInt(9);
                r = random.nextInt(8);
            }
            // สุ่ม wall อีเวนต์ 1 อัน
            board.spawnEvent("wall", c, r);
            eventPositions.add(new Point(c, r));

            // สุ่มอีเวนต์เพิ่ม 3 อัน
            for (int i = 0; i < 3; i++) {
                int eventType = random.nextInt(3); // 3 ประเภท
                c = random.nextInt(9);
                r = random.nextInt(8);

                // ถ้าทับตัวหมากหรืออีเวนต์ที่มีอยู่แล้ว สุ่มใหม่
                while (c == piece_col && r == piece_row || eventPositions.contains(new Point(c, r))) {
                    c = random.nextInt(9);
                    r = random.nextInt(8);
                }
                getEvent(eventType, c, r);
                eventPositions.add(new Point(c, r)); // เพิ่มตำแหน่งอีเวนต์ที่สุ่มใหม่
            }
        } else if (turn % 4 == 0) {
            board.removeAllEvent();
            eventPositions.clear();
        }
    }

    private void getEvent(int eventType, int c, int r) {
        switch (eventType) {
            case 0 -> board.spawnEvent("barrier", c, r);
            case 1 -> board.spawnEvent("extraRange", c, r);
            case 2 -> board.spawnEvent("wall", c, r);
            default -> {}
        }
    }

    public boolean isBlockOath() {
        return turn == 1;
    }
}
