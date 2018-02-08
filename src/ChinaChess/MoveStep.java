package ChinaChess;
import java.awt.Point;
import java.io.Serializable;

public class MoveStep implements Serializable{
    public Point   pStart,pEnd;
    
    public MoveStep(Point p1,Point p2) {
        pStart=p1;
        pEnd=p2;
    }
    
}
