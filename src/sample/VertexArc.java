package sample;

import javafx.scene.shape.Arc;
import lombok.Data;

//класс для хранения углов
@Data
public class VertexArc {
    private Arc arc;
    private String id;//имя угла, типа АВС
    private String nameAngle;//имя вершины греческие символы
    private double centerX;//центр дуги
    private double centerY;
    private double radiusX;//радиус дуги
    private double radiusY;
    private double startAngle;//Начальный угол
    private double lengthAngle;//длина дуги
    private boolean bSelect;//true- для группового выделения
    VertexArc(Arc a, String i, String nameAngle, double cX,double cY, double rX, double rY, double sA, double lA, boolean bS){
    this.arc=a;
    this.id=i;
    this.nameAngle=nameAngle;
    this.centerX=cX;
    this.centerY=cY;
    this.radiusX=rX;
    this.radiusY=rY;
    this.startAngle=sA;
    this.lengthAngle=lA;
    this.bSelect=bS;
    }
}