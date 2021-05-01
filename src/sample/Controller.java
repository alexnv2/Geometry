package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class  Controller extends View {

    @FXML
    public AnchorPane apView;//Доска для геометрический фигур
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Circle cl;//точка
    private Line nl;//отрезок
    private boolean linePoind2=false;//true - для завершения создания второй точки
    private boolean poindAdd2=false;//true - создать вторую точку для отрезка
    private boolean lineAdd=false;//true - создать отрезок
    private  boolean poindAdd=false;//true - создать точку


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
        if (lineAdd && nl!=null && poindAdd2){
           model.SideGo(nl);
           linePoind2=true;
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
        if (poindAdd == true) {
            cl = model.createPoind(apView);//Создать
            apView.getChildren().add(cl);//добавить
            model.VertexGo(cl);//куда добавить
            //Увеличить индекс
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            poindAdd = false;
            }
        //Добавление линии
        if (lineAdd == true && linePoind2==false) {
            cl = model.createPoind(apView);//Создать
            apView.getChildren().add(cl);//добавить на доску
            model.VertexGo(cl);//куда добавить
            //Увеличить индекс
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            nl = model.createLine(apView);//добавить линию
            apView.getChildren().add(nl);//добавить на доску
            model.setIndexLine((char) (model.getIndexLine() + 1));//увеличить индекс
            poindAdd2=true;//режим добавления второй точки
        }
        //Вторая точка для отрезка
        if(linePoind2==true && lineAdd==true){
            cl = model.createPoind(apView);//Создать точку
            apView.getChildren().add(cl);//добавить на доску
            model.VertexGo(cl);//куда добавить
            //Увеличить индекс
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            //закрыть режим добавления
            lineAdd=false;
            linePoind2=false;
            poindAdd2=false;
        }
    }
}
