package sample;

import javafx.scene.shape.Polygon;
import lombok.Data;

/**
 * Класс для колекции треугольников
 */
@Data
public class TreangleName {
    private Polygon polygon;
    private String ID;

    TreangleName(Polygon p,String id){
        this.polygon=p;
        this.ID=id;
    }
}
