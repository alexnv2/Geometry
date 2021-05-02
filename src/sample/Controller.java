package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.LinkedList;


public class  Controller extends View {

    @FXML
    public AnchorPane apView;//Доска для геометрический фигур
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Line nl;//отрезок
    private boolean poindAdd1 =false;//true - создание первой точки для отрезка
    private boolean poindAdd2=false;//true - создание второй точки для отрезка
    private boolean lineAdd=false;//true - создать отрезок
    private  boolean poindAdd=false;//true - создать точку
    private LinkedList<Circle> circles=new LinkedList<>();//коллекция для точек
    private LinkedList<Line> lines=new LinkedList<>();//коллекция для линий


    //Инициализация контролера
    @FXML
    private void initialize() {
        model.setLeftStatus(leftStatus);//Передать ссылку на статус
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
    }
    //Перемещение мыши без нажатия кнопки по доске
    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        rightStatus.setText("Координаты доски x: " + mouseEvent.getX() + " y: " + mouseEvent.getY());
        //координаты для создания отрезка
        if (lineAdd && nl!=null && poindAdd2){
           model.SideGo(nl);//проводим отрезок
           poindAdd1 =true;//первая точка создана
        }
        //Вторая точка, если растояние до неё меньше 15px, конец отрезка
        //переходит на точку. Щелчок мышкой, вторая точка выбрана заданная
        for(Circle c: circles){
           if(c!=null && nl!=null && poindAdd2) {
              double d=model.distance(c.getCenterX(),c.getCenterY(),mouseEvent.getX(),mouseEvent.getY());
           if (d<15){
               model.setPoindOldAdd(true);
               model.setVerX(c.getCenterX());
               model.setVerY(c.getCenterY());
               model.SideGo(nl);
              }else {
               model.setPoindOldAdd(false);
           }

           }
        }
    }


    public void onMouseClick(MouseEvent mouseEvent) {
    }

    public void onMouseEntered(MouseEvent mouseEvent) {
    }
    //Перемещение мыши с нажатой кнопкой
    public void onMouseDragged(MouseEvent mouseEvent) {
        //координаты, нужны для перемещения объектов на доске
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
    }

    //Нажата кнопка мыши на доске
    public void onMousePressed(MouseEvent mouseEvent) {

        //Добавление точки
        //точка
        Circle cl;
        if (poindAdd == true) {
            cl = model.createPoind(apView);//Создать
            apView.getChildren().add(cl);//добавить
            circles.add(cl);
            model.VertexGo(cl);//куда добавить
            //Увеличить индекс
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            poindAdd = false;

            }
        //Добавление отрезка
        if (lineAdd == true && poindAdd1 ==false) {
            if(model.isPoindOldAdd()==false) {
                cl = model.createPoind(apView);//Создать первую точку
                circles.add(cl);//добавить в коллекцию
                apView.getChildren().add(cl);//добавить на доску
                model.VertexGo(cl);//куда добавить
                model.setIndexPoind((char) (model.getIndexPoind() + 1)); //Увеличить индекс
            }
            nl = model.createLine(apView);//добавить линию
            apView.getChildren().add(nl);//добавить на доску
            lines.add(nl);//добавить в коллекцию
            model.setIndexLine((char) (model.getIndexLine() + 1));//увеличить индекс
            poindAdd2=true;//режим добавления второй точки

        }
        //Вторая точка для отрезка
        if(poindAdd1 ==true && lineAdd==true){
            if(model.isPoindOldAdd()==false) {
                cl = model.createPoind(apView);//Создать точку
                apView.getChildren().add(cl);//добавить на доску
                circles.add(cl);
                model.VertexGo(cl);//куда добавить
                //Увеличить индекс
                model.setIndexPoind((char) (model.getIndexPoind() + 1));
            }
            //закрыть режим добавления
            lineAdd=false;
            poindAdd1 =false;
            poindAdd2=false;
            model.setPoindOldAdd(false);
        }
    }
}
