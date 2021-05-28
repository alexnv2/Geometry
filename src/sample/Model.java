package sample;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static ContstantString.StringStatus.*;
import static ContstantString.StringWeb.*;
import static java.lang.StrictMath.*;


/**
 * Класс Model расширение интерфейса Observable
 * Класс Модели, получает запросы на отработку событий от котроллера и
 * выставляет переменные и отправляет сообщения классу отобраюения (View),
 * который выводит эти данные на экран.
 */
@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements  Observable {

    //Переменные класса
    private Circle vertex; //точка
    private Circle selected;//Хранит ссылки на выбранные объекты
    private Line line;//линия, луч, прямая
    private Label Status;//левый статус, вывод действий
    private Label rightStatus;//правый статус, вывод координат
    private Text textGo;//Для наименования точек, отрезков, прямых и т.д
    private WebView webView; //браузер в левой части доски
    private TextArea textArea;//получаем ссылку на правую часть доски для вывода информации о фигурах
    private Pane paneBoards;//получаем ссылку на доску, где размещены объекты
    private GridView gridViews;
    private Arc arcGo;
    private Color ColorGo;


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
    private double textX;//координата Х для имен точек
    private double textY;//координата Y для имен точек

    private Circle timeVer;//для временного хранения выбранных вершин
    private String verSegmentStart;//имя начала отрезка для метода txtAreaOutput
    private String verSegmentEnd;//имя конца отрезка для метода txtAreaOutput
    private String verSegmentAngle;//имя угла

    private String indexPoind="A";//Индекс для точек
    private int indexPoindInt =0;
    private String indexLine="a";//Индекс для прямых, отрезков, лучей
    private int indexLineInt=0;

    private int inDash=2;//индекс определяет внешний вид прямой (0-4 вида)

    private boolean poindOldAdd=false;//true - Берем существующие точки для отрезка
    private double radiusPoind=5;//радиус точки

    private double arcRadius;//радиус дуги
    private double angleStart;//начало дуги гр.
    private double angleLength;//длина дуги гр.

    private boolean removeObject=false;//true - режим удаления
    //Коллекции
    private LinkedList<PoindCircle> poindCircles=new LinkedList<>();//коллекция для точек по классу
    private LinkedList<PoindLine> poindLines=new LinkedList<>();//коллекция для линий по классу
    private LinkedList<VertexArc> vertexArcs=new LinkedList<>();//коллекция для арок углов
    private ArrayList<Double> arrDash=new ArrayList<>();//массив для создания вида строк
    private LinkedList<NamePoindLine> namePoindLines=new LinkedList<>();//колекция для имен

    //Определяем связанный список для регистрации классов слушателей
    private LinkedList<Observer> observers=new LinkedList<>();

    public void setWindShow(int w){
        WIND_SHOW=w;
    }
    public int getWindShow(){
        return WIND_SHOW;
    }

    /**
     * Конструктор класса без переменных
     */
    Model(){
     }


    /**
     * Метод registerObserver(Observer o)
     * Метод регистрации слушателя, ереопределяем функцию интерфейса
     * @param o - объект слушатель, добавляем в коллекцию слушателей
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }
    /**
     * Метод notifyObservers(String message)
     * Уведомление слушателя, переопределяем функцию интерфейса.
     * @param message - сообщение  для слушателя
     */
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

    /**
     * Метод indexPoindAdd().
     * Предназначен для увелечения индекса в названии точки.
     */
    private String indexPoindAdd(){
        String s;
        if(indexPoindInt >0) {
            s = indexPoind + indexPoindInt;
        }else{
            s=indexPoind;
        }
        char[] chars=indexPoind.toCharArray();
        if(String.valueOf(chars[0]).equals("Z")){
            indexPoind="A";
            indexPoindInt +=1;
        }else {
            chars[0]+=1;
            indexPoind=String.valueOf(chars[0]);
        }
        return s;
    }

    /**
     * Метод indexLinedAdd().
     * Предназначен для увелечения индекса в названии прямых, отрезков, лучей.
     */
    private String indexLineAdd(){
        String s;
        if(indexLineInt >0) {
            s = indexLine + indexLineInt;
        }else{
            s=indexLine;
        }
        char[] chars=indexLine.toCharArray();
        if(String.valueOf(chars[0]).equals("z")){
            indexLine="a";
            indexLineInt +=1;
        }else {
            chars[0]+=1;
            indexLine=String.valueOf(chars[0]);
        }
        return s;
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

    StringBuilder nameSplit(String s){
        String[] name=s.split("_");
        StringBuilder sb=new StringBuilder();
        for (String n: name){
            sb.append(n);
        }
        return sb;
    }
    /**
     * Метод txtAreaOutput()
     * Предназначен для выборки из коллекций объектов информацию о геометрических фигурах
     * и выводе в правой части доски.
     */
   public void txtAreaOutput() {
       //Информация о точках
       for (PoindCircle p : poindCircles) {
           if (p.getCircle() != null) {
               String s1 = p.getId();
               double s2 = p.getX();
               double s3 = p.getY();
               txtShape = txtShape + "Точка: " + s1 + " (" + s2 + "," + s3 + ")\n";
           }
       }
       //Информация об отрезках, лучах и прямых
       for (PoindLine p : poindLines) {
           if (p.getLine() != null) {
               int l = p.getSegment();
               if (l == 0) {
                   double lengthSegment = Math.round(distance(p.getStX(), p.getStY(), p.getEnX(), p.getEnY()) * 100);
                   txtShape = txtShape + STA_10+ nameSplit(p.getId()) + " Длина:" + lengthSegment / 100 + "\n";
               }
                else if (l == 1) {
                    txtShape = txtShape + STA_11 + p.getLine().getId() + " или " + nameSplit(p.getId()) + "\n";
                }else if (l == 2) {
                    txtShape = txtShape + STA_12 + p.getLine().getId() + " или " + nameSplit(p.getId()) + "\n";
                }else if(l==3){
                   double lengthSegment = Math.round(distance(p.getStX(), p.getStY(), p.getEnX(), p.getEnY()) * 100);
                   txtShape = txtShape + STA_17+ nameSplit(p.getId()) + " Длина:" + lengthSegment / 100 + "\n";
               }
            }
        }
       //Информация об углах
        for( VertexArc v: vertexArcs){
            if(v!=null) {
                txtShape = txtShape + "Угол " + nameSplit(v.getId()) +"= "+v.getLengthAngle()+ " гр. \n";
                }
            }
       textAreaGo();
    }

    /**
     * Метод createNameShapes().
     * Предназначен для создания объекта хранения имени геометрической фигуру.
     * Прявязка свойст мышки к объекту.
     * @return - возвращает созданнай объект
     */
   Text createNameShapes(String name){
       Text nameText=new Text();
       nameText.setId(name);

       //Привязка к событию мышки
       nameText.setOnMouseDragged(e-> {
           if(nameText.xProperty().isBound()){//проверить на связь
               textUnBindCircle(nameText);//снять связь для перемещения
           }
           //Найти точку с которой связано имя
           Circle circle=findCircle(nameText.getId());
           //Максимальное растояние при перемещении от точки
           double maxRadius=distance(e.getX(),e.getY(),circle.getCenterX(),circle.getCenterY());
            if(maxRadius<80){
               //Перемещаем имя точки
               nameText.setX(e.getX());
               nameText.setY(e.getY());
           }
           nameUpdateXY(nameText.getId());//обновляем данные колекции
           //устанавливаем связь с точкой
           textBindCircle(circle,nameText,(int)(nameText.getX()-circle.getCenterX()),(int)(nameText.getY()-circle.getCenterY()));
       });
       //Наведение мышки на объект
       nameText.setOnMouseEntered(e-> {
           nameText.setCursor(Cursor.HAND);
         //  System.out.println("do "+nameText.getId());
       });
       //Уход мышки с объекта
       nameText.setOnMouseExited(e-> {
           nameText.setCursor(Cursor.DEFAULT);
        });

       return nameText;
   }

    /**
     * Метод nameUpdateXY(String id).
     * Предназначен для обновления мировых координат и растояния до точки при
     * перемещении объекта Text. Вызывается из метода createNameShapes(String name).
     * @param id - строка имя объекта Text.
     */
   private void nameUpdateXY(String id){
       //Найти точку в колекции
       Circle circle=findCircle(id);
     //  System.out.println("do "+id);
       for(NamePoindLine np: namePoindLines) {
           if(np!=null) {
               if(np.getId().equals(id)) {
                   np.setDX(gridViews.revAccessX(np.getText().getX())-gridViews.revAccessX(circle.getCenterX()));
                   np.setDY(gridViews.revAccessY(np.getText().getY())-gridViews.revAccessY(circle.getCenterY()));
                   np.setX(gridViews.revAccessX(circle.getCenterX()));
                   np.setY(gridViews.revAccessY(circle.getCenterY()));
               }
           }
       }
   }

    /**
     * Метод nameCircleAdd(Circle circle).
     * Предназначен для добавления объекта Text связанного с именем точки.
     * Вызывается из метода createPoindAdd() при добавлении точек.
     * @param circle - объект точка.
     */
   private void nameCircleAdd(Circle circle){
       Text textCircle=createNameShapes(circle.getId());//создать объект текст (имя точки)
       //Добавить в коллекцию NamePoindLine
       namePoindLines.add(new NamePoindLine(textCircle,circle.getId(),-1,1,gridViews.revAccessX(circle.getCenterX()),gridViews.revAccessY(circle.getCenterY())));
       textCircle.setText(circle.getId());//Имя для вывода на жоску
       textX=circle.getCenterX()-20;//место вывода Х при создании
       textY=circle.getCenterY()+20;//место вывода Y при создании
       TextGo(textCircle);//вывести на доску
       //Добавить в колекцию объектов на доске
       paneBoards.getChildren().add(textCircle);
       //Односторонняя связь точки с именем объекта для перемещения
       textBindCircle(circle,textCircle,-20,20);
   }

    /**
     * Метод findNameText(Circle circle).
     * Предназначен для поиска в коллекции NamePoindLine объекта Text связанного с именем точки.
     * @param circle  - объект точка
     * @return - объект Text
     */
   private Text findNameText(Circle circle){
       for (NamePoindLine np: namePoindLines){
           if(np!=null){
               if(np.getId().equals(circle.getId())){
                   return np.getText();
               }
           }
       }
       return null;
   }
   private void nameLineAdd(Line line){
       Text nameLine=createNameShapes(line.getId());
       //Добавить в коллекцию NamePoindLine

   }

   private void nameArc(Arc arc){
       Text nameArc=createNameShapes(arc.getId());
       //Добавить в коллекцию NamePoindLine

   }
    /**
     * Метод createPoindAdd()
     * Предназначен для создания точек и вывод на доску.
     * Для создания точки вызывается метод createPoind().
     * @return новая точка
     */
    Circle createPoindAdd(){
        Circle newPoind;//Объявить переменную
        newPoind = createPoind();//Создать точку
        //добавить в коллецию точек
        poindCircles.add(new PoindCircle(newPoind,newPoind.getId(),verX0,verY0,true,false,0));
        //Передать в View для вывода на экран
        VertexGo(newPoind);
        //Добавить имя на доску
        nameCircleAdd(newPoind);
        //Добавить в правую часть доски
        setTxtShape("");
        txtAreaOutput();
        return newPoind;//возвращает точку
    }
    /**
     * Метод createPoind()
     * Предназначен для создания точек в виде кругов, а также привязке событий к данным точкам.
     * Определяются основные свойства объекта.
     * @return точку.
     */
    Circle  createPoind(){
        Circle circle=new Circle();
        circle.setRadius(radiusPoind);
        circle.setFill(Color.LIGHTBLUE);
        circle.setStroke(Color.DARKSLATEBLUE);
        circle.setId(indexPoindAdd());//Индефикатор узла

        //Обработка событий
        //Перемещение с нажатой клавишей
        circle.setOnMouseDragged(e-> {
                if(findPoindCircleMove(circle.getId())) {
                    //Найти по точке имя к оллекции
                    Text txt=findNameText(circle);
                    if(!txt.xProperty().isBound()){//проверить на связь, если нет связать
                        textBindCircle(circle,txt, (int)(txt.getX()-circle.getCenterX()),(int)(txt.getY()-circle.getCenterY()));//если нет, связать
                    }
                    circle.setCenterX(e.getX());
                    circle.setCenterY(e.getY());
                    //добавить новые координаты в коллекцию PoindCircle
                    findCirclesUpdateXY(circle.getId());
                    //добавить новые координаты в коллекцию PoindLine
                    findLineUpdateXY(circle.getId());
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
                selectCircle(circle);
                //circle.setFill(Color.RED);
                poindOldAdd = true;//взять эту точку для отрезка
                timeVer = circle;//сохранить выбранную точку для построения
                //Вызвать метод для увелечения счетчика index в колекции PoindCircles
            if(removeObject==true) { //Режим  для  удаления
                boolean remove=removePoindAdd(circle);
            }
        });
        //Наведение на точку
        circle.setOnMouseEntered(e->{
            circle.setCursor(Cursor.HAND);
            circle.setRadius(12);
            Stop[] stops = new Stop[] {
                    new Stop(0.0, Color.BLUE), new Stop(1.0, Color.WHITE)
            };
            circle.setFill(new RadialGradient(0.0, 0.0, 0.5, 0.5, 0.5, true,
                    CycleMethod.NO_CYCLE,stops));
            //Установить статус "Точка + выбранная точка"
            setStringLeftStatus(STA_9+circle.getId());
            statusGo(Status);
        });
        //Отпускание кнопки
        circle.setOnMouseReleased(e->{
           // circle.setFill(Color.DARKSLATEBLUE);
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

    /**
     * Метод removePoindAdd(Circle c)
     * Ищет выбранную точку в коллекции PoindCircle и удаляет.
     * Вызывается из метода createPoind() из события onMousePressed().
     * @param c -объект точка
     */
    private boolean removePoindAdd(Circle c){
        for(PoindCircle p: poindCircles){
            if(p!=null){
                if(p.getId().equals(c.getId())){
                    if(p.getIndex()==0){//индекс = 0 удаляем, иначе уменьшаем индекс и удаляем связанные фигуры
                        Text txt=findNameText(p.getCircle());//найти имя объекта
                        if(txt.xProperty().isBound()){//проверить на связь
                            textUnBindCircle(txt);//отменить связь
                        }
                        paneBoards.getChildren().remove(txt);//удаление имени с доски
                        removeNameText(txt);//удаление имени объекта их коллекции
                        paneBoards.getChildren().remove(p.getCircle());//удаление объекта с доски
                        poindCircles.remove(p);//удаление объекта точка из коллекции
                        removeObject=false;//сбросить статус удаления
                        //Вывод информации об объектах в правую часть доски
                        setTxtShape("");
                        txtAreaOutput();
                        return true;
                    }else{
                       // System.out.println("poind связана");
                        removeObject=false;
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * Метод removeNameText(Text txt).
     * Предназначен для удаления имени точки. Вызывается из метода removePoindAdd(Circle c).
     * @param txt -объект имя точки
     * @return -true - успешное удаление
     */
    private boolean removeNameText(Text txt){
        for(NamePoindLine pn: namePoindLines){
            if(pn!=null){
                if(txt.getId().equals(pn.getId())){
                    namePoindLines.remove(pn);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Метод indexAdd(Circle circle)
     * Метод для увелечения счетчика, когда точка принадлежит разным геометрическим фигурам.
     * @param circle
     */
    public void indexAdd(Circle circle) {
        for(PoindCircle c: poindCircles){
            if(c!=null){
                if(c.getId().equals(circle.getId())){
                    c.setIndex(c.getIndex()+1);
                }
            }
        }
    }

    /**
     * Метод removeIndex(Circle circle)
     * Метод для уменьшения счетчика, вызывается перед удалением точки.
     * Точку можно удалить, если индекс равен 0.
     * @param circle
     * Возврат: индекс точки.
     */
   private int removeIndex(Circle circle){
       for(PoindCircle c: poindCircles){
           if(c!=null){
               if(c.getId().equals(circle.getId())){
               if(c.getIndex()==0){
                   return 0;
               }else {
                   c.setIndex(c.getIndex() - 1);
                   return c.getIndex();
                    }
               }
           }
       }
      return -1;//точка не найдена
   }



//Создание  отрезка
     Line createLine(int seg){
        Line newLine=new Line();
        if(seg==0 || seg==3) {//Отрезок или треугольник
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
        newLine.setId(indexLineAdd());//Индефикатор узла
        newLine.setStroke(Color.DARKSLATEBLUE);//Color
        newLine.setStrokeWidth(2);//Толщина

         //Прявязка событий мышки
         newLine.setOnMousePressed(e->{
            // System.out.println("poind");
         });
         //Наведение на отрезок
         newLine.setOnMouseEntered(e->{
             newLine.setCursor(Cursor.HAND);
             //Установить статус
             for (PoindLine p: poindLines) {

                 if (p.getLine().getId().equals(newLine.getId())) {
                     if (p.getSegment() == 0) {
                         //Найти и вывести имя отрезка
                         setStringLeftStatus(STA_10 + nameSplit(p.getId()));
                         statusGo(Status);
                     }else if(p.getSegment()==1){
                         setStringLeftStatus(STA_11 + nameSplit(p.getId()));
                         statusGo(Status);
                     }else if(p.getSegment()==2){
                         setStringLeftStatus(STA_12 + nameSplit(p.getId()));
                         statusGo(Status);
                     }else if(p.getSegment()==3){
                         setStringLeftStatus(STA_17 + nameSplit(p.getId()));
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
         //Вид линии
         switch (inDash){
             case 0->Collections.<Double>addAll(arrDash,2.0);
             case 1->Collections.<Double>addAll(arrDash,15.0, 5.0);
             case 2->Collections.<Double>addAll(arrDash,5.0, 4.0, 5.0, 4.0, 5.0);
             case 3->Collections.<Double>addAll(arrDash,2.0, 10.0);
             case 4->Collections.<Double>addAll(arrDash,10.0,4.0,10.0);
         }
         newLine.getStrokeDashArray().addAll(arrDash);
         poindLines.add(new PoindLine(newLine,newLine.getId(),verX0,verY0,verX0,verY0,true,false,segment));
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

    /**
     * Метод  distance(double x1, double y1, double x2, double y2).
     * Предназнпчен для расчета растояния межде двумя вершинами, заданные коорднатами.
     * @param x1 - координаты вершины x1
     * @param y1 - координаты вершины y1
     * @param x2 - координаты вершины x2
     * @param y2 -координаты вершины y2
     * @return возвращает длину.
     */
     public double distance(double x1, double y1, double x2, double y2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
    }

    /**
     * Метод VertexGo(Circle o).
     * Уведомляет класс отображения View, о том что точка готова к перемещению на доске
     * @param o - объект точка
     */
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

    //Слева
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
                   p.setId(poindA+"_"+poindB);
               }
           }
       }
    }
    /**
     * Метод findCircle(String c)
     * Предназначен для поска в коллекции точек объектов Circle по имени.
     * @param c - имя точки
     * @return объект Circle или null если не найден.
     */
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


    /**
     * Метод findCirclesUpdateXY(String id).
     * Предназначен для поиска объектов в коллекции PoindCircle по имени и
     * замены мировых координат
     * @param id - имя объекта
     */
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

    /**
     * Метод findLinesUpdateXY(String id).
     * Предназначен для замены мировых координат при построении отрезков, лучей и прямых
     * @param id - имя линии до замены
     * Особенность метода, должен всегда вызываться до метода findNameId(Circle1, Circle2, Line), который
     * меняет имя в коллекции PoindLines.
     */
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
    /**
     * Метод findLineUpdateXY(String s).
     * Предназначен для обновления мировых координат в коллекции PoindLine после
     * перемещения одной из точек.
     * @param s - имя точки перемещения
     */
     public void findLineUpdateXY(String s){
          for(PoindLine p: poindLines) {
            if (p != null) {
             String name=p.getId();
             String[] chName=name.split("_");
             //Обновляем начало отрезка, луча, прямой
             if(s.equals(chName[0])){
                 //Обновляем координаты
                 p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                 p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
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
             if(s.equals(chName[1])){
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
    /**
     * Метод findPoindCircleMove(String id).
     * Предназначен для поиска по колекции PoindCircle по имени точки разрешения на
     * перемещение точки по доске.
     * @param id  - имя точки
     * @return true- перемещение точки разрешено, false - точка расчетная, перемещение запрещено
     */
     private boolean findPoindCircleMove(String id){
        boolean bfMove=false;//всегда запрещено
        for (PoindCircle p: poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    bfMove=p.isBMove();//определяется в коллекции
                }
            }
        }
        return bfMove;
    }
    /**
     * Метод arcVertexAdd(String arc).
     * Предназначен для создания нового объекта дуги, которая задается в виде
     * трех вершин состоящих из точек. Для расчета параметров дуги и вывода на доску
     * вызывается метод arcVertex(o1,o2,o3,arcNew). о1, о2, о3 - точки вершин из класса Circle.
     * arcNew - новая дуга.
     * @param arc - срока для добавления дуги состоит из имен вершин, угол АВС
     * @return - возвращает объект дугу
     */
    Arc arcVertexAdd(String arc){
        String[] arcChar=arc.split("_");
        //char[] arcChar=arc.toCharArray();//преобразовать в массив символов
        Circle o1=findCircle(arcChar[0]);
        Circle o2=findCircle(arcChar[1]);
        Circle o3=findCircle(arcChar[2]);
        Arc arcNew=new Arc();
        arcNew.setId(String.valueOf(arcChar[1]));
        setColorGo(Color.DARKSLATEBLUE);
        arcNew.setType(ArcType.ROUND);
        arcNew.setFill(Color.LIGHTBLUE);
        arcNew.setOpacity(0.5);//прозрачность
        ArcColorGo(arcNew);//задать цвет
        arcVertex(o1,o2,o3,arcNew);//расчитать арку угла для построения

        //добавить в коллекцию дуг
        vertexArcs.add(new VertexArc(arcNew,arc,gridViews.revAccessX(o2.getCenterX()),gridViews.revAccessY(o2.getCenterY()),arcRadius,
                arcRadius,angleStart,angleLength,false));
        //При наведении мышки на дугу, вывод статусной строки
        arcNew.setOnMouseEntered(e->{
            //Установить статус "Угол + выбранный угол + длина дуги в градусах"
            setStringLeftStatus(STA_16+arcNew.getId()+" = "+arcNew.getLength()+" гр.");
            statusGo(Status);
        });
        //При выходе мышки из дуги, сбросить статусную стоку.
        arcNew.setOnMouseExited(e->{
            setStringLeftStatus("");
            statusGo(Status);
        });


        return arcNew;
    }

    /**
     * Метод arcVertex(Circle o1, Circle o2, Circle o3, Arc a1).
     * Предназначен для расчета угла по координатам трех точек. Построения дуги, перемещения дуги.
     * @param o1 -первая точка А
     * @param o2 - вторая точка В (центр угла)
     * @param o3 - третья точка С
     * @param arc - арка угла
     * Устанавливает для класса View следующие переменные:
     * angleLength - длину дуги в градусах
     * arcRadius - радиус дуги
     * angleStart - начальный угол в градусах
     * verX и verY - координаты центра дуги
     * Вызывает метод ArcGo(a1) для построения и перемещения дуги
     */
    public void arcVertex(Circle o1, Circle o2, Circle o3, Arc arc){
        //Длина дуги в градусах
        double angleABC=angleTriangle(o1.getCenterX(), o1.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        angleLength=angleABC;
        arcRadius=30;//радиус
        //Начальный угол в градусах
        double arcStart=angleTriangle(o2.getCenterX()+200,o2.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        //System.out.println("Угол ВС "+arcStart);
        double str=areaTriangle(o2.getCenterX()+200,o2.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        double str1=areaTriangle(o1.getCenterX(), o1.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        //System.out.println("str "+str+" str1 "+str1);
        if (str<0){
            arcStart=360-arcStart;
         //   System.out.println("str<0 "+arcStart);
        }else {
            arcStart=arcStart;
         //   System.out.println("иначе минус "+arcStart);
        }
        if(str1>0){
            arcStart=arcStart-angleABC;
        }
        angleStart=arcStart;
        //Запомнить текущие координаты мышки
        double stX=verX;
        double stY=verY;
        //Заменить для построения арки угла
        verX=o2.getCenterX();
        verY=o2.getCenterY();
        ArcGo(arc);//перемещение дуги
        //Восстановить текущие координаты мышки
        verX=stX;
        verY=stY;

    }
    /**
     * Метод findArcUpdate(String s)
     * Предназначен для поиска дуги по вершине угла и изменения в коллекции дуг
     * мировых координат после премещения дуги.
     * @param s - вершина угла
     */
    private void findArcUpdate(String s){
        for(VertexArc v: vertexArcs ) {
            if (v != null) {
                String[] arcChar=v.getId().split("_");
                if (v.getArc().getId().equals(s)) {
                    v.setCenterX(gridViews.revAccessX(verX));
                    v.setCenterY(gridViews.revAccessY(verY));
                    v.setStartAngle(v.getArc().getStartAngle());
                    v.setLengthAngle(v.getArc().getLength());
                }else if(s.equals(arcChar[0])){
                    v.setCenterX(gridViews.revAccessX(v.getArc().getCenterX()));
                    v.setCenterY(gridViews.revAccessY(v.getArc().getCenterY()));
                    v.setStartAngle(v.getArc().getStartAngle());
                    v.setLengthAngle(v.getArc().getLength());
                }else if(s.equals(arcChar[2])){
                    v.setCenterX(gridViews.revAccessX(v.getArc().getCenterX()));
                    v.setCenterY(gridViews.revAccessY(v.getArc().getCenterY()));
                    v.setStartAngle(v.getArc().getStartAngle());
                    v.setLengthAngle(v.getArc().getLength());
                }
            }
        }
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
    }

    /**
     * Метод angleTriangle(double x1, double y1, double x2, double y2, double x3, double y3).
     * Предназначен для расчета угла по координатам вершин.
     * @param x1  - координаты х1 первой вершины
     * @param y1 - координаты y1 первой вершины
     * @param x2 - координаты х2 тророй вершины
     * @param y2 - координаты y2 второй вершины
     * @param x3 - координаты х3 третьей вершины
     * @param y3 - координаты y3 третьей вершины
     * @return угол в градусах c точностью до десятых градуса.
     */
     private double angleTriangle(double x1, double y1, double x2, double y2, double x3, double y3){
        double ab=distance(x1,y1,x2,y2);
        double ac=distance(x1,y1,x3,y3);
        double bc=distance(x2,y2,x3,y3);
        double angle=round(toDegrees(acos((pow(ab,2)+pow(bc,2)-pow(ac,2))/(2*ab*bc)))*10);
        return angle/10;
    }

    /**
     * Метод areaTriangle(double x1, double y1, double x2, double y2, double x3, double y3).
     * Предназанчен для расчета площади треугольника по координанам трех вершин
     * @param x1 - координаты вершины x1
     * @param y1 - координаты вершины y1
     * @param x2 - координаты вершины x2
     * @param y2 - координаты вершины y2
     * @param x3 - координаты вершины x3
     * @param y3 - координаты вершины y3
     * @return возвращает площадь треугольника.
     */
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
    /**
     * Метод lineBindCircles(Circle c1, Circle c2, Line l)
     * Метод двунаправленного связывания точек начала и конца линии с самой линией.
     * Используется при создании отрезков и треугольников.
     * @param c1 - точка начала отрезка
     * @param c2 - точка конца отрезка
     * @param l - линия между этими точками
     */
    public void lineBindCircles(Circle c1, Circle c2, Line l){
        l.startXProperty().bindBidirectional(c1.centerXProperty());
        l.startYProperty().bindBidirectional(c1.centerYProperty());
        l.endXProperty().bindBidirectional(c2.centerXProperty());
        l.endYProperty().bindBidirectional(c2.centerYProperty());

    }

    /**
     * Метод textBindCircle(Circle c, Text txt, int dx, int dy).
     * Предназначен для связывания место расположения надписи с точкой.
     * @param c - объект круг.
     * @param txt - объект имя точки.
     * @param dx - смещения имени от центра точки.
     * @param dy - смещение имени от цертра точки.
     */
    private void textBindCircle(Circle c, Text txt, int dx, int dy){
         txt.xProperty().bind(c.centerXProperty().add(dx));
         txt.yProperty().bind(c.centerYProperty().add(dy));
    }

    /**
     * Метод textUnBindCircle(Text txt).
     * Предназначен для отключения связи имени точки с текстом.
     * @param txt - объект имя точки.
     */
    private void textUnBindCircle(Text txt){
        txt.xProperty().unbind();
        txt.yProperty().unbind();
    }
    /**
     * Метод lineUnBindCircles(Circle c1, Circle c2, Line l)
     * Метод отмены двунаправленного связывания точек начала и конца линии с самой линий.
     * Используется при удалении отрезков и треугольников
     * @param c1 - точка начала отрека
     * @param c2 - точка конца отрезка
     * @param l - линия между отрезками
     */
    public void lineUnBindCircles(Circle c1, Circle c2, Line l){
        l.startXProperty().unbindBidirectional(c1.centerXProperty());
        l.startYProperty().unbindBidirectional(c1.centerYProperty());
        l.endXProperty().unbindBidirectional(c2.centerXProperty());
        l.endYProperty().unbindBidirectional(c2.centerYProperty());
    }

    /**
     * Метод rayBindCircles(Circle cStart, Circle cEnd, Line ray)
     * Метод создания двунаправленного связывания точки начала луча и начала линии,
     * а также однонаправленноного связывания второй точки на луче с окончанием линии.
     * @param cStart - точка начала луча
     * @param cEnd - вторая точка на луче
     * @param ray -прямая от начала луча через вторую точку
     * Вызывает метод rayLineX(cStart, cEnd) и  rayLineY(cStart, cEnd) для расчета окончания прямой при построении.
     */
    public void rayBindCircles(Circle cStart, Circle cEnd, Line ray) {
        ray.startXProperty().bindBidirectional(cStart.centerXProperty());
        ray.startYProperty().bindBidirectional(cStart.centerYProperty());
        //Расчет конца луча
        ray.startYProperty().addListener((obj, oldValue, newValue) -> {
            ray.setEndY(rayLineY(cStart, cEnd));
        });
        ray.startXProperty().addListener((obj, oldValue, newValue) -> {
            ray.setEndX(rayLineX(cStart, cEnd));
        });
        //Точка на луче
        cEnd.centerXProperty().addListener((obj, oldValue, newValue) -> {
            ray.setEndX(rayLineX(cStart, cEnd));
        });
        cEnd.centerYProperty().addListener((obj, oldValue, newValue) -> {
            ray.setEndY(rayLineY(cStart, cEnd));
        });
    }

    /**
     * Метод rayUnBindCircles(Circle cStart, Circle cEnd, Line ray)
     * Метод отмены двунаправленного связывания точки начала луча и начала линии,
     * а также отмены однонаправленноного связывания второй точки на луче с окончанием линии.
     * Вызывается перед удалением объекта.
     * @param cStart
     * @param cEnd
     * @param ray
     */
    public void rayUnBindCircles(Circle cStart, Circle cEnd, Line ray) {
        ray.startXProperty().unbindBidirectional(cStart.centerXProperty());
        ray.startYProperty().unbindBidirectional(cStart.centerYProperty());
        //Расчет конца луча
        ray.startYProperty().removeListener((obj, oldValue, newValue) -> {
            // ray.setEndY(rayLineY(cStart, cEnd));
        });
        ray.startXProperty().removeListener((obj, oldValue, newValue) -> {
            // ray.setEndX(rayLineX(cStart, cEnd));
        });
        //Точка на луче
        cEnd.centerXProperty().removeListener((obj, oldValue, newValue) -> {
            // ray.setEndX(rayLineX(cStart, cEnd));
        });
        cEnd.centerYProperty().removeListener((obj, oldValue, newValue) -> {
            // ray.setEndY(rayLineY(cStart, cEnd));
        });
    }
    /**
     * Метод  arcBindPoind(String s, Arc arc)
     * Метод двунаправленного связывания центра угла с точкой, а также однонаправленного связывания
     * двух точек угла с расчетными размерами угла.
     * Для расчета размеров угла вызывается метод из класса Model arcVertex(Circle1, Circle2, Circle3, Arc)
     * @param s - строка содержащая назавание угла, типа АВС (А и С - боковые точки, В - центральная точка угла
     * @param arc - объект арка дуги угла.
     */
    public void arcBindPoind(String s, Arc arc){
        String[] arcChar=s.split("_");

        Circle c1=findCircle(arcChar[0]);
        Circle c2=findCircle(arcChar[1]);
        Circle c3=findCircle(arcChar[2]);
        c2.centerXProperty().bindBidirectional(arc.centerXProperty());
        c2.centerYProperty().bindBidirectional(arc.centerYProperty());
        arc.centerXProperty().addListener((obj, oldValue, newValue)->{
            arcVertex(c1,c2,c3,arc);
        });
        c1.centerXProperty().addListener((obj, oldValue, newValue)->{
            arcVertex(c1,c2,c3,arc);
        });
        c3.centerXProperty().addListener((obj, oldValue, newValue)->{
            arcVertex(c1,c2,c3,arc);
        });
    }
    /**
     * Метод  arcUnBindPoind(String s, Arc arc)
     * Метод отмены двунаправленного связывания центра угла с точкой, а также отмены однонаправленного связывания
     * двух точек угла. Вызывается перед удалением арки угла.
     * @param s - строка строка содержащая назавание угла, типа АВС (А и С - боковые точки, В - центральная точка угла.
     * @param arc - объект арка угла.
     */
    public void arcUnBindPoind(String s, Arc arc){
        char[] arcChar=s.toCharArray();
        Circle c1=findCircle(String.valueOf(arcChar[0]));
        Circle c2=findCircle(String.valueOf(arcChar[1]));
        Circle c3=findCircle(String.valueOf(arcChar[2]));
        c2.centerXProperty().unbindBidirectional(arc.centerXProperty());
        c2.centerYProperty().unbindBidirectional(arc.centerYProperty());
        arc.centerXProperty().removeListener((obj, oldValue, newValue)->{
            // model.arcVertex(c1,c2,c3,arc);
        });
        c1.centerXProperty().removeListener((obj, oldValue, newValue)->{
            //model.arcVertex(c1,c2,c3,arc);
        });
        c3.centerXProperty().removeListener((obj, oldValue, newValue)->{
            //model.arcVertex(c1,c2,c3,arc);
        });
    }
    /**
     * Метод circlesBindLine(Circle cStart, Circle cEnd, Line line)
     * Метод однонаправленного связывания двух точек на прямой с прямой и расчетом начала и конца прямой.
     * Для расчета начала и оконяания прямой вызываются методы:
     * rayLineX() и rayLineY()
     * @param cStart - первая точка на прямой
     * @param cEnd - вторая точка на прямой
     * @param line - прямая
     */
    //Связывание прямой с точками
    public void circlesBindLine(Circle cStart, Circle cEnd, Line line) {
        //Точка на прямой
        cEnd.centerXProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndX(rayLineX(cStart, cEnd));
            line.setStartX(rayLineX(cEnd, cStart));
        });
        cEnd.centerYProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndY(rayLineY(cStart, cEnd));
            line.setStartY(rayLineY(cEnd, cStart));
        });
        cStart.centerXProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndX(rayLineX(cStart, cEnd));
            line.setStartX(rayLineX(cEnd, cStart));
        });
        cStart.centerYProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndY(rayLineY(cStart, cEnd));
            line.setStartY(rayLineY(cEnd, cStart));
        });
    }

    /**
     * Метод rayLineX(Circle c1, Circle c2)
     * Метод для расчета по параметрическому уравнению прямой координат начала и конца для прямой,
     * окончания линии для луча для координаты Х.
     * @param c1 - объект первая точка
     * @param c2 - объект вторая точка
     * @return - координата Х
     */
    double rayLineX(Circle c1, Circle c2){
        return c1.getCenterX()+(c2.getCenterX()-c1.getCenterX())*3;
    }

    /**
     * Метод rayLineY(Circle c1, Circle c2)
     * Метод для расчета по параметрическому уравнению прямой координат начала и конца для прямой,
     * окончания линии для луча для координаты Y.
     * @param c1 - объект первая точка
     * @param c2 - объект вторая точка
     * @return - координата Y
     */
    double rayLineY(Circle c1, Circle c2){
        return c1.getCenterY()+(c2.getCenterY()-c1.getCenterY())*3;
    }

    //Тестовый метод для вывода информации по коолекциям
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
        System.out.println("Коллекция имен");
        int n=0;
        for(NamePoindLine nm: namePoindLines){
            System.out.println(n+" "+nm);
            n+=1;
        }
    }

    private void selectCircle(Circle dot)
    {
        //Если выбран существющий объект
        if (selected == dot) return;
        //Создан новый объект, для старого поменять цвет
        if (selected != null) selected.setFill(Color.DARKSLATEBLUE);
        selected = dot;//и установить выбранный круг
        //Если объект существует
        if (selected != null)
        {
            selected.requestFocus(); //установить на него фокус
            selected.setFill(Color.RED);//поменять цвет
        }
    }

    /*
    private void selectLine(Line dot)
    {
        //Если выбран существющий объект
        if (selLine == dot) return;
        //Создан новый объект, для старого поменять цвет
        if (selLine != null) selLine.setStroke(Color.BLACK);
        selLine = dot;//и установить выбранный круг
        //Если объект существует
        if (selLine != null)
        {
            selLine.requestFocus(); //установить на него фокус
            selLine.setStroke(Color.RED);//поменять цвет
        }
    }

     */

}

