package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import static ContstantString.StringStatus.*;

/**
 * Класс управления приложением (Controller), наследует класс View.
 * Реагирует на все события управления от мыши и клавиатуры.
 * Вызывает методы из класса модели для обработки событий
 * @author A. Nosov
 * @version 1.0
 * @see View
 */

public class  Controller extends View {

    //Связать переменные с шаблоном FXML
    @FXML
    public Pane paneShape;//контейнер для геометрических фигур
    public StackPane Cartesian;//контейнер для декартовых координат
    public TextArea txtShape;//контейнер для правой части доски
    @FXML
    private Button btnTreangle;
    @FXML
    private Button btnPoind;//кнопка добавить точку
    @FXML
    private Button btnSegment;//кнопка добавить отрезок
    @FXML
    private Button btnRay;//кнопка добавить луч
    @FXML
    private Button btnLine;//кнопка добавить прямую
    @FXML
    private Button btnAngle;
    @FXML
    private Button btnVertical;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnMediana;
    @FXML
    //Web браузер для вывода данных
    public WebView webViewLeft;//для размещения информации слева от доски
    @FXML
    private Pane paneGrid;//контейнер для сетки
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Line newLine;//отрезок
    private Circle poindLine1;//первая  точка луча, прямой, отрезка
    private Circle poindLine2;//вторая точка луча, прямой, отрезка
    private Arc arc;
    @FXML
    private CheckMenuItem menuShowPoindName;
    @FXML
    private CheckMenuItem menuShowLineName;
    @FXML
    private CheckMenuItem menuGrid;
    @FXML
    private CheckMenuItem menuCartesian;


    //Режимы создания
    private boolean poindAdd=false;//true - создать точку
    private boolean segmentAdd =false;//true - создать отрезок
    private boolean rayAdd=false;//true - создание луча
    private boolean lineAdd=false;//true - создание прямой
    private boolean angleAdd=false;//true -создание угла
    private boolean treangleAdd=false;//true - создание треугольника
    private boolean medianaAdd=false;//true - проведение медианы из квершину треугольника
    private Circle poindTreangle1;//первая точка треугольника
    private Line lineTriangle1;//первая сторона треугольника для построения
    private int angleCol=0;//индекс счета углов


    private boolean poindAdd1=false;//true - создание первой точки для отрезка
    private boolean poindAdd2=false;//true - создание второй точки для отрезка


    private String infoStatus;//Вершины угла
    //Для всплывающих подсказок
    private final Tooltip tooltip=new Tooltip()  ;

    /**
     * Метод инициализации для класс Controller
     */
    @FXML
    private void initialize() {
        //Передача ссылок в класс Model
        model.setStatus(leftStatus);//Передать ссылку на статус для модели
        model.setTextArea(txtShape);//Передать ссылку фигуры
        model.setGridViews(gridViews);//Передать ссылку для пересчета координат для Model
        model.setPaneBoards(paneShape);//Передать ссылку на доску для Model
        model.webHTML(webViewLeft,"geometry.html");//Вывод в web файла html
        //формирование линий координат и сетки, перерасчет при изменении размеров доски
        gridViews.setPaneGrid(paneGrid);
        gridViews.setCartesian(Cartesian);
        //Изменение ширины окна
        Cartesian.widthProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVr(Cartesian.getWidth());
            gridViews.setWl(-Cartesian.getWidth() / 2);
            gridViews.setWr(Cartesian.getWidth() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();//обновить координаты геометрических фигур
        });
        //Изменение высоты окна
        Cartesian.heightProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVb(Cartesian.getHeight());
            gridViews.setWt(Cartesian.getHeight() / 2);
            gridViews.setWb(-Cartesian.getHeight() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();//обновить координаты геометрических фигур
        });

        if (model.isShowGrid()) {
            gridViews.gridCartesian();//Вывод сетки
        }
        //Настройки всплывающих подсказок
        tooltip.setShowDelay(Duration.millis(10.0));
        tooltip.setFont(Font.font(12));
        tooltip.setStyle("-fx-background-color: LIGHTBLUE;" +
                         "-fx-text-fill: black");
        //Добавить изображение к кнопкам
        btnPoind.setStyle("-fx-background-image: url(/Images/point.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnSegment.setStyle("-fx-background-image: url(/Images/point&line.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnAngle.setStyle("-fx-background-image: url(/Images/angle.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnVertical.setStyle("-fx-background-image: url(/Images/vertical.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnDelete.setStyle("-fx-background-image: url(/Images/delete.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnLine.setStyle("-fx-background-image: url(/Images/line.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");

    }


    /**
     * Метод btnPoindClick()
     * Cобытие нажатия кнопки "Добавить точку"
     * Устанавливает режим добавления точки
     */
    public void btnPoindClick() {
        //Установить статус
        model.setStringLeftStatus(STA_1);
        model.statusGo(leftStatus);//Установить статус
        poindAdd=true;//Установить режим добавления

    }
    /**
     * Метод btnSegmentClick()
     * Метод для события нажатия кнопки "Добавить отрезок
     * Устанавливает режим добавления отрезка
     */
     public void btnSegmentClick() {
        //Установить статус
        model.setStringLeftStatus(STA_2);
        model.statusGo(leftStatus);
        segmentAdd =true;

    }

    /**
     * Метод btnRay()
     * Метод для события нажатия кнопки "Добавить луч"
     * Устанавливает режим добавления луча
     */
     public void btnRay() {
        //Установить статус
        model.setStringLeftStatus(STA_3);
        model.statusGo(leftStatus);
        rayAdd=true;//режим построения луча

    }

    /**
     * Метод btLine()
     * Метод для события нажатия кнопки "Добавить прямую"
     * Устанавливает режим добавления прямой
     */
    public void btnLine() {
        //Установить статус
        model.setStringLeftStatus(STA_4);
        model.statusGo(leftStatus);
        lineAdd=true;//режим построения прямой

    }

    /**
     * Метод btnAngle()
     * Метод для события нажатия кнопки "Добавить угол"
     * Устанавливает режим добавления угла
     */
    public void btnAngle() {
        //Установить статус
        model.setStringLeftStatus(STA_14);
        model.statusGo(leftStatus);
        angleAdd=true;//режим построения угла
        infoStatus="";//Имя для коллекции VertexArc
    }

    /**
     * Метод btnTreangle()
     * Метод на события нажатия кнопки "Добавить треугольник"
     * Устанавливает режим добавления треугольника
     */
    public void btnTreangle() {
        //Установить статус
        model.setStringLeftStatus(STA_5);
        model.statusGo(leftStatus);
        treangleAdd = true;
        infoStatus="";//Для коллекции TreangleName
        model.webViewLeftString(webViewLeft, 0);//Определения
      //  model.webViewLeftString(webViewLeft, 10);//Определения остроугольного треугольника
    }

    public void btnMedian() {
        model.setStringLeftStatus(STA_18);
        model.statusGo(leftStatus);
        medianaAdd = true;
    }

    /**
     * Метод btnDelete()
     * Метод запускает режим удаления выбранных объектов.
     * Признак для удаления флаг bSelect=true вовсех колеекциях
     */
    public void btnDelete() {
        //Установить статус
        model.setStringLeftStatus(STA_15);
        model.statusGo(leftStatus);
        model.setRemoveObject(true);//установить режим удаления
        }
    /**
     * Метод onMouseMoved()
     * Отслеживает события перемещения мышки по доске без нажатой кнопки
     * Выводит координаты мыши в статусной строке
     * Испльзуется при добавлении геометрических фигур на доску.
     * @param mouseEvent - координаты мыши
     */
    public void onMouseMoved(@NotNull MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        model.setVerX0(gridViews.revAccessX(mouseEvent.getX()));
        model.setVerY0(gridViews.revAccessY(mouseEvent.getY()));
        rightStatus.setText("x "+mouseEvent.getX()+" y "+mouseEvent.getY()  +" Координаты доски x: " + gridViews.revAccessX(mouseEvent.getX()) + " y: " + gridViews.revAccessY(mouseEvent.getY()));
       //Добавление треугольника
        if(treangleAdd==true  && newLine!=null && poindAdd2==true ){
            model.SideGo(newLine);//проводим отрезок
            model.findLinesUpdateXY(newLine.getId());
            if(angleCol==2){//2-для угла, треугольника
                poindAdd1=true;
            }
        }

        //Создание отрезка
        if (segmentAdd ==true && newLine!=null && poindAdd2==true){
            model.SideGo(newLine);//проводим отрезок
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            poindAdd1 = true;//первая точка создана
        }
        //Вторая точка, если растояние до неё меньше 15px, конец отрезка
        //переходит на точку. Щелчок мышкой, вторая точка выбрана заданная
        model.lineAddPoind(newLine,poindAdd2);

        //Создание луча
        if(rayAdd==true && newLine!=null && poindAdd2==true){
            //Расчитать координаты окончагия луча
            double x=model.getRayEndX()+(model.getVerX()-model.getRayEndX())*3;
            double y=model.getRayEndY()+(model.getVerY()-model.getRayEndY())*3;
            //Добавить коордитаны пересчета в коллекцию
            model.setRayStartX(x);
            model.setRayStartY(y);
            //Пересчет координат в мировые
            model.setVerLineStartX(gridViews.revAccessX(model.getRayEndX()));
            model.setVerLineStartY(gridViews.revAccessY(model.getRayEndY()));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));

            model.RayGo(newLine);//проводим отрезок
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            poindAdd1 = true;//первая точка создана
        }

        //Создание прямой
        if (lineAdd==true && newLine!=null && poindAdd2==true){
            //расчитать концов прямой по уравнению прямой
            double x=poindLine1.getCenterX()+(model.getVerX()-poindLine1.getCenterX())*3;
            double y=poindLine1.getCenterY()+(model.getVerY()-poindLine1.getCenterY())*3;
            double x1=poindLine1.getCenterX()+(model.getVerX()-poindLine1.getCenterX())*-3;
            double y1=poindLine1.getCenterY()+(model.getVerY()-poindLine1.getCenterY())*-3;

            //Добавить коордитаны пересчета в коллекцию
            model.setVerLineStartX(gridViews.revAccessX(x1));
            model.setVerLineStartY(gridViews.revAccessY(y1));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));
            model.findLinesUpdateXY(newLine.getId());
           //задать координаты прямой
            model.setRayStartX(x1);
            model.setRayStartY(y1);
            model.setRayEndX(x);
            model.setRayEndY(y);
            //проводим прямую
            model.RayGo(newLine);
            poindAdd1 = true;//разрешение для постройки 2 точки
        }
    }
    /**
     * Метод onMouseDraggen()
     * Отслеживает события перемещения мышки с нажатой левой кнопкой
     * Используется для перемещения сетки и координатных осей по доске.
     * @param mouseEvent
     */
    public void onMouseDraggen(MouseEvent mouseEvent) {
        //координаты, нужны для перемещения объектов на доске
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        model.setVerX0(gridViews.revAccessX(mouseEvent.getX()));
        model.setVerY0(gridViews.revAccessY(mouseEvent.getY()));

        //Перемещение сетки
        if(mouseEvent.getTarget()==paneShape){
            val dx = gridViews.getVPx() - mouseEvent.getX();//Вычисляем смещение мышки по Х
            val dy = gridViews.getVPy() - mouseEvent.getY();// по Y
            gridViews.setVPx(mouseEvent.getX()); //Сохраняем текущие координаты мышки
            gridViews.setVPy(mouseEvent.getY());
            //Вычисляем смещение окна
            gridViews.setWl(gridViews.getWl() + dx);
            gridViews.setWr(gridViews.getWr() + dx);
            gridViews.setWt(gridViews.getWt() - dy);
            gridViews.setWb(gridViews.getWb() - dy);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();
        }
        mouseEvent.consume();
    }

    /**
     * Метод oMousePressed()
     * Отслеживает нажатие кнопки на доске
     * Используется для перемещения сетки и координатных осей, а также
     * для создания геометрических фигур.
     * @param mouseEvent
     */
    public void onMousePressed(MouseEvent mouseEvent) {
        // Фиксируем точку нажатия кнопки мыши для перемещения сетки и координатных осей
        if(mouseEvent.getTarget()==paneShape) {
            gridViews.setVPx(mouseEvent.getX());
            gridViews.setVPy(mouseEvent.getY());
        }
        //Добавление точки на доске
        if (poindAdd) {
            Circle newPoind=model.createPoindAdd(true);//создать точку
            paneShape.getChildren().add(newPoind);//добавить на доску
            poindAdd = false;//Режим добавления точки окончен
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }
        //Добавление отрезка
        if (segmentAdd ==true && poindAdd1==false) {
            addLineRayStart(0);//Создание первой точки и линии
        }
        //Вторая точка для отрезка
        if (segmentAdd == true && poindAdd1 == true) {
              addLineRayEnd();
              segmentAdd = false;//окончание режима добавления
              //Связать точки с прямой
              model.lineBindCircles(poindLine1,poindLine2,newLine );
              //Заменить имя
              model.findNameId(poindLine1.getId(),poindLine2.getId(),newLine.getId());
              //Вывод информации об объектах в правую часть доски
              model.setTxtShape("");
              model.txtAreaOutput();
        }

        //Добавление угла первая  точка
        if (angleAdd ==true && poindAdd1==false) {
            if (model.isPoindOldAdd() == false) {//false - новая вершина true - взять имеющую
                poindLine1 = model.createPoindAdd(true);//создать новую вершину
                infoStatus = infoStatus+poindLine1.getId()+"_";//добавить вершину в список
                paneShape.getChildren().add(poindLine1);
                model.indexAdd(poindLine1);//увеличить индекс
            } else {
                infoStatus = infoStatus +model.getTimeVer().getId()+"_";//Вершина из временной переменной
                poindLine1=model.getTimeVer();
                model.indexAdd(poindLine1);//увеличить индекс
            }
            if(angleCol==1){

            }
            angleCol+=1;//увеличиваем счетчик вершин
            if (angleCol==3){
                poindAdd2 = false;//закрыть 2 точку
                model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
                System.out.println(infoStatus);
                arc=model.arcVertexAdd(infoStatus);
                paneShape.getChildren().add(arc);//рисуем арку дуги
                arc.toBack();//перемещать узел вниз только после добавления на стол
                //Связываем арку с углом
                model.arcBindPoind(infoStatus,arc);
                angleAdd= false;//окончание режима добавления
                poindAdd1=false;
                angleCol=0;
                //Вывод информации об объектах в правую часть доски
                model.setTxtShape("");
                model.txtAreaOutput();
            }
        }
        //Построение треугольника
        if (treangleAdd ==true && poindAdd1==false) {
            addLineRayStart(3);//Создание первой точки и линии
            angleCol+=1;//увеличиваем счетчик вершин
         }
        //Окончание построения треугольника
        if(treangleAdd==true && poindAdd1==true){
            //Двунаправленная связь
            model.lineBindCircles(poindTreangle1,poindLine1,lineTriangle1);
            Point2D v1=new Point2D(poindTreangle1.getCenterX(),poindTreangle1.getCenterY());
            infoStatus=poindTreangle1.getId()+"_";
            //Заменить имя
            model.findNameId(poindTreangle1.getId(),poindLine1.getId(),lineTriangle1.getId());
            Point2D v2=new Point2D(poindLine1.getCenterX(),poindLine1.getCenterY());
            infoStatus=infoStatus+poindLine1.getId()+"_";
            addLineRayEnd();
            //Двунаправленная связь
            model.lineBindCircles(poindLine1,poindLine2,newLine);

            //Заменить имя
            model.findNameId(poindLine1.getId(),poindLine2.getId(),newLine.getId());
            newLine = model.createLineAdd(3);
            model.setVerX(poindTreangle1.getCenterX());
            model.setVerY(poindTreangle1.getCenterY());
            model.SideGo(newLine);
            //Двунаправленная связь
            model.lineBindCircles(poindTreangle1,poindLine2,newLine);
            Point2D v3=new Point2D(poindLine2.getCenterX(),poindLine2.getCenterY());
            infoStatus=infoStatus+poindLine2.getId();
            Polygon t=model.treangleAdd(v1,v2,v3,infoStatus);

            paneShape.getChildren().addAll(newLine,t);
            newLine.toBack();
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            //Заменить имя
            model.findNameId(poindTreangle1.getId(),poindLine2.getId(),newLine.getId());
            treangleAdd= false;//окончание режима добавления
            poindAdd1=false;
            angleCol=0;
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }

        //Добавления луча
        if (rayAdd==true && poindAdd1==false) {
            addLineRayStart(1);
        }
        //Окончание построения луча
        if(rayAdd==true && poindAdd1==true){
            addLineRayEnd();
            rayAdd=false;//закончить построение луча
            //Связать точки с лучом
            model.rayBindCircles(poindLine1,poindLine2,newLine );
            //Заменить имя
            model.findNameId(poindLine1.getId(),poindLine2.getId(),newLine.getId());
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();

        }
         //Добавление прямой
        if (lineAdd==true && poindAdd1==false){
            addLineRayStart(2);//Создание первой точки и линии
        }
        if (lineAdd==true && poindAdd1==true){
            addLineRayEnd();
            //Связать точки с прямой
            model.circlesBindLine(poindLine1, poindLine2, newLine);
            //Заменить имя
            model.findNameId(poindLine1.getId(),poindLine2.getId(),newLine.getId());
            lineAdd=false;
            //Вывод информации об объектах в правую часть доски
           // model.setCol(infoStatus);
            model.setTxtShape("");
            model.txtAreaOutput();
        }

        //Добавление медианы
        if(medianaAdd){
            poindLine1=model.getTimeVer();
            model.medianaAdd(poindLine1);
            medianaAdd=false;
            model.setTxtShape("");
            model.txtAreaOutput();
        }


        mouseEvent.consume();
        }//End onMousePressed()

    /**
     * Метод addLineRayStart(int Segment)
     * Метод начала построения отрезков, лучей и прямых.
     * Вход: номер геометрической фигуры, 0-отрезок, 1-луч, 2 прямая,3 треугольник
     * @param  Segment
     */
    public void addLineRayStart(int Segment){
        if (model.isPoindOldAdd() == false) {//false - новая вершина true - взять имеющую
            poindLine1 = model.createPoindAdd(true);//создать новую вершину
            paneShape.getChildren().add(poindLine1);
            model.indexAdd(poindLine1);//увеличить индекс
         } else {
            poindLine1=model.getTimeVer();
            model.indexAdd(poindLine1);//увеличить индекс
        }

        newLine = model.createLineAdd(Segment);//создать линию
        //Сохранить первую точку треугольника и первую сторону
        if (treangleAdd==true && poindAdd2==false){
            poindTreangle1=poindLine1;
            lineTriangle1=newLine;//
        }
        paneShape.getChildren().add(newLine);//добавить на доску
        newLine.toBack();//переместить линию вниз под точку
        poindAdd2 = true;//режим добавления второй точки и последующих
    }

    /**
     * Метод addLineRayEnd()
     * Метод окончания построения геометрической фигуры.
     * Закравает режим добавления фигур.
     */
    //Метод окончания добавления фигур
    public void addLineRayEnd(){
        if (model.isPoindOldAdd() == false) {
            poindLine2 = model.createPoindAdd(true);//создать новую
            paneShape.getChildren().add(poindLine2);
            model.indexAdd(poindLine2);//увеличить индекс
        }else {
            poindLine2=model.getTimeVer();
            model.indexAdd(poindLine1);//увеличить индекс
        }
        //закрыть режим добавления
        poindAdd1 = false;//закрыть 1 точку
        poindAdd2 = false;//закрыть 2 точку
        model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
     }



    /**
     * Метод onScroll(ScrollEvent scrollEvent)
     * Метод изменения масштаба координатной сетки при вращении колесика мышки
     * @param scrollEvent
     */
     public void onScroll(ScrollEvent scrollEvent) {
        double sc=scrollEvent.getDeltaY();
        gridViews.onScrollView(sc);
        updateShape();
    }

    /**
     * Метод updateShape()
     * Метод перемещения всех геоиетрических объектов на доске при
     * перемещении координатной сетки. Для перемещения используются мировые координаты фигур
     * из коллекций.
     */
    public void updateShape() {
        //обновление точек
        for (PoindCircle p : model.getPoindCircles())
            if (p != null) {
                Circle c = p.getCircle();
                c.setCenterX(gridViews.accessX(p.getX()));
                c.setCenterY(gridViews.accessY(p.getY()));
            }
        //Обновление всех линий
        for (PoindLine pl : model.getPoindLines()){
            if(pl!=null){
                //Обновляем отрезки
                    Line l = pl.getLine();
                    l.setStartX(gridViews.accessX(pl.getStX()));
                    l.setStartY(gridViews.accessY(pl.getStY()));
                    l.setEndX(gridViews.accessX(pl.getEnX()));
                    l.setEndY(gridViews.accessY(pl.getEnY()));
            }
        }
        //Обновление дуг углов
        for (VertexArc va: model.getVertexArcs()){
            if(va!=null){
                Arc a=va.getArc();
                a.setCenterX(gridViews.accessX(va.getCenterX()));
                a.setCenterY(gridViews.accessY(va.getCenterY()));
                a.setRadiusX(va.getRadiusX());
                a.setRadiusY(va.getRadiusY());
                a.setStartAngle(va.getStartAngle());
                a.setLength(va.getLengthAngle());

            }
        }

    }
    //Нажата кнопка меню "Точка"
    public void menuPoindClick() {
        //Вывод информации о геометрических фигурах
        //Сюда добавить код
        //Установка режима добавления точек
        //Установить статус
        model.setStringLeftStatus(STA_6);
        model.statusGo(leftStatus);
        poindAdd=true;//Установить режим добавления
    }
    //Нажата кнопка меню "Отрезок"
    public void menuLineClick() {
        //Вывод информации об отрезах
        //Сюда добавить код
        //Установка режима добавления отрезков
        //Установить статус
        model.setStringLeftStatus(STA_7);
        model.statusGo(leftStatus);
        segmentAdd =true;
    }
    /**
     * Метод menuShowPoindName()
     * Предназначен для замены логической переменной, показывать большие буквы в именах точек.
     * Пункт из меню "Настройки"
     */
    public void menuShowPoindName() {
            visibleNamePoind(menuShowPoindName.isSelected());
            model.setShowPoindName(menuShowPoindName.isSelected());
        }

    /**
     * Метод visibleNamePoind(boolean bName).
     * Предназначен для скрытия от показа или показа имен точек на доске
     * @param bName -логическая переменная определяется в меню "Настроки->Показывать большие буквы в именах"
     */
    private void visibleNamePoind(boolean bName){
        for (NamePoindLine pn: model.getNamePoindLines()){
            if(pn!=null){
                pn.getText().setVisible(bName);
                pn.setVisiblePoind(bName);
            }
        }
    }

    /**
     * Метод menuShowLineName().
     * Предназначен скрытия от показа имен прямых, лучей, отрезков.
     * Пункт из меню "Настройки"
     */
    public void menuShowLineName() {
        visibleNameLine(menuShowLineName.isSelected());
        model.setShowPoindName(menuShowLineName.isSelected());
    }

    /**
     * Метод visibleNameLine(boolean bName).
     * Предназначен скрытия от показа имен прямых, лучей, отрезков.
     * @param bName - логическая переменная определяется в меню "Настроки->Показывать маленькие буквы в именах"
     */
    private void visibleNameLine(boolean bName){
        for (NamePoindLine pn: model.getNamePoindLines()){
            if(pn!=null){
                pn.getText().setVisible(bName);
                pn.setVisibleLine(bName);
            }
        }
    }

    /**
     * Метод menuCartesian().
     * Предназначен для показа или скрытия координатных осей
     */
    public void menuCartesian() {
        model.setShowCartesian(menuCartesian.isSelected());
    }

    /**
     * Метод  menuGrid().
     * Предназначен для показа или скрытия сетки
     */
    public void menuGrid() {
        model.setShowGrid(menuGrid.isSelected());
        if (model.isShowGrid()) {
            gridViews.gridCartesian();//Вывод сетки
        }else{
            paneGrid.getChildren().clear();//Очистить экран и память

        }
    }

    //Нажата кнопка меню "Признаки равнобедренного треугольника
    public void btnIsosceles(ActionEvent actionEvent) {
        model.webViewLeftString(webViewLeft, 1);
    }


    //Всплывающие подсказки при наведении на кнопку "Точка"
    public void onMouseEnteredPoind() {
        tooltip.setText("Добавить точку");
        btnPoind.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Отрезок"
    public void onMoseEnteredSegment() {
        tooltip.setText("Добавить отрезок");
        btnSegment.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Луч"
    public void onMouseEnteredRay() {
        tooltip.setText("Добавить луч");
        btnRay.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Прямая"
    public void onMouseEnteredLine() {
        tooltip.setText("Добавить прямую");
        btnLine.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Треугольник"
    public void onMouseEnteredTreangle() {
         tooltip.setText("Добавить треугольник");
         btnTreangle.setTooltip(tooltip);
    }
    public void onMouseEnteredMediana() {
        tooltip.setText("Добавить медиану");
        btnMediana.setTooltip(tooltip);
    }
    /**
     * Метод onMouseEnteredDelete()
     * Всплывающая подсказака при наведении на кнопку "Удалить"
     */
    public void onMouseEnteredDelete() {
        tooltip.setText("Удалить геометричекую фигуру");
        btnDelete.setTooltip(tooltip);
    }
   
    //Тестовая кнопка вывод информации по всем коолекциям для тестирования системы
    public void btnTest() {
        model.ColTest();
    }


    //первый признак равенства треугольников
    public void onClickEquil(ActionEvent actionEvent) {
        model.setWindShow(0);
        TwofxmlLoader();
    }

    //втророй признак равенства треугольников
    public void onClickSecond(ActionEvent actionEvent) {
        model.setWindShow(1);
        TwofxmlLoader();
    }

    //третий признак равенства треугольников
    public void onClickTread(ActionEvent actionEvent) {
        model.setWindShow(2);
        TwofxmlLoader();
    }
    //первый признак подобия треугольников
    public void onClickEquilPod(ActionEvent actionEvent) {

    }
    //втророй подобия равенства треугольников
    public void onClickSecondPod(ActionEvent actionEvent) {

    }
    //третий подобия равенства треугольников
    public void onClickTreadPod(ActionEvent actionEvent) {
    }
//Загрузка шаблона окна для признаков равенства треугольников
    public void TwofxmlLoader() {
        try {
            Parent root1 = FXMLLoader.load(getClass().getResource("equality.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Признаки равенства треугольников");
            stage.setResizable(false);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//Нажата кнопка меню "О программе"
    public void onAbout() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);//Блокирует все окна приложения
        window.initStyle(StageStyle.UTILITY);//Только кнопка закрыть
        VBox root=new VBox();
        root.setStyle(
                "-fx-background-image: url(/Images/About.png); " +
                        "-fx-background-repeat: no-repeat;"
        );
        root.setAlignment(Pos.TOP_CENTER);

        Label label2=new Label("МБОУ \"Центр образования Опочецкого района\"\nСтруктурное подразделение \"Средняя школа № 4\"\n\n\n ");
        label2.setFont(Font.font("Verdana", FontWeight.BOLD,24.0));
        label2.setTextFill(Color.SANDYBROWN);
        label2.setTextAlignment(TextAlignment.CENTER);
        Label label=new Label("Учебно-справочное пособие");
        label.setFont(Font.font("Verdana", FontWeight.BOLD,34.0));
        label.setTextFill(Color.YELLOW);
        label.setTextAlignment(TextAlignment.CENTER);
        Label label1=new Label("Геометрия\n\n");
        label1.setFont(Font.font("Verdana", FontWeight.BOLD,58.0));
        label1.setTextFill(Color.YELLOW);
        label1.setTextAlignment(TextAlignment.CENTER);
        Label label3=new Label("Выполнил ученик 8Б класса \n Носов Алексей \n2021 г.");
        label3.setFont(Font.font("Verdana", FontWeight.BOLD,24.0));
        label3.setTextFill(Color.YELLOW);
        label3.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().addAll(label2,label,label1,label3);
        Scene scene = new Scene(root, 864, 489);
        window.setScene(scene);
        window.setTitle("О программе");
        window.show();

    }





}

