package sample;
//Класс для хранения информации о геометрических фигурах
import javafx.scene.shape.Shape;
import lombok.Data;

@Data
public class Figura {
    private Shape shape;
    private String id;
    private double vX;
    private double vY;

    public Figura(Shape s, String i, double x, double y){
        this.shape=s;
        this.id=i;
        this.vX=x;
        this.vY=y;
    }
}
