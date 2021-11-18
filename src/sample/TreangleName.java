package sample;

import javafx.scene.shape.Polygon;
import lombok.Data;

/**
 * Класс TreangleName для коллекции треугольников TreangleName.
 * Предназначен для хранения информации об треугольниках.
 */
@Data
public class TreangleName {
    private Polygon polygon;
    private String ID;

    TreangleName(Polygon p, String id) {
        this.polygon = p;
        this.ID = id;
    }
}
