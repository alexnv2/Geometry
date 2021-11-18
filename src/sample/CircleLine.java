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
    private double x;//мировые координаты центра окружности
    private double y;
    private String id; //имя линии одна буква
    private double radius;//радиус окружности в мировых координатах
    private String poindID;//имя точки центра окружности

    CircleLine(Circle c, double x0, double y0, String o, double r1, String p){
        this.circle=c;
        this.x=x0;
        this.y=y0;
        this.id=o;
        this.radius=r1;
        this.poindID=p;
    }
}
