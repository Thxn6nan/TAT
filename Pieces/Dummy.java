package tat;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import tat.Pieces;

class Dummy extends Pieces {
    public Dummy(Board board, int col, int row, boolean isFirst) {
        super(board, col, row, 0, 1, isFirst);
        this.col = col;
        this.row = row;
        this.coordinate_x = col * board.tileSize;
        this.coordinate_y = row * board.tileSize;
        this.isFirst = isFirst;
        this.name = "dummy";

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
        int maxRange = Math.min(0 + 0, 4); // ระยะเริ่มต้น + ระยะที่เพิ่ม (ระยะสูงสุดคือ 4)

        // ตรวจสอบแนวตั้ง (ขึ้นและลง)
        for (int i = 1; i <= maxRange; i++) {
            int newRow = row + i;
            if (newRow < board.getRow()) {
                tiles.add(new Point(col, newRow));
            }
            newRow = row - i;
            if (newRow >= 0) {
                tiles.add(new Point(col, newRow));
            }
        }

        // ตรวจสอบแนวนอน (ซ้ายและขวา)
        for (int i = 1; i <= maxRange; i++) {
            int newCol = col + i;
            if (newCol < board.getCol()) {
                tiles.add(new Point(newCol, row));
            }
            newCol = col - i;
            if (newCol >= 0) {
                tiles.add(new Point(newCol, row));
            }
        }

        return tiles;
    }
    
}
