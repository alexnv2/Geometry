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

    PoindLine(Line l, String i, double x0, double y0, double x1, double y1 ){
        this.line=l;
        this.id=i;
        this.stX=x0;
        this.stY=y0;
        this.enX=x1;
        this.enY=y1;
    }
}
