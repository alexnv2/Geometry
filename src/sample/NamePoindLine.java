package sample;

import javafx.scene.text.Text;
import lombok.Data;

/**
 * Класс NamePoindLine.
 * Предназначен для коллекции NamePoindLine. Предназначен для хранения имен точек, прямых, углов.
 */
@Data
public class NamePoindLine {
    private Text text;
    private String id;//имя точка привязки (центр окружности, для отрезка, лучей и прямых центр между этими точками)
    private double dX;//смещение по оси Х от точки привязки
    private double dY;// в мировых координатах
    private double X;//координаты размещения имени мировые
    private double Y;
    private boolean visiblePoind;//показывать имя или нет (по умолчанию true)
    private boolean visibleLine;//показывать имя линий или нет(по умолчанию false)
    private boolean visibleArc;//показывать имя угла или нет (по умолчанию false)
    private String type;//poind - имя точки line -имя линии arc - имя угла
    //Конструктор
    NamePoindLine(Text text, String id, double dx, double dy, double x, double y, boolean visP, boolean viiL, boolean visArc, String type){
        this.text=text;
        this.id=id;
        this.dX=dx;
        this.dY=dy;
        this.X=x;
        this.Y=y;
        this.visiblePoind=visP;
        this.visibleLine=viiL;
        this.type=type;
    }
}
