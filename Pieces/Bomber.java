package tat;

import java.util.ArrayList;

import tat.Pieces;

import java.awt.*;
import java.awt.image.BufferedImage;

class Bomber extends Pieces {
    public Bomber(Board board, int col, int row, boolean isFirst) {
        super(board, col, row, 6, 1, isFirst);
        this.col = col;
        this.row = row;
        this.coordinate_x = col * board.tileSize;
        this.coordinate_y = row * board.tileSize;
        this.isFirst = isFirst;
        this.name = "Bomber";

        int sheetScale = sheet.getWidth();
        int imgHeight = sheet.getHeight();
        int yPos = isFirst ? 0 : sheetScale;

        if (yPos + sheetScale > imgHeight) {
            yPos = 0; // ป้องกันค่าเกินขนาดภาพ
        }

        // ดึงภาพย่อยจาก sprite sheet
        BufferedImage subImage = sheet.getSubimage(0, yPos, sheetScale, sheetScale);

        // สร้าง BufferedImage ใหม่เพื่อเก็บภาพที่ถูกย่อขนาด
        this.sprite = new BufferedImage(board.tileSize, board.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = this.sprite.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(subImage.getScaledInstance(board.tileSize, board.tileSize, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();
    }

    @Override
    public String getName(){
        return name;
    }
    
    // คำนวณระยะโจมตีได้
    public ArrayList<Point> getAttackTiles() {
        ArrayList<Point> tiles = new ArrayList<>();
        int maxRange = Math.min(1 + 0, 1); // ระยะเริ่มต้น + ระยะที่เพิ่ม (ระยะสูงสุดคือ 4)

        // ตรวจสอบรอบตัว
        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, // แนวตรง
            {1, 1}, {-1, -1}, {1, -1}, {-1, 1} // แนวเฉียง
        };
    
        for (int[] dir : directions) {
            for (int i = 1; i <= maxRange; i++) {
                int newCol = col + dir[0] * i;
                int newRow = row + dir[1] * i;
    
                Pieces piece = board.getPieces(newCol, newRow);
                tiles.add(new Point(newCol, newRow));
    
                if (piece != null) {
                    break; // เดินทับได้แต่ไม่ทะลุ
                }
            }
        }

        return tiles;
    }
}
