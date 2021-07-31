package sample;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Класс View расширяет интерфейс Observer.
 * Базовый класс, предназначен отображения информации на экране.
 */
class View implements Observer{
    //Объявляем класс Model
    Model model=new Model();
    //Объявляем класс сетки с методами перемещения координатной сетки и масштабирования
    //Как расширение класса пересчета мировых координат в окне просмотра
    GridView gridViews=new GridView();
    //Конструктор класса отображения
    View() {
        //Регистрация слушателя в классе Model
        model.registerObserver(this);
     }
     //Переопределить метод из класса интерфейса Observer.
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
           // case "ColorGo" -> this.SrokeColor(model.getColorLine());//цвет
            case "ArcGo" -> this.arcGo(model.getArcGo());//дуги для углов и дуг для треугольников
            case "ArcColorGo"->this.ArcColor(model.getArcGo());//цвет дуги
            case "TextGo" -> this.TextGo(model.getTextGo());//буквы
        }
    }
    //Вывод в правую часть доски
    private void textShapeGo(TextArea txtArea) {
        txtArea.setText(model.getTxtShape());
    }


    /*
    Методы для вывода информации на экран
    Все данные расчитываются в классе модели (Model)
     */
    //Перемещение точек
    private void vertexGo(Circle ver){
        ver.setCenterX(model.getVerX());
        ver.setCenterY(model.getVerY());
    }
    //Перемещение отрезков
    private void sideGo(Line side){
        side.setStartX(model.getVerX1());
        side.setStartY(model.getVerY1());
        side.setEndX(model.getVerX());
        side.setEndY(model.getVerY());
    }
    //Перемещение луча и прямой
    private void rayGo(Line ray){
        ray.setStartX(model.getRayEndX());
        ray.setStartY(model.getRayEndY());
        ray.setEndX(model.getRayStartX());
        ray.setEndY(model.getRayStartY());
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

    //Изменение цвета линий
    private void ArcColor(Arc arc){
        Color c=model.getColorGo();
        arc.setStroke(c);
    }

    //Дуги углов
    private void arcGo(Arc arc){
        arc.setCenterX(model.getVerX());
        arc.setCenterY(model.getVerY());
        arc.setRadiusX(model.getArcRadius());
        arc.setRadiusY(model.getArcRadius());
        arc.setStartAngle(model.getAngleStart());
        arc.setLength(model.getAngleLength());
    }

    /**
     * Метод TextGo(Text t).
     * Предназначен для выводов имен точек, прямых, отрезков
     * @param t - объект текст
     */
     private void TextGo(Text t){
        t.setX(model.getTextX());
        t.setY(model.getTextY());
    }
}
