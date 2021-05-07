package sample;
/*
Класс Модели, получает запросы на отработку событий их котроллера и
выставляет переменные и отправляет сообщения классу отобраюения (View),
который выводит эти данные на экран.
 */
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import lombok.Data;

import java.io.File;
import java.util.LinkedList;

import static ContstantString.StringStatus.*;
//import static ContstantString.StringStatus.STA_8;
import static ContstantString.StringWeb.*;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;


@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements  Observable {

    //Переменные класса
    private Circle vertex;
    private Line sideAll;
    private Label Status;//левый статус, вывод действий
    private Label rightStatus;//правый статус, вывод координат
    private Text textGo;//text
    private TableView mTableView;
    private WebView webView;

    private double textX, textY;//координаты букв
    private String stringWebView;//text left
    private String stringLeftStatus;

    private double verX;//координата Х на доске от мышки
    private double verY;//координата Y на доске от мышки
    private double verX1;//координата StartX для отрезков
    private double verY1;//Координата StartY для отрезков
    private double rayStartX;//
    private double rayStartY;
    private double rayEndX;
    private double rayEndY;
    private double verX0;//координата X мировая на доске, зависят от мышки
    private double verY0;//координата Y мировая на доске, зависят от мышки
    private double verLineStartX;//координата X мировая для Line StartX
    private double verLineStartY;//координата Y мировая для Line StartY
    private double verLineEndX;//координата X мировая для Line EndX
    private double verLineEndY;//координата Y мировая для Line EndY

    private String timeVer;//для временного хранения выбранных вершин
    private char indexPoind='A';//Индекс для точек
    private char indexLine='a';//Индекс для линий и отрезков
    private char indexArc='A';//Индекс для уголов
    private boolean poindOldAdd=false;//true - Берем существующие точки для отрезка
    //Коллекции
    private LinkedList<String> col=new LinkedList<>();//колекция ID геометрических фигур
    private LinkedList<PoindCircle> poindCircles=new LinkedList<>();//коллекция для точек по классу
    private LinkedList<PoindLine> poindLines=new LinkedList<>();//коллекция для линий по классу

    //Определяем связанный список для регистрации классов слушателей
    private LinkedList<Observer> observers=new LinkedList<>();

    public void setWindShow(int w){
        WIND_SHOW=w;
    }
    public int getWindShow(){
        return WIND_SHOW;
    }




    //Конструктор без переменных
    Model(){
     }


    //регистрация слушателя, переопределяем функцию интерфейса
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }
    //Уведомление слушателя, переопределяем функцию интерфейса
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.notification(message);
          }
    }

    //Текст для отобоажения в левой части
    public void webViewLeftString(WebView o, int c){
        String pathImages= new File(".").getAbsolutePath();
        System.out.println(pathImages);
        String pathImg1="<img src=file:\\"+pathImages+"\\src\\Images\\dlina_bisector.png"+" width=274 height=242>";
        String pathImg2="<img src=file:\\"+pathImages+"\\src\\Images\\dlina_median.png"+ " width=343 height=194>";
        String pathImg3="<img src=file:\\"+pathImages+"\\src\\Images\\dlina_higth.png"+" width=344 height=292>";
        String pathImg4="<img src=file:\\"+pathImages+"\\src\\Images\\Area_1.png"+" width=91 height=53>";
        String pathImg5="<img src=file:\\"+pathImages+"\\src\\Images\\Area_2.png"+" width=131 height=61>";
        String pathImg6="<img src=file:\\"+pathImages+"\\src\\Images\\Area_4.png"+" width=91 height=53>";
        String pathImg7="<img src=file:\\"+pathImages+"\\src\\Images\\Area_5.png"+" width=91 height=53>";
        String pathImg8="<img src=file:\\"+pathImages+"\\src\\Images\\Area_8.png"+" width=91 height=53>";
        String pathImg9="<img src=file:\\"+pathImages+"\\src\\Images\\Area_9.png"+" width=83 height=54>";
        String pathImg10="<img src=file:\\"+pathImages+"\\src\\Images\\Area_10.png"+" width=91 height=55";
        String pathImg11="<img src=file:\\"+pathImages+"\\src\\Images\\AreaGeron.png"+" width=275 height=122";

        switch (c) {
            case 0: setStringWebView(WEB_HTML+TR_OPR + TR_ANGLE + TR_NERAVENSVO+ TR_SUNANGLE+TR_AREA_1+
                    pathImg4+TR_AREA_2+pathImg5+TR_AREA_3+pathImg11+WEB_END);break;
            case 1: setStringWebView(WEB_HTML+TR_TEOREMA33+TR_TEOREMA34+WEB_END);break;
            case 2: setStringWebView(WEB_HTML+TR_BISSECTOR+TR_BISSEC_FOR+pathImg1+WEB_END);break;
            case 3: setStringWebView((WEB_HTML+TR_MEDIANA+TR_DLINA_MEDIAN+pathImg2+WEB_END));break;
            case 4: setStringWebView(WEB_HTML+TR_HIGTH+TR_ORTOSENTR+TR_DLINA_HIGHT+pathImg3+WEB_END);break;
            case 5: setStringWebView(WEB_HTML+TR_AREA_8+pathImg8+TR_AREA_9+pathImg9+WEB_END);break;
            case 6: setStringWebView(WEB_HTML+TR_AREA_10+pathImg10+WEB_END);break;
            case 7: setStringWebView(WEB_HTML+TR_CIRCLE+TR_CIRCLE_IN+TR_AREA_4+pathImg6+WEB_END);break;
            case 8: setStringWebView(WEB_HTML+TR_CIRCLE+TR_CIRCLE_OUT+TR_AREA_5+pathImg7+WEB_END);break;
            case 9: setStringWebView(WEB_HTML+TR_MIDDLE_PER+WEB_END);break;
            case 10: setStringWebView(WEB_HTML+TR_OXYGEN+WEB_END);break;
            case 11: setStringWebView(WEB_HTML+OP_GEOMETRY_1+WEB_END);break;
        }
        webViewGo(o);//на вывод
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
            if(!movePoindCircles(a)){
                //Установить статус
                setStringLeftStatus(STA_8);
                statusGo(Status);//Установить статус
            }//передать нажатую точку
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
            a.setRadius(8);
            //Установить статус
            setStringLeftStatus(STA_9+a.getId());
            statusGo(Status);
        });
        //Отпускание кнопки
        a.setOnMouseReleased(e->{
            a.setFill(Color.DARKSLATEBLUE);
            //Установить статус
            setStringLeftStatus("");
            statusGo(Status);
         });
        //Уход с точкм
        a.setOnMouseExited(e->{
            a.setCursor(Cursor.DEFAULT);
            a.setRadius(5);
            //Установить статус
            setStringLeftStatus("");
            statusGo(Status);
        });
        //Добавить точку на рабочий стол
        return a;
    }
    //Добавление точек на доску
    String createPoindAdd(Pane a){
        Circle cl;
        String c=String.valueOf(indexPoind);
        cl = createPoind(a);//Создать
        a.getChildren().add(cl);//добавить
        poindCircles.add(new PoindCircle(cl,cl.getId(),verX0,verY0,true,false));
        VertexGo(cl);//куда добавить
        //Увеличить индекс
        indexPoind += 1;
        return c;
    }
//Создание  отрезка
     Line createLine(Pane s, int seg){
        Line l=new Line();
        if(seg==0) {
            verX1 = verX;
            verY1 = verY;
            l.setStartX(verX1);
            l.setStartY(verY1);
            l.setEndX(verX);
            l.setEndY(verY);
        }else{
            rayEndX=verX;
            rayEndY=verY;
            l.setStartX(verX);
            l.setStartY(verY);
            l.setEndX(rayEndX);
            l.setEndY(rayEndY);
        }

        l.setId(String.valueOf(indexLine));//Индефикатор узла
        l.setUserData(String.valueOf(indexLine));//Имя узла
        l.setStroke(Color.DARKSLATEBLUE);//Color
        l.setStrokeWidth(2);//Толщина
         //Наведение на отрезок
         l.setOnMouseEntered(e->{
             l.setCursor(Cursor.HAND);
             //Установить статус
             setStringLeftStatus(STA_10+l.getId());
             statusGo(Status);
             l.setStrokeWidth(3);
         });
         l.setOnMouseExited(e->{
             //Установить статус
             setStringLeftStatus("");
             statusGo(Status);
             l.setStrokeWidth(2);
         });
         return l;
     }
     //Добавление линии на доску
     Line createLineAdd(Pane pane, int segment){
         Line newLine;
         newLine = createLine(pane,segment);//добавить линию
         pane.getChildren().add(newLine);//добавить на доску
         poindLines.add(new PoindLine(newLine,newLine.getId(),verX0,verY0,verX0,verY0,true,false,segment));
         indexLine+=1;//увеличить индекс
         return newLine;
      }
      //Добавление луча на доску
    Line createRayAdd(Pane pane){
        Line ray;
        ray=createRay(pane);
        pane.getChildren().add(ray);
        poindLines.add(new PoindLine(ray,ray.getId(),verX0,verY0,verX0,verY0,true,false,1));
        indexLine+=1;
        return ray;
    }
      Line createRay(Pane pane){
          Line line=new Line();
          verX1=verX;
          verY1=verY;
          line.setStartX(verX1);
          line.setStartY(verY1);
          line.setEndX(verX);
          line.setEndY(verY);
          line.setId(String.valueOf(indexLine));//Индефикатор узла
          line.setUserData(String.valueOf(indexLine));//Имя узла
          line.setStroke(Color.DARKSLATEBLUE);//Color
          line.setStrokeWidth(2);//Толщина
          //Наведение на отрезок
          line.setOnMouseEntered(e->{
              line.setCursor(Cursor.HAND);
              //leftStatus.setText("Отрезок "+line.getId());
              line.setStrokeWidth(3);
          });
          line.setOnMouseExited(e-> {
              //Установить статус
              setStringLeftStatus("");
              statusGo(Status);
              line.setStrokeWidth(2);
          });
          return line;
      }
     //Присоеденить вторую точку к линии, когда линия проходит близко к точке
    public void lineAddPoind(Line nl, boolean poindAdd2){
        Circle pCl;
        for(PoindCircle c: poindCircles){
           if(c!=null && nl!=null && poindAdd2) {
              pCl=c.getCircle();
              double d=distance(pCl.getCenterX(),pCl.getCenterY(),verX,verY);
                 if (d<15){
                        poindOldAdd=true;
                        verX=pCl.getCenterX();
                        verY=pCl.getCenterY();
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
    //Перемещение луча и прямой
    void RayGo(Line o){
        sideAll=o;
        notifyObservers("RayGo");
    }
    //Перемещение букв
    void TextGo(Text o){
        textGo=o;
        notifyObservers("TextGo");
    }

    //Слева и внизу
    void webViewGo(WebView o){
        webView =o;
        notifyObservers("WebView");
    }
    //Вывод в статусной строке
    public void statusGo(Label o){
        Status=o;
        notifyObservers("LeftStatusGo");
     }
    //Добавление в коллекцию из контролера
    public void setCol(String valueOf) {
        col.add(valueOf);
    }

    //Поиск по коллеуции PoindCircles (передается имя точки, типа А)
    //Вызов из movePoindCircles(Circle a)
    //Возвращает объект точку
    Circle findCircle(String c){
        for (PoindCircle c0: poindCircles){
            if (c0.getId().equals(String.valueOf(c))){
                return c0.getCircle();
            }
        }
       return null;//ничего не найдено
    }
    //Поиск по коллекции PoindLine (передается имя отрезка, типа а)
    //Вызов из
    //Возвращает найденную линию
    Line findLine(char c){
        for(PoindLine lo: poindLines){
            if(lo.getId().equals(String.valueOf(c))) {
                return lo.getLine();
            }
        }
        return null;//если ничего не найдено
    }
   //Поиск по коллекции PoindCircles, вход ID
    public void findPoindCircles(String i){
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
                    verLineStartX =p.getX();
                    verLineStartY =p.getY();
                }
            }
        }
    }

    //Поиск по коллекции PoindLine, для замены декартовых координат вход ID
    //Вызов из
    public void findPoindLines(String i){
        for (PoindLine pl: poindLines){
            if(pl!=null){
                if(pl.getId().equals(i)){
                    pl.setEnX(verLineEndX);
                    pl.setEnY(verLineEndY);
                    pl.setStX(verLineStartX);
                    pl.setStY(verLineStartY);
                }
            }
        }
    }
    //Заносятся стартовые координаты линии луча при его создании
    //Вызов из контролера onMouseMoved
    public void findPoindLines1(String i){
        for (PoindLine pl: poindLines){
            if(pl!=null){
                if(pl.getId().equals(i)){
                    pl.setStX(verLineStartX);
                    pl.setStY(verLineStartY);
                }
            }
        }
    }

    //Перемещение точки на доске с нажатой кнопкой
    //Вызов из метода Circle createPoind(Pane s) прикрепленного события
    public boolean movePoindCircles(Circle a) {
        a.setFill(Color.RED);
        if (findPoindCircleMove(a.getId())){//true-разрешено перемещение
            VertexGo(a);
            findPoindCircles(a.getId());//меняем координаты в коллекции

            for (String s1 : col) {//в цикл коллекцию фигур
                if (s1.length() == 3) {//только отрезки типа АаВ
                    char[] c1 = s1.toCharArray();//строку в массив
                    //выбираем точку начала отрезка
                    if (a.getId().equals(String.valueOf(c1[0]))) {
                        //найти точку
                        Circle c3 = findCircle(String.valueOf(c1[2]));
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
                    } else if (a.getId().equals(String.valueOf(c1[2]))) {
                        Circle c3 = findCircle(String.valueOf(c1[0]));
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
        }else return false;
        return true;
    }
     //Поиск по коллекция PoindCircle, разрешено ли редактирование
    //Вызов из  movePoindCircles(Circle a)
    //Возвращает логическое значение bMove
    private boolean findPoindCircleMove(String id){
        boolean bfMove=false;
        for (PoindCircle p: poindCircles) {

            if (p != null) {
                if (p.getId().equals(id)) {
                    bfMove=p.isBMove();
                }
            }
        }
        return bfMove;
    }
    //Тестовый медод для вывода информации по коолекциям
    public void ColTest(){
        System.out.println("Коллекция Sting");
        int a=0;
        for(String s: col){
            System.out.println(a+" "+s);
            a+=1;
        }
        System.out.println("Коллекция PoindCircle");
        int z=0;
        for(PoindCircle p: poindCircles){
            System.out.println(z+" "+p);
            z+=1;
        }
        System.out.println("Коллекция PoindLine");
        int t=0;
        for( PoindLine d: poindLines){
            System.out.println(t+" "+d);
            t+=1;
        }

    }


}

