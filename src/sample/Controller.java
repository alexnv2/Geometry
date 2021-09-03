package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.val;

import java.util.Objects;

import static ContstantString.StringStatus.*;

/**
 * Класс управления приложением (Controller), наследует класс View.
 * Реагирует на все события управления от мыши и клавиатуры.
 * Вызывает методы из класса модели для обработки событий
 *
 * @author A. Nosov
 * @version 1.0
 * @see sample.View
 * @see sample.Model
 */

public class Controller extends View {

    //Связать переменные с шаблоном FXML
    @FXML
    public Pane paneShape;//контейнер для геометрических фигур
    public StackPane Cartesian;//контейнер для декартовых координат
    public TextArea txtShape;//контейнер для правой части доски
    public MenuItem menuTreangle;
    public MenuItem menuSecondTr;
    public MenuItem menuEqualTr;
    public MenuItem menuTread;
    public MenuItem menuAbout;
    public Font x3;
    @FXML
    private Button btnTreangle;
    @FXML
    private Button btnPoind;//кнопка добавить точку
    @FXML
    private Button btnSegment;//кнопка добавить отрезок
    @FXML
    private Button btnRay;//кнопка добавить луч
    @FXML
    private Button btnLine;//кнопка добавить прямую
    @FXML
    private Button btnAngle;
    @FXML
    private Button btnVertical;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnMediana;
    @FXML
    private Button btnBisector;
    @FXML
    private Button btnHeight;
    //Web браузер для вывода данных
    public WebView webViewLeft;//для размещения информации слева от доски
    @FXML
    private Pane paneGrid;//контейнер для сетки
    public Label leftStatus;//Левый статус
    public Label rightStatus;//Правый статус
    private Line newLine;//отрезок
    private Circle poindLine1;//первая точка луча, прямой, отрезка
    private Circle poindLine2;//вторая точка луча, прямой, отрезка
    @FXML
    private CheckMenuItem menuShowPoindName;
    @FXML
    private CheckMenuItem menuShowLineName;
    @FXML
    private CheckMenuItem menuGrid;
    @FXML
    private CheckMenuItem menuCartesian;
    @FXML
    private CheckMenuItem menuAngleName;


    //Режимы создания
    private boolean poindAdd = false;//true - создать точку
    private boolean segmentAdd = false;//true - создать отрезок
    private boolean rayAdd = false;//true - создание луча
    private boolean lineAdd = false;//true - создание прямой
    private boolean angleAdd = false;//true -создание угла
    private boolean treangleAdd = false;//true - создание треугольника
    private boolean medianaAdd = false;//true - проведение медианы из вершины треугольника
    private boolean bisectorAdd = false;//true - проведение биссектрисы из вершины треугольника
    private boolean heightAdd = false;//true - проведение высоты из вершины треугольника
    private boolean verticalAdd=false;//true - построение перпендикуляра к прямой

    private Circle poindTreangle1;//первая точка треугольника
    private Line lineTriangle1;//первая сторона треугольника для построения
    private int angleCol = 0;//индекс счета углов


    private boolean poindAdd1 = false;//true - создание первой точки для отрезка
    private boolean poindAdd2 = false;//true - создание второй точки для отрезка


    private String infoStatus;//Вершины угла

    /**
     * Метод инициализации для класса Controller
     */
    @FXML
    private void initialize() {
        //Передача ссылок в класс Model
        model.setStatus(leftStatus);//Передать ссылку на статус для модели
        model.setTextArea(txtShape);//Передать ссылку фигуры
        model.setGridViews(gridViews);//Передать ссылку для пересчета координат класса модели.
        model.setPaneBoards(paneShape);//Передать ссылку на доску для класса модели.
        model.webHTML(webViewLeft, "geometry.html");//Вывод в web файла html
        //формирование линий координат и сетки, перерасчет при изменении размеров доски
        gridViews.setPaneGrid(paneGrid);
        gridViews.setCartesian(Cartesian);
        //Изменение ширины окна
        Cartesian.widthProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVr(Cartesian.getWidth());
            gridViews.setWl(-Cartesian.getWidth() / 2);
            gridViews.setWr(Cartesian.getWidth() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();//обновить координаты геометрических фигур
        });
        //Изменение высоты окна
        Cartesian.heightProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVb(Cartesian.getHeight());
            gridViews.setWt(Cartesian.getHeight() / 2);
            gridViews.setWb(-Cartesian.getHeight() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();//обновить координаты геометрических фигур
        });

        if (model.isShowGrid()) {
            gridViews.gridCartesian();//Вывод сетки
        }

        //Добавить изображение к кнопкам
        btnPoind.setStyle("-fx-background-image: url(/Images/point.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnSegment.setStyle("-fx-background-image: url(/Images/point&line.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnAngle.setStyle("-fx-background-image: url(/Images/angle.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnVertical.setStyle("-fx-background-image: url(/Images/vertical.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnDelete.setStyle("-fx-background-image: url(/Images/delete.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnLine.setStyle("-fx-background-image: url(/Images/line.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnRay.setStyle("-fx-background-image: url(/Images/ray.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnTreangle.setStyle("-fx-background-image: url(/Images/triangle.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnHeight.setStyle("-fx-background-image: url(/Images/triangle_height.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnMediana.setStyle("-fx-background-image: url(/Images/mediana.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnBisector.setStyle("-fx-background-image: url(/Images/bisector.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");


    }

    /**
     * Метод btnPoindClick().
     * Cобытие нажатия кнопки "Добавить точку".
     * Устанавливает режим добавления точки.
     */
    public void btnPoindClick() {
        //Установить статус
        model.setStringLeftStatus(STA_1);
        model.statusGo(leftStatus);//Установить статус
        poindAdd = true;//Установить режим добавления

    }

    /**
     * Метод btnSegmentClick().
     * Метод для события нажатия кнопки "Добавить отрезок".
     * Устанавливает режим добавления отрезка.
     */
    public void btnSegmentClick() {
        //Установить статус
        model.setStringLeftStatus(STA_2);
        model.statusGo(leftStatus);
        segmentAdd = true;
    }

    /**
     * Метод btnRay().
     * Метод для события нажатия кнопки "Добавить луч".
     * Устанавливает режим добавления луча.
     */
    public void btnRay() {
        //Установить статус
        model.setStringLeftStatus(STA_3);
        model.statusGo(leftStatus);
        rayAdd = true;//режим построения луча
    }

    /**
     * Метод btLine().
     * Метод для события нажатия кнопки "Добавить прямую".
     * Устанавливает режим добавления прямой.
     */
    public void btnLine() {
        //Установить статус
        model.setStringLeftStatus(STA_4);
        model.statusGo(leftStatus);
        lineAdd = true;//режим построения прямой
    }

    /**
     * Метод btnAngle()
     * Метод для события нажатия кнопки "Добавить угол".
     * Устанавливает режим добавления угла.
     */
    public void btnAngle() {
        //Установить статус
        model.setStringLeftStatus(STA_14);
        model.statusGo(leftStatus);
        angleAdd = true;//режим построения угла
        infoStatus = "";//Имя для коллекции VertexArc
    }

    /**
     * Метод btnVertical()
     * Метод для события нажатия кнопки "Провести перпендикуляр к прямой"
     * Устанавливает режим построения перпендикуляра к прямой.
     */
    public void btnVertical() {
        model.setStringLeftStatus(STA_26);
        model.statusGo(leftStatus);
        verticalAdd = true;//режим построения угла
        infoStatus = "";//Имя для коллекции VertexArc
    }
    /**
     * Метод btnTreangle().
     * Метод на события нажатия кнопки "Добавить треугольник".
     * Устанавливает режим добавления треугольника.
     */
    public void btnTreangle() {
        //Установить статус
        model.setStringLeftStatus(STA_5);
        model.statusGo(leftStatus);
        treangleAdd = true;
        infoStatus = "";//Для коллекции TreangleName
    }

    /**
     * Метод btnMedian().
     * Предназначен для установления режима добавить медиану. Нажата кнопка "Добавить медиану"
     */
    public void btnMedian() {
        model.setStringLeftStatus(STA_18);
        model.statusGo(leftStatus);
        medianaAdd = true;
    }

    /**
     * Метод btnBisector()
     * Предназначен для установления режима добавить биссектрису. Нажата кнопка "Добавить биссектрису".
     */
    public void btnBisector() {
        model.setStringLeftStatus(STA_22);
        model.statusGo(leftStatus);
        bisectorAdd = true;
    }

    /**
     * Метод btnHeight()
     * Нажата кнопка добавить высоту.
     */
    public void btnHeight() {
        model.setStringLeftStatus(STA_24);
        model.statusGo(leftStatus);
        heightAdd = true;
    }

    /**
     * Метод btnDelete()
     * Нажата копка "Удалить геометрическую фигуру." Метод запускает режим удаления выбранных объектов.
     * Признак для удаления флаг bSelect=true во всех коллекциях
     */
    public void btnDelete() {
        //Установить статус
        model.setStringLeftStatus(STA_15);
        model.statusGo(leftStatus);
        model.setRemoveObject(true);//установить режим удаления
    }

    /**
     * Метод onMouseMoved()
     * Отслеживает события перемещения мышки по доске без нажатой кнопки
     * Выводит координаты мыши в статусной строке
     * Используется при добавлении геометрических фигур на доску.
     *
     * @param mouseEvent - координаты мыши
     */
    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setVerX(mouseEvent.getX());
        model.setVerY(mouseEvent.getY());
        model.setVerX0(gridViews.revAccessX(mouseEvent.getX()));
        model.setVerY0(gridViews.revAccessY(mouseEvent.getY()));
        rightStatus.setText("x " + mouseEvent.getX() + " y " + mouseEvent.getY() + " Координаты доски x: " + gridViews.revAccessX(mouseEvent.getX()) + " y: " + gridViews.revAccessY(mouseEvent.getY()));
        //Добавление треугольника
        if (treangleAdd && newLine != null && poindAdd2) {
            model.SideGo(newLine);//проводим отрезок
            model.findLinesUpdateXY(newLine.getId());
            if (angleCol == 2) {//2-для угла, треугольника
                poindAdd1 = true;
            }
        }

        //Создание отрезка
        if (segmentAdd && newLine != null && poindAdd2) {
            model.SideGo(newLine);//проводим отрезок
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            poindAdd1 = true;//первая точка создана
        }
        //Вторая точка, если расстояние до неё меньше 15px, конец отрезка
        //переходит на точку. Щелчок мышкой, вторая точка выбрана заданная
        model.lineAddPoind(newLine, poindAdd2);

        //Создание луча
        if (rayAdd && newLine != null && poindAdd2) {
            //Расчитать координаты окончания луча
            double x = model.getRayEndX() + (model.getVerX() - model.getRayEndX()) * 3;
            double y = model.getRayEndY() + (model.getVerY() - model.getRayEndY()) * 3;
            //Добавить координаты пересчета в коллекцию
            model.setRayStartX(x);
            model.setRayStartY(y);
            //Пересчет координат в мировые
            model.setVerLineStartX(gridViews.revAccessX(model.getRayEndX()));
            model.setVerLineStartY(gridViews.revAccessY(model.getRayEndY()));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));

            model.RayGo(newLine);//проводим отрезок
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            poindAdd1 = true;//первая точка создана
        }

        //Создание прямой
        if (lineAdd && newLine != null && poindAdd2) {
            //расчитать концов прямой по уравнению прямой
            double x = poindLine1.getCenterX() + (model.getVerX() - poindLine1.getCenterX()) * 3;
            double y = poindLine1.getCenterY() + (model.getVerY() - poindLine1.getCenterY()) * 3;
            double x1 = poindLine1.getCenterX() + (model.getVerX() - poindLine1.getCenterX()) * -3;
            double y1 = poindLine1.getCenterY() + (model.getVerY() - poindLine1.getCenterY()) * -3;

            //Добавить координаты пересчета в коллекцию
            model.setVerLineStartX(gridViews.revAccessX(x1));
            model.setVerLineStartY(gridViews.revAccessY(y1));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));
            model.findLinesUpdateXY(newLine.getId());
            //задать координаты прямой
            model.setRayStartX(x1);
            model.setRayStartY(y1);
            model.setRayEndX(x);
            model.setRayEndY(y);
            //проводим прямую
            model.RayGo(newLine);
            poindAdd1 = true;//разрешение для постройки 2 точки
        }
    }

    /**
     * Метод onMouseDraggen()
     * Отслеживает события перемещения мышки с нажатой левой кнопкой
     * Используется для перемещения сетки и координатных осей по доске.
     *
     * @param event - свойство мыши.
     */
    public void onMouseDraggen(MouseEvent event) {
        //координаты, нужны для перемещения объектов на доске
        model.setVerX(event.getX());
        model.setVerY(event.getY());
        model.setVerX0(gridViews.revAccessX(event.getX()));
        model.setVerY0(gridViews.revAccessY(event.getY()));

        //Перемещение сетки
        if (event.getTarget() == paneShape) {
            val dx = gridViews.getVPx() - event.getX();//Вычисляем смещение мышки по Х
            val dy = gridViews.getVPy() - event.getY();// по Y
            gridViews.setVPx(event.getX()); //Сохраняем текущие координаты мышки
            gridViews.setVPy(event.getY());
            //Вычисляем смещение окна
            gridViews.setWl(gridViews.getWl() + dx);
            gridViews.setWr(gridViews.getWr() + dx);
            gridViews.setWt(gridViews.getWt() - dy);
            gridViews.setWb(gridViews.getWb() - dy);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();
        }
        event.consume();
    }

    /**
     * Метод oMousePressed()
     * Отслеживает нажатие кнопки на доске
     * Используется для перемещения сетки и координатных осей, а также
     * для создания геометрических фигур.
     *
     * @param event - событие мыши
     */
    public void onMousePressed(MouseEvent event) {
        // Фиксируем точку нажатия кнопки мыши для перемещения сетки и координатных осей
        if (event.getTarget() == paneShape) {
            gridViews.setVPx(event.getX());
            gridViews.setVPy(event.getY());
        }
        //Добавление точки на доске
        if (poindAdd) {
            model.setPoindLineAdd(true);
            Circle newPoind = model.createPoindAdd(true);//создать точку
            paneShape.getChildren().add(newPoind);//добавить на доску
            poindAdd = false;//Режим добавления точки окончен
            model.setPoindLineAdd(false);
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }
        //Добавление отрезка
        if (segmentAdd && !poindAdd1) {
            model.setPoindLineAdd(true);
            addLineRayStart(0);//Создание первой точки и линии
        }
        //Вторая точка для отрезка
        if (segmentAdd && poindAdd1) {
            addLineRayEnd();
            segmentAdd = false;//окончание режима добавления
            //Связать точки с прямой
            model.lineBindCircles(poindLine1, poindLine2, newLine);
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }

        //Добавление угла первая точка
        if (angleAdd && !poindAdd1) {
            //увеличить индекс
            if (!model.isPoindOldAdd()) {//false - новая вершина true - взять имеющую
                poindLine1 = model.createPoindAdd(true);//создать новую вершину
                infoStatus = infoStatus + poindLine1.getId() + "_";//добавить вершину в список
                paneShape.getChildren().add(poindLine1);
            } else {
                infoStatus = infoStatus + model.getTimeVer().getId() + "_";//Вершина из временной переменной
                poindLine1 = model.getTimeVer();
            }
            model.indexAdd(poindLine1);//увеличить индекс
            angleCol += 1;//увеличиваем счетчик вершин
            if (angleCol == 3) {
                poindAdd2 = false;//закрыть 2 точку
                model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
                //угол
                Arc arcAngle = model.createVertexAdd(infoStatus);
                paneShape.getChildren().add(arcAngle);//рисуем арку дуги
                arcAngle.toBack();//перемещать узел вниз только после добавления на стол
                //Связываем арку с углом и именем
                model.arcBindPoind(infoStatus, arcAngle);
                angleAdd = false;//окончание режима добавления
                poindAdd1 = false;
                angleCol = 0;
                //Вывод информации об объектах в правую часть доски
                model.setTxtShape("");
                model.txtAreaOutput();
            }
        }
        //Построение треугольника
        if (treangleAdd && !poindAdd1) {
            addLineRayStart(3);//Создание первой точки и линии
            angleCol += 1;//увеличиваем счетчик вершин
        }
        //Окончание построения треугольника
        if (treangleAdd && poindAdd1) {
            //Двунаправленная связь
            model.lineBindCircles(poindTreangle1, poindLine1, lineTriangle1);
            //Свойство мышки
            model.mouseLine(lineTriangle1);
            Point2D v1 = new Point2D(poindTreangle1.getCenterX(), poindTreangle1.getCenterY());
            infoStatus = poindTreangle1.getId() + "_";
            //Заменить имя
            model.findNameId(poindTreangle1.getId(), poindLine1.getId(), lineTriangle1.getId());
            Point2D v2 = new Point2D(poindLine1.getCenterX(), poindLine1.getCenterY());
            infoStatus = infoStatus + poindLine1.getId() + "_";
            addLineRayEnd();
            //Двунаправленная связь
            model.lineBindCircles(poindLine1, poindLine2, newLine);
            model.mouseLine(newLine);
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            newLine = model.createLineAdd(3);
            model.setVerX(poindTreangle1.getCenterX());
            model.setVerY(poindTreangle1.getCenterY());
            model.SideGo(newLine);
            //Двунаправленная связь
            model.lineBindCircles(poindTreangle1, poindLine2, newLine);
            model.mouseLine(newLine);
            Point2D v3 = new Point2D(poindLine2.getCenterX(), poindLine2.getCenterY());
            infoStatus = infoStatus + poindLine2.getId();
            //Добавить многоугольник в форме треугольника
            Polygon t = model.treangleAdd(v1, v2, v3, infoStatus);
            paneShape.getChildren().addAll(newLine, t);
            t.toBack();
            newLine.toBack();
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            //Заменить имя
            model.findNameId(poindTreangle1.getId(), poindLine2.getId(), newLine.getId());
            treangleAdd = false;//окончание режима добавления
            poindAdd1 = false;
            angleCol = 0;
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }

        //Добавления луча
        if (rayAdd && !poindAdd1) {
            addLineRayStart(1);
        }
        //Окончание построения луча
        if (rayAdd && poindAdd1) {
            addLineRayEnd();
            rayAdd = false;//закончить построение луча
            //Связать точки с лучом
            model.rayBindCircles(poindLine1, poindLine2, newLine);
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            //Добавить имя на доску
            model.nameLineAdd(newLine);
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }
        //Добавление прямой
        if (lineAdd && !poindAdd1) {
            addLineRayStart(2);//Создание первой точки и линии
        }
        if (lineAdd && poindAdd1) {
            addLineRayEnd();
            //Привязка свойств мышки
            model.mouseLine(newLine);
            //Связать точки с прямой
            model.circlesBindLine(poindLine1, poindLine2, newLine);
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            //Добавить имя
            model.nameLineAdd(newLine);
            lineAdd = false;
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }

        //Добавление медианы
        if (medianaAdd) {
            poindLine1 = model.getTimeVer();
            newLine = model.mbhLineAdd(poindLine1, 4);
            model.mouseLine(newLine);
            medianaAdd = false;
            model.setTxtShape("");
            model.txtAreaOutput();
        }
        //Добавление биссектрисы
        if (bisectorAdd) {
            poindLine1 = model.getTimeVer();
            newLine = model.mbhLineAdd(poindLine1, 5);
            model.mouseLine(newLine);
            bisectorAdd = false;
            model.setTxtShape("");
            model.txtAreaOutput();
        }
        //Добавление высоты
        if (heightAdd) {
            poindLine1 = model.getTimeVer();
            newLine = model.mbhLineAdd(poindLine1, 6);
            model.mouseLine(newLine);
            heightAdd = false;
            model.setTxtShape("");
            model.txtAreaOutput();
        }

        event.consume();
    }//End onMousePressed()

    /**
     * Метод addLineRayStart(int Segment)
     * Метод начала построения отрезков, лучей и прямых.
     * Вход: номер геометрической фигуры, 0-отрезок, 1-луч, 2 прямая,3 треугольник
     *
     * @param Segment - номер фигуры
     */
    public void addLineRayStart(int Segment) {
        //увеличить индекс
        if (!model.isPoindOldAdd()) {//false - новая вершина true - взять имеющую
            poindLine1 = model.createPoindAdd(true);//создать новую вершину
            paneShape.getChildren().add(poindLine1);
        } else {
            poindLine1 = model.getTimeVer();
        }
        model.indexAdd(poindLine1);//увеличить индекс

        newLine = model.createLineAdd(Segment);//создать линию
        //Сохранить первую точку треугольника и первую сторону
        if (treangleAdd && !poindAdd2) {
            poindTreangle1 = poindLine1;
            lineTriangle1 = newLine;//
        }
        paneShape.getChildren().add(newLine);//добавить на доску
        newLine.toBack();//переместить линию вниз под точку
        poindAdd2 = true;//режим добавления второй точки и последующих

    }

    /**
     * Метод addLineRayEnd()
     * Метод окончания построения геометрической фигуры.
     * Закрывает режим добавления фигур.
     */
    public void addLineRayEnd() {
        model.setPoindLineAdd(false);
        if (!model.isPoindOldAdd()) {
            model.setPoindLineAdd(true);
            poindLine2 = model.createPoindAdd(true);//создать новую
            paneShape.getChildren().add(poindLine2);
            model.indexAdd(poindLine2);//увеличить индекс
        } else {
            poindLine2 = model.getTimeVer();
            model.indexAdd(poindLine1);//увеличить индекс
        }
        //Привязка свойств мышки
        model.mouseLine(newLine);
        //закрыть режим добавления
        poindAdd1 = false;//закрыть 1 точку
        poindAdd2 = false;//закрыть 2 точку
        model.setPoindLineAdd(false);
        model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
    }

    /**
     * Метод onScroll(ScrollEvent event)
     * Метод изменения масштаба координатной сетки при вращении колесика мышки
     * @param event - изменения колесика мышки
     */
    public void onScroll(ScrollEvent event) {
        double sc = event.getDeltaY();
        gridViews.onScrollView(sc);
        updateShape();
    }

    /**
     * Метод updateShape()
     * Метод перемещения всех геометрических объектов на доске при
     * перемещении координатной сетки. Для перемещения используются мировые координаты фигур
     * из коллекций.
     */
    public void updateShape() {
        //обновление точек
        for (PoindCircle p : model.getPoindCircles())
            if (p != null) {
                Circle c = p.getCircle();
                c.setCenterX(gridViews.accessX(p.getX()));
                c.setCenterY(gridViews.accessY(p.getY()));
            }
        //Обновление всех линий
        for (PoindLine pl : model.getPoindLines()) {
            if (pl != null) {
                //Обновляем отрезки
                Line l = pl.getLine();
                l.setStartX(gridViews.accessX(pl.getStX()));
                l.setStartY(gridViews.accessY(pl.getStY()));
                l.setEndX(gridViews.accessX(pl.getEnX()));
                l.setEndY(gridViews.accessY(pl.getEnY()));
            }
        }
        //Обновление дуг углов
        for (VertexArc va : model.getVertexArcs()) {
            if (va != null) {
                Arc a = va.getArc();
                a.setCenterX(gridViews.accessX(va.getCenterX()));
                a.setCenterY(gridViews.accessY(va.getCenterY()));
                a.setRadiusX(va.getRadiusX());
                a.setRadiusY(va.getRadiusY());
                a.setStartAngle(va.getStartAngle());
                a.setLength(va.getLengthAngle());

            }
        }
    }

    /**
     * Метод menuPoindClick().
     * Предназначен для вывода определения точки, прямой, отрезка.
     * Вызывается из пункта меню Фигуры->Точка, прямая, отрезок.
     */
    public void menuPoindClick() {
        model.webHTML(webViewLeft, "line.html");//Вывод в web файла

        //Создать точки
        for (int i = 0; i < 5; i++) {
            final double max = 500.;
            double x;
            double y;
            x = Math.random() * max;
            y = Math.random() * max;
            model.setVerX(Math.round(x));
            model.setVerY(Math.round(y));
            model.setVerX0(gridViews.revAccessX(Math.round(x)));
            model.setVerY0(gridViews.revAccessY(Math.round(y)));
            Circle newPoind = model.createPoindAdd(true);//создать точку
            paneShape.getChildren().add(newPoind);//добавить на доску
        }


    }

    /**
     * Метод menuRayClick().
     * Предназначен для вывода определений луча и угла
     * Вызывается из пункта меню Фигуры->Луч и угол.
     */
    public void menuRayClick() {
        model.webHTML(webViewLeft, "rayandangle.html");//Вывод в web файла html
    }

    /**
     * Метод menuAngleClick().
     * Предназначен для вывода определений смежных и вертикальных углов
     * Вызывается из пункта меню Фигуры->Смежные и вертикальные углы
     */
    public void menuAngleClick() {
        model.webHTML(webViewLeft, "angle.html");//Вывод в web файла html
    }

    /**
     * Метод menuAcsiomy1Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы принадлежности
     */
    public void menuAcsiomy1Click() {
        model.webHTML(webViewLeft, "acsiomy_1.html");//Вывод в web файла html
    }

    /**
     * Метод menuAcsiomy2Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы равенства и наложения
     */
    public void menuAcsiomy2Click() {
        model.webHTML(webViewLeft, "acsiomy_2.html");//Вывод в web файла html
    }

    /**
     * Метод menuAcsiomy3Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы измерения
     */
    public void menuAcsiomy3Click() {
        model.webHTML(webViewLeft, "acsiomy_3.html");//Вывод в web файла html
    }

    /**
     * Метод menuAcsiomy4Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы параллельности.
     */
    public void menuAcsiomy4Click() {
        model.webHTML(webViewLeft, "acsiomy_4.html");//Вывод в web файла html
    }


    /**
     * Метод menuShowPoindName()
     * Предназначен для замены логической переменной, показывать большие буквы в именах точек.
     * Пункт из меню "Настройки - Показывать большие буквы."
     */
    public void menuShowPoindName() {
        visibleName(menuShowPoindName.isSelected(), "poind");
        model.setShowPoindName(menuShowPoindName.isSelected());//поменять логическую переменную
    }


    /**
     * Метод menuShowLineName().
     * Предназначен скрытия от показа имен прямых, лучей, отрезков.
     * Пункт из меню "Настройки - Показывать маленькие буквы".
     */
    public void menuShowLineName() {
        visibleName(menuShowLineName.isSelected(), "line");
        model.setShowLineName(menuShowLineName.isSelected()); //поменять логическую переменную
    }

    /**
     * Метод menuAngleName()
     * Предназначен для показа и скрытия имен углов
     * Пункт из меню "Настройки - Показывать имена углов".
     */
    public void menuAngleName() {
        visibleName(menuAngleName.isSelected(), "arc");
        model.setShowAngleName(menuAngleName.isSelected());//поменять логическую переменную
    }

    /**
     * Метод visibleNameLine(boolean bName, String name).
     * Предназначен для показа и скрытия имен.
      * @param bName - логическая переменная (true - показывать, false - не показывать)
     * @param name  - какие имена (line, poind, arc)
     */
    private void visibleName(boolean bName, String name) {
        for (NamePoindLine pn : model.getNamePoindLines()) {
            if (pn != null) {
                if (pn.getType().equals(name)) {
                    pn.getText().setVisible(bName);
                    pn.setVisibleLine(bName);
                }
            }
        }
    }

    public void menuTriangle() {
        model.webViewLeftString(webViewLeft, 0);//Определения
    }

    /**
     * Метод menuCartesian().
     * Пункт меню "Настройки-Показывать координатные оси".
     */
    public void menuCartesian() {
        model.setShowCartesian(menuCartesian.isSelected());
    }

    /**
     * Метод menuGrid().
     * Пункт меню "Настройки -> Показывать сетку".
     */
    public void menuGrid() {
        model.setShowGrid(menuGrid.isSelected());
        if (model.isShowGrid()) {
            gridViews.gridCartesian();//Вывод сетки
        } else {
            paneGrid.getChildren().clear();//Очистить экран и память
        }
    }
    /**
     * Метод menuIsosceles().
     * Нажат пункт меню "Теоремы и свойства-> Свойства равнобедренного треугольника".
     */
    public void menuIsosceles() {
        model.webViewLeftString(webViewLeft, 1);
    }
    /**
     * Метод menuPrIsosceles().
     * Нажат пункт меню "Теоремы и свойства-> Признак равнобедренного треугольника".
     */
    public void menuPrIsosceles() {
        model.webViewLeftString(webViewLeft, 12);
    }
    /**
     * Метод menuEquil()
     * Нажат пункт меню "Теоремы и свойства-> Первый признак равенства треугольников"
     */
    public void menuEquil() {
        model.setWindShow(0);
        TwofxmlLoader();
    }
    /**
     * Метод menuSecond()
     * Нажат пункт меню "Теоремы и свойства-> Второй признак равенства треугольников"
     */
    public void menuSecond() {
        model.setWindShow(1);
        TwofxmlLoader();
    }
    /**
     * Метод menuTread()
     * Нажат пункт меню "Теоремы и свойства-> Третий признак равенства треугольников"
     */
    public void menuTread() {
        model.setWindShow(2);
        TwofxmlLoader();
    }
    /**
     * Метод onMouseEnteredPoind().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить точку".
     */
    public void onMouseEnteredPoind() {
        model.setTextToolTip("Добавить точку");
        model.ToolTipGo(btnPoind);
    }

    /**
     * Метод onMoseEnteredSegment().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить отрезок".
     */
    public void onMoseEnteredSegment() {
        model.setTextToolTip("Добавить отрезок");
        model.ToolTipGo(btnSegment);
    }

    /**
     * Метод onMouseEnteredRay().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить луч".
     */
    public void onMouseEnteredRay() {
        model.setTextToolTip("Добавить луч");
        model.ToolTipGo(btnRay);
    }

    /**
     * Метод onMouseEnteredLine().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить прямую".
     */
    public void onMouseEnteredLine() {
        model.setTextToolTip("Добавить прямую");
        model.ToolTipGo(btnLine);
    }

    /**
     * Метод onMouseEnteredAngle()
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить угол"
     */
    public void onMouseEnteredAngle() {
        model.setTextToolTip("Добавить угол");
        model.ToolTipGo(btnAngle);
    }
    /**
     * Метод onMouseEnteredTreangle().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить треугольник".
     */
    public void onMouseEnteredTreangle() {
        model.setTextToolTip("Добавить треугольник");
        model.ToolTipGo(btnTreangle);
    }

    /**
     * Метод onMouseEnteredMediana().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить медиану".
     */
    public void onMouseEnteredMediana() {
        model.setTextToolTip("Добавить медиану");
        model.ToolTipGo(btnMediana);
    }

    /**
     * Метод onMouseEnteredBisector().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить биссектрису".
     */
    public void onMouseEnteredBisector() {
        model.setTextToolTip("Добавить биссектрису");
        model.ToolTipGo(btnBisector);
    }

    /**
     * Метод onMouseEnteredVertical()
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить перпендикуляр к прямой"
     */
    public void onMouseEnteredVertical() {
        model.setTextToolTip("Добавить перпендикуляр к прямой");
        model.ToolTipGo(btnVertical);
    }
    public void onMouseEnteredHeight() {
        model.setTextToolTip("Добавить высоту");
        model.ToolTipGo(btnHeight);
    }

    /**
     * Метод onMouseEnteredDelete()
     * Всплывающая подсказка при наведении на кнопку "Удалить"
     */
    public void onMouseEnteredDelete() {
        model.setTextToolTip("Удалить геометрическую фигуру");
        model.ToolTipGo(btnDelete);
    }

    //Тестовая кнопка вывод информации по всем коллекциям для тестирования системы
    public void btnTest() {
        model.ColTest();
    }




    //Загрузка шаблона окна для признаков равенства треугольников
    public void TwofxmlLoader() {
        try {
            Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("equality.fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Признаки равенства треугольников");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Нажата кнопка меню "О программе"
    public void onAbout() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);//Блокирует все окна приложения
        window.initStyle(StageStyle.UTILITY);//Только кнопка закрыть
        VBox root = new VBox();
        root.setStyle(
                "-fx-background-image: url(/Images/About.png); " +
                        "-fx-background-repeat: no-repeat;"
        );
        root.setAlignment(Pos.TOP_CENTER);

        Label label2 = new Label("МБОУ \"Центр образования Опочецкого района\"\nСтруктурное подразделение \"Средняя школа № 4\"\n\n\n ");
        label2.setFont(Font.font("Verdana", FontWeight.BOLD, 24.0));
        label2.setTextFill(Color.SANDYBROWN);
        label2.setTextAlignment(TextAlignment.CENTER);
        Label label = new Label("Учебно-справочное пособие");
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 34.0));
        label.setTextFill(Color.YELLOW);
        label.setTextAlignment(TextAlignment.CENTER);
        Label label1 = new Label("Геометрия\n\n");
        label1.setFont(Font.font("Verdana", FontWeight.BOLD, 58.0));
        label1.setTextFill(Color.YELLOW);
        label1.setTextAlignment(TextAlignment.CENTER);
        Label label3 = new Label("Выполнил ученик 8Б класса \n Носов Алексей \n2021 г.");
        label3.setFont(Font.font("Verdana", FontWeight.BOLD, 24.0));
        label3.setTextFill(Color.YELLOW);
        label3.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().addAll(label2, label, label1, label3);
        Scene scene = new Scene(root, 864, 489);
        window.setScene(scene);
        window.setTitle("О программе");
        window.show();

    }



}

