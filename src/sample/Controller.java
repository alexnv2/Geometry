package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;




public class  Controller extends View {

    @FXML
    public Pane paneShape;
    public StackPane Cartesian;
    @FXML
    private Pane paneGrid;
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Line nl;//отрезок
    private boolean poindAdd2=false;//true - создание второй точки для отрезка
    private boolean lineAdd=false;//true - создать отрезок
    private  boolean poindAdd=false;//true - создать точку
    private  boolean poindAdd1=false;//true - создание первой точки для отрезка
    String sr;


    //Инициализация контролера
    @FXML
    private void initialize() {

        model.setLeftStatus(leftStatus);//Передать ссылку на статус

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

        });
        //Изменение высоты окна
        Cartesian.heightProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVb(Cartesian.getHeight());
            gridViews.setWt(Cartesian.getHeight() / 2);
            gridViews.setWb(-Cartesian.getHeight() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            gridViews.gridCartesian();//Вывод сетки

        });
    }


    //Создание новой точки
    public void btnPoindClick() {
        leftStatus.setText("Укажите на доске место для точки");//Установить статус
        poindAdd=true;//Установить режим добавления
    }
    //Создаем отрезок
    public void btnLineClick() {
        leftStatus.setText("Укажите на доске начало и конец отрезка");//Установить статус
        lineAdd=true;
        sr="";//названия отрезка для коллекции(AaB -А-первая точка а - отрезок В - вторая точка)
    }
    //Перемещение мыши без нажатия кнопки по доске
    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        rightStatus.setText("Координаты доски x: " + mouseEvent.getX() + " y: " + mouseEvent.getY());
        //координаты для создания отрезка
        if (lineAdd==true && nl!=null && poindAdd2==true){
            model.SideGo(nl);//проводим отрезок
            poindAdd1 = true;//первая точка создана

        }
        //Вторая точка, если растояние до неё меньше 15px, конец отрезка
        //переходит на точку. Щелчок мышкой, вторая точка выбрана заданная
        model.lineAddPoind(nl,poindAdd2);
    }



    //Перемещение мыши с нажатой кнопкой
    public void onMouseDraggen(MouseEvent mouseEvent) {
        //координаты, нужны для перемещения объектов на доске
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
    }

    //Нажата кнопка мыши на доске
    public void onMousePressed(MouseEvent mouseEvent) {

        //Добавление точки
        if (poindAdd) {
            char d = model.createPoindAdd(paneShape);
            model.setCol(String.valueOf(d));
            poindAdd = false;
        }
        //Добавление отрезка
        if (lineAdd==true && poindAdd1==false) {
            if (model.isPoindOldAdd() == false) {//false - новая вершина true - взять имеющую
                char d1 = model.createPoindAdd(paneShape);
                sr = String.valueOf(d1);//добавить вершину в список
            } else {
                sr=sr+model.getTimeVer();//Вершина из временной переменной
            }
            nl = model.createLineAdd(paneShape);//создать линию
            sr = sr + nl.getId();//Добавить динию в список
            poindAdd2 = true;//режим добавления второй точки
        }
            //Вторая точка для отрезка
            if (poindAdd1 == true && lineAdd == true) {
               if (model.isPoindOldAdd() == false) {
                    char d2 = model.createPoindAdd(paneShape);
                    sr = sr + String.valueOf(d2);//добавить вторую вершину(получтся типа AaB)
               }else {
                    sr=sr+model.getTimeVer();
                }
                //закрыть режим добавления отрезка
                model.setCol(sr);
                lineAdd = false;//окончание режима добавления
                poindAdd1 = false;//закрыть 1 точку
                poindAdd2 = false;//закрыть 2 точку
                model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
            }
        }//End onMousePressed()



    public void onScroll(ScrollEvent scrollEvent) {
    }
}

