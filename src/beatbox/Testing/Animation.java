/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beatbox.Testing;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Jonathan
 */
public class Animation {
    JFrame frame;
    int x, y;
    
    public static void main(String[] args){
        
        Animation test = new Animation();
        test.go();
        
    }
    
    public void go(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        MyDrawPanel animation = new MyDrawPanel();
        frame.getContentPane().add(BorderLayout.CENTER, animation);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        
        //Animation
        x = 100;
        y = 150;
        
        for(int i = 0; i<800; i++){
            x++;
            y++;
            
            animation.repaint();
            
            try{
                Thread.sleep(50);
            }
            catch(Exception ex){}
                    
        }
    }
    
    public class MyDrawPanel extends JPanel{
        
        public void paintComponent(Graphics g){
            g.setColor(Color.black);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            g.setColor(Color.green);
            g.fillOval(x, y, 100, 100); //make use of the x, y in the outer class 
            
        }
        
    }
    
    
}
