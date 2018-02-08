package ChinaChess;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class MakeChessManual extends JPanel implements ActionListener{
    Sound  hisStep,myStep,mesSound;
    JTextArea  text=null;
    JScrollPane  scroll=null;
    ChessBoard  board=null;
    ChessPoint[][]  point;
    LinkedList  棋谱=null;
    LinkedList  吃掉的棋子=null;
    JButton  buttonUndo,buttonLink,buttonServer;
    JPanel panelButton,panelStatus;
    JLabel labelAlert;
    Chess chessFrame;
    int i=0;
    private boolean hasLinked=false;
    boolean who;                        //true代表以服务器发送信息******否则代表以客户发送信息；   
    ServerListen  server;    
    ClientListen client;
    
    public MakeChessManual(ChessBoard board,ChessPoint[][] point,Chess label) {
        this.board=board;
        chessFrame=label;
        this.point=point;
        labelAlert=new JLabel("☆信息提示栏☆");
        labelAlert.setForeground(Color.BLUE);
        labelAlert.setFont(new Font("隶书",Font.BOLD,18));
        text=new JTextArea();
        text.setForeground(Color.blue);
        text.setEditable(false);
        scroll=new JScrollPane(text);
        panelButton=new JPanel(new GridLayout(1,3,1,0));
        panelStatus=new JPanel();
        panelStatus.setBackground(Color.GREEN);
        棋谱=new LinkedList();
        吃掉的棋子=new LinkedList();
        buttonServer=new JButton("建主");
        buttonServer.setFont(new Font("隶书",Font.BOLD,15));
        buttonServer.addActionListener(this);
        buttonUndo=new JButton("悔棋");
        buttonLink=new JButton("连机");
        buttonUndo.setFont(new Font("隶书",Font.BOLD,15));
        buttonLink.setFont(new Font("隶书",Font.BOLD,15));
        setLayout(new BorderLayout());
        add(scroll,BorderLayout.CENTER); 
        panelButton.add(buttonServer);
        panelButton.add(buttonUndo);
        panelButton.add(buttonLink);
        add(panelButton,BorderLayout.SOUTH);
        panelStatus.add(labelAlert);
        add(panelStatus,BorderLayout.NORTH);
        buttonUndo.addActionListener(this);
        buttonLink.addActionListener(this);
        hisStep=new Sound("sound/hisstep.wav");
        myStep=new Sound("sound/mystep.wav");
        mesSound= new Sound("sound/message.wav");
    }

    public void 记录棋谱(ChessPiece piece,int startI,int startJ,int endI,int endJ){
        Point  pStart=new Point(startI,startJ);
        Point  pEnd=new Point(endI,endJ);
        MoveStep  step=new MoveStep(pStart,pEnd);
        棋谱.add(step);
        String  getChessSex=piece.getChessSex();
        String  name=piece.getName();
        String  m;        
        if(piece.getChessSex().equals(board.黑方颜色)){
            m="☆    "+getChessSex+name+startI+numberToLetter(startJ)+"到"+endI+numberToLetter(endJ);
            text.append(m+"\n");
        }else{
            m="☆    "+getChessSex+name+startI+numberToLetter(startJ)+"到"+endI+numberToLetter(endJ)+"                ";
            text.append(m);
        }
    }
    public void 记录吃掉的棋子(Object object){
        吃掉的棋子.add(object);
    } 
    public LinkedList 获取棋谱(){
        return 棋谱;
    }  
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==buttonServer){
            if(server==null){
                server=new ServerListen(this);
            }
            who=true;            
            server.start();
            chessFrame.labelReceive.setText("服务器已成功建立！请等候连接！");
            //this.chessFrame.resetGame.setEnabled(false);
        }
        if(e.getSource()==buttonUndo){
            if(!board.can){
                sendMessage("undo");
            }else{
                JOptionPane.showMessageDialog(this,"已轮你走棋，不能悔棋了。");
                return;
            }
        }
        if(e.getSource()==buttonLink){                
            String hisIP;
            hisIP=JOptionPane.showInputDialog("对方IP：");         
            if(hisIP==null){
                return;
            }
            if(hisIP.equals("")){
                JOptionPane.showMessageDialog(this,"无效的IP，请重新输入对方IP");
                return;                
            }
            if(who){
                server.endServer();
            }
            if(client==null){
                client=new ClientListen(this,hisIP);
            }
            client.start();
            buttonLink.setEnabled(false);
        }
    }
    //悔棋
    public void backChess(){
        int  position=text.getText().lastIndexOf("☆");
        if(position!=-1){
            text.replaceRange("",position,text.getText().length());            
        }
        if(棋谱.size()>0){
            MoveStep  lastStep=(MoveStep)棋谱.getLast();
            棋谱.removeLast();
            Object  qizi=吃掉的棋子.getLast();
            吃掉的棋子.removeLast();
            String  temp=qizi.toString();
            if(temp.equals("没吃棋子")){
                int  startI=lastStep.pStart.x;
                int  startJ=lastStep.pStart.y;
                int  endI=lastStep.pEnd.x;
                int  endJ=lastStep.pEnd.y;
                ChessPiece  piece=point[endI][endJ].getPiece();
                point[startI][startJ].setPiece(piece,board);
                point[endI][endJ].setPiece(false);
            }else {
                ChessPiece removedPiece=(ChessPiece)qizi;
                int  startI=lastStep.pStart.x;
                int  startJ=lastStep.pStart.y;
                int  endI=lastStep.pEnd.x;
                int  endJ=lastStep.pEnd.y;
                ChessPiece  piece=point[endI][endJ].getPiece();
                point[startI][startJ].setPiece(piece,board);                
                point[endI][endJ].setPiece(removedPiece,board);
                (point[endI][endJ]).setPiece(true);
            }
        }        
    }
    //处理收到的字符串
    public void doMessage(String s) {
        if(s.startsWith("step")){
            s=s.substring(4,8);
            int startI,startJ = 0,endI,endJ;
            startI=charToNumber(s.charAt(0));    
            startJ=charToNumber(s.charAt(1))+1;
            endI=charToNumber(s.charAt(2));
            endJ=charToNumber(s.charAt(3))+1; 
            board.hisStep(startI,startJ,endI,endJ);
            hisStep.run();
            return;
        }
        if(s.equals("reset")){
            int canUndo=JOptionPane.showConfirmDialog(chessFrame,"对方请求重新开局，是否同意？");
            if(canUndo==JOptionPane.CANCEL_OPTION||canUndo==JOptionPane.NO_OPTION){
                sendMessage("noreset");
            }else if(canUndo==JOptionPane.OK_OPTION){
                sendMessage("yesreset");
                chessFrame.resetGame("reset");
            }
        }
        if(s.equals("noreset")){
            JOptionPane.showMessageDialog(chessFrame,"对方不同意重新开局！！！");
        }
        if(s.equals("yesreset")){
            chessFrame.resetGame("reset");
        }
        if(s.equals("no")){
            JOptionPane.showMessageDialog(chessFrame,"对方无情的拒绝了您的请求！");
            return;
        }
        if(s.equals("yes")){
            backChess();
            board.can=true;
            hisStep.run();
            return;
        }
        if(s.startsWith("mes")){
            mesSound.run();
            chessFrame.labelReceive.setText("对手说:  "+s.substring(3,s.length()));
            return;
        }
    }
    public void afterLink(String s){
        if(s.equals("client")){
            who=false;
            setHasLinked(true);
        }
        if(s.equals("server")){
            who=true;
        }
        board.can=true;
        board.myColor=Color.red;
        buttonLink.setEnabled(false);
        buttonServer.setEnabled(false);
    }
    //发送字符串
    public boolean sendMessage(String send) {
        boolean ok = false;
        if(who){
           if(send.startsWith("step")){
               server.sendServerStep(send);
               setAlertText("☆请等候对方走棋☆");
               ok=true;
               myStep.run();
           }else{
               server.sendServerStep(send);
           }
        }else{
            if(send.startsWith("step")){
                client.sendClientStep(send);
                setAlertText("☆请等候对方走棋☆");
                ok=true;             
                myStep.run();
            }else{
               client.sendClientStep(send);
           }
        }        
        return ok;
    }

    public void setAlertText(String s) {
        labelAlert.setText(s);
    } 

    public void gameEnd() {
        if(this.hasLinked){
            if(who){
                server.sendServerStep("end"); 
                server.endServer();
            }else{
                client.sendClientStep("end");
                client.endClient();
            }
        }else{
            if(server!=null){
                server.endServer();
            }
            if(client!=null){
                client.endClient();
            }
        }
        setHasLinked(false);
        buttonLink.setEnabled(true);
        buttonServer.setEnabled(true);
    }
            
    public int charToNumber(char n){
        int c=0;        
        switch(n){
            case 'A': c=1; break;
            case 'B': c=2; break;
            case 'C': c=3; break;
            case 'D': c=4; break;
            case 'E': c=5; break;
            case 'F': c=6; break;
            case 'G': c=7; break;
            case 'H': c=8; break;
            case 'I': c=9; break;
            case 'J': c=10; break;
            
            case '1': c=1; break;
            case '2': c=2; break;
            case '3': c=3; break;
            case '4': c=4; break;
            case '5': c=5; break;
            case '6': c=6; break;
            case '7': c=7; break;
            case '8': c=8; break;
            case '9': c=9; break;
        }
        return c;
    }
    public char numberToLetter(int n){
        char  c='\0';
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
    public boolean hasLinked(){
        return hasLinked;
    }

    public void setHasLinked(boolean b) {
        hasLinked=b;
    }
}
