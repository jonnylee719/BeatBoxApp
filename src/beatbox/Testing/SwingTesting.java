/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beatbox.Testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javafx.scene.text.Font.font;

/**
 *
 * @author Jonathan
 */
public class SwingTesting implements ActionListener{
    
    JFrame frame;
    JTextField field;
    JLabel label;
    JCheckBox check;
    
    public static void main(String[] args){
        SwingTesting gui = new SwingTesting();
        gui.go();
        
    }
    
    public JFrame setUpGui(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000,2000);
        frame.setVisible(true);
        return frame;
    }
    
    public JTextField setUpTField(){
        field = new JTextField(20);
        field.setText("");
        return field;
    }
    
    public JPanel setUpPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
    public void go() {
        JPanel panel = setUpPanel();
        Font font = new Font ("serif", Font.BOLD, 36);
        frame = setUpGui();
        field = setUpTField();
        field.setFont(font);
        JPanel panel1 = setUpPanel();
        label = new JLabel("Nothing here yet.");
        label.setFont(font);
        check = new JCheckBox("tick is Yes, no tick is No");
        
        
        JButton button = new JButton();
        button.setText("Just add me!");
        button.setFont(font);
        button.addActionListener(this);
        
        panel.add(button);
        panel1.add(field);
        panel1.add(label);
        frame.getContentPane().add(BorderLayout.EAST, panel);
        frame.getContentPane().add(BorderLayout.NORTH, check);
        frame.getContentPane().add(BorderLayout.CENTER, panel1);
        frame.setVisible(true);
       
    }
    
    public void actionPerformed(ActionEvent ae){
        String text = this.field.getText();
        if (this.check.isSelected())
            text = text + "It is on Baby!";
        this.label.setText(text);
        System.out.println(text);
        this.field.requestFocus();
        
    }

}
