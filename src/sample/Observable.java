package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

/**
 * Интерфейс Observable.
 * На его основе построен класс Model. Выполняет роль наблюдателя.
 *
 * @author A. Nosov
 * @version 1.0
 */

interface Observable {
    void registerObserver(Observer o);//регистрация слушателей

    void notifyObservers(String message);//Передача сообщений для слушателей

    void VertexGo(Circle o);

    void SideGo(Line o);

    void RayGo(Line o);

    void TextGo(Text o);

    void WebViewGo(WebView o);

    void ToolTipGo(Button o);

    void WebGo(WebView o);

    void ArcGo(Arc o);

    void ArcColorGo(Arc o);

    void LineColorGo(Line o);

    void StatusGo(Label o);

    void TextAreaGo();
}