package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

//Первый класс для вывода на экран
class View implements Observer{

    //Объявляем класс Model
    Model model=new Model();
    //Объявляем класс сетки с методами перемещения координатной сетки и масштабирования
    //Как расширение класса пересчетв мировых координат в в окна просмотра
    GridView gridViews=new GridView();
    View() {
        //Регистрация слушателя в классе Model
        model.registerObserver(this);
        //Регистрация еще одного слушателя в классе Model
       // View1 v = new View1();
       // model.registerObserver(v);
    }
    @Override
    //Какую информацию надо вывести, переопределяем класс интефейса
    public  void  notification(String message){
        switch (message) {

            case "VertexGo" -> this.vertexGo(model.getVertex());//перемещение вершин
            case "SideGo" -> this.sideGo(model.getSideAll());//отрисовка сторон отрезков
            case "RayGo" ->this.rayGo(model.getSideAll());//для луча и прямой
          /*
            case "TextGo" -> this.TextGo(model.getTextGo());//буквы
            case "ColorGo" -> this.SrokeColor(model.getColorLine());//цвет
            case "ArcGo" -> this.arcGo(model.getArcGo());//дуги
            case "ArcColorGo"->this.ArcColor(model.getArcGo());
            case "TableGo"->this.tableGo(model.getMTableView());//таблица
            case "WebView"->this.webViewGo(model.getWebView());//Заполнение слева и внизу
            case "ToolTip"->this.toolTipGo(model.getOToolTip());//Подсказка

           */
        }
    }

    //Перемещение точек
    private void vertexGo(Circle ver){
        ver.setCenterX(model.getVerX());
        ver.setCenterY(model.getVerY());

    }
    //Перемещение сторон
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
}
