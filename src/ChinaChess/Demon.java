package ChinaChess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
  
public class Demon extends JPanel implements ActionListener,Runnable{
    public JButton replay=null,next=null,auto=null,stop=null;
    LinkedList  棋谱=null;
    Thread autoPlayThread=null;
    int  index=-1;
    ChessBoard  board=null;
    JTextArea  text;
    JTextField  timeField=null;
    int  timeInterval=1000;
    String  playProcess=" ";
    JSplitPane  splitH=null,splitV=null;
    Chess labelReceive;
    public Demon(ChessBoard board,Chess labelReceive) {
        this.board=board;
        this.labelReceive=labelReceive;
        replay=new JButton("重新演示");
        next=new JButton("下一步");
        auto=new JButton("自动演示");
        stop=new JButton("暂停演示");
        autoPlayThread=new Thread(this);
        replay.addActionListener(this);
        next.addActionListener(this);
        auto.addActionListener(this);
        stop.addActionListener(this);
        text=new JTextArea();
        text.setForeground(Color.blue);
        timeField=new JTextField("1");
        timeField.setEditable(false);
        setLayout(new BorderLayout());
        JScrollPane  pane=new JScrollPane(text);
        JPanel  p=new JPanel(new GridLayout(3,2));
        p.add(next);
        p.add(replay);
        p.add(auto);
        p.add(stop);
        p.add(new JLabel("时间间隔/s",SwingConstants.CENTER));
        p.add(timeField);
        splitV=new JSplitPane(JSplitPane.VERTICAL_SPLIT,pane,p);
        splitH=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,board,splitV);
        splitV.setDividerSize(5);
        splitV.setDividerLocation(400);
        splitH.setDividerSize(5);
        splitH.setDividerLocation(460);
        add(splitH,BorderLayout.CENTER);
        validate();       
        
    }    
    public void set棋谱(LinkedList 棋谱){
        this.棋谱=棋谱;
    }
    public char numberToLetter(int n){
        char c='\0';
        switch(n){
            case 1: c='A'; break;
            case 2: c='B'; break;
            case 3: c='C'; break;
            case 4: c='D'; break;
            case 5: c='E'; break;
            case 6: c='F'; break;
            case 7: c='G'; break;
            case 8: c='H'; break;
            case 9: c='I'; break;
            case 10: c='J'; break;
        }
        return c;
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==next){
            index++;
            if(index<棋谱.size()){
                playOneStep(index);
            }else {
                playEnd("棋谱演示完毕");
            }
        }
        if(e.getSource()==replay){
            board=new ChessBoard(45,45,9,10,labelReceive);
            splitH.remove(board);
            splitH.setDividerSize(5);
            splitH.setDividerLocation(460);
            splitH.setLeftComponent(board);
            splitH.validate();
            index=-1;
            text.setText(null);
        }
        if(e.getSource()==auto){
            next.setEnabled(false);
            replay.setEnabled(false);
            try{
                timeInterval=1000*Integer.parseInt(timeField.getText().trim());                
            }
            catch(NumberFormatException ee){
                timeInterval=1000;
            }
            if(!(autoPlayThread.isAlive())){
                autoPlayThread=new Thread(this);
                board=new ChessBoard(45,45,9,10,labelReceive);
                splitH.remove(board);
                splitH.setDividerSize(5);
                splitH.setDividerLocation(460);
                splitH.setLeftComponent(board);
                splitH.validate();
                text.setText(null);
                autoPlayThread.start();
            }
        }
        if(e.getSource()==stop){            
            if(e.getActionCommand().equals("暂停演示")){                
                playProcess="暂停演示";
                stop.setText("继续演示");
                stop.repaint();
            }
            if(e.getActionCommand().equals("继续演示")){
                playProcess="继续演示";
                autoPlayThread.interrupt();
                stop.setText("暂停演示");
                stop.repaint();
            }
        }        
    }
    public synchronized void run(){
        for(index=0;index<棋谱.size();index++){
            try{
                Thread.sleep(timeInterval);                
            }
            catch(InterruptedException e){
                
            }
            while(playProcess.equals("暂停演示")){
                try{
                    wait();                    
                }
                catch(InterruptedException e){
                    notifyAll();
                }
            }
            playOneStep(index);
        }  
        if(index>=棋谱.size()){
            playEnd("棋谱演示完毕");
            next.setEnabled(true);
            replay.setEnabled(true);
        }
    }
    public void playOneStep(int index){
        MoveStep  step=(MoveStep)棋谱.get(index);
        Point  pStart=step.pStart;
        Point  pEnd=step.pEnd;
        int  startI=pStart.x;
        int  startJ=pStart.y;
        int  endI=pEnd.x;
        int  endJ=pEnd.y;
        ChessPiece  piece=(board.point)[startI][startJ].getPiece();
        if((board.point)[endI][endJ].isPiece()==true){
            ChessPiece pieceRemoved=(board.point)[endI][endJ].getPiece();
            (board.point)[endI][endJ].reMovePiece(pieceRemoved,board);
            board.repaint();
            (board.point)[endI][endJ].setPiece(piece,board);
            (board.point)[startI][startJ].setPiece(false);
            board.repaint();
        } else {
            (board.point)[endI][endJ].setPiece(piece,board);
            (board.point)[startI][startJ].setPiece(false);            
        }
        String  getChessSex=piece.getChessSex();
        String  name=piece.getName();
        String  m;
        if(piece.getChessSex().equals(board.黑方颜色)){
            m="☆    "+getChessSex+name+"到"+endI+numberToLetter(endJ);
            text.append(m+"\n");
        }
        if(piece.getChessSex().equals(board.红方颜色)){
            m="☆    "+getChessSex+name+"到"+endI+numberToLetter(endJ)+"                ";
            text.append(m);
        }
    }
    public void playEnd(String message){
        splitH.remove(board);
        splitH.setDividerSize(5);
        splitH.setDividerLocation(460);
        JLabel  label=new JLabel(message);
        label.setFont(new Font("隶书",Font.BOLD,40));
        label.setForeground(Color.blue);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        splitH.setLeftComponent(label);
        splitH.validate();
    }
}
   