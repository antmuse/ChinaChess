package ChinaChess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;


public class Chess extends JFrame implements ActionListener{
        
    ChessBoard  board=null;
    Demon  demon=null;
    MakeChessManual  record=null;
    Container  con=null;
    JMenuBar  bar;
    JMenu  fileMenu;
    JMenuItem  resetGame,saveChessBoard,playChessBoard,help;
    JFileChooser  fileChooser=null;
    LinkedList  棋谱=null;    
    JTextField fieldSend;
    JButton  send;
    JPanel  panelSend;
    JLabel  labelReceive;
    public Chess() {
        bar=new JMenuBar();
        fileMenu=new JMenu("象棋菜单");        
        resetGame=new JMenuItem("重新开局");
        saveChessBoard=new JMenuItem("保存棋谱");
        playChessBoard=new JMenuItem("演示棋谱");         
        help=new JMenuItem("关于Antmuse");
        fileMenu.add(resetGame);
        fileMenu.add(saveChessBoard);
        fileMenu.add(playChessBoard); 
        fileMenu.add(help);
        bar.add(fileMenu);          
        setJMenuBar(bar);
        setTitle("中国象棋☆☆☆----------Author:Antmuse");
        resetGame.addActionListener(this);
        saveChessBoard.addActionListener(this);
        playChessBoard.addActionListener(this);
        help.addActionListener(this);
        board=new ChessBoard(45,45,9,10,this);
        record=board.record;
        labelReceive=new JLabel("                                                                   ");
        labelReceive.setForeground(Color.blue);
        labelReceive.setFont(new Font("隶书",Font.BOLD,15));
        fieldSend=new JTextField(45);
        fieldSend.setBackground(Color.cyan);
        send=new JButton("发送");
        send.setToolTipText("发送信息");
        send.setMnemonic(KeyEvent.VK_ENTER);
        send.addActionListener(this);
        send.setFont(new Font("隶书",Font.BOLD,15));
        panelSend=new JPanel(new BorderLayout());
        con=getContentPane();
        con.setLayout(new BorderLayout());
        JSplitPane  split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,board,record);
        split.setDividerSize(5);
        split.setDividerLocation(460);
        con.add(labelReceive,BorderLayout.NORTH);
        con.add(split,BorderLayout.CENTER);
        panelSend.add(fieldSend,BorderLayout.CENTER);
        panelSend.add(send,BorderLayout.EAST);
        con.add(panelSend,BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                record.gameEnd();
                System.exit(0);
            }
        });         
        this.setVisible(true);
        this.setSize(750,600);
        this.setLocation(100,50);
        //setBounds(60,20,670,540);
        setResizable(false);
        fileChooser=new JFileChooser();
        con.validate();
        validate(); 
    }
    //end  Chess();
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==send){
            if(record.hasLinked()){
                record.sendMessage("mes"+fieldSend.getText());
            }else{
                JOptionPane.showMessageDialog(this,"尚未连接对手，请先建立连接");
            }
        }
        if(e.getSource()==resetGame){
            if(record.hasLinked()){
                record.sendMessage("reset");
                this.labelReceive.setText("已经请示对方重新开局，请等待回应！");
            }else{
                resetGame("reset");
            }            
        }
        //end make
        if(e.getSource()==saveChessBoard){
            int  state=fileChooser.showSaveDialog(null);
            File  saveFile=fileChooser.getSelectedFile();
            if(saveFile!=null&&state==JFileChooser.APPROVE_OPTION){
                try{
                    FileOutputStream  outOne=new FileOutputStream(saveFile);
                    ObjectOutputStream  outTwo=new ObjectOutputStream(outOne);
                    outTwo.writeObject(record.获取棋谱());
                    outOne.close();
                    outTwo.close();
                }
                catch(IOException event){
                    System.out.println("保存棋谱出错。");
                }
            }            
        }
        if(e.getSource()==playChessBoard){
            con.removeAll();
            con.repaint();
            con.validate();
            validate();
            saveChessBoard.setEnabled(false);
            int  state=fileChooser.showOpenDialog(null);
            File  openFile=fileChooser.getSelectedFile();
            if(openFile!=null&&state==JFileChooser.APPROVE_OPTION){
                try{
                    FileInputStream  inOne=new FileInputStream(openFile);
                    ObjectInputStream  inTwo=new  ObjectInputStream(inOne);
                    棋谱=(LinkedList)inTwo.readObject();
                    inOne.close();
                    inTwo.close();
                    ChessBoard  board=new ChessBoard(45,45,9,10,this);
                    demon=new Demon(board,this);
                    demon.set棋谱(棋谱);
                    con.add(demon,BorderLayout.CENTER);
                    con.validate();
                    this.setTitle(playChessBoard.getText()+":"+openFile);
                }
                catch(Exception event){
                    JLabel  label=new JLabel("你选择的文件非棋谱文件");
                    label.setFont(new Font("隶书",Font.BOLD,40));
                    label.setForeground(Color.red);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    con.add(label,BorderLayout.CENTER);
                    con.validate();
                    this.setTitle("无法打开文件");
                    validate();                    
                }
            }else {
                JLabel  label=new JLabel("请选择要演示的棋谱文件");
                label.setFont(new Font("隶书",Font.BOLD,40));
                label.setForeground(Color.pink);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                con.add(label,BorderLayout.CENTER);
                con.validate();
                this.setTitle("未选择棋谱文件");
                validate();           
                
            }          
            
        }
        if(e.getSource()==help){
            About  about=new About(this);                
        }
    }
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        Chess  ok=new Chess(); 
    }   

    public void resetGame(String flag) {
        record.gameEnd();
        con.removeAll();
        saveChessBoard.setEnabled(true);
        this.setTitle(resetGame.getText());
        board=new ChessBoard(45,45,9,10,this);
        record=board.record;
        JSplitPane  split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,board,record);
        split.setDividerSize(5);
        split.setDividerLocation(460);
        con.add(labelReceive,BorderLayout.NORTH);
        con.add(split,BorderLayout.CENTER);
        con.add(panelSend,BorderLayout.SOUTH);
        this.labelReceive.setText("已经重新开始");
        validate();
        if(flag.equals("end")){
            labelReceive.setText("此局结束");
        }
        if(flag.equals("reset")){
            labelReceive.setText("重新开局");
        }
    }
}
