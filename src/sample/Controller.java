package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
import static ContstantString.StringStatus.*;

/*
Класс управления приложением (Controller)
Реагирует на все события управления от мыши и клавиатуры.
Вызывает методы из класса модели для обработки событий
 */
public class  Controller extends View {

    //Связать переменные с шаблоном FXML
    @FXML
    public Pane paneShape;//контейнер для геометрических фигур
    public StackPane Cartesian;//контейнер для декартовых координат
    public TextArea txtShape;//контейнер для правой части доски
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
    //Web браузер для вывода данных
    public WebView webViewLeft;//для размещения информации слева от доски
    @FXML
    private Pane paneGrid;//контейнер для сетки
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Line nl;//отрезок
    private Line ray;//тестовый луч
    private String poindLine1;//первая  точка луча, прямой, отрезка
    private String poindLine2;//вторая точка луча, прямой, отрезка

    //Ркжимы создания
    private boolean poindAdd=false;//true - создать точку
    private boolean segmentAdd =false;//true - создать отрезок
    private boolean rayAdd=false;//true - создание луча
    private boolean lineAdd=false;//true - создание прямой
    private boolean angleAdd=false;//true -создание угла
    private int angleCol=0;//индекс счета углов

    private boolean poindAdd1=false;//true - создание первой точки для отрезка
    private boolean poindAdd2=false;//true - создание второй точки для отрезка
    private boolean poindAdd3=false;//true - создание третьей точки для угла

    private String infoStatus;//строка для коллекции, типа А - точка, АаВ - точка, отрезок, точка
    //Для всплывающих подсказок
    private final Tooltip tooltip=new Tooltip()  ;

    //Инициализация контролера
    @FXML
    private void initialize() {
        model.setStatus(leftStatus);//Передать ссылку на статус для модели
        model.setTextArea(txtShape);//Передать ссылку фигуры
        model.setGridViews(gridViews);//Передать ссылку для пересчета координат для Model
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
        //Добавить изображение к кнопкам
        btnPoind.setStyle("-fx-background-image: url(/Images/point.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnSegment.setStyle("-fx-background-image: url(/Images/point&line.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnAngle.setStyle("-fx-background-image: url(/Images/angle.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnVertical.setStyle("-fx-background-image: url(/Images/vertical.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnDelete.setStyle("-fx-background-image: url(/Images/delete.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");
        btnLine.setStyle("-fx-background-image: url(/Images/line.png);" +"-fx-background-repeat: no-repeat;"+"-fx-background-position:center center");

    }


    //Нажата кнопкп "Добавить точку"
    public void btnPoindClick() {
        //Установить статус
        model.setStringLeftStatus(STA_1);
        model.statusGo(leftStatus);//Установить статус
        poindAdd=true;//Установить режим добавления

    }
    //Нажата кнока "Добавить отрезок"
    public void btnSegmentClick() {
        //Установить статус
        model.setStringLeftStatus(STA_2);
        model.statusGo(leftStatus);
        segmentAdd =true;
        infoStatus ="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
    }
    //Нажата кнопка "Добавить Луч"
    public void btnRay(ActionEvent actionEvent) {
        //Установить статус
        model.setStringLeftStatus(STA_3);
        model.statusGo(leftStatus);
        rayAdd=true;
        infoStatus="";//Для коллекции Col
    }
    //Нажата кнопка "Добавить прямую"
    public void btnLine() {
        //Установить статус
        model.setStringLeftStatus(STA_4);
        model.statusGo(leftStatus);
        lineAdd=true;
        infoStatus="";//Для коллекции Col
    }
    //Режим добавления угла
    public void btnAngle(ActionEvent actionEvent) {
        //Установить статус
        model.setStringLeftStatus(STA_14);
        model.statusGo(leftStatus);
        angleAdd=true;
        infoStatus="";//Для коллекции Col
    }

    //Кропка треугольники
    public void btnTreangle(ActionEvent actionEvent) {
        //Установить статус
        model.setStringLeftStatus(STA_5);
        model.statusGo(leftStatus);
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
       //Создание угла
        if(angleAdd==true && nl!=null && poindAdd2==true){
            poindSetRevAccess(nl);
            model.SideGo(nl);//проводим отрезок


            if(angleCol==1){
                poindAdd3=true;//третья точка создана
            }
        }



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
        // Фиксируем точку нажатия кнопки мыши для перемещения сетки и координатных осей
        if(mouseEvent.getTarget()==paneShape) {
            gridViews.setVPx(mouseEvent.getX());
            gridViews.setVPy(mouseEvent.getY());
        }
        //Добавление точки на доске
        if (poindAdd) {
            String newPoind=model.createPoindAdd(paneShape);
            poindAdd = false;//Режим добавления точки окончен
        }
        //Добавление отрезка
        if (segmentAdd ==true && poindAdd1==false) {
            addLineRayStart(0);//Создание первой точки и линии
        }
        //Вторая точка для отрезка
        if (segmentAdd == true && poindAdd1 == true) {
              addLineRayEnd();
              segmentAdd = false;//окончание режима добавления
              //Вывод информации об объектах в правую часть доски
              model.setTxtShape("");
              model.txtAreaOutput();
            }
        //Добавление угла первая и вторая точка
        if (angleAdd ==true && poindAdd1==false) {
            addLineRayStart(3);//Создание первой точки и линии
            angleCol+=1;
        }
        //Третья точка для угла
        if(angleAdd==true && poindAdd3==true){
            addLineRayEnd();
            angleAdd= false;//окончание режима добавления
            poindAdd3=false;
            angleCol=0;
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }

        //Добавления луча
        if (rayAdd==true && poindAdd1==false) {
            addLineRayStart(1);
        }
        //Втрорая точка луча
        if(rayAdd==true && poindAdd1==true) {
              addLineRayEnd();
              rayAdd=false;
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
            lineAdd=false;
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
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
        nl.toBack();//переместить линию вниз под точку
        infoStatus = infoStatus + nl.getId();//Добавить линию в список
        poindAdd2 = true;//режим добавления второй точки
    }
    //Метод окончания добавления фигур
    public void addLineRayEnd(){
        if (model.isPoindOldAdd() == false) {
            poindLine2 = model.createPoindAdd(paneShape);//создать новую
            infoStatus = infoStatus + poindLine2;//добавить вторую вершину(получтся типа AaB)
        }else {
            infoStatus = infoStatus +model.getTimeVer();
            poindLine2=model.getTimeVer();
        }
        //закрыть режим добавления
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
        infoStatus ="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
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
    public void onMouseEnteredLine(MouseEvent mouseEvent) {
        tooltip.setText("Добавить прямую");
        btnLine.setTooltip(tooltip);
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

