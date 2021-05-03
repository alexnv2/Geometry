package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Data;
import java.util.LinkedList;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

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
   private String timeVer;//для временного хранения выбранных вершин
    private char indexPoind='A';//Индекс для точек
    private char indexLine='a';//Индекс для линий и отрезков
    private char indexArc='A';//Индекс для уголов
    private boolean poindOldAdd=false;//true - Берем существующие точки для отрезка

    private LinkedList<Circle> circles=new LinkedList<>();//коллекция для точек
    private LinkedList<Line> lines=new LinkedList<>();//коллекция для линий
    private LinkedList<String> col=new LinkedList<>();//колекция ID геометрических фигур


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
    //Создание  точек и прявязка свойств
    Circle createPoind(Pane s){
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

            for (String s1: col) {//в цикл коллекцию фигур
                if (s1.length() == 3) {//только отрезки типа АаВ
                    char c1[] = s1.toCharArray();//строку в массив
                    //выбираем точку начала отрезка
                    if(a.getId().equals(String.valueOf(c1[0]))) {
                        //найти точку
                        Circle c3 = findCircle(c1[2]);
                        if (c3 != null) {
                            verX1 = c3.getCenterX();
                            verY1 = c3.getCenterY();
                            Line l1 = findLine(c1[1]);//найти линию
                            SideGo(l1);//перемещение линии
                        }
                        //выбираем точку конца отрезка
                    }else if(a.getId().equals(String.valueOf(c1[2]))) {
                         Circle c3 = findCircle(c1[0]);
                         if (c3 != null) {
                            verX1 = c3.getCenterX();
                            verY1 = c3.getCenterY();
                            Line l2 = findLine(c1[1]);
                            SideGo(l2);
                        }
                    }
                }
            }
        });
         //Нажатие клавиши
        a.setOnMousePressed(e->{
            a.setFill(Color.RED);
            poindOldAdd=true;//взять эту точку для отрезка
            timeVer=a.getId();

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
           // poindOldAdd=false;//не брать точку для отрезка, создать новую
        });
        //Уход с точкм
        a.setOnMouseExited(e->{
            a.setCursor(Cursor.DEFAULT);
            leftStatus.setText("");
          //  poindOldAdd=false;//взять эту точку для отрезка
        });
        //Добавить точку на рабочий стол
        return a;
    }
    //Добавление точек на доску
    char createPoindAdd(Pane a){
        Circle cl;
        char c=indexPoind;
        cl = createPoind(a);//Создать
        a.getChildren().add(cl);//добавить
        circles.add(cl);
        VertexGo(cl);//куда добавить
        //Увеличить индекс
        indexPoind += 1;
        return c;
    }
//Создание  отрезка
     Line createLine(Pane s){
        Line l=new Line();
        verX1=verX;
        verY1=verY;
        l.setStartX(verX1);
        l.setStartY(verY1);
        l.setEndX(verX);
        l.setEndY(verY);
        l.setId(String.valueOf(indexLine));//Индефикатор узла
        l.setUserData(String.valueOf(indexLine));//Имя узла
         //Наведение на отрезок
         l.setOnMouseEntered(e->{
             l.setCursor(Cursor.HAND);
             leftStatus.setText("Отрезок "+l.getId());
         });
         l.setOnMouseExited(e->{
             leftStatus.setText("");
         });
         return l;
     }
     //Добавление линии на доску
     Line createLineAdd(Pane a){
         Line nl;
         nl = createLine(a);//добавить линию
         a.getChildren().add(nl);//добавить на доску
         lines.add(nl);//добавить в коллекцию
         col.add(String.valueOf(indexLine));
         indexLine+=1;//увеличить индекс
         return nl;
      }
     //Присоеденить вторую точку к линии
    public void lineAddPoind(Line nl, boolean poindAdd2){
        for(Circle c: circles){
           if(c!=null && nl!=null && poindAdd2) {
              double d=distance(c.getCenterX(),c.getCenterY(),verX,verY);
                 if (d<15){
                        poindOldAdd=true;
                        verX=c.getCenterX();
                        verY=c.getCenterY();
                       SideGo(nl);
                 }else {
                     setPoindOldAdd(false);
                }
           }
        }
    }


    //Растояние между точками (координаты x1,y1,x2,y2)
    public double distance(double x1, double y1, double x2, double y2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
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

    //Добавление в коллекцию из контролера
    public void setCol(String valueOf) {
        col.add(valueOf);
    }

    //Поиск по коллеуции circles (передается имя точки, типа А)
    Circle findCircle(char c){
        for (Circle c0: circles){
            if (c0.getId().equals(String.valueOf(c))){;
                return c0;
            }
        }
       return null;//ничего не найдено
    }
    //Поиск по коллекции Lines (передается имя отрезка, типа а)
    Line findLine(char c){
        for(Line lo: lines){
            if(lo.getId().equals(String.valueOf(c))) {
                return lo;
            }
        }
        return null;//если ничего не найдено
    }
}

