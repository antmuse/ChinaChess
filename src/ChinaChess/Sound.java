package ChinaChess;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Vector;

public class Sound implements Runnable{
    
    final int bufSize = 16384;
    Vector sounds = new Vector();
    Thread thread;
    Sequencer sequencer;
    Synthesizer synthesizer;
    Object currentSound;
    String currentName;
    int num;
    boolean bump;
    boolean paused = false;
    String errStr;
    private File fileS;
    
    public Sound(String name) { 
        thread = new Thread(this);
        thread.setName("sound");
        thread.start();
        fileS = new File(name);
        if (fileS!= null) {
            addSound(fileS); 
            open();
        }
    }

    public void open() {
        try {
            sequencer = MidiSystem.getSequencer();
            if (sequencer instanceof Synthesizer) {
                synthesizer = (Synthesizer)sequencer;	
            } 
        } catch (Exception ex) {
            ex.printStackTrace();
            return; 
        }        
    }
    public void close() {
        if (sequencer != null) {
            sequencer.close();
        }
    }
    private void addSound(File file) {
        String s = file.getName();
        if (s.endsWith(".au") || s.endsWith(".rmf") ||
            s.endsWith(".mid") || s.endsWith(".wav") ||
            s.endsWith(".aif") || s.endsWith(".aiff"))   {
            sounds.add(file);
        }
    }
    public boolean loadSound(Object object) {  
        if (object instanceof File) {
            currentName = ((File) object).getName();
            try {
                currentSound = AudioSystem.getAudioInputStream((File) object);
            } catch(Exception e1) {
                try { 
                        FileInputStream is = new FileInputStream((File) object);
                        currentSound = new BufferedInputStream(is, 1024);
                    } catch (Exception e3) { 
                        e3.printStackTrace(); 
			currentSound = null;
			return false;
                    }
            }
        }
        // user pressed stop or changed tabs while loading
        if (sequencer == null) {
            currentSound = null;
            return false;
        } 
        if (currentSound instanceof AudioInputStream) {
           try {
                AudioInputStream stream = (AudioInputStream) currentSound;
                AudioFormat format = stream.getFormat();

                /**
                 * we can't yet open the device for ALAW/ULAW playback,
                 * convert ALAW/ULAW to PCM
                 */
                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
                    (format.getEncoding() == AudioFormat.Encoding.ALAW)) 
                {
                    AudioFormat tmp = new AudioFormat(
                                              AudioFormat.Encoding.PCM_SIGNED, 
                                              format.getSampleRate(),
                                              format.getSampleSizeInBits() * 2,
                                              format.getChannels(),
                                              format.getFrameSize() * 2,
                                              format.getFrameRate(),
                                              true);
                    stream = AudioSystem.getAudioInputStream(tmp, stream);
                    format = tmp;
                }
                DataLine.Info info = new DataLine.Info(
                                          Clip.class, 
                                          stream.getFormat(), 
                                          ((int) stream.getFrameLength()*format.getFrameSize()));

                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                currentSound = clip;
            } catch (Exception ex) { 
		ex.printStackTrace(); 
		currentSound = null;
		return false;
	    }
        } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
            try {
                sequencer.open();
                if (currentSound instanceof Sequence) {
                    sequencer.setSequence((Sequence) currentSound);
                } else {
                    sequencer.setSequence((BufferedInputStream) currentSound);
                }
            } catch (InvalidMidiDataException imde) { 
		System.out.println("Unsupported audio file.");
		currentSound = null;
		return false;
            } catch (Exception ex) { 
		ex.printStackTrace(); 
		currentSound = null;
		return false;
	    }
        }

	return true;
    }

    public void playSound() {
        if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream && thread != null) {
            sequencer.start();
            //sequencer.stop();
            //sequencer.close();
        } else if (currentSound instanceof Clip && thread != null) {
            Clip clip = (Clip) currentSound;
            clip.start();
            try { thread.sleep(99); } catch (Exception e) { }
            //clip.stop();
            //clip.close();
        }
        currentSound = null;
    }

    public void start() {
        thread.start();        
    }

    public boolean isRun(){
        if(thread!=null){
            return true;
        }else {
            return false;
        }
    }
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = null;
    }

    public void run() {
        if( loadSound(fileS) == true ) {
            playSound();
        }     
    }  
}