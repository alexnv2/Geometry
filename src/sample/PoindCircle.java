package sample;
//Коллекция для хранения точек
import javafx.scene.shape.Circle;
import lombok.Data;

@Data
public class PoindCircle {
private Circle circle; //точка
private String id; //номер
private double x; //координата x
private double y; //координата y
private boolean bMove;// true- разрешено перемещение, false - точка расчетная, перемещение запрещено
private boolean bSelect;//true - выделена на экране для группового удаления, false - по умолчанию
PoindCircle(Circle c, String i, double rx, double ry, boolean bm, boolean bs){
    this.circle=c;
    this.id=i;
    this.x=rx;
    this.y=ry;
    this.bMove=bm;
    this.bSelect=bs;

}
}
