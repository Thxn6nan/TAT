package tat;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import tat.Pieces;

class Gunman extends Pieces {
    public Gunman(Board board, int col, int row, boolean isFirst) {
        super(board, col, row, 2, 1, isFirst);
        this.col = col;
        this.row = row;
        this.coordinate_x = col * board.tileSize;
        this.coordinate_y = row * board.tileSize;
        this.isFirst = isFirst;
        this.name = "gunman";

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
        int maxRange = Math.min(2 + addPieceRange() + (board.isPieceOnExtraRange(this) ? 2 : 0), 4); // ระยะเริ่มต้น + ระยะที่เพิ่ม (ระยะสูงสุดคือ 4)

        // ตรวจสอบแนวเฉียง
        int[][] diagonalDirections = {
            {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
        };
    
        for (int[] dir : diagonalDirections) {
            for (int i = 1; i <= maxRange; i++) {
                int newCol = col + dir[0] * i;
                int newRow = row + dir[1] * i;
    
                // ตรวจสอบว่าอยู่ในขอบเขตของกระดาน
                if (!board.isValidPosition(newCol, newRow)) break;
    
                tiles.add(new Point(newCol, newRow));
            }
        }

        return tiles;
    }
}
