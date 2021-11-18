package sample;
//Коллекция для хранения диний

import javafx.scene.shape.Line;
import lombok.Data;

/**
 * Класс PoindLine для коллекции PoindLine. Предназначен для хранения информации об отрезках, прямых,
 * лучей, перпендикуляров, медиан, биссектрис, высот.
 */
@Data
public class PoindLine {
    private Line line;
    private String id;//имя линии по двум точкам (АВ)
    private double stX;//координаты начал
    private double stY;
    private double enX;//координаты конца
    private double enY;
    private boolean bMove;// true- разрешено перемещение, false - линия расчетная, перемещение запрещено
    private boolean bSelect;//true - выделена на экране для группового удаления, false - по умолчанию
    private int segment;//0-отрезок, 1-луч, 2-прямая, 3-треугольник, 4-медиана, 5-биссектриса, 6-высота, 7-перпендикуляр

    PoindLine(Line l, String i, double x0, double y0, double x1, double y1, boolean bm, boolean bs, int seg) {
        this.line = l;
        this.id = i;
        this.stX = x0;
        this.stY = y0;
        this.enX = x1;
        this.enY = y1;
        this.bMove = bm;
        this.bSelect = bs;
        this.segment = seg;
    }
}
