/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beatbox.Testing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Jonathan
 */
public class Testing implements ActionListener {
        JFrame frame;
        JLabel label;
        
    public void go (){
        // button code for circle color
        frame = new JFrame ();
        JButton button = new JButton("click me");
        button.addActionListener(new ColorListener());
        
        //button code for label color
        JButton labelButton = new JButton("To change the label");
        labelButton.addActionListener(new LabelListener());
        frame.getContentPane().add(BorderLayout.EAST, labelButton);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.SOUTH, button);
        
        label = new JLabel("label here!");
        frame.getContentPane().add(BorderLayout.WEST, label);
        
        frame.setSize(1000,1000);
        
        frame.setVisible(true);
        
    
        
       
       
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }       
    
    public class ColorListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            frame.repaint();
        }
    }
    
    public class LabelListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            label.setText("Dont mess with me!");
        }
    }
    
    public static void main (String [] args){
        
        Testing gui = new Testing();
        gui.go();
        
    }
    
}
