/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beatbox;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 *
 * @author Jonathan
 */
public class BeatBox {
    
    JPanel mainPanel;
    ArrayList<JCheckBox> checkboxList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;
    Font bigFont;
    
    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
    
    public static void main (String[] args){
        new BeatBox().buildGUI();
    }
    
    public void buildGUI(){
        bigFont = new Font ("Serif",Font.BOLD ,30);
        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        checkboxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton start = new JButton("Start");
        start.setFont(bigFont);
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);
        
        JButton stop = new JButton("Stop");
        stop.setFont(bigFont);
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);
        
        JButton upTempo = new JButton("Tempo Up");
        upTempo.setFont(bigFont);
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);
        
        JButton downTempo = new JButton("Tempo Down");
        downTempo.setFont(bigFont);
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);
        
        JButton reset = new JButton("Reset");
        reset.setFont(bigFont);
        reset.addActionListener(new MyResetListener());
        buttonBox.add(reset);
        
        JButton serializeIt = new JButton("Serialize It");
        serializeIt.setFont(bigFont);
        serializeIt.addActionListener(new MySerializeListener());
        buttonBox.add(serializeIt);
        
        JButton restoreIt = new JButton("Restore It");
        restoreIt.setFont(bigFont);
        restoreIt.addActionListener(new MyRestoreListener());
        buttonBox.add(restoreIt);
        
        
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for(int i = 0; i<16; i++){
            Label label = new Label (instrumentNames[i]);
            label.setFont(bigFont);
            nameBox.add(label);
            
        }
        
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);
        
        theFrame.getContentPane().add(background);
        
        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(2);
        grid.setHgap(4);
        mainPanel= new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);
        
        for (int i = 0; i<256; i++){
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        }
        
        setUpMidi();
        
        theFrame.setBounds(1000,500,800,800);
        theFrame.pack();
        theFrame.setVisible(true);
    }
    
    public void setUpMidi(){
        try{
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch (Exception e){e.printStackTrace();}
    }
    
    public void buildTrackAndStart(){
        int[] trackList = null;
        
        sequence.deleteTrack(track);
        track = sequence.createTrack();
        
        for(int i = 0; i<16; i++){
            trackList = new int[16];
            
            int key = instruments[i];
            
            for(int j = 0; j<16; j++){
                JCheckBox jc = (JCheckBox)checkboxList.get(i*16 + j);
                if (jc.isSelected()){
                    trackList[j]= key;
                } else {
                    trackList[j]= 0;
                }
            }
            
            makeTracks(trackList);
            track.add(makeEvent(176,1,127,0,16));
            
            track.add(makeEvent(192,9,1,0,15));
            try {
                sequencer.setSequence(sequence);
                sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
                sequencer.setTempoInBPM(120);
            } catch (Exception e){e.printStackTrace();}
        }
    }
    
    public class MyStartListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            buildTrackAndStart();
        }
    }
    
    public class MyStopListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            sequencer.stop();
        }
    }
    
    public class MyUpTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor*1.03));
        }
    }
    
    public class MyDownTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor*0.97));
        }
    }
   
    public class MyResetListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            for(int i = 0; i < 256; i++){
                JCheckBox c = checkboxList.get(i);
                c.setSelected(false);
            }
        }
    }
    
    public class MySerializeListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            boolean[] checkboxState = new boolean[256];
            for(int i = 0; i < 256; i++){
                JCheckBox toBeChecked = (JCheckBox) checkboxList.get(i);
                if(toBeChecked.isSelected())
                    checkboxState[i]=true;
            }
            try{
                FileOutputStream fileStream = new FileOutputStream(new File("Checkbox.ser"));
                ObjectOutputStream os = new ObjectOutputStream(fileStream);
                os.writeObject(checkboxState);
                os.close();
            }
            catch(Exception e){
                
                e.printStackTrace();
            }
        }
    }
    
    public class MyRestoreListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            boolean[] boxState = null;
            try{
                FileInputStream fileIn = new FileInputStream(new File("Checkbox.ser"));
                ObjectInputStream is = new ObjectInputStream(fileIn);
                boxState = (boolean[]) is.readObject();
                is.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            for(int i = 0; i<256; i++){
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if(boxState[i])
                    check.setSelected(true);
                else
                    check.setSelected(false);
            }
            sequencer.stop();
            buildTrackAndStart();
        }
    }
    
    public void makeTracks(int[] list){
        for(int i = 0; i<16; i++){
            int key = list[i];
            
            if (key!=0){
                track.add(makeEvent(144,9,key,100,i));
                track.add(makeEvent(128,9,key,100,i+1));
            }
        }
    }
    
    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
            
        }
        catch(Exception ex){}
        return event;
    }
    
    public class MyDrawPanel extends JPanel implements ControllerEventListener{
        
        boolean msg = false;
        
        public void controlChange (ShortMessage event){
            msg = true;
            repaint();
        }
        
        public void paintComponent(Graphics g){
            
            if(msg){
                Graphics2D g2 = (Graphics2D) g;
                
                int r= (int)(Math.random()*250);
                int gr= (int)(Math.random()*250);
                int b= (int)(Math.random()*250);
                
                g.setColor(new Color(r, gr, b));
                
                int ht = (int)((Math.random()*120)+10);
                int width = (int)((Math.random()*120)+10);
                int x = (int)((Math.random()*40)+10);
                int y = (int)((Math.random()*40)+10);
                g.fillRect(x,y,ht,width);
                msg = false;
            }
            
        }
        
        //this creates one track of 16 beats for one instrument at a time
        public void makeTrack(int[] list){
            
            for(int i = 0; i < 16; i++){
                int key = list[i];
                
                if(key != 0){
                    track.add(makeEvent(144,9,key,100,i));
                    track.add(makeEvent(128,9,key,100,i+1));
                }
            }
            
        }
        
        //this creates an event of one note which contains the msg of the note 
        public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
            MidiEvent event = null;
            try{
                ShortMessage a = new ShortMessage();
                a.setMessage(comd, chan, one, two);
                event = new MidiEvent(a, tick);
                
            } catch (Exception e){e.printStackTrace();}
            return event;
        }
    }
    
}
