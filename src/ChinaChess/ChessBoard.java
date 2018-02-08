package ChinaChess;

import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;


public class ChessBoard extends JPanel implements MouseListener,MouseMotionListener{    
    public ChessPoint  point[][];
    public int  unitWidth,unitHeight;
    int  x轴长,y轴长;
    int  x,y;
    boolean  move=false;
    public String  红方颜色="红色",黑方颜色="黑色";
    ChessPiece  红车1,红车2,红马1,红马2,红相1,红相2,红帅,红士1,红士2,红兵1,红兵2,红兵3,红兵4,红兵5,红炮1,红炮2;
    ChessPiece  黑车1,黑车2,黑马1,黑马2,黑象1,黑象2,黑将,黑士1,黑士2,黑卒1,黑卒2,黑卒3,黑卒4,黑卒5,黑炮1,黑炮2;
    int  startX,startY;
    int startI;
    int startJ;      
    Rule  rule=null;
    public MakeChessManual  record=null;
    String send;
    public Color myColor;
    boolean can=false;                                //可否走棋  //主动挑战者不先走棋。
    boolean gameEnd=false;
    
    public ChessBoard(int w,int h,int r,int c,Chess label) {

        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);
        Color  bc=getBackground();
        unitWidth=w;
        unitHeight=h;
        x轴长=r;
        y轴长=c;
        point=new ChessPoint[r+1][c+1];
        for(int i=1;i<=r;i++){
            for(int j=1;j<=c;j++){
                point[i][j]=new ChessPoint(i*unitWidth,j*unitHeight,false);
            }
        }       
        //////////////////////////////////////
        红车1=new ChessPiece("车",Color.red,bc,w-4,h-4,this);        
        红车2=new ChessPiece("车",Color.red,bc,w-4,h-4,this);
        红马1=new ChessPiece("马",Color.red,bc,w-4,h-4,this);
        红马2=new ChessPiece("马",Color.red,bc,w-4,h-4,this);
        红相1=new ChessPiece("相",Color.red,bc,w-4,h-4,this);
        红相2=new ChessPiece("相",Color.red,bc,w-4,h-4,this);
        红帅= new ChessPiece("帅",Color.red,bc,w-4,h-4,this);
        红士1=new ChessPiece("士",Color.red,bc,w-4,h-4,this);
        红士2=new ChessPiece("士",Color.red,bc,w-4,h-4,this);
        红兵1=new ChessPiece("兵",Color.red,bc,w-4,h-4,this);
        红兵2=new ChessPiece("兵",Color.red,bc,w-4,h-4,this);
        红兵3=new ChessPiece("兵",Color.red,bc,w-4,h-4,this);
        红兵4=new ChessPiece("兵",Color.red,bc,w-4,h-4,this);
        红兵5=new ChessPiece("兵",Color.red,bc,w-4,h-4,this);
        红炮1=new ChessPiece("炮",Color.red,bc,w-4,h-4,this);
        红炮2=new ChessPiece("炮",Color.red,bc,w-4,h-4,this);
        红车1.setChessSex(红方颜色);
        红车2.setChessSex(红方颜色);
        红马1.setChessSex(红方颜色);
        红马2.setChessSex(红方颜色);
        红相1.setChessSex(红方颜色);
        红相2.setChessSex(红方颜色);
        红帅.setChessSex(红方颜色);
        红士1.setChessSex(红方颜色);
        红士2.setChessSex(红方颜色);
        红兵1.setChessSex(红方颜色);
        红兵2.setChessSex(红方颜色);
        红兵3.setChessSex(红方颜色);        
        红兵4.setChessSex(红方颜色);
        红兵5.setChessSex(红方颜色);
        红炮1.setChessSex(红方颜色);
        红炮2.setChessSex(红方颜色);     
        //end  red
        黑车1=new ChessPiece("车",Color.blue,bc,w-4,h-4,this);
        黑车2=new ChessPiece("车",Color.blue,bc,w-4,h-4,this);
        黑马1=new ChessPiece("马",Color.blue,bc,w-4,h-4,this);
        黑马2=new ChessPiece("马",Color.blue,bc,w-4,h-4,this);
        黑象1=new ChessPiece("象",Color.blue,bc,w-4,h-4,this);
        黑象2=new ChessPiece("象",Color.blue,bc,w-4,h-4,this);
        黑将=new ChessPiece("将",Color.blue,bc,w-4,h-4,this);
        黑士1=new ChessPiece("士",Color.blue,bc,w-4,h-4,this);
        黑士2=new ChessPiece("士",Color.blue,bc,w-4,h-4,this);
        黑卒1=new ChessPiece("卒",Color.blue,bc,w-4,h-4,this);
        黑卒2=new ChessPiece("卒",Color.blue,bc,w-4,h-4,this);
        黑卒3=new ChessPiece("卒",Color.blue,bc,w-4,h-4,this);
        黑卒4=new ChessPiece("卒",Color.blue,bc,w-4,h-4,this);
        黑卒5=new ChessPiece("卒",Color.blue,bc,w-4,h-4,this);
        黑炮1=new ChessPiece("炮",Color.blue,bc,w-4,h-4,this);
        黑炮2=new ChessPiece("炮",Color.blue,bc,w-4,h-4,this);
        黑车1.setChessSex(黑方颜色);
        黑车2.setChessSex(黑方颜色);
        黑马1.setChessSex(黑方颜色);
        黑马2.setChessSex(黑方颜色);
        黑象1.setChessSex(黑方颜色);
        黑象2.setChessSex(黑方颜色);
        黑将.setChessSex(黑方颜色);
        黑士1.setChessSex(黑方颜色);
        黑士2.setChessSex(黑方颜色);
        黑卒1.setChessSex(黑方颜色);
        黑卒2.setChessSex(黑方颜色);
        黑卒3.setChessSex(黑方颜色);
        黑卒4.setChessSex(黑方颜色);
        黑卒5.setChessSex(黑方颜色);
        黑炮1.setChessSex(黑方颜色);
        黑炮2.setChessSex(黑方颜色);
        //end black        
        point[1][10].setPiece(红车1,this);
        point[2][10].setPiece(红马1,this);
        point[3][10].setPiece(红相1,this);
        point[4][10].setPiece(红士1,this);
        point[5][10].setPiece(红帅,this);
        point[6][10].setPiece(红士2,this);
        point[7][10].setPiece(红相2,this);
        point[8][10].setPiece(红马2,this);
        point[9][10].setPiece(红车2,this);
        point[2][8].setPiece(红炮1,this);
        point[8][8].setPiece(红炮2,this);
        point[1][7].setPiece(红兵1,this);
        point[3][7].setPiece(红兵2,this);
        point[5][7].setPiece(红兵3,this);
        point[7][7].setPiece(红兵4,this);
        point[9][7].setPiece(红兵5,this);
        
        point[1][1].setPiece(黑车1,this);
        point[2][1].setPiece(黑马1,this);
        point[3][1].setPiece(黑象1,this);
        point[4][1].setPiece(黑士1,this);
        point[5][1].setPiece(黑将,this);
        point[6][1].setPiece(黑士2,this);
        point[7][1].setPiece(黑象2,this);
        point[8][1].setPiece(黑马2,this);
        point[9][1].setPiece(黑车2,this);
        point[2][3].setPiece(黑炮1,this);
        point[8][3].setPiece(黑炮2,this);
        point[1][4].setPiece(黑卒1,this);
        point[3][4].setPiece(黑卒2,this);
        point[5][4].setPiece(黑卒3,this);
        point[7][4].setPiece(黑卒4,this);
        point[9][4].setPiece(黑卒5,this); 
        rule=new Rule(this,point);
        record=new MakeChessManual(this,point,label);        
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int j=1;j<=y轴长;j++){
            g.drawLine(point[1][j].x,point[1][j].y,point[x轴长][j].x,point[x轴长][j].y);
        }
        for(int i=1;i<=x轴长;i++){
            if(i!=1&&i!=x轴长){
                g.drawLine(point[i][1].x,point[i][1].y,point[i][y轴长-5].x,point[i][y轴长-5].y);
                g.drawLine(point[i][y轴长-4].x,point[i][y轴长-4].y,point[i][y轴长].x,point[i][y轴长].y);
            }else {
                g.drawLine(point[i][1].x,point[i][1].y,point[i][y轴长].x,point[i][y轴长].y);
            }            
        }
        g.drawLine(point[4][1].x,point[4][1].y,point[6][3].x,point[6][3].y);
        g.drawLine(point[6][1].x,point[6][1].y,point[4][3].x,point[4][3].y);
        g.drawLine(point[4][8].x,point[4][8].y,point[6][y轴长].x,point[6][y轴长].y);
        g.drawLine(point[4][y轴长].x,point[4][y轴长].y,point[6][8].x,point[6][8].y);
        for(int i=1;i<=x轴长;i++){
            g.drawString(i+"",i*unitWidth,unitHeight/2);
        }
        int j=1;
        for(char c='A';c<='J';c++){
            g.drawString(""+c,unitWidth/4,j*unitHeight);
            j++;
        }        
    }
    public void mousePressed(MouseEvent e){
        if(myColor==null){
            JOptionPane.showMessageDialog(this,"尚未连接对手，请先建立连接");
            return;
        }
        ChessPiece  piece=null;
        Rectangle  rect=null;
        if(e.getSource()==this){
            move=false;
        }
        if(move==false){
            if(e.getSource() instanceof ChessPiece){
                piece=(ChessPiece)e.getSource();
                startX=piece.getBounds().x;
                startY=piece.getBounds().y;
                rect=piece.getBounds();
                for(int i=1;i<=x轴长;i++){
                    for(int j=1;j<=y轴长;j++){
                        int x=point[i][j].getX();
                        int y=point[i][j].getY();
                        if(rect.contains(x,y)){
                            startI=i;
                            startJ=j;
                            break;
                        }
                    }
                }            
            }
        }  
    }
    public void mouseMoved(MouseEvent e){

    }
    public void mouseDragged(MouseEvent e){
        ChessPiece  piece=null;
        if(e.getSource() instanceof ChessPiece){
            piece=(ChessPiece)e.getSource();
            move=true;  
            e=SwingUtilities.convertMouseEvent(piece,e,this);
        }
        if(e.getSource()==this){
            if(move&&piece!=null){
                x=e.getX();
                y=e.getY();
                if(can&&piece.getChessColor().equals(myColor)){
                    piece.setLocation(x-piece.getWidth()/2,y-piece.getHeight()/2);
                }
            }
        }
    }
    public void mouseReleased(MouseEvent e){
        ChessPiece  piece=null;
        move=false;
        Rectangle  rect=null;
        if(e.getSource() instanceof ChessPiece){
            piece=(ChessPiece)e.getSource();
            rect=piece.getBounds();
            e=SwingUtilities.convertMouseEvent(piece,e,this);                   
        }
        if(piece!=null&&!can){
            if(piece.getChessColor().equals(myColor)){
                JOptionPane.showMessageDialog(this,"请等候对方走棋. . . . . . . . .");
            }else{
                JOptionPane.showMessageDialog(this,"您不能动对方的棋子.");
            }
            return;
        }
        if(e.getSource()==this){
            boolean  containChessPoint=false;
            int x=0,y=0;
            int m=0,n=0;
            if(piece!=null){
                for(int i=1;i<=x轴长;i++){
                    for(int j=1;j<=y轴长;j++){
                        x=point[i][j].getX();
                        y=point[i][j].getY();
                        if(rect.contains(x,y)){
                            containChessPoint=true;
                            m=i;
                            n=j;
                            break;
                        }
                    }
                }
            }
            if(piece!=null&&containChessPoint){
                Color  pieceColor=piece.getChessColor();
                if(point[m][n].isPiece()){
                    Color  c=(point[m][n].getPiece()).getChessColor();
                    if(pieceColor.getRGB()==c.getRGB()){
                        piece.setLocation(startX,startY);
                        (point[startI][startJ]).setPiece(true);
                    }else{
                        boolean ok=false;
                        if(piece.getChessColor().equals(myColor)&&can){
                            ok=rule.movePieceRule(piece,startI,startJ,m,n);
                        }                      
                         if(ok){
                             ChessPiece  pieceRemoved=point[m][n].getPiece();
                             point[m][n].reMovePiece(pieceRemoved,this);
                             point[m][n].setPiece(piece,this);
                             (point[startI][startJ]).setPiece(false);
                             record.记录棋谱(piece,startI,startJ,m,n);
                             send=produceSend(startI,startJ,m,n);    
                             if(can&&piece.getChessColor().equals(myColor)){
                                 record.sendMessage(send);
                                 if(gameEnd){
                                     record.setAlertText("☆恭喜您，旗开得胜☆");
                                     record.gameEnd();
                                     return ;
                                 }
                                 can=false;
                             }                            
                             record.记录吃掉的棋子(pieceRemoved);                           
                             validate();
                             repaint();
                         }else{
                             piece.setLocation(startX,startY);
                             (point[startI][startJ]).setPiece(true);
                         }                         
                    }
                }else {
                    boolean  ok=rule.movePieceRule(piece,startI,startJ,m,n);
                    if(ok){
                        point[m][n].setPiece(piece,this);
                        (point[startI][startJ]).setPiece(false);
                        record.记录棋谱(piece,startI,startJ,m,n);                        
                        send=produceSend(startI,startJ,m,n);                     
                        if(can&&piece.getChessColor().equals(myColor)){
                            record.sendMessage(send);
                            can=false;
                        }
                        record.记录吃掉的棋子("没吃棋子");
                        can=false;
                    }else {
                        piece.setLocation(startX,startY);
                        (point[startI][startJ]).setPiece(true);
                    }
                }                    
            }
            if(piece!=null&&!containChessPoint){
                piece.setLocation(startX,startY);
                (point[startI][startJ]).setPiece(true);
            }
            if(piece!=null&&!piece.getChessColor().equals(myColor)){
                piece.setLocation(startX,startY);
                (point[startI][startJ]).setPiece(true);                
            }
        }
    }
    public void mouseEntered(MouseEvent e){
        
    }
    public void mouseExited(MouseEvent e){
        
    }
    public void mouseClicked(MouseEvent e){
        
    }
    public void hisStep(int startI,int startJ,int endI,int endJ){        
        ChessPiece piece=point[startI][startJ].getPiece();  
        rule.movePieceRule(piece,startI,startJ,endI,endJ);
        if(point[endI][endJ].isPiece()){  
            ChessPiece  pieceRemoved=point[endI][endJ].getPiece();
            point[endI][endJ].reMovePiece(pieceRemoved,this);
            point[endI][endJ].setPiece(piece,this);
            (point[startI][startJ]).setPiece(false);
            record.记录棋谱(piece,startI,startJ,endI,endJ);
            record.记录吃掉的棋子(pieceRemoved);         
            validate();
            repaint();
        } else{
            point[endI][endJ].setPiece(piece,this);
            (point[startI][startJ]).setPiece(false);
            record.记录棋谱(piece,startI,startJ,endI,endJ);
            record.记录吃掉的棋子("没吃棋子");
        }        
        if(gameEnd){
            record.setAlertText("☆对方大获全胜☆");
            record.gameEnd();
        }else{
            can=true;       //可以走棋了
            record.setAlertText("☆请您走棋☆"); 
        }
    }
    //处理发送用的信息。
    private String produceSend(int startI, int startJ, int m, int n) {
        //座标反转公式：x=10-x; y=11-y;但Y方向要多减1，并在接收时加1；
        return "step"+(10-startI)+""+(10-startJ)+""+(10-m)+""+(10-n);
    }
    public void gameEnd(){
        gameEnd=true;
    }
}
