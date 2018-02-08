package ChinaChess;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ChessPiece extends JLabel{
    String  name;
    Color  backColor=null;
    Color  foreColor;
    String  chessColor=null;
    ChessBoard  board=null;
    int  width,height;
    public ChessPiece(String name,Color fc,Color bc,int width,int height,ChessBoard board) {
        this.name=name;
        this.board=board;
        this.width=width;
        this.height=height;
        foreColor=fc;
        backColor=bc;
        setSize(width,height);
        setBackground(bc);   
        addMouseMotionListener(board);        
        addMouseListener(board);
    }
    public void paint(Graphics g){
        g.setColor(foreColor);
        g.fillOval(2,2,width-2,height-2);
        g.setColor(Color.white);
        g.setFont(new Font("隶书",Font.BOLD,28));
        g.drawString(name,7,height-8);
        g.setColor(Color.yellow);
        g.drawOval(2,2,width-2,height-2);        
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public String  getName(){
        return name;        
    }
    public Color  getChessColor(){
        return  foreColor;
    }
    public String  getChessSex(){
        return chessColor;
    }
    public void setChessSex(String s){
        chessColor=s;
    }

}
