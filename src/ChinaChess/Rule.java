package ChinaChess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Rule {
    ChessBoard  board=null;
    ChessPiece  pieceStart=null;
    ChessPoint  point[][];
    int  startI,startJ,endI,endJ;     
    int redManX=5,redManY=10;               //红帅座标
    int blackManX=5,blackManY=1;             //黑将座标
    private boolean eat;
    ChessPiece  pieceEat=null;
    
    public Rule(ChessBoard board,ChessPoint point[][]) {
        this.board=board;
        this.point=point;        
    }
    public boolean movePieceRule(ChessPiece piece,int startI,int startJ,int endI,int endJ){
        if(point[endI][endJ].isPiece()){
            pieceEat=point[endI][endJ].getPiece();
            eat=true;
        }else{
            eat=false;
        }
        pieceStart=piece;        
        this.startI=startI;
        this.startJ=startJ;
        this.endI=endI;
        this.endJ=endJ;
        int  maxI=Math.max(startI,endI);
        int  minI=Math.min(startI,endI);
        int  maxJ=Math.max(startJ,endJ);
        int  minJ=Math.min(startJ,endJ);
        boolean  canMove=false;
        if(pieceStart.getName().equals("车")){
            if(startI==endI){
                int  j=0;
                for(j=minJ+1;j<=maxJ-1;j++){
                    if(point[startI][j].isPiece()){
                        canMove=false;
                        break;
                    }                            
                }
                if(j==maxJ){
                    canMove=true;
                }            
            }else  if(startJ==endJ){
                int i=0;
                for(i=minI+1;i<=maxI-1;i++){
                    if(point[i][startJ].isPiece()){
                        canMove=false;
                        break;
                    }                
                }
                if(i==maxI){
                    canMove=true;
                }           
            }else {
                canMove=false;
            } 
        }else if(pieceStart.getName().equals("马")){
            int xAxle=Math.abs(startI-endI);
            int yAxle=Math.abs(startJ-endJ);
            if(xAxle==2&&yAxle==1){
                if(endI>startI){
                    if(point[startI+1][startJ].isPiece()){
                        canMove=false;
                        JOptionPane.showMessageDialog(board,"小心马腿！");
                    }else {
                        canMove=true;
                    }
                }
                if(endI<startI){
                    if(point[startI-1][startJ].isPiece()){
                        JOptionPane.showMessageDialog(board,"小心马腿！");
                        canMove=false;
                    }else{
                        canMove=true;
                    }
                }
            }else if(xAxle==1&&yAxle==2){
                if(endJ>startJ){
                    if(point[startI][startJ+1].isPiece()){
                        canMove=false;
                        JOptionPane.showMessageDialog(board,"小心马腿！");
                    }else {
                        canMove=true;
                    }
                }            
                if(endJ<startJ){
                    if(point[startI][startJ-1].isPiece()){
                        canMove=false;
                        JOptionPane.showMessageDialog(board,"小心马腿！");
                    }else  {
                        canMove=true;
                    }
                }
            }else {
                canMove=false;
            }            
        }else if(pieceStart.getName().equals("象")){
            int centerI=(startI+endI)/2;
            int centerJ=(startJ+endJ)/2;
            int xAxle=Math.abs(startI-endI);
            int yAxle=Math.abs(startJ-endJ);
            if(xAxle==2&&yAxle==2&&endJ<=5){
                if(point[centerI][centerJ].isPiece()){
                    canMove=false;
                    JOptionPane.showMessageDialog(board,"象眼有情况！");
                }else {
                    canMove=true;
                }
            }else {
                canMove=false;
            }
        }else if(pieceStart.getName().equals("相")){
            int centerI=(startI+endI)/2;
            int centerJ=(startJ+endJ)/2;
            int xAxle=Math.abs(startI-endI);
            int yAxle=Math.abs(startJ-endJ);
            if(xAxle==2&&yAxle==2&&endJ>=6){
                if(point[centerI][centerJ].isPiece()){
                    canMove=false;
                    JOptionPane.showMessageDialog(board,"相眼有情况！");
                }else{
                    canMove=true;
                }
            }else {
                canMove=false;
            }
        }else if(pieceStart.getName().equals("炮")){
            int number=0;
            if(startI==endI){
                int j=0;
                for(j=minJ+1;j<=maxJ-1;j++){
                    if(point[startI][j].isPiece()){
                        number++;
                    }
                }
                if(number>1){
                    JOptionPane.showMessageDialog(board,"炮不能架一个以上的棋子！");
                    canMove=false;
                }else if(number==1){
                    if(point[endI][endJ].isPiece()){
                        canMove=true;
                    }else{
                        JOptionPane.showMessageDialog(board,"炮架空了！");
                    }
                }else if(number==0&&!point[endI][endJ].isPiece()){
                    canMove=true;
                }
            }else if(startJ==endJ){
                int i=0;
                for(i=minI+1;i<=maxI-1;i++){
                    if(point[i][startJ].isPiece()){
                        number++;
                    }
                }
                if(number>1){
                    canMove=false;
                }else if(number==1){
                    if(point[endI][endJ].isPiece()){
                        canMove=true; 
                    }                                       
                }else if(number==0&&!point[endI][endJ].isPiece()){
                    canMove=true;
                }
            }else {
                canMove=false;
            }
        }else if(pieceStart.getName().equals("兵")){
            int  xAxle=Math.abs(startI-endI);
            int  yAxle=Math.abs(startJ-endJ);
            if(endJ>=6){
                if(startJ-endJ==1&&xAxle==0){
                    canMove=true;
                }else {
                    canMove=false;
                }
            }else if(endJ<=5){
                if((startJ-endJ==1)&&xAxle==0){
                    canMove=true;
                }else if((endJ-startJ==0)&&(xAxle==1)){
                    canMove=true;
                }else {
                    canMove=false;
                }
            }
        }else if(pieceStart.getName().equals("卒")){
            int  xAxle=Math.abs(startI-endI);
            int  yAxle=Math.abs(startJ-endJ);
            if(endJ<=5){
                if(endJ-startJ==1&&xAxle==0){
                    canMove=true;
                }else {
                    canMove=false;
                }
            }else if(endJ>=6){
                if((endJ-startJ==1)&&xAxle==0){
                    canMove=true;
                }else if((endJ-startJ==0)&&(xAxle==1)){
                    canMove=true;
                }else {
                    canMove=false;
                }
            }
        }else if(pieceStart.getName().equals("士")){
            int  xAxle=Math.abs(startI-endI);
            int  yAxle=Math.abs(startJ-endJ);
            if(endI<=6&&endI>=4&&xAxle==1&&yAxle==1){                
                canMove=true;
            }else {
                canMove=false;
            }            
        }else if((pieceStart.getName().equals("帅"))||(pieceStart.getName().equals("将"))){
            int  xAxle=Math.abs(startI-endI);
            int  yAxle=Math.abs(startJ-endJ);            
            boolean faceToFace = false;              //是否已经将帅对脸
            boolean has=false;                        //将帅间有棋没有
            if(redManX==blackManX){ 
                int j,man;
                if(redManY>blackManY){
                    j=blackManY+1;
                    man=redManY;
                }else{
                    j=redManY+1;
                    man=blackManY;
                }
                for(;j<man;j++){
                    if(point[startI][j].isPiece()){
                        has=true;
                        break;
                    }
                }
                if(has){
                    faceToFace=false;
                }else{
                    faceToFace=true;
                }
            }
            if(faceToFace==true&&startI==endI){
                canMove=true;
                faceToFace=false;
            }else if(endI<=6&&endI>=4){
                if((xAxle==1&&yAxle==0)||(xAxle==0&&yAxle==1)){
                    if(pieceStart.getName().equals("帅")&&endJ>=8){
                        canMove=true;
                    }
                    if(pieceStart.getName().equals("将")&&endJ<=3){
                        canMove=true;
                    }
                }else {
                    canMove=false;
                }
            }else {
                canMove=false;
            }
            if(canMove){
                if(pieceStart.getName().equals("帅")){
                    redManX+=endI-startI;
                    redManY+=endJ-startJ;
                }
                if(pieceStart.getName().equals("将")){
                    blackManX+=endI-startI;
                    blackManY+=endJ-startJ;
                }              
            }
        }
        if(eat&&canMove){
            if(pieceEat.getName().equals("帅")||pieceEat.getName().equals("将")){            
                board.gameEnd();
            }            
        }        
        return canMove;
    }
}
