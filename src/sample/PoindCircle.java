package sample;
//Коллекция для хранения точек

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
private Line line;//линия к которой принадлежит  точка
private double t;//кооффициент для параметрического уравнения прямой (0.0 по умолчанию)
private boolean bLine;//true - точка принадлежит прямой (по умолчанию false)

PoindCircle(Circle c, String n, double rx, double ry, boolean bm, boolean bs, int i,Line l, double t, boolean bL){
    this.circle=c;
    this.id=n;
    this.x=rx;
    this.y=ry;
    this.bMove=bm;
    this.bSelect=bs;
    this.index=i;
    this.line=l;
    this.t=t;
    this.bLine=bL;
}
}
