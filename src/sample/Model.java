package sample;
/*
Класс Модели, получает запросы на отработку событий их котроллера и
выставляет переменные и отправляет сообщения классу отобраюения (View),
который выводит эти данные на экран.
 */
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import lombok.Data;

import java.io.File;
import java.util.LinkedList;

import static ContstantString.StringStatus.*;
import static ContstantString.StringWeb.*;
import static java.lang.StrictMath.*;


@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements  Observable {

    //Переменные класса
    private Circle vertex; //точка
    private Line line;//линия, луч, прямая
    private Label Status;//левый статус, вывод действий
    private Label rightStatus;//правый статус, вывод координат
    private Text textGo;//Для наименования точек, отрезков, прямых и т.д
    private WebView webView; //браузер в левой части доски
    private TextArea textArea;//получаем ссылку на правую часть доски для вывода информации о фигурах
    private GridView gridViews;
    private Arc arcGo;
    private Color ColorGo;

    private double textX, textY;//координаты букв для передачи в View
    private String stringWebView;//text left
    private String stringLeftStatus;//для хранения и передачи в View статусных сообщений
    private String leftHTML;//хранит адрес файла HTML из папки Web для передачи в View
    private String txtShape="";//хранит строку о геометрической фигуре на доске

    private double verX;//координата экрана Х  от мышки
    private double verY;//координата экрана Y от мышки
    private double verX1;//координата StartX для отрезков
    private double verY1;//Координата StartY для отрезков

    private double rayStartX;//координаты экрана для луча и прямой StartX
    private double rayStartY;//координаты экрана для луча и прямой StartY
    private double rayEndX;//координаты экрана для луча и прямой EndX
    private double rayEndY;//координаты экрана для луча и прямой EndY
    private double verX0;//координата X мировая на доске, зависят от мышки
    private double verY0;//координата Y мировая на доске, зависят от мышки
    private double verLineStartX;//координата X мировая для Line StartX
    private double verLineStartY;//координата Y мировая для Line StartY
    private double verLineEndX;//координата X мировая для Line EndX
    private double verLineEndY;//координата Y мировая для Line EndY

    private Circle timeVer;//для временного хранения выбранных вершин
    private String verSegmentStart;//имя начала отрезка для метода txtAreaOutput
    private String verSegmentEnd;//имя конца отрезка для метода txtAreaOutput
    private String verSegmentAngle;//имя угла
    private char indexPoind='A';//Индекс для точек
    private char indexLine='a';//Индекс для линий и отрезков
    private char indexArc='A';//Индекс для уголов
    private boolean poindOldAdd=false;//true - Берем существующие точки для отрезка

    private double arcRadius;//радиус дуги
    private double angleStart;//начало дуги гр.
    private double angleLength;//длина дуги гр.
    //Коллекции
  //  private LinkedList<String> col=new LinkedList<>();//колекция ID геометрических фигур
    private LinkedList<PoindCircle> poindCircles=new LinkedList<>();//коллекция для точек по классу
    private LinkedList<PoindLine> poindLines=new LinkedList<>();//коллекция для линий по классу
    private LinkedList<VertexArc> vertexArcs=new LinkedList<>();//коллекция для арок углов

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
    //для вывода файла html в web
    public  void webHTML(WebView o,String file){
      String pathFile= new File("").getAbsolutePath();
        leftHTML= "file:"+pathFile+"\\src\\Web\\"+file;
       // setStringLeftStatus(leftHTML);
       // statusGo(Status);
      webGo(o);
    }

    //Текст для отобоажения в левой части
    public void webViewLeftString(WebView o, int c){
        String pathImages= new File(".").getAbsolutePath();
       // System.out.println(pathImages);
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
            case 0 -> setStringWebView(WEB_HTML + TR_OPR + TR_ANGLE + TR_NERAVENSVO + TR_SUNANGLE + TR_AREA_1 +
                    pathImg4 + TR_AREA_2 + pathImg5 + TR_AREA_3 + pathImg11 + WEB_END);
            case 1 -> setStringWebView(WEB_HTML + TR_TEOREMA33 + TR_TEOREMA34 + WEB_END);
            case 2 -> setStringWebView(WEB_HTML + TR_BISSECTOR + TR_BISSEC_FOR + pathImg1 + WEB_END);
            case 3 -> setStringWebView((WEB_HTML + TR_MEDIANA + TR_DLINA_MEDIAN + pathImg2 + WEB_END));
            case 4 -> setStringWebView(WEB_HTML + TR_HIGTH + TR_ORTOSENTR + TR_DLINA_HIGHT + pathImg3 + WEB_END);
            case 5 -> setStringWebView(WEB_HTML + TR_AREA_8 + pathImg8 + TR_AREA_9 + pathImg9 + WEB_END);
            case 6 -> setStringWebView(WEB_HTML + TR_AREA_10 + pathImg10 + WEB_END);
            case 7 -> setStringWebView(WEB_HTML + TR_CIRCLE + TR_CIRCLE_IN + TR_AREA_4 + pathImg6 + WEB_END);
            case 8 -> setStringWebView(WEB_HTML + TR_CIRCLE + TR_CIRCLE_OUT + TR_AREA_5 + pathImg7 + WEB_END);
            case 9 -> setStringWebView(WEB_HTML + TR_MIDDLE_PER + WEB_END);
            case 10 -> setStringWebView(WEB_HTML + TR_OXYGEN + WEB_END);
            case 11 -> setStringWebView(WEB_HTML + OP_GEOMETRY_1 + WEB_END);
        }
        webViewGo(o);//на вывод
    }
    //Выбираем и размещаем в правой части доски информацию о геометрической фигуре
   public void txtAreaOutput() {
       for (PoindCircle p : poindCircles) {
           if (p.getCircle() != null) {
               String s1 = p.getId();
               double s2 = p.getX();
               double s3 = p.getY();
               txtShape = txtShape + "Точка: " + s1 + " (" + s2 + "," + s3 + ")\n";
           }
       }
       for (PoindLine p : poindLines) {
           if (p.getLine() != null) {
               int l = p.getSegment();
               if (l == 0) {
                   double lengthSegment = Math.round(distance(p.getStX(), p.getStY(), p.getEnX(), p.getEnY()) * 100);
                   txtShape = txtShape + "Отрезок: " + p.getId() + " Длина:" + lengthSegment / 100 + "\n";
               }
                else if (l == 1) {
                    txtShape = txtShape + "Луч: " + p.getLine().getId() + " или " + p.getId() + "\n";
                }else if (l == 2) {
                    txtShape = txtShape + "Прямая: " + p.getLine().getId() + " или " + p.getId() + "\n";
                }
            }
        }
        for( VertexArc v: vertexArcs){
            if(v!=null) {
                txtShape = txtShape + "Угол " + v.getId() +"= "+v.getLengthAngle()+ " гр. \n";
                }
            }
       textAreaGo();
    }

    // расчет угла в градусах по названию угла
    //Вход угол АВС
    public double angleAccount(String s1, String s2, String s3){
        Circle c1=findCircle(s2);
        Circle c2=findCircle(s1);
        Circle c3=findCircle(s3);
        double a=angleTriangle(c1.getCenterX(),c1.getCenterY(),c2.getCenterX(),c2.getCenterY(),c3.getCenterX(),c3.getCenterY());
        return a;
    }

    // Поиск координат точек
    //Вход: ID первой точки и ID второй точки
    //Ввозвращает: длину отрезка
    double findPoindandLine(String poindStart, String poindEnd){
        double x1 = 0,y1=0,x2=0,y2=0;
        for(PoindCircle p:  poindCircles){
            if(p.getId().equals(poindStart)){
                x1=p.getX();
                y1=p.getY();
            }
            if (p.getId().equals(poindEnd)){
                x2=p.getX();
                y2=p.getY();
            }
        }
        return distance(x1,y1,x2,y2);
    }
    //Создание  точек и прявязка свойств
    Circle  createPoind(){
        Circle circle=new Circle();
        circle.setRadius(5);
        circle.setFill(Color.LIGHTBLUE);
        circle.setFill(Color.DARKSLATEBLUE);
        circle.setId(String.valueOf(indexPoind));//Индефикатор узла
        circle.setUserData(String.valueOf(indexPoind));//Имя узла
        //Обработка событий
        //Перемещение с нажатой клавишей
        circle.setOnMouseDragged(e-> {
                if(findPoindCircleMove(circle.getId())) {
                    circle.setCenterX(e.getX());
                    circle.setCenterY(e.getY());
                    //добавить новые координаты в коллекцию PoindCircle
                    findCirclesUpdateXY(circle.getId());
                    //добавить новые координаты в коллекцию PoindLine
                    findSegmentLine(circle.getId());
                    //Добавить новые данные коллекцию VertexArc
                    findArcUpdate(circle.getId());
                    //Обновить координаты точки в правой части
                    setTxtShape("");
                    txtAreaOutput();
                }else {
                    setStringLeftStatus(STA_8);
                    statusGo(Status);//Установить статус "Перемещение запрещено"
                }
        });
         //Нажатие клавиши
        circle.setOnMousePressed(e->{
            circle.setFill(Color.RED);
            poindOldAdd=true;//взять эту точку для отрезка
            timeVer=circle;//сохранить выбранную точку для построения

        });
        //Наведение на точку
        circle.setOnMouseEntered(e->{
            circle.setCursor(Cursor.HAND);
            circle.setRadius(8);
            //Установить статус "Точка + выбранная точка"
            setStringLeftStatus(STA_9+circle.getId());
            statusGo(Status);
        });
        //Отпускание кнопки
        circle.setOnMouseReleased(e->{
            circle.setFill(Color.DARKSLATEBLUE);
            poindOldAdd=false;//запрет брать точку для отрезков, прямых, лучей

         });
        //Уход с точкм
        circle.setOnMouseExited(e->{
            circle.setCursor(Cursor.DEFAULT);
            circle.setRadius(5);
            poindOldAdd=false;//запрет брать точку для отрезков, прямых, лучей
            //Установить статус пустая строка
            setStringLeftStatus("");
            statusGo(Status);
        });
        return circle;//завершено создание новой точки
    }
    //Добавление точек на доску
    Circle createPoindAdd(){
        Circle newPoind;//Объявить переменную
        newPoind = createPoind();//Создать точку
        //добавить в коллецию точек
        poindCircles.add(new PoindCircle(newPoind,newPoind.getId(),verX0,verY0,true,false));
        //добавить в коллекцию фигур
         //Передать в View для вывода на экран
        VertexGo(newPoind);
        //Увеличить индекс
        indexPoind += 1;
        //Добавить в правую часть доски
        setTxtShape("");
        txtAreaOutput();
        return newPoind;//возвращает точку
    }
//Создание  отрезка
     Line createLine(int seg){
        Line newLine=new Line();
        if(seg==0 || seg==3 || seg==4) {//Отрезок или угол или треугольник
            verX1 = verX;
            verY1 = verY;
            newLine.setStartX(verX);
            newLine.setStartY(verY);
            newLine.setEndX(verX1);
            newLine.setEndY(verY1);
        }else{
            rayEndX=verX;
            rayEndY=verY;
            newLine.setStartX(rayEndX);
            newLine.setStartY(rayEndY);
            newLine.setEndX(verX);
            newLine.setEndY(verY);
        }

        newLine.setId(String.valueOf(indexLine));//Индефикатор узла
        newLine.setUserData(String.valueOf(indexLine));//Имя узла
        newLine.setStroke(Color.DARKSLATEBLUE);//Color
        newLine.setStrokeWidth(2);//Толщина
         newLine.setOnMousePressed(e->{
             System.out.println("poind");
         });
         //Наведение на отрезок
         newLine.setOnMouseEntered(e->{
             newLine.setCursor(Cursor.HAND);
             //Установить статус
             for (PoindLine p: poindLines) {
                 if (p.getId().equals(newLine.getId())) {
                     if (p.getSegment() == 0) {
                         //Найти и вывести имя отрезка
                         //findNameSegment(newLine.getId());
                         setStringLeftStatus(STA_10 + verSegmentStart + verSegmentEnd);
                         statusGo(Status);
                     }else if(p.getSegment()==1){
                         setStringLeftStatus(STA_11 + newLine.getId());
                         statusGo(Status);
                     }else if(p.getSegment()==2){
                         setStringLeftStatus(STA_12 + newLine.getId());
                         statusGo(Status);
                     }
                 }
             }
             newLine.setStrokeWidth(3);
         });
         newLine.setOnMouseExited(e->{
             //Установить статус
             setStringLeftStatus("");
             statusGo(Status);
             newLine.setStrokeWidth(2);
         });
         return newLine;
     }
     //Добавление линии на доску
     Line createLineAdd(int segment){
         Line newLine;
         newLine = createLine(segment);//добавить линию
         poindLines.add(new PoindLine(newLine,newLine.getId(),verX0,verY0,verX0,verY0,true,false,segment));
         indexLine+=1;//увеличить индекс
         return newLine;
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
    //Перемещение точек
    void VertexGo(Circle o){
        vertex=o;
        notifyObservers("VertexGo");
    }
    //Перемещение сторон
    void SideGo(Line o){
        line =o;
        notifyObservers("SideGo");
    }
    //Перемещение луча и прямой
    void RayGo(Line o){
        line =o;
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
    void webGo(WebView o){
        webView =o;
        notifyObservers("WebGo");

    }

    //Дуги
    void ArcGo(Arc o){
        arcGo=o;
        notifyObservers("ArcGo");
    }
    //Цвет дуг
    void ArcColorGo(Arc o){
        arcGo=o;
        notifyObservers("ArcColorGo");
    }

    //Вывод в статусной строке
    public void statusGo(Label o){
        Status=o;
        notifyObservers("LeftStatusGo");
     }
     //Вывод в правую часть доски
    public void textAreaGo(){
        notifyObservers("TextShapeGo");
    }

    //Поиск по коллекции PoindLine для замены имени отрезка в коллекции
    //Входа: Sting A, String B, String a (вершины созданного отрезка и линия)
    //Выход: нет
    public void findNameId(String poindA, String poindB, String linaA ){
       for(PoindLine p: poindLines){
           if(p!=null){
               if(p.getId().equals(linaA)){
                   p.setId(poindA+poindB);
               }
           }
       }
    }


    //Поиск по коллеуции PoindCircles (передается имя точки, типа А)
    //Вызов из movePoindCircles(Circle a)
    //Возвращает объект точку
    Circle findCircle(String c){
        for (PoindCircle c0: poindCircles) {
            if (c0 != null) {
                if (c0.getId().equals(String.valueOf(c))) {
                    return c0.getCircle();
                }
            }
        }
       return null;//ничего не найдено
    }


   //Поиск по коллекции PoindCircles, вход ID
    //Меняем координаты точкм в коллекции
    public void findCirclesUpdateXY(String id){
      for(PoindCircle p: poindCircles) {
          if (p != null) {
              if (p.getId().equals(id)) {
                  p.setX(verX0);//меняем координаты X
                  p.setY(verY0);//меняем координаты Y
              }
          }
      }
    }
    //Поиск по коллекции PoindLines и замена мировых координат
    //Вход: строка ID
    //Выход: нет
    public void findLinesUpdateXY(String id){
       for(PoindLine p: poindLines){
           if(p!=null){
               if(p.getId().equals(id)){
                 p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                 p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                 p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                 p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
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


    //Поиск по коллекции
    //Вход: имя вершины отрезка Sting A
    //Замена координат для отрезка
    public void findSegmentLine(String s){
          for(PoindLine p: poindLines) {
            if (p != null) {
             String name=p.getId();
             char[] chName=name.toCharArray();
             if(s.equals(String.valueOf(chName[0]))){
                 p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                 p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                 //Обновляем координаты окончания луча
                 if(p.getSegment()==1){
                     p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                     p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                 }
                 //Обновляем коорлинаты прямой
                 if(p.getSegment()==2) {
                     p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                     p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                 }
             }
             if(s.equals(String.valueOf(chName[1]))){
                 p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                 p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                 p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                 p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
             }
                //Обновляем коорлинаты прямой
                if(p.getSegment()==2) {
                    p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                    p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                    p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                    p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                }
            }
        }
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
        System.out.println("Коллекция дуг");
        int b=0;
        for(VertexArc va:vertexArcs){
            System.out.println(b+" "+va);
            b+=1;
        }
    }

    //Рисуем дуги на входе строка вида АВС (три вершины)
    Arc arcVertexAdd(String arc){
        char[] arcChar=arc.toCharArray();//преобразовать в массив символов
        Circle o1=findCircle(String.valueOf(arcChar[0]));
        Circle o2=findCircle(String.valueOf(arcChar[1]));
        Circle o3=findCircle(String.valueOf(arcChar[2]));
        Arc arc1=new Arc();
        arc1.setId(String.valueOf(arcChar[1]));
        setColorGo(Color.DARKSLATEBLUE);
        arc1.setType(ArcType.ROUND);
        arc1.setFill(Color.LIGHTBLUE);
        arc1.setOpacity(0.5);
        ArcColorGo(arc1);
        arcVertex(o1,o2,o3,arc1);
        arc1.toBack();
        //добавить в коллекцию дуг
        vertexArcs.add(new VertexArc(arc1,arc,gridViews.revAccessX(o2.getCenterX()),gridViews.revAccessY(o2.getCenterY()),arcRadius,
                arcRadius,angleStart,angleLength,false));
        return arc1;
    }

    //Поиск по коллекции дуг углов
    //Вход Строка вершина дуги
    //Возврат дуга
  /*  Arc findArc(String arc){
        for(VertexArc v: vertexArcs ) {
            if (v != null) {
                char[] arcChar=v.getId().toCharArray();
                if (v.getArc().getId().equals(arcChar[0])) {
                    return v.getArc();
                }
                if (v.getArc().getId().equals(arcChar[2])) {
                    return v.getArc();
                }

            }
        }
        return null;
    }

   */
    //Обновление коллекции дуг после перемещения
   private void findArcUpdate(String s){
       for(VertexArc v: vertexArcs ) {
           if (v != null) {
               char[] arcChar=v.getId().toCharArray();
              if (v.getArc().getId().equals(s)) {
                   v.setCenterX(gridViews.revAccessX(verX));
                   v.setCenterY(gridViews.revAccessY(verY));
                   v.setStartAngle(v.getArc().getStartAngle());
                   v.setLengthAngle(v.getArc().getLength());
               }else if(s.equals(String.valueOf(arcChar[0]))){
                   v.setCenterX(gridViews.revAccessX(v.getArc().getCenterX()));
                   v.setCenterY(gridViews.revAccessY(v.getArc().getCenterY()));
                   v.setStartAngle(v.getArc().getStartAngle());
                   v.setLengthAngle(v.getArc().getLength());
               }else if(s.equals(String.valueOf(arcChar[2]))){
                   v.setCenterX(gridViews.revAccessX(v.getArc().getCenterX()));
                   v.setCenterY(gridViews.revAccessY(v.getArc().getCenterY()));
                   v.setStartAngle(v.getArc().getStartAngle());
                   v.setLengthAngle(v.getArc().getLength());
               }
           }


       }

   }
    //Расчет для построения угла ABC
    public void arcVertex(Circle o1, Circle o2, Circle o3, Arc a1){
        double angleABC=angleTriangle(o1.getCenterX(), o1.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        angleLength=angleABC;
        arcRadius=30;
        double arcLight=angleTriangle(o2.getCenterX()+200,o2.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        //System.out.println("Угол ВС "+arcLight);
        double str=areaTriangle(o2.getCenterX()+200,o2.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        double str1=areaTriangle(o1.getCenterX(), o1.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        //System.out.println("str "+str+" str1 "+str1);
        if (str<0){
            arcLight=360-arcLight;
         //   System.out.println("str<0 "+arcLight);
        }else {
            arcLight=arcLight;
         //   System.out.println("иначе минус "+arcLight);
        }
        if(str1>0){
            arcLight=arcLight-angleABC;
        }
        setAngleStart(arcLight);
       // System.out.println("угол "+angleLength+" нач. угол "+angleStart+" ");
        double stX=verX;
        double stY=verY;
        verX=o2.getCenterX();
        verY=o2.getCenterY();
        ArcGo(a1);//перемещение дуги
        verX=stX;
        verY=stY;
    }
    //Прямой угол вместо дуги
    public void rectangle90(Circle o1,Circle o2, Circle o3, Circle o4, Line l1, Line l2){
        double tx=o2.getCenterX()-o1.getCenterX();
        double ty=o2.getCenterY()-o1.getCenterY();
        double t= 0.15;
        double ax=o1.getCenterX()+tx*t;
        double ay=o1.getCenterY()+ty*t;
        intersection(ax,ay,o1.getCenterX(),o1.getCenterY(),o3.getCenterX(),o3.getCenterY());
        setVerX1(ax);
        setVerY1(ay);
        SideGo(l1);
        intersection(ax,ay,o1.getCenterX(),o1.getCenterY(),o4.getCenterX(),o4.getCenterY());
        setVerX1(ax);
        setVerY1(ay);
        SideGo(l2);
        //System.out.println(ax+" "+ay+"  "+getVerX()+"  "+getVerY() );
    }

    //Нахождение углов  АВС координаты А, В, С, возвращает угол в градусах
    private double angleTriangle(double x1, double y1, double x2, double y2, double x3, double y3){
        double ab=distance(x1,y1,x2,y2);
        double ac=distance(x1,y1,x3,y3);
        double bc=distance(x2,y2,x3,y3);
       // return round(toDegrees(acos((pow(ab,2)+pow(ac,2)-pow(bc,2))/(2*ab*ac))));
        return round(toDegrees(acos((pow(ab,2)+pow(bc,2)-pow(ac,2))/(2*ab*bc))));
    }

    //Площадь треугольника
    private double areaTriangle(double x1, double y1, double x2, double y2, double x3, double y3){
        return ((x2-x1)*(y3-y1)-(x3-x1)*(y2-y1))/2;
    }

    //Точка пересечения двух прямых под 90 градусов, для высот
    private void intersection(double x1, double y1, double x2, double y2, double x3, double y3) {
        double a1 = y3 - y2;
        double b1 = x2 - x3;
        double c1 = x2 * y3 - x3 * y2;
        double c2 = -x1 * (x3 - x2) + y1 * (y2 - y3);
        //Вычисление главного определителя
        double o = -pow(a1, 2) - pow(b1, 2);
        setVerX((-c1 * a1 - c2 * b1)/o);
        setVerY((a1 * c2 - b1 * c1)/o);
    }

}

