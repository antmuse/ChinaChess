package ChinaChess;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

public class About extends JDialog implements WindowListener{

	private Container container;
    private String[]  s;
    private JTabbedPane  tabbedpane;
    private JTextArea[]  textarea;    
    public About(JFrame frame){        
        super(frame);
        container = getContentPane();
        textarea = new JTextArea[2];
        s=new String[2];
        s[0]="此软件为一联机版象棋\n适用于局城网内使用\n";
        s[1]="Author: Antmuse\nMail: antmuse@live.cn\nQQ: 289609698";
        try{
            setupPane();
        }   
         catch(Exception e)
        {
            System.out.println(e+"出错。");
        }       
        setTitle("关于");
        setModal(true);
        setDefaultCloseOperation(2);
        setSize(300,200);
        setResizable(false);
        setVisible(true);
    }
    public void setupPane() throws Exception    {
        String as[] = {"本软件","Author"};
        tabbedpane = new JTabbedPane(1);
        for(int i = 0; i < 2; i++) {
            textarea[i] = new JTextArea();
            textarea[i].setEditable(false);
            textarea[i].setWrapStyleWord(true);
            textarea[i].setLineWrap(true);
            textarea[i].setBackground(new Color(255, 255, 204));
            textarea[i].setMargin(new Insets(5, 10, 10, 10));
            textarea[i].setBorder(BorderFactory.createEtchedBorder());            
            textarea[i].setText(s[i]);
            tabbedpane.addTab(as[i], new JScrollPane(textarea[i]));
            container.add(tabbedpane, "Center");
        }

    }
    public void windowActivated(WindowEvent windowevent){
    }

    public void windowClosed(WindowEvent windowevent){
    }

    public void windowClosing(WindowEvent windowevent){
        dispose();
    }

    public void windowDeactivated(WindowEvent windowevent){
    }

    public void windowDeiconified(WindowEvent windowevent){
    }

    public void windowIconified(WindowEvent windowevent){
    }

    public void windowOpened(WindowEvent windowevent){
    }  
}
