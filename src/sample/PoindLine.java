package sample;
//Коллекция для хранения диний
import javafx.scene.shape.Line;
import lombok.Data;

@Data
public class PoindLine {
    private Line line;
    private String id;
    private double stX;
    private double stY;
    private double enX;
    private  double enY;
    private boolean bMove;// true- разрешено перемещение, false - линия расчетная, перемещение запрещено
    private boolean bSelect;//true - выделена на экране для группового удаления, false - по умолчанию
    private int segment;//0-отрезок, 1-луч, 2 прямая,
    PoindLine(Line l, String i, double x0, double y0, double x1, double y1, boolean bm, boolean bs, int seg ){
        this.line=l;
        this.id=i;
        this.stX=x0;
        this.stY=y0;
        this.enX=x1;
        this.enY=y1;
        this.bMove=bm;
        this.bSelect=bs;
        this.segment=seg;
    }
}
