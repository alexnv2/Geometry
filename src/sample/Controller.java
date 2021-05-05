package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import lombok.val;


public class  Controller extends View {

    @FXML
    public Pane paneShape;//контейнер для геометрических фигур
    public StackPane Cartesian;//контейнер для декартовых координат
    @FXML
    private Button btnPoind;//кнопка добавить точку
    @FXML
    private Button btnLine;
    @FXML
    private Button btnRay;
    @FXML
    private Pane paneGrid;//контейнер для сетки
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Line nl;//отрезок
    private Line ray;//тестовый луч
    private String poindRay1;//вторая точка луча
    private String poindRay2;//вторая точка луча

    private boolean poindAdd2=false;//true - создание второй точки для отрезка
    private boolean lineAdd=false;//true - создать отрезок
    private boolean poindAdd=false;//true - создать точку
    private boolean poindAdd1=false;//true - создание первой точки для отрезка
    private String infoStatus;//строка для коллекции, типа А - точка, АаВ - точка, отрезок, точка
    private boolean rayAdd=false;//true - создание луча
    private boolean rayAdd1=false;//true - первая точка луча
    private boolean rayAdd2=false;//true - вторая точка луча
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
    public void btnLineClick() {
        leftStatus.setText("Укажите на доске начало и конец отрезка");//Установить статус
        lineAdd=true;
        infoStatus ="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
    }
    //Нажата кнопка "Добавить Луч"
    public void btnRay(ActionEvent actionEvent) {
        leftStatus.setText("Укажите на доске точку начала луча");//Установить статус
        rayAdd=true;
        infoStatus="";//Для коллекции Col
    }
    //Перемещение мыши без нажатия кнопки по доске
    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        model.setVerX0(gridViews.revAccessX(mouseEvent.getX()));
        model.setVerY0(gridViews.revAccessY(mouseEvent.getY()));
        rightStatus.setText("x "+mouseEvent.getX()+" y "+mouseEvent.getY()  +" Координаты доски x: " + gridViews.revAccessX(mouseEvent.getX()) + " y: " + gridViews.revAccessY(mouseEvent.getY()));
        //координаты для создания отрезка
        if (lineAdd==true && nl!=null && poindAdd2==true){
            model.SideGo(nl);//проводим отрезок
            poindAdd1 = true;//первая точка создана
        }
        //Вторая точка, если растояние до неё меньше 15px, конец отрезка
        //переходит на точку. Щелчок мышкой, вторая точка выбрана заданная
        model.lineAddPoind(nl,poindAdd2);

        //Создание луча
        if(rayAdd==true && ray!=null && rayAdd2==true){
            //Найти первую точку луча
            Circle c1=model.findCircle(poindRay1);
            //найти вторую точку луча
            Circle c2=model.findCircle(poindRay2);
            model.VertexGo(c2);
            //обновить координаты точки
            model.findPoindCircles(poindRay2);
            double x=c1.getCenterX()+(c2.getCenterX()-c1.getCenterX())*5;
            double y=c1.getCenterY()+(c2.getCenterY()-c1.getCenterY())*5;
            //Добавить коордитаны пересчета в коллекцию
            model.setVerX01(gridViews.revAccessX(ray.getStartX()));
            model.setVerY01(gridViews.revAccessY(ray.getStartY()));
            model.findPoindLines1(ray.getId());
            model.setVerX(x);
            model.setVerY(y);
            model.SideGo(ray);//проводим отрезок
            rayAdd1 = true;//первая точка создана
        }
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
        if (lineAdd==true && poindAdd1==false) {
            if (model.isPoindOldAdd() == false) {//false - новая вершина true - взять имеющую
                String d1 = model.createPoindAdd(paneShape);//сщздать новую
                infoStatus = String.valueOf(d1);//добавить вершину в список
            } else {
                infoStatus = infoStatus +model.getTimeVer();//Вершина из временной переменной
            }
            nl = model.createLineAdd(paneShape);//создать линию
            //Пересчитать координаты старта линии для PoindLine
            model.setVerX01(gridViews.revAccessX(nl.getStartX()));
            model.setVerY01(gridViews.revAccessY(nl.getStartY()));
            infoStatus = infoStatus + nl.getId();//Добавить линию в список
            poindAdd2 = true;//режим добавления второй точки
        }
            //Вторая точка для отрезка
            if (lineAdd == true && poindAdd1 == true) {
               if (model.isPoindOldAdd() == false) {
                    String d2 = model.createPoindAdd(paneShape);//создать новую
                    infoStatus = infoStatus + String.valueOf(d2);//добавить вторую вершину(получтся типа AaB)
               }else {
                    infoStatus = infoStatus +model.getTimeVer();
                }
                //закрыть режим добавления отрезка
                model.setCol(infoStatus);
                model.findPoindLines(nl.getId());
                lineAdd = false;//окончание режима добавления
                poindAdd1 = false;//закрыть 1 точку
                poindAdd2 = false;//закрыть 2 точку
                model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
            }

        //Добавления луча
            if (rayAdd==true && rayAdd1==false) {
                if (model.isPoindOldAdd() == false) {//false - новая вершина true - взять имеющую
                    poindRay1 = model.createPoindAdd(paneShape);
                    infoStatus = String.valueOf(poindRay1);//добавить вершину в список
                } else {
                    infoStatus = infoStatus + model.getTimeVer();//Вершина из временной переменной
                    poindRay1=model.getTimeVer();
                }
                ray = model.createRayAdd(paneShape);//создать луч
                //Пересчитать координаты старта линии для PoindLine
                model.setVerX01(gridViews.revAccessX(ray.getStartX()));
                model.setVerY01(gridViews.revAccessY(ray.getStartY()));
                infoStatus = infoStatus + ray.getId();//Добавить линию в список
                //сразу создать вторую точку имя в d2
                poindRay2 = model.createPoindAdd(paneShape);
                model.setTimeVer(String.valueOf(poindRay2));//для временного хранения, передается в onMouseMoved()
                infoStatus = infoStatus + String.valueOf(poindRay2);//добавить вторую вершину(получтся типа AaB)
                model.setCol(infoStatus);
                rayAdd2=true;
            }
                if(rayAdd==true && rayAdd1==true) {
                    rayAdd = false;//закончили режим добавления луча
                    rayAdd1 = false;//закрыть 1 точку
                    rayAdd2=false;
                    model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
                }
        mouseEvent.consume();
        }//End onMousePressed()


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
        lineAdd=true;
        infoStatus ="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
    }
    //Всплывающие подсказки при наведении на кнопку "Точка"
    public void onMouseEnteredPoind() {
        tooltip.setText("Добавить точку");
        btnPoind.setTooltip(tooltip);
    }
    //Всплывающее окно при наведении на кнопку "Отрезок"
    public void onMoseEnteredLine(MouseEvent mouseEvent) {
        tooltip.setText("Добавить отрезок");
        btnLine.setTooltip(tooltip);
    }
    //МВсплывающее окно пр наведении на кнопку "Луч"
    public void onMouseEnteredRay() {
        tooltip.setText("Добавить луч");
        btnRay.setTooltip(tooltip);
        infoStatus ="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
    }

    //Тестовая кнопка вывод информации по всем коолекциям для тестирования системы
    public void btnTest() {
        model.ColTest();
    }

}

