// ข้อมูลผู้เล่น
package tat;

public class Player {
    private int id; // รหัสไอดีผู้เล่น
    private int mp; // ปริมาณมานา
    private int pieceCounts; // จำนวนหมากทั้งหมดที่ผู้เล่นมี

    public Player(int id, int mp){
        this.id = id;
    }

    public int getID(){
        return id;
    }

    public int getMP(){
        return mp;
    }

    public void setMP(int MP){ // รอเปลี่ยนค่าอินพุต
        this.mp = Math.max(0, MP); // ไม่ให้ค่าต่ำกว่า 0
    }

    public int getPieceCounts(){
        return pieceCounts;
    }

    public void setPieceCount(int pieceCounts){ // รอเปลี่ยนค่าอินพุต
        this.pieceCounts = Math.max(0, pieceCounts);
    }

    /*
    public void playerAction(String command){ // รอเปลี่ยนค่าอินพุต
        while(true){
            if(command.equal("attack")){ // โจมตี
                // เรียกใช้เมทอด โจมตี
            } 
            else if(command.equal("summon")){ // ซัมม่อน
                // เรียกใช้เมทอด วางหมาก
            }
            else if(command.equal("pass")){ // ผ่าน
                // เรียกใช้เมทอด ผ่าน
            }
            else if(command.equal("surrender")){ // ยอมแพ้
                // ้รียกใช้เมทอด ยอมแพ้
                break;
            }
            else{
                System.out.println("Error: wrong input");
            }
        }
    }
    */
}
