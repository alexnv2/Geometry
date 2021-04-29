package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Segment extends Line {

    public Segment(){
      this.cirStart();
      this.cirEnd();

       }
       Circle cirStart() {
          return( new Circle(super.getStartX(), super.getStartY(), 5, Color.RED));

       }
       Circle cirEnd(){
           return (new Circle(super.getEndX(),super.getEndY(),5, Color.RED));

       }
}
