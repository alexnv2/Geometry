package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

/**
 * Класс View расширяет интерфейс Observer.
 * Базовый класс, предназначен отображения информации на экране.
 *
 * @author A. Nosov
 * @version 1.0
 */
class View implements Observer {
    //Объявляем класс Model
    Model model = new Model();
    //Объявляем класс сетки с методами перемещения координатной сетки и масштабирования
    //Как расширение класса пересчета мировых координат в окне просмотра
    GridView gridViews = new GridView();
    //Для всплывающих подсказок
    private final Tooltip tooltip = new Tooltip();

    /**
     * Констриктор класса отображения View.
     */
    View() {
        //Регистрация слушателя в классе Model
        model.registerObserver(this);
        //Свойства сплывающих подсказок
        tooltip.setShowDelay(Duration.millis(10.0));
        tooltip.setFont(Font.font(12));
        tooltip.setStyle("-fx-background-color: LIGHTBLUE;" +
                "-fx-text-fill: black");
    }

    /**
     * Метод notification(String message).
     * Получает сообщения из класса Model о готовности информации к выводу на экран.
     * Метод переопределяется из интерфейса Observer.
     *
     * @param message - какую информацию надо вывести
     */
    @Override
    public void notification(String message) {
        switch (message) {
            case "VertexGo" -> this.vertexGo(model.getVertex());//перемещение вершин
            case "SideGo" -> this.sideGo(model.getLine());//отрисовка сторон отрезков
            case "RayGo" -> this.rayGo(model.getLine());//для луча и прямой
            case "WebView" -> this.webViewGo(model.getWebView());//Заполнение слева и внизу
            case "LeftStatusGo" -> this.statusGo(model.getStatus());//вывод статуса
            case "WebGo" -> this.webFileHTMLGo(model.getWebView());//вывод файла HTML
            case "TextShapeGo" -> this.textShapeGo(model.getTextArea());//для вывода в правое окно
            case "ColorLine" -> this.SrokeColor(model.getLine());//цвет линий
            case "ArcGo" -> this.arcGo(model.getArcGo());//дуги для углов и дуг для треугольников
            case "ColorArc" -> this.ArcColor(model.getArcGo());//цвет дуги
            case "TextGo" -> this.TextGo(model.getTextGo());//буквы
            case "ToolTip" -> this.ToolTipGo(model.getBtnToolTip());//добавить всплывающие подсказки
        }
    }

    /**
     * Метод SrokeColor(Line l)
     * Предназначен для изменения цвета линий.
     *
     * @param l - объект Line
     */
    private void SrokeColor(Line l) {
        l.setStroke(model.getColorLine());
    }

    /**
     * Метод ToolTipGo(Button btnToolTip)
     * Предназначен для вывода всплывающих подсказок
     *
     * @param btnToolTip - объект кнопка на которую наведена мышка
     */
    private void ToolTipGo(Button btnToolTip) {
        tooltip.setText(model.getTextToolTip());//получить текст подсказки
        btnToolTip.setTooltip(tooltip);//вывести подсказку
    }

    /**
     * Метод textShapeGo(TextArea txtArea)
     * Предназначен для вывода в правую часть доски информации по объектам.
     *
     * @param txtArea - объект Текст
     */
    private void textShapeGo(TextArea txtArea) {
        txtArea.setText(model.getTxtShape());
    }

    /**
     * Метод vertexGo(Circle ver)
     * Предназначен для вывода точки на доске при перемещении.
     *
     * @param ver - объект Окружность
     */
    private void vertexGo(Circle ver) {
        ver.setCenterX(model.getScreenX());
        ver.setCenterY(model.getScreenY());
    }

    /**
     * Метод sideGo(Line side)
     * Предназначен для вывода линии на доске.
     *
     * @param side - объект Линия
     */
    private void sideGo(Line side) {
        side.setStartX(model.getSegmentStartX());
        side.setStartY(model.getSegmentStartY());
        side.setEndX(model.getScreenX());
        side.setEndY(model.getScreenY());
    }

    /**
     * Метод rayGo(Line ray)
     * Предназначен для вывода лучей и прямых на доске.
     *
     * @param ray - объект Линия
     */
    private void rayGo(Line ray) {
        ray.setStartX(model.getRayEndX());
        ray.setStartY(model.getRayEndY());
        ray.setEndX(model.getRayStartX());
        ray.setEndY(model.getRayStartY());
    }

    /**
     * Метод webViewGo(WebView webView)
     * Предназначен для вывода справочной информации в левую часть доски в виде констант.
     *
     * @param webView - объект WebView
     */
    private void webViewGo(WebView webView) {
        webView.setContextMenuEnabled(false);
        WebEngine w = webView.getEngine();
        w.loadContent(model.getStringWebView());
    }

    /**
     * Метод webFileHTMLGo(WebView web)
     * Предназначен для вывода в левую часть доски файлов справочной информации в формате html.
     *
     * @param web - объект WebView
     */
    private void webFileHTMLGo(WebView web) {
        web.setContextMenuEnabled(false);
        WebEngine w = web.getEngine();
        w.load(model.getLeftHTML());
    }

    /**
     * Метод statusGo(Label label)
     * Предназначен для вывода подсказок в статусной строке.
     *
     * @param label - объект Label
     */
    private void statusGo(Label label) {
        label.setText(model.getStringLeftStatus());
    }

    /**
     * Метод ArcColor(Arc arc)
     * Предназначен для изменения цвета дуги и цвета заполнения дуги на доске.
     *
     * @param arc - объект Arc
     */
    private void ArcColor(Arc arc) {
        arc.setStroke(model.getColorArc());//цвет дуги
        arc.setFill(model.getColorFillArc());//цвет фона дуги
    }

    /**
     * Метод arcGo(Arc arc)
     * Предназначен для вывода углов на доске.
     *
     * @param arc - объект Arc
     */
    private void arcGo(Arc arc) {
        arc.setCenterX(model.getScreenX());
        arc.setCenterY(model.getScreenY());
        arc.setRadiusX(model.getArcRadius());
        arc.setRadiusY(model.getArcRadius());
        arc.setStartAngle(model.getAngleStart());
        arc.setLength(model.getAngleLength());
    }

    /**
     * Метод TextGo(Text t).
     * Предназначен для выводов имен точек, прямых, отрезков
     *
     * @param t - объект текст
     */
    private void TextGo(Text t) {
        t.setX(model.getTextX());
        t.setY(model.getTextY());
    }
}
