
// this is only for test 
package tat;
import java.awt.*;
import java.util.Scanner;

import javax.swing.*;
public class TAT {

    public static void main(String[] args) {
        boolean DEBUG = true; // Debug mode
        //Scanner input = new Scanner(System.in);

        // player system
        Player player_1 = new Player(1, 10);
        Player player_2 = new Player(2, 10);

        // board system
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);

        TAT tatInstance = new TAT();
        Board board = new Board(0, 0, true);
        frame.add(board);

        frame.setVisible(true);

        if(DEBUG == true){
            System.out.printf("id: %d mp: %d\n", player_1.getID(), player_1.getMP());
            System.out.printf("id: %d mp: %d\n", player_2.getID(), player_2.getMP());
        }
    }
    
    /*
    public static time(){

    }

    public static turn(){
        
    }
*/
}
