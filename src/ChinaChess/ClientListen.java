package ChinaChess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
/**
 *
 * @author waveas
 */
public class ClientListen implements Runnable{
    PrintWriter writer;
    int PORT;  
    Socket socket;
    MakeChessManual record;
    Thread run;
    String hisIP;
    boolean gameEnd;
    BufferedReader reader;  
    
    public ClientListen(MakeChessManual rec,String ip) {
        record=rec;
        hisIP=ip;
        PORT=8888; 
    }
    public void sendClientStep(String s){
        writer.println(s);
        writer.flush();
    }
    
    public void run(){
        try {
            socket=new Socket(hisIP,PORT);            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(record.chessFrame,"连接失败！"+ex);
            record.buttonLink.setEnabled(true);
            record.buttonServer.setEnabled(true);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(record.chessFrame,"连接失败！"+ex);
            record.buttonLink.setEnabled(true);
            record.buttonServer.setEnabled(true);
            return;
        }
        JOptionPane.showMessageDialog(record.chessFrame,"连接成功，您持蓝子，请等候对方走棋！");
        try {
            writer=new PrintWriter(socket.getOutputStream(),true); 
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        gameEnd=false;        
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
        endClient();
    }
    
    public void start() { 
        run=new Thread(this);        
        record.afterLink("client");
        run.start();
    }
    public void endClient(){
        try{
            writer.close();
            reader.close();
            socket.close();
            run=null;
        }catch(Exception exc){
            JOptionPane.showMessageDialog(record.chessFrame,"客户服务器关闭异常！！！！");
        }
        record.chessFrame.labelReceive.setText("对方退出，请重开局");
        gameEnd=true;
    }
}
