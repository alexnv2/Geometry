package sample;

import javafx.scene.text.Text;
import lombok.Data;

/**
 * Класс для имен точек, прямых, углов
 */
@Data
public class NamePoindLine {
    private Text text;
    private String id;//имя точка привязки (центр оркужности, для отрезкоа, лучей и прямых центр между этими точками)
    private double dX;//смещение по оси Х от точки привязки
    private double dY;// в мировых координатах
    private double X;//координаты размещения имени мировые
    private double Y;

    //Констуктор
    NamePoindLine(Text text, String id, double dx, double dy, double x, double y){
        this.text=text;
        this.id=id;
        this.dX=dx;
        this.dY=dy;
        this.X=x;
        this.Y=y;
    }
}
