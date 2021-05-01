package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class  Controller extends View {

    @FXML
    public AnchorPane apView;
    public Label leftStatus;
    public Label rightStatus;
    private Circle cl;
    private Line nl;
    private boolean linePoind2=false;
    private boolean poindAdd2=false;

    //Инициализация контролера
    @FXML
    private void initialize() {
        model.setLeftStatus(leftStatus);//Передать ссылку на статус
    }


    //Создание новой точки
    public void btnPoindClick() {
        leftStatus.setText("Укажите место для добавления точки на доске");//Установить статус
        model.setPoindAdd(true);//Установить режим добавления

    }

    //Создаем отрезок
    public void btnLineClick() {
        leftStatus.setText("Укажите на доске начало и конец отрезка");//Установить статус
        model.setLineAdd(true);


    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        rightStatus.setText("Координаты доски x: " + mouseEvent.getX() + " y: " + mouseEvent.getY());
        if (model.lineAdd==true && nl!=null && poindAdd2==true){
            System.out.println("AddLine");
            model.SideGo(nl);
            linePoind2=true;
        }
    }


    public void onMouseClick(MouseEvent mouseEvent) {
    }

    public void onMouseEntered(MouseEvent mouseEvent) {
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
    }


    public void onMousePressed(MouseEvent mouseEvent) {
        //Добавление точки
        if (model.poindAdd == true) {
            cl = model.createPoind(apView);//Создать
            apView.getChildren().add(cl);//добавить
            model.VertexGo(cl);//куда добавить
            //Увеличить индекс
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            model.poindAdd = false;
            }
        //Добавление линии
        if (model.lineAdd == true && linePoind2==false) {
            cl = model.createPoind(apView);//Создать
            apView.getChildren().add(cl);//добавить
            model.VertexGo(cl);//куда добавить
            //Увеличить индекс
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            nl = model.createLine(apView);
            apView.getChildren().add(nl);//добавить
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            poindAdd2=true;

        }
        if(linePoind2==true && model.lineAdd==true){
            cl = model.createPoind(apView);//Создать
            apView.getChildren().add(cl);//добавить
            model.VertexGo(cl);//куда добавить
            //Увеличить индекс
            model.setIndexPoind((char) (model.getIndexPoind() + 1));
            model.lineAdd=false;
            linePoind2=false;
            poindAdd2=false;
        }
    }
}
