package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.val;


public class  Controller extends View {

    @FXML
    public Pane paneShape;//контейнер для геометрических фигур
    public StackPane Cartesian;//контейнер для декартовых координат
    @FXML
    private Button btnPoind;//кнопка добавить точку
    @FXML
    private Button btnSegment;
    @FXML
    private Button btnRay;
    @FXML
    private Button btnLine;
    @FXML
    //Web браузер для вывода данных
    public WebView webViewLeft;
    @FXML
    private Pane paneGrid;//контейнер для сетки
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Line nl;//отрезок
    private Line ray;//тестовый луч
    private String poindLine1;//первая  точка луча, прямой, отрезка
    private String poindLine2;//вторая точка луча, прямой, отрезка


    private boolean poindAdd=false;//true - создать точку
    private boolean segmentAdd =false;//true - создать отрезок
    private boolean rayAdd=false;//true - создание луча
    private boolean lineAdd=false;//true - создание прямой

    private boolean poindAdd1=false;//true - создание первой точки для отрезка
    private boolean poindAdd2=false;//true - создание второй точки для отрезка

    private String infoStatus;//строка для коллекции, типа А - точка, АаВ - точка, отрезок, точка

    private  Tooltip tooltip=new Tooltip()  ;



    //Инициализация контролера
    @FXML
    private void initialize() {

        model.setLeftStatus(leftStatus);//Передать ссылку на статус
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
            gridViews.gridCartesian();//Вывод сетки
            updateShape();//обновить координаты геометрических фигур
        });
        //Изменение высоты окна
        Cartesian.heightProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVb(Cartesian.getHeight());
            gridViews.setWt(Cartesian.getHeight() / 2);
            gridViews.setWb(-Cartesian.getHeight() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            gridViews.gridCartesian();//Вывод сетки
            updateShape();//обновить координаты геометрических фигур
        });
        gridViews.gridCartesian();//вывод на доску
        //Настройки всплывающих подсказок
        tooltip.setShowDelay(Duration.millis(10.0));
        tooltip.setFont(Font.font(12));
        tooltip.setStyle("-fx-background-color: LIGHTBLUE;" +
                         "-fx-text-fill: black");
    }


    //Нажата кнопкп "Добавить точку"
    public void btnPoindClick() {
        leftStatus.setText("Укажите на доске место для точки");//Установить статус
        poindAdd=true;//Установить режим добавления
    }
    //Нажата кнока "Добавить отрезок"
    public void btnSegmentClick() {
        leftStatus.setText("Укажите на доске начало и конец отрезка");//Установить статус
        segmentAdd =true;
        infoStatus ="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
    }
    //Нажата кнопка "Добавить Луч"
    public void btnRay(ActionEvent actionEvent) {
        leftStatus.setText("Укажите на доске точку начала луча");//Установить статус
        rayAdd=true;
        infoStatus="";//Для коллекции Col
    }
    //Нажата кнопка "Добавить прямую"
    public void btnLine() {
        leftStatus.setText("Укажите на доске точку начала прямой");//Установить статус
        lineAdd=true;
        infoStatus="";//Для коллекции Col
    }

    //Кропка треугольники
    public void btnTreanle(ActionEvent actionEvent) {
        leftStatus.setText("Укажите на доске три точки для построения ьреугольника");//Установить статус
        lineAdd=true;
        infoStatus="";//Для коллекции Col
        model.webViewLeftString(webViewLeft, 0);//Определения
      //  model.webViewLeftString(webViewLeft, 10);//Определения остроугольного треугольника

    }

    //Перемещение мыши без нажатия кнопки по доске
    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        model.setVerX0(gridViews.revAccessX(mouseEvent.getX()));
        model.setVerY0(gridViews.revAccessY(mouseEvent.getY()));
        rightStatus.setText("x "+mouseEvent.getX()+" y "+mouseEvent.getY()  +" Координаты доски x: " + gridViews.revAccessX(mouseEvent.getX()) + " y: " + gridViews.revAccessY(mouseEvent.getY()));
        //Создание отрезка
        if (segmentAdd ==true && nl!=null && poindAdd2==true){
            poindSetRevAccess(nl);
            model.SideGo(nl);//проводим отрезок
            poindAdd1 = true;//первая точка создана
        }
        //Вторая точка, если растояние до неё меньше 15px, конец отрезка
        //переходит на точку. Щелчок мышкой, вторая точка выбрана заданная
        model.lineAddPoind(nl,poindAdd2);

        //Создание луча
        if(rayAdd==true && nl!=null && poindAdd2==true){
            //Расчитать координаты окончагия луча
            double x=model.getRayEndX()+(model.getVerX()-model.getRayEndX())*3;
            double y=model.getRayEndY()+(model.getVerY()-model.getRayEndY())*3;
            //Добавить коордитаны пересчета в коллекцию
            model.setRayStartX(x);
            model.setRayStartY(y);
           // poindSetRevAccess(nl);
            model.setVerLineStartX(gridViews.revAccessX(model.getRayEndX()));
            model.setVerLineStartY(gridViews.revAccessY(model.getRayEndY()));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));
            model.findPoindLines(nl.getId());
            model.RayGo(nl);//проводим отрезок
            poindAdd1 = true;//первая точка создана
        }
        //Создание прямой
        if (lineAdd==true && nl!=null && poindAdd2==true){
            Circle c=model.findCircle(poindLine1);//первая точка
            //расчитать концов прямой по уравнению прямой
            double x=c.getCenterX()+(model.getVerX()-c.getCenterX())*3;
            double y=c.getCenterY()+(model.getVerY()-c.getCenterY())*3;
            double x1=c.getCenterX()+(model.getVerX()-c.getCenterX())*-3;
            double y1=c.getCenterY()+(model.getVerY()-c.getCenterY())*-3;

            //Добавить коордитаны пересчета в коллекцию
            model.setVerLineStartX(gridViews.revAccessX(x1));
            model.setVerLineStartY(gridViews.revAccessY(y1));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));
            model.findPoindLines(nl.getId());
           //задать координаты прямой
            model.setRayStartX(x1);
            model.setRayStartY(y1);
            model.setRayEndX(x);
            model.setRayEndY(y);
            //проводим прямую
            model.RayGo(nl);
            poindAdd1 = true;//разрешение для постройки 2 точки
        }
    }
    //Добавить перерасчетные координаты
    public void poindSetRevAccess(Line o){
        model.setVerLineStartX(gridViews.revAccessX(o.getStartX()));
        model.setVerLineStartY(gridViews.revAccessY(o.getStartY()));
        model.findPoindLines1(nl.getId());
    }

    //Перемещение мыши с нажатой кнопкой
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
            gridViews.gridCartesian();//Вывод сетки
            updateShape();
        }
        mouseEvent.consume();
    }

    //Нажата кнопка мыши на доске
    public void onMousePressed(MouseEvent mouseEvent) {
        // Фиксируем точку нажатия кнопки мыши
        if(mouseEvent.getTarget()==paneShape) {
            gridViews.setVPx(mouseEvent.getX());
            gridViews.setVPy(mouseEvent.getY());
        }
        //Добавление точки
        if (poindAdd) {
            String  d = model.createPoindAdd(paneShape);
            model.setCol(String.valueOf(d));
            poindAdd = false;
        }
        //Добавление отрезка
        if (segmentAdd ==true && poindAdd1==false) {
            addLineRayStart(0);//Создание первой точки и линии
        }
        //Вторая точка для отрезка
        if (segmentAdd == true && poindAdd1 == true) {
              addLineRayEnd();
              segmentAdd = false;//окончание режима добавления
            }
        //Добавления луча
        if (rayAdd==true && poindAdd1==false) {
              addLineRayStart(1);
        }
        //Втрорая точка луча
        if(rayAdd==true && poindAdd1==true) {
              addLineRayEnd();
              rayAdd=false;
        }
         //Добавление прямой
        if (lineAdd==true && poindAdd1==false){
            addLineRayStart(2);//Создание первой точки и линии
        }
        if (lineAdd==true && poindAdd1==true){
            addLineRayEnd();
            lineAdd=false;
        }
        mouseEvent.consume();
        }//End onMousePressed()

    //Добавление отрезков, лучей, прямых
    public void addLineRayStart(int Segment){
        if (model.isPoindOldAdd() == false) {//false - новая вершина true - взять имеющую
            poindLine1 = model.createPoindAdd(paneShape);//создать новую
            infoStatus = String.valueOf(poindLine1);//добавить вершину в список
        } else {
            infoStatus = infoStatus +model.getTimeVer();//Вершина из временной переменной
            poindLine1=model.getTimeVer();
        }
        nl = model.createLineAdd(paneShape,Segment);//создать линию
        infoStatus = infoStatus + nl.getId();//Добавить линию в список
        poindAdd2 = true;//режим добавления второй точки
    }
    public void addLineRayEnd(){
        if (model.isPoindOldAdd() == false) {
            poindLine2 = model.createPoindAdd(paneShape);//создать новую
            infoStatus = infoStatus + String.valueOf(poindLine2);//добавить вторую вершину(получтся типа AaB)
        }else {
            infoStatus = infoStatus +model.getTimeVer();
            poindLine2=model.getTimeVer();
        }
        //закрыть режим добавления отрезка
        model.setCol(infoStatus);
        poindAdd1 = false;//закрыть 1 точку
        poindAdd2 = false;//закрыть 2 точку
        model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
    }

    // Изменение масштаба координатной сетки
    public void onScroll(ScrollEvent scrollEvent) {
        double sc=scrollEvent.getDeltaY();
        gridViews.onScrollView(sc);
        updateShape();
    }



    //Обновление всех точек
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
                //if(pl.getSegment()==0) {
                    Line l = pl.getLine();
                    l.setStartX(gridViews.accessX(pl.getStX()));
                    l.setStartY(gridViews.accessY(pl.getStY()));
                    l.setEndX(gridViews.accessX(pl.getEnX()));
                    l.setEndY(gridViews.accessY(pl.getEnY()));
                //}
                //обновляем лучи

            }
        }

    }
    //Нажата кнопка меню "Точка"
    public void menuPoindClick() {
        //Вывод информации о геометрических фигурах
        //Сюда добавить код
        //Установка режима добавления точек
        leftStatus.setText("Укажите на доске место для точки");//Установить статус
        poindAdd=true;//Установить режим добавления
    }
    //Нажата кнопка меню "Отрезок"
    public void menuLineClick() {
        //Вывод информации об отрезах
        //Сюда добавить код
        //Установка режима добавления отрезков
        leftStatus.setText("Укажите на доске начало и конец отрезка");//Установить статус
        segmentAdd =true;
        infoStatus ="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
    }
    //Всплывающие подсказки при наведении на кнопку "Точка"
    public void onMouseEnteredPoind() {
        tooltip.setText("Добавить точку");
        btnPoind.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Отрезок"
    public void onMoseEnteredSegment(MouseEvent mouseEvent) {
        tooltip.setText("Добавить отрезок");
        btnSegment.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Луч"
    public void onMouseEnteredRay() {
        tooltip.setText("Добавить луч");
        btnRay.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Прямая"
    public void onMouseEnteredLine(MouseEvent mouseEvent) {
        tooltip.setText("Добавить прямую");
        btnLine.setTooltip(tooltip);
    }

    //Тестовая кнопка вывод информации по всем коолекциям для тестирования системы
    public void btnTest() {
        model.ColTest();
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

//Нажата кнопка меню "Признаки равнобедренного треугольника
    public void btnIsosceles(ActionEvent actionEvent) {
        model.webViewLeftString(webViewLeft, 1);
    }
//Пункт меню "О программе"
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

