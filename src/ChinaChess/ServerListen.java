package ChinaChess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import ChinaChess.*;

public class ServerListen implements Runnable{
    Socket receiveSocket;
    ServerSocket serverSocket;
    BufferedReader reader;
    String send;    
    int PORT;    
    PrintWriter writer;   //用来发送message
    MakeChessManual record;
    Thread run;
    private boolean gameEnd;
    
    public ServerListen(MakeChessManual record) {
        this.record=record;
        this.PORT=8888;
    }

    public void run() {
        try {
            serverSocket=new ServerSocket(PORT);
        }catch(IOException ex){
            ex.printStackTrace();
        }
        try {
            receiveSocket=serverSocket.accept();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        if(receiveSocket!=null){
            record.setHasLinked(true);
            JOptionPane.showMessageDialog(record.chessFrame,"成功接收对方连接，您持红子，请走棋！");
            gameEnd=false;
        }
        try {
            writer=new PrintWriter(receiveSocket.getOutputStream(),true);
            reader=new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }         
        while(!gameEnd) {
            String message = null;
            try {
                message=reader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(message.equals("undo")){
                int canUndo=JOptionPane.showConfirmDialog(record.chessFrame,"对方渴求悔棋，是否同意？");
                if(canUndo==JOptionPane.CANCEL_OPTION||canUndo==JOptionPane.NO_OPTION){
                    writer.println("no");
                    writer.flush();
                }else if(canUndo==JOptionPane.OK_OPTION){
                    writer.println("yes");
                    record.board.can=false;
                    record.backChess();
                    writer.flush(); 
                }
            }else if(message.equals("end")){
                break;
            }
            record.doMessage(message);
        }
        endServer();
    }
    
    public void sendServerStep(String s){
        writer.println(s);
        writer.flush();
    }
    public void endServer(){
        try{
            serverSocket.close();
            reader.close();
            writer.close();
            receiveSocket.close();
            record.chessFrame.labelReceive.setText("成功退出服务器,请重开局.");
            gameEnd=true;
            run=null;
            return;
        }catch(Exception exc){
            record.chessFrame.labelReceive.setText(exc+"服务器关闭异常！！！！");
            return;
        }
        //record.chessFrame.labelReceive.setText("对方退出，请重开局");
    }
    public void start() {
        if(run==null){
            run = new Thread(this);
        }         
        record.afterLink("server"); 
        run.start();                          //启动监听线程
    }
}
