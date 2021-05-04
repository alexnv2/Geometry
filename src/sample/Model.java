package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Data;
import lombok.val;

import java.util.LinkedList;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements  Observable {

    //Переменные класса
    private Circle vertex;
    private Line sideAll;
    private Label leftStatus;//левый статус, вывод действий
    private Label rightStatus;//правый статус, вывод координат
    private double verX;//координата Х на доске от мышки
    private double verY;//координата Y на доске от мышки
    private double verX1;//координата StartX для Line
    private double verY1;//Координата StartY для Line
    private double verX0;//координата X мировая на доске, зависят от мышки
    private double verY0;//координата Y мировая на доске, зависят от мышки
    private double verX01;//координата X мировая для Line StartX and StartY
    private double verY01;//координата Y мировая для Line StartX and StartY

    private String timeVer;//для временного хранения выбранных вершин
    private char indexPoind='A';//Индекс для точек
    private char indexLine='a';//Индекс для линий и отрезков
    private char indexArc='A';//Индекс для уголов
    private boolean poindOldAdd=false;//true - Берем существующие точки для отрезка
    //Коллекции
    private LinkedList<Circle> circles=new LinkedList<>();//коллекция для точек
    private LinkedList<Line> lines=new LinkedList<>();//коллекция для линий
    private LinkedList<String> col=new LinkedList<>();//колекция ID геометрических фигур

    private LinkedList<PoindCircle> poindCircles=new LinkedList<>();//коллекция для точек по классу
    private LinkedList<PoindLine> poindLines=new LinkedList<>();//коллекция для линий по классу

    //Определяем связанный список для регистрации классов слушателей
    private LinkedList<Observer> observers=new LinkedList<>();
    //private val x;
    //private val y;


    //Конструктор без переменных
    Model(){
     }

    //регистрация слушателя, переопределяем функцию интерфейса
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }
    //уведомление слушателя, переопределяем функцию интерфейса
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
        a.setFill(Color.LIGHTBLUE);
        a.setFill(Color.DARKSLATEBLUE);
        a.setId(String.valueOf(indexPoind));//Индефикатор узла
        a.setUserData(String.valueOf(indexPoind));//Имя узла
        //Обработка событий
        //Перемещение с нажатой клавишей
        a.setOnMouseDragged(e-> {
             a.setFill(Color.RED);
             VertexGo(a);
             findPoindCircles(a.getId());//меняем координаты в коллекции

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
                            //обновить мировые координаты
                            findPoindCircles1(c3.getId());

                            Line l1 = findLine(c1[1]);//найти линию
                            findPoindLines(l1.getId());
                            SideGo(l1);//перемещение линии
                        }
                        //выбираем точку конца отрезка
                    }else if(a.getId().equals(String.valueOf(c1[2]))) {
                         Circle c3 = findCircle(c1[0]);
                         if (c3 != null) {
                            verX1 = c3.getCenterX();
                            verY1 = c3.getCenterY();
                            //обновить мировые координаты
                            findPoindCircles1(c3.getId());
                            Line l2 = findLine(c1[1]);
                            findPoindLines(l2.getId());
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
            a.setRadius(10);
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
            a.setRadius(5);
            leftStatus.setText("");

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
        circles.add(cl);//добавить в коллекцию
        poindCircles.add(new PoindCircle(cl,cl.getId(),verX0,verY0));
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
        l.setStroke(Color.DARKSLATEBLUE);//Color
        l.setStrokeWidth(2);//Толщина
         //Наведение на отрезок
         l.setOnMouseEntered(e->{
             l.setCursor(Cursor.HAND);
             leftStatus.setText("Отрезок "+l.getId());
             l.setStrokeWidth(3);
         });
         l.setOnMouseExited(e->{
             leftStatus.setText("");
             l.setStrokeWidth(2);
         });
         return l;
     }
     //Добавление линии на доску
     Line createLineAdd(Pane a){
         Line nl;
         nl = createLine(a);//добавить линию
         a.getChildren().add(nl);//добавить на доску
         lines.add(nl);//добавить в коллекцию
         poindLines.add(new PoindLine(nl,nl.getId(),verX0,verY0,verX0,verY0));
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
   //Поиск по коллекции PoindCircles, вход ID
    private void findPoindCircles(String i){
      for(PoindCircle p: poindCircles) {
          if (p != null) {
              if (p.getId().equals(i)) {
                  p.setX(verX0);//меняем координаты X
                  p.setY(verY0);//меняем координаты Y
              }
          }
      }
    }
    //Поиск по коллекции PoindCircles для замены координат при перемещении линий
    private void findPoindCircles1(String i){
        for (PoindCircle p: poindCircles){
            if(p!=null){
                if(p.getId().equals(i)){
                    verX01=p.getX();
                    verY01=p.getY();
                }
            }
        }
    }

    //Поиск по коллекции PoindLine, вход ID
    public void findPoindLines(String i){
        for (PoindLine pl: poindLines){
            if(pl!=null){
                if(pl.getId().equals(i)){
                    pl.setEnX(verX0);
                    pl.setEnY(verY0);
                    pl.setStX(verX01);
                    pl.setStY(verY01);
                }
            }
        }
    }
}

