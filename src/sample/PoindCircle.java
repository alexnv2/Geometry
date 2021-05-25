package sample;
//Коллекция для хранения точек
import javafx.scene.shape.Circle;
import lombok.Data;

/**
 * Класс PoindCircle
 * Для колеккции PoindCircle
 */
@Data
public class PoindCircle {
private Circle circle; //точка
private String id; //номер
private double x; //координата x
private double y; //координата y
private boolean bMove;// true- разрешено перемещение, false - точка расчетная, перемещение запрещено
private boolean bSelect;//true - выделена на экране для группового удаления, false - по умолчанию
private int index; //счетчик точек которые входят в геомтрические фигуры. Точку нельзя удалить, пока индекс нестанет равным 0.
PoindCircle(Circle c, String n, double rx, double ry, boolean bm, boolean bs, int i){
    this.circle=c;
    this.id=n;
    this.x=rx;
    this.y=ry;
    this.bMove=bm;
    this.bSelect=bs;
    this.index=i;

}
}
