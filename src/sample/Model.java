package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Data;
import java.util.LinkedList;

@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements  Observable {

    //Переменные класса
    private Circle vertex;
    private Line sideAll;
    private Label leftStatus;
    private Label rightStatus;
    private double verX;
    private double verY;
    private double verX1;
    private double verY1;
   // public boolean poindAdd;//true - режим добавления, false - обычный режим
    private char indexPoind='A';//Индекс для точек
    private char indexLine='a';//Индекс для линий и отрезков
    private char indexArc='A';//Индекс для уголов
   // public  boolean lineAdd;



    //Определяем связанный список для регистрации классов слушателей
    private LinkedList<Observer> observers=new LinkedList<>();
    //Коллекция для хранения фигур
    private ObservableList<Figura> figCol = FXCollections.observableArrayList();

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
    //Добавление точек
    Circle createPoind(AnchorPane s){
        Circle a=new Circle();
        a.setRadius(5);
        a.setFill(Color.DARKSLATEBLUE);
        a.setId(String.valueOf(indexPoind));//Индефикатор узла
        a.setUserData(String.valueOf(indexPoind));//Имя узла

        //Обработка событий
        //Перемещение с нажатой клавишей
        a.setOnMouseDragged(e-> {
             a.setFill(Color.RED);
             VertexGo(a);
         });
         //Нажатие клавиши
        a.setOnMousePressed(e->{
            a.setFill(Color.RED);
        });
        //Наведение на точку
        a.setOnMouseEntered(e->{
            a.setCursor(Cursor.HAND);
            leftStatus.setText("Точка "+a.getId());
        });
        //Отпускание кнопки
        a.setOnMouseReleased(e->{
            a.setFill(Color.DARKSLATEBLUE);
            leftStatus.setText("");
        });
        //Уход с точкм
        a.setOnMouseExited(e->{
            a.setCursor(Cursor.DEFAULT);
            leftStatus.setText("");
        });

        //Добавить точку на рабочий стол
        return a;
    }
//Добавление отрезка
     Line createLine(AnchorPane s){
        Line l=new Line();
        verX1=verX;
        verY1=verY;
        l.setStartX(verX1);
        l.setStartY(verY1);
        l.setEndX(verX);
        l.setEndY(verY);
        l.setId(String.valueOf(indexLine));//Индефикатор узла
        l.setUserData(String.valueOf(indexLine));//Имя узла


         //Наведение на точку
         l.setOnMouseEntered(e->{
             l.setCursor(Cursor.HAND);
             leftStatus.setText("Отрезок "+l.getId());
         });
         l.setOnMouseExited(e->{
             leftStatus.setText("");
         });
return l;

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

