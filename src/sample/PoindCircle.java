package sample;
//Коллекция для хранения точек
import javafx.scene.shape.Circle;
import lombok.Data;

@Data
public class PoindCircle {
private Circle circle;
private String id;
private double x;
private double y;
PoindCircle(Circle c, String i, double rx, double ry){
    this.circle=c;
    this.id=i;
    this.x=rx;
    this.y=ry;

}
}
