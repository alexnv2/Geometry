package sample;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements  Observable {
    public boolean getAddPoind;
    //Переменные класса
    private Circle vertex;
    private Line sideAll;
    private Label leftStatus;
    private double verX;
    private double verY;
    private double verX1;
    private double verY1;
    public boolean poindAdd;//true - режим добавления, false - обычный режим
    private int indexPoind=1;
    public boolean addPoind;



    //Определяем связанный список для регистрации классов слушателей
    private LinkedList<Observer> observers=new LinkedList<>();


    //Конструктор без переменных
    Model(){
     }

    //регистрация слушателя
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }
    //уведомление слушателя
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.notification(message);
          }
    }

    public void createPoind(AnchorPane s){
        PoindCircle a=new PoindCircle();
        addPoind=false;
        a.setCenterX(50);
        a.setCenterY(50);
        a.setRadius(5);
        a.setFill(Color.RED);
        a.setId("Точка "+indexPoind);//Индефикатор узла
        a.setUserData(indexPoind);//Имя узла
        getAddPoind=true;

        //Обработка событий
        EventHandler<MouseEvent> eventHandler = e -> {
            if(e.getEventType()==MouseEvent.MOUSE_DRAGGED) {
                a.setFill(Color.RED);
                VertexGo(a);
            }
            if(e.getEventType()==MouseEvent.MOUSE_MOVED && poindAdd==true && a.getUserData().equals(indexPoind)) {
                a.setCursor(Cursor.NONE);
                VertexGo(a);
            }
            if(e.getEventType()==MouseEvent.MOUSE_CLICKED && poindAdd==true){
                a.setFill(Color.DARKSLATEBLUE);
                poindAdd=false;
                indexPoind+=1;
            }
            if(e.getEventType()==MouseEvent.MOUSE_ENTERED){
                leftStatus.setText(a.getId());
            }
            if(e.getEventType()==MouseEvent.MOUSE_PRESSED) {
                a.setFill(Color.RED);
                a.setCursor(Cursor.CLOSED_HAND);
            }
            if(e.getEventType()==MouseEvent.MOUSE_RELEASED){
                a.setFill(Color.DARKSLATEBLUE);
                leftStatus.setText("");
            }

        };
        //Фильтры регистрации событий для объекта
        a.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        a.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler);
        a.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler);
        a.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
        a.addEventFilter(MouseEvent.MOUSE_PRESSED,eventHandler);
        a.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler);
        s.addEventFilter(MouseEvent.MOUSE_MOVED,eventHandler);
        //Добавить точку на рабочий стол
        s.getChildren().add(a);


    }
     public void createLine(AnchorPane s){
         Segment ab=new Segment();
         ab.setStartX(100);
         ab.setStartY(100);
         verX1=100;
         verY1=100;
         ab.setEndX(160);
         ab.setEndY(260);
         Circle ab1=ab.cirStart();
         Circle ab2=ab.cirEnd();
         ab.setId("Line");
         s.getChildren().addAll(ab,ab1,ab2);

         EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

             @Override
             public void handle(MouseEvent e) {

                 if(e.getEventType()==MouseEvent.MOUSE_ENTERED){
                     System.out.println(ab.getId());
                 }
                 if(e.getEventType()==MouseEvent.MOUSE_PRESSED){
                     ab.setFill(Color.RED);
                 }
                 if(e.getEventType()==MouseEvent.MOUSE_DRAGGED) {
                     ab2.setFill(Color.YELLOW);
                     VertexGo(ab2);
                     SideGo(ab);
                 }
             }


         };




         //Registering the event filter
         ab.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
         ab.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler);
         ab.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler);
         ab.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
         ab.addEventFilter(MouseEvent.MOUSE_PRESSED,eventHandler);
         s.addEventFilter(MouseEvent.MOUSE_MOVED,eventHandler);



     }

    //Перемещение вершин треугольника
    void VertexGo(Circle o){
        vertex=o;
        notifyObservers("VertexGo");
    }
    //Перемещение сторон
    void SideGo(Line o){
        sideAll=o;
        notifyObservers("SideGo");
    }
}
