package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class  Controller extends View {

    @FXML
    public AnchorPane apView;
    public Label leftStatus;

    //Инициализация контролера
    @FXML
    private void initialize() {
        model.setLeftStatus(leftStatus);//Передать ссылку на статус
    }


    //Создание новой точки
    public void btnPoindClick() {
       leftStatus.setText("Добавление точки");//Установить статус
       model.createPoind(apView);//Передать контейнер
       model.setPoindAdd(true);//Установить режим добавления

    }
    //Создаем отрезок
    public void btnLineClick() {
        model.createLine(apView);
       
    }
    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());


    }


    public void onMouseClick(MouseEvent mouseEvent) {
    }

    public void onMouseEntered(MouseEvent mouseEvent) {
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
    }


}
