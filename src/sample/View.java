package sample;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

//Первый класс для вывода на экран
class View implements Observer{

    //Объявляем класс Model
    Model model=new Model();
    //Объявляем класс сетки с методами перемещения координатной сетки и масштабирования
    //Как расширение класса пересчетв мировых координат в в окна просмотра
    GridView gridViews=new GridView();
    //Конструктор класса отображения
    View() {
        //Регистрация слушателя в классе Model
        model.registerObserver(this);
     }
     //Переопределить метод из класса интерфейса
    @Override
    //Какую информацию надо вывести
    public  void  notification(String message){
        switch (message) {
            case "VertexGo" -> this.vertexGo(model.getVertex());//перемещение вершин
            case "SideGo" -> this.sideGo(model.getLine());//отрисовка сторон отрезков
            case "RayGo" ->this.rayGo(model.getLine());//для луча и прямой
            case "WebView"->this.webViewGo(model.getWebView());//Заполнение слева и внизу
            case "LeftStatusGo" ->this.statusGo(model.getStatus());//вывод статуса
            case "WebGo"->this.webGo(model.getWebView());//вывод файла HTML
            case "TextShapeGo"->this.textShapeGo(model.getTextArea());//для вывода в правое окно
          /*
            case "TextGo" -> this.TextGo(model.getTextGo());//буквы
            case "ColorGo" -> this.SrokeColor(model.getColorLine());//цвет
            case "ArcGo" -> this.arcGo(model.getArcGo());//дуги
            case "ArcColorGo"->this.ArcColor(model.getArcGo());
            case "TableGo"->this.tableGo(model.getMTableView());//таблица
            case "ToolTip"->this.toolTipGo(model.getOToolTip());//Подсказка

           */
        }
    }
    //Вывод в правую часть доски
    private void textShapeGo(TextArea txtArea) {
        txtArea.setText(model.getTxtShape());
    }


    /*
    Методы для вывода информации на экран
    Все данные расчитываются в класса модели (Model)
     */
    //Перемещение точек
    private void vertexGo(Circle ver){
        ver.setCenterX(model.getVerX());
        ver.setCenterY(model.getVerY());
    }
    //Перемещение отрезков
    private void sideGo(Line side){
        side.setStartX(model.getVerX());
        side.setStartY(model.getVerY());
        side.setEndX(model.getVerX1());
        side.setEndY(model.getVerY1());
    }
    //Перемещение луча и прямой
    private void rayGo(Line ray){
        ray.setStartX(model.getRayStartX());
        ray.setStartY(model.getRayStartY());
        ray.setEndX(model.getRayEndX());
        ray.setEndY(model.getRayEndY());
    }
    //Заполнение web страниц слева
    private void webViewGo(WebView webView) {
        webView.setContextMenuEnabled(false);
        WebEngine w=webView.getEngine();
        w.loadContent(model.getStringWebView());
    }
    //Загрузить файл в формате html и вывести слева
    private void webGo(WebView web){
        web.setContextMenuEnabled(false);
        WebEngine w=web.getEngine();
        w.load(model.getLeftHTML());
    }
    //Вывод в статусную строку
    private void statusGo(Label label){
        label.setText(model.getStringLeftStatus());
    }
}
