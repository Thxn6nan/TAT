package tat;

// ใช้เป็น * อย่างเดียวทุกอันก็ไม่ได้ด้วยนะ บางอันเอ๋ออีก
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

final class Board extends JPanel {
    int tileSize = 85; // ขนาดช่อง
    private final int col = 9; // แนวนอน x
    private final int row = 8; // แนวตั้ง y
    //private boolean isFirst;
    private final BoardEvent event;
    
    ArrayList<Pieces> piecesList = new ArrayList<>(); // เก็บข้อมูลตัวหมาก
    ArrayList<BoardEvent> eventList = new ArrayList<>(); // เก็บข้อมูลอีเวนต์
    public Pieces selectedPieces;
    public ArrayList<Point> highlightedTiles = new ArrayList<>(); // ตำแหน่งที่สามารถโจมตีได้
    private Point hoveredTile = null;
    private String hoveredDirection = "";  // เก็บทิศที่เมาส์ชี้
    
    private final Map<Point, Color> tileColors = new HashMap<>();
    
    public Board(int par, int par1, boolean par2) {
        this.setPreferredSize(new Dimension(col * tileSize, row * tileSize));
        this.selectedPieces = new Pieces(this, 0, 0, 0, 0, true); // กำหนด selectedPieces ก่อนที่จะใช้
        this.event = new BoardEvent(this, 0, 0); // สร้าง event และเรียก sumEvent
        
        //System.out.println(piecesList.size());

        if (event != null) {
            event.sumEvent();
        }
        //addPiece(); // main
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int clickedCol = e.getX() / tileSize;
                int clickedRow = e.getY() / tileSize;

                Pieces clickedPiece = getPieces(clickedCol, clickedRow);
                handleClick(clickedCol, clickedRow);
                repaint();
            }

        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int hoveredCol = e.getX() / tileSize;
                int hoveredRow = e.getY() / tileSize;
                handleHover(hoveredCol, hoveredRow);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // วาดกระดาน
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                Point p = new Point(c, r);
                g2d.setColor(tileColors.getOrDefault(p, (c + r) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK));
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }

        // วาดตัวหมาก
        for (Pieces piece : piecesList) {
           piece.paint(g2d);
        }

        // ไฮไลต์ช่องที่สามารถโจมตีได้ (สีเขียว)
        g2d.setColor(new Color(68, 180, 57, 190));
        for (Point p : highlightedTiles) {
            g2d.fillRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
        }

        // ไฮไลต์ช่องที่เมาส์ชี้อยู่ (สีแดง) เฉพาะทิศที่กำหนด
        if (hoveredTile != null) {
            g2d.setColor(new Color(255, 0, 0, 140));
            for (Point p : highlightedTiles) {
                // วาดเฉพาะทิศทางที่ตรงกับทิศทางที่เมาส์ชี้
                // โลจิกช่อง
                // x : > ขวา, < ซ้าย, == กลาง ของตัวหมาก
                // y : > ล่าง, < บน, == กลาง ของตัวหมาก
                // direction(p.x, hoveredTile.x, p.y, hoveredTile.y) == true;
                if ((hoveredDirection.equals("UP") && p.x == hoveredTile.x && p.y < selectedPieces.row) ||
                    (hoveredDirection.equals("DOWN") && p.x == hoveredTile.x && p.y > selectedPieces.row) ||
                    (hoveredDirection.equals("LEFT") && p.y == hoveredTile.y && p.x < selectedPieces.col) ||
                    (hoveredDirection.equals("RIGHT") && p.y == hoveredTile.y && p.x > selectedPieces.col) ||
                    (hoveredDirection.equals("UP-LEFT") && p.x < hoveredTile.x && p.y < selectedPieces.row) ||
                    (hoveredDirection.equals("UP-RIGHT") && p.x > hoveredTile.x && p.y < selectedPieces.row) ||
                    (hoveredDirection.equals("DOWN-LEFT") && p.x < hoveredTile.x && p.y > selectedPieces.row) ||
                    (hoveredDirection.equals("DOWN-RIGHT") && p.x > hoveredTile.x && p.y > selectedPieces.row)) {
                    g2d.fillRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
                } else if (hoveredDirection.equals("AoE")){
                    g2d.fillRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
                }
                repaint();
            }
        }
    }

    public void setTileColor(int col, int row, Color color) {
        Point p = new Point(col, row);
        if (color == null) {
            tileColors.remove(p); // ลบสีที่กำหนดไว้เพื่อคืนค่าปกติ
        } else {
            tileColors.put(p, color);
        }
        repaint();
    }
    


    public boolean isValidPosition(int col, int row) {
        return col >= 0 && col < this.col && row >= 0 && row < this.row;
    }
    
    // เช็คคลิกเมาส์
    public void handleClick(int clickedCol, int clickedRow) {
        Pieces clickedPiece = getPieces(clickedCol, clickedRow);

        if (clickedPiece != null && "".equals(hoveredDirection)) {
            selectedPieces = clickedPiece;
            highlightedTiles.clear();

            if (selectedPieces instanceof Dummy dummy) {
                highlightedTiles = dummy.getAttackTiles();
            }

            else if (selectedPieces instanceof Magician magician) {
                highlightedTiles = magician.getAttackTiles();
            }

            else if (selectedPieces instanceof Swordsman swordsman) {
                highlightedTiles = swordsman.getAttackTiles();
            }

            else if (selectedPieces instanceof Gunman gunman) {
                highlightedTiles = gunman.getAttackTiles();
            }

            else if (selectedPieces instanceof Bomber bomber) {
                highlightedTiles = bomber.getAttackTiles();
            }

            else if (selectedPieces instanceof Shielder shielder) {
                highlightedTiles = shielder.getAttackTiles();
            }
        }
        else if (selectedPieces != null && highlightedTiles.contains(new Point(clickedCol, clickedRow))) {
            handleAttack(clickedCol, clickedRow);
        } else {
            selectedPieces = null;
            highlightedTiles.clear();
            hoveredTile = null;
        }
        repaint();
    }

    // ลบหมากและอีเวนต์ที่ตัวหมากใช้ไปแล้ว
    private void handleAttack(int clickedCol, int clickedRow) {
        ArrayList<Pieces> toRemovePieces = new ArrayList<>();
        ArrayList<BoardEvent> toRemoveEvents = new ArrayList<>();
        
        // อยู่ในรัศมีไหม ฝั่งเดียวกันไหม มีหมากอื่นบังไหม  เป็นคลาสโล่ไหม มีบาเรียไหม 
        for (Point p : highlightedTiles) {
            Pieces targetPiece = getPieces(p.x, p.y);
            if (targetPiece == null) continue; // ข้ามถ้าไม่มีหมาก
            
            // เช็คทิศทางก่อนลบ
            if ((hoveredDirection.equals("UP") && p.x == selectedPieces.col && p.y < selectedPieces.row) ||
                (hoveredDirection.equals("DOWN") && p.x == selectedPieces.col && p.y > selectedPieces.row) ||
                (hoveredDirection.equals("LEFT") && p.y == selectedPieces.row && p.x < selectedPieces.col) ||
                (hoveredDirection.equals("RIGHT") && p.y == selectedPieces.row && p.x > selectedPieces.col) ||
                (hoveredDirection.equals("UP-LEFT") && p.x < selectedPieces.col && p.y < selectedPieces.row) ||
                (hoveredDirection.equals("UP-RIGHT") && p.x > selectedPieces.col && p.y < selectedPieces.row) ||
                (hoveredDirection.equals("DOWN-LEFT") && p.x < selectedPieces.col && p.y > selectedPieces.row) ||
                (hoveredDirection.equals("DOWN-RIGHT") && p.x > selectedPieces.col && p.y > selectedPieces.row) ||
                (hoveredDirection.equals("AoE"))) {

                // ตรวจสอบว่ามีหมากบังระหว่างการโจมตี
                if (isBlocking(selectedPieces, targetPiece, hoveredDirection) && !"AoE".equals(hoveredDirection)) {
                    System.out.println("All enemy in range have protecter");
                    continue;  // ข้ามถ้าหมากถูกบัง
                }

                // ตรวจสอบว่าหมากที่ถูกลบเป็นพันธมิตรหรือไม่
                if (targetPiece.isFirst == selectedPieces.isFirst) {
                    continue; // ข้ามการลบหมากนี้
                } else if (targetPiece instanceof Shielder shielder) {
                    if (shielder.isInvulnerable()) {
                        shielder.setInvulnerable(false);
                        System.out.println("Enemy's shielder is blocking");
                    } else if (isPieceOnBarrier(targetPiece) || targetPiece.isInvul()) {
                        targetPiece.setInvul(false);
                        removeEventAt(targetPiece.col, targetPiece.row);
                        System.out.println("Enemy's barrier has blocked");
                    } else {
                        toRemovePieces.add(targetPiece); // ลบตัวหมากทันทีถ้าไม่มี Invul
                        System.out.println("Nearest enemy is dead");
                    }
                } else {
                    if (isPieceOnBarrier(targetPiece) || targetPiece.isInvul()) {
                        targetPiece.setInvul(false);
                        removeEventAt(targetPiece.col, targetPiece.row);
                        System.out.println("Enemy's barrier has blocked");
                    } else {
                        toRemovePieces.add(targetPiece); // ลบตัวหมากถ้าไม่มี Invul
                        System.out.println("Nearest enemy is dead");
                    }
                }
            }
            repaint();
            System.out.println("==================");
            // ลบอีเวนต์เพิ่มระยะของหมากหลังโจมตีแล้ว
            if (isPieceOnExtraRange(selectedPieces)) removeEventAt(selectedPieces.col, selectedPieces.row);
        }

        // ลบหมากที่อยู่ในทิศที่เลือก
        piecesList.removeAll(toRemovePieces);

        // ลบอีเวนต์ที่อยู่ในตำแหน่งของตัวหมากที่ถูกลบ
        for (Pieces piece : toRemovePieces) {
            removeEventAt(piece.col, piece.row);
        }
        
        highlightedTiles.clear(); // เคลียร์แสดงรัศมีโจมตี
        repaint();
    }

    private boolean isBlocking(Pieces attacker, Pieces target, String direction) {
        int startX = attacker.col;
        int startY = attacker.row;
        int endX = target.col;
        int endY = target.row;

        // เช็คทิศทางที่กำหนด
        switch (direction) {
            case "UP":
                for (int y = startY - 1; y > endY; y--) {
                    Pieces blocker = getPieces(startX, y);
                    if (blocker != null) {
                        if (blocker.isFirst != attacker.isFirst) {
                            return true; // ข้ามถ้าหมากศัตรูบัง
                        }
                    }
                }
                break;
            case "DOWN":
                for (int y = startY + 1; y < endY; y++) {
                    Pieces blocker = getPieces(startX, y);
                    if (blocker != null) {
                        if (blocker.isFirst != attacker.isFirst) {
                            return true; // ข้ามถ้าหมากศัตรูบัง
                        }
                    }
                }
                break;
            case "LEFT":
                for (int x = startX - 1; x > endX; x--) {
                    Pieces blocker = getPieces(x, startY);
                    if (blocker != null) {
                        if (blocker.isFirst != attacker.isFirst) {
                            return true; // ข้ามถ้าหมากศัตรูบัง
                        }
                    }
                }
                break;
            case "RIGHT":
                for (int x = startX + 1; x < endX; x++) {
                    Pieces blocker = getPieces(x, startY);
                    if (blocker != null) {
                        if (blocker.isFirst != attacker.isFirst) {
                            return true; // ข้ามถ้าหมากศัตรูบัง
                        }
                    }
                }
                break;
            // เพิ่มกรณีอื่นๆ ตามทิศทางที่ต้องการ
        }
        return false; // ไม่มีหมากบัง
    }


    // เช็คตำแหน่งเมาส์
    public void handleHover(int hoveredCol, int hoveredRow) {
        hoveredTile = null;
        hoveredDirection = ""; // ล้างค่าทิศทาง

        // โลจิกช่อง
        // x : > ขวา, < ซ้าย, == กลาง ของตัวหมาก
        // y : > ล่าง, < บน, == กลาง ของตัวหมาก
    
        if (selectedPieces instanceof Swordsman || selectedPieces instanceof Shielder || selectedPieces instanceof Dummy) {
            for (Point p : highlightedTiles) {
                if (p.x == hoveredCol && p.y == hoveredRow) {
                    hoveredTile = p;
    
                    // คำนวณทิศทางตามตำแหน่งเมาส์เทียบกับตัวหมากที่เลือก
                    if (p.x == selectedPieces.col) {
                        hoveredDirection = (p.y < selectedPieces.row) ? "UP" : "DOWN";
                    } else if (p.y == selectedPieces.row) {
                        hoveredDirection = (p.x < selectedPieces.col) ? "LEFT" : "RIGHT";
                    }
    
                    repaint(); // อัปเดต UI ทันที
                    return;
                }
            }
        } else if(selectedPieces instanceof Gunman){
            for (Point p : highlightedTiles) {
                if (p.x == hoveredCol && p.y == hoveredRow) {
                    hoveredTile = p;
                    
                    // คำนวณทิศทางตามตำแหน่งเมาส์เทียบกับตัวหมากที่เลือก
                    if (p.x < selectedPieces.col) {
                        hoveredDirection = (p.y < selectedPieces.row) ? "UP-LEFT" : "DOWN-LEFT";
                    } else if (p.x > selectedPieces.col) {
                        hoveredDirection = (p.y < selectedPieces.row) ? "UP-RIGHT" : "DOWN-RIGHT";
                    }

                    repaint();
                    return;
                }
            }
        } else if(selectedPieces instanceof Magician || selectedPieces instanceof Bomber){
            for (Point p : highlightedTiles) {
                if (p.x == hoveredCol && p.y == hoveredRow) {
                    hoveredTile = p;
    
                    // คำนวณทิศทางตามตำแหน่งเมาส์เทียบกับตัวหมากที่เลือก
                    hoveredDirection = "AoE";
    
                    repaint(); // อัปเดต UI ทันที
                    return;
                }
            }
        }
    }

    public Pieces getPieces(int col, int row) {
        for (Pieces piece : piecesList) {
            if (piece.col == col && piece.row == row) return piece;
        }
        return null;
    }
    
    public void addPiece(String pieceName, int col, int row, boolean isFirst ) { // 
        // piece-type(board, row, col, isFirst)
        // แก้ไม่ให้ตัวหมากทับกัน รอรับค่าจาก main, player
        if("dummy".equals(pieceName)){
            piecesList.add(new Dummy(this, col, row, isFirst));
            Pieces p = getPieces(col, row);
            System.out.print(p.getName()+" "+p.col+" "+p.row);
        } //
        if("shielder".equals(pieceName)) piecesList.add(new Shielder(this, col, row, isFirst));
        if("gunman".equals(pieceName)) piecesList.add(new Gunman(this, col, row, isFirst));
        if("swordsman".equals(pieceName)) piecesList.add(new Swordsman(this, col, row, isFirst));
        if("magician".equals(pieceName)) piecesList.add(new Magician(this, col, row, isFirst));
        if("bomber".equals(pieceName)) piecesList.add(new Bomber(this, col, row, isFirst));

        piecesList.add(new Shielder(this, 4, 3, true));
        piecesList.add(new Gunman(this, 4, 2, false));
        piecesList.add(new Bomber(this, 5, 3, false));
        piecesList.add(new Swordsman(this, 3, 3, false));
        piecesList.add(new Magician(this, 4, 4, false));
    }

    public void spawnEvent(String eventName, int c, int r){
        if("barrier".equals(eventName)) eventList.add(new Barrier(this, c, r));
        if("extraRange".equals(eventName)) eventList.add(new ExtraRange(this, c, r));
        if("wall".equals(eventName)) eventList.add(new Wall(this, c, r));

        // ระบบแมนนวล ถถถ
        eventList.add(new ExtraRange(this, 4, 2));
        eventList.add(new Barrier(this, 4, 4));
        //eventList.add(new Wall(this, 6, 6));
        repaint();
    }

    // หมากอยู่ในช่องเพิ่มระยะหรือไม่
    public boolean isPieceOnExtraRange(Pieces piece) {
        for (BoardEvent event : eventList) {
            if (event instanceof ExtraRange) {
                ExtraRange extraRange = (ExtraRange) event;
                if (extraRange.col == piece.col && extraRange.row == piece.row) {
                    return true;
                }
            }
        }
        return false;
    }

    // หมากอยู่ในช่องบาเรียหรือไม่
    public boolean isPieceOnBarrier(Pieces targetPiece) {
        for (BoardEvent event : eventList) {
            if (event instanceof Barrier) {
                if (event.col == targetPiece.col && event.row == targetPiece.row) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeEventAt(int col, int row) {
        Iterator<BoardEvent> iterator = eventList.iterator();
        
        while (iterator.hasNext()) { 
            BoardEvent event = iterator.next();
            if (event.col == col && event.row == row) {
                iterator.remove(); // ลบอีเวนต์อย่างถูกต้อง แชทบอกมาให้ทำงี้
                repaint(); // รีเฟรชหน้าจอ
            }
        }
        tileColors.remove(new Point(col, row)); // รีเซ็ตสีของช่องให้เป็นค่าเริ่มต้น
        repaint(); // รีเฟรชหน้าจอ
    }

    public void removeAllEvent() {
        eventList.clear();
        tileColors.clear(); // รีเซ็ตทุกสีที่อีเวนต์เคยเปลี่ยนแปลง
        repaint();
    }
    
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
}
