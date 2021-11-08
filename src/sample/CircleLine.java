package sample;


import javafx.scene.shape.Circle;
import lombok.Data;

/**
 * Класс CircleLine предназначен для хранения коллекции CircleLine. Предназначен для хранения информации об
 * окружностях.
 */
@Data
public class CircleLine {
    private Circle circle;
    private String id; //имя линии одна буква
    private double radius;//радиус окружности в мировых координатах
    private String poindID;//имя точки центра окружности

    CircleLine(Circle c, String o, double r, String p){
        this.circle=c;
        this.id=o;
        this.radius=r;
        this.poindID=p;
    }
}
