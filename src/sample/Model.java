package sample;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import lombok.Data;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

import static ContstantString.StringStatus.*;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;


/**
 * Класс Model расширение интерфейса Observable
 * Класс Модели, получает запросы на отработку событий от контроллера и
 * выставляет переменные и отправляет сообщения классу отображения (View),
 * который выводит эти данные на экран.
 *
 * @author A. Nosov
 * @version 1.0
 */
@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements Observable {

    //Переменные класса
    private Circle circle;//окружность
    private Circle vertex; //точка
    private Circle selected;//Хранит ссылки на выбранные объекты
    private Line line;//линия, луч, прямая
    private Label Status;//левый статус, вывод действий
    private Label rightStatus;//правый статус, вывод координат
    private Text textGo;//Для наименования точек, отрезков, прямых и т.д
    private WebView webView; //браузер в левой части доски
    private TextArea textArea;//получаем ссылку на правую часть доски для вывода информации о фигурах
    private Pane paneBoards;//получаем ссылку на доску, где размещены объекты
    private GridView gridViews;//сетка
    private Arc arcGo;//дуга угла
    private Button btnToolTip;//ссылка на кнопку
    private String textToolTip;//текст при наведении на кнопку

    private String stringWebView;//text left
    private String stringLeftStatus;//для хранения и передачи в View статусных сообщений
    private String leftHTML;//хранит адрес файла HTML из папки Web для передачи в View
    private String txtShape = "";//хранит строку о геометрической фигуре на доске

    //координаты
    private double screenX;//координата экрана Х от мышки
    private double screenY;//координата экрана Y от мышки
    private double decartX;//координата X мировая на доске, зависят от мышки
    private double decartY;//координата Y мировая на доске, зависят от мышки
    private double segmentStartX;//координата StartX для отрезков
    private double segmentStartY;//Координата StartY для отрезков
    private double rayStartX;//координаты экрана для луча и прямой StartX
    private double rayStartY;//координаты экрана для луча и прямой StartY
    private double rayEndX;//координаты экрана для луча и прямой EndX
    private double rayEndY;//координаты экрана для луча и прямой EndY
    private double verLineStartX;//координата X мировая для Line StartX
    private double verLineStartY;//координата Y мировая для Line StartY
    private double verLineEndX;//координата X мировая для Line EndX
    private double verLineEndY;//координата Y мировая для Line EndY
    private double textX;//координата Х для имен точек
    private double textY;//координата Y для имен точек
    private double dXStart; //смещение по х от нажатой мышки до начала линии для её перемещения
    private double dYStart;
    private double dXEnd;//смещение по х от нажатой мышки до конца линии для её перемещения
    private double dYEnd;

    //Для временного хранения точек и линий.
    private Circle timeVer;//для временного хранения выбранных вершин
    private Line timeLine;// для временного хранения выбранной линии

    //Индексы
    private String indexPoind = "A";//Индекс для точек
    private int indexPoindInt = 0;
    private String indexLine = "a";//Индекс для прямых, отрезков, лучей
    private int indexLineInt = 0;
    private char indexAngle = '\u03b1';//Индекс для углов, начинается с альфа
    private int indexAngleInt = 0;

    private boolean poindOldAdd = false;//true - Берем существующие точки для построения фигур
    private boolean lineOldAdd = false;//true - Берем существующую линию для построения фигур
    private boolean poindAdd = false;//true- режим добавления точки
    private boolean poindLineAdd = false;//true - добавление точки на линию
    private boolean createLine = false;//true - режим добавления отрезка, луча, прямой (необходима для перемещения линий)

    //режимы создания
    private boolean angleAdd;//true -создание угла
    private boolean removeObject;//true - режим удаления
    //Свойства углов
    private double arcRadius = 30;//радиус дуги
    private double angleStart;//начало дуги гр.
    private double angleLength;//длина дуги гр.
    private Color ColorArc = Color.DARKSLATEBLUE;//цвет дуги угла
    private Color ColorFillArc = Color.LIGHTBLUE;//цвет заполнения дуги угла
    //Свойства точек
    private double radiusPoind = 5;//радиус точки
    private Color circleColorFill = Color.LIGHTBLUE;//цвет фона
    private Color circleColorStroke = Color.DARKSLATEBLUE;//цвет рамки
    //Свойства линий
    private Color ColorLine = Color.DARKSLATEBLUE;//цвет линий по умолчанию
    private double lineStokeWidth = 2;//толщина линий
    private double selectStrokeWidth = 3;//толщина линии при наведении на неё мыши
    private int inDash = 0;//индекс определяет внешний вид прямой (0-4 вида), по умолчанию 0
    //Логические переменные из меню настроек
    private boolean showPoindName = true;//по умолчанию, всегда показывать имена точек
    private boolean showLineName = false;//по умолчанию, не показывать имена линий
    private boolean showDate = false;//по умолчанию, не показывать данные объектов на доске
    private boolean showGrid = true;//по умолчанию, показывать сетку
    private boolean showCartesian = true;//по умолчанию, показывать координатную ось
    private boolean showAngleName = false;//по умолчанию, не показывать имя углов

    private boolean poindMove = false;

    /**
     * Логическая переменная createShape.
     * Задает глобальный режим построения всех фигур, блокирует
     * возможность случайного перемещения фигуры при построении.
     * Задается контролером при построении, после построения устанавливает false.
     * Используется при подключении к фигуре свойств мышки.
     * Блокирует режим перемещения фигуры.
     */
    private boolean createShape = false;
    private double t;//для параметрической прямой, когда точка принадлежит прямой

    private double radiusCircle;//радиус окружности, для View
    private double radiusCircleW;//радиус окружности в мировых координатах


    //Коллекции
    private LinkedList<PoindCircle> poindCircles = new LinkedList<>();//коллекция для точек по классу
    private LinkedList<PoindLine> poindLines = new LinkedList<>();//коллекция для линий по классу
    private LinkedList<VertexArc> vertexArcs = new LinkedList<>();//коллекция для арок углов
    private ArrayList<Double> arrDash = new ArrayList<>();//массив для создания вида строк
    private LinkedList<NamePoindLine> namePoindLines = new LinkedList<>();//коллекция для имен
    private LinkedList<TreangleName> treangleNames = new LinkedList<>();//коллекция треугольников
    private LinkedList<CircleLine> circleLines = new LinkedList<>();//коллекция окружностей

    //Определяем связанный список для регистрации классов слушателей
    private LinkedList<Observer> observers = new LinkedList<>();

    //Переменные для передачи в другой контроллер
    public void setWindShow(int w) {
        WIND_SHOW = w;
    }

    public int getWindShow() {
        return WIND_SHOW;
    }

    /**
     * Конструктор класса без переменных
     */
    Model() {
    }

    /**
     * Метод registerObserver(Observer o)
     * Метод регистрации слушателя, переопределяем функцию интерфейса
     *
     * @param o - объект слушатель, добавляем в коллекцию слушателей
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Метод notifyObservers(String message)
     * Уведомление слушателя, переопределяем функцию интерфейса.
     *
     * @param message - сообщение для слушателя
     */
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.notification(message);
        }
    }

    /**
     * Метод initIndex()
     * Инициализация переменных после очистки доски.
     */
    public void initIndex() {
        setIndexPoind("A");
        setIndexPoindInt(0);
        setIndexLine("a");
        setIndexLineInt(0);
        setIndexAngle('\u03b1');
        setIndexAngleInt(0);
    }

    /**
     * Метод indexPoindAdd().
     * Предназначен для увелечения индекса в названии точки.
     */
    private String indexPoindAdd() {
        String s;
        if (indexPoindInt > 0) {
            s = indexPoind + indexPoindInt;
        } else {
            s = indexPoind;
        }
        char[] chars = indexPoind.toCharArray();
        if (String.valueOf(chars[0]).equals("Z")) {
            indexPoind = "A";
            indexPoindInt += 1;
        } else {
            chars[0] += 1;
            indexPoind = String.valueOf(chars[0]);
        }
        return s;
    }

    /**
     * Метод indexLinedAdd().
     * Предназначен для увелечения индекса в названии прямых, отрезков, лучей.
     */
    private String indexLineAdd() {
        String s;
        if (indexLineInt > 0) {
            s = indexLine + indexLineInt;
        } else {
            s = indexLine;
        }
        char[] chars = indexLine.toCharArray();
        if (String.valueOf(chars[0]).equals("z")) {
            indexLine = "a";
            indexLineInt += 1;
        } else {
            chars[0] += 1;
            indexLine = String.valueOf(chars[0]);
        }
        return s;
    }

    /**
     * Метод indexAngledAdd()
     * Предназначен для задания имени угла греческим алфавитом
     *
     * @return - имя
     */
    private String indexAngledAdd() {
        String s;
        if (indexAngleInt > 0) {
            s = indexAngle + String.valueOf(indexAngleInt);
        } else {
            s = String.valueOf(indexAngle);
        }
        indexAngle++;
        if (indexAngle == '\u03ca') {
            indexAngle = '\u03b1';
            indexAngleInt++;
        }
        return s;
    }

    /**
     * Метод WebHTML()
     * Предназначен для вывода справочной информации в левую часть доски.
     *
     * @param o    - ссылка на объект WebView
     * @param file - имя файла html.
     */
    public void webHTML(WebView o, String file) {
        String pathFile = new File("").getAbsolutePath();//получить полный путь к файлу
        leftHTML = "file:" + pathFile + "\\src\\Web\\" + file;//установить ссылку
        //Передать в View для вывода
        webView = o;
        notifyObservers("WebGo");
    }


    /**
     * Метод nameSplitRemove(String s).
     * Предназначен для удаления символа разделителя в именах названий точек, прямых и т.д.
     *
     * @param s - строка вида А_В (отрезок АВ).
     * @return - возвращает строку АВ.
     */
    StringBuilder nameSplitRemove(String s) {
        String[] name = s.split("_");
        StringBuilder sb = new StringBuilder();
        for (String n : name) {
            sb.append(n);
        }
        return sb;
    }

    /**
     * Метод txtAreaOutput()
     * Предназначен для выборки из коллекций объектов информацию о геометрических фигурах
     * и выводе в правой части доски.
     */
    public void txtAreaOutput() {
        //Информация о точках
        for (PoindCircle p : poindCircles) {
            if (p.getCircle() != null) {
                String s1 = p.getId();
                double s2 = p.getX();
                double s3 = p.getY();
                txtShape = MessageFormat.format("{0}Точка: {1} ({2}, {3})\n", txtShape, s1, s2, s3);

            }
        }

        //Информация об отрезках, лучах и прямых
        for (PoindLine p : poindLines) {
            if (p.getLine() != null) {
                int l = p.getSegment();
                switch (l) {
                    case 0 -> {
                        double lengthSegment = Math.round(distance(p.getStX(), p.getStY(), p.getEnX(), p.getEnY()) * 100);
                        txtShape = MessageFormat.format("{0}{1}{2} Длина:{3}\n", txtShape, STA_10, nameSplitRemove(p.getId()), lengthSegment / 100);
                    }
                    case 1 -> txtShape = txtShape + STA_11 + p.getLine().getId() + " или " + nameSplitRemove(p.getId()) + "\n";
                    case 2 -> txtShape = txtShape + STA_12 + p.getLine().getId() + " или " + nameSplitRemove(p.getId()) + "\n";
                    case 3 -> {
                        double lengthSegment = Math.round(distance(p.getStX(), p.getStY(), p.getEnX(), p.getEnY()) * 100);
                        txtShape = txtShape + STA_17 + nameSplitRemove(p.getId()) + " Длина:" + lengthSegment / 100 + "\n";
                    }
                    case 4 -> txtShape = txtShape + STA_20 + nameSplitRemove(p.getId()) + "\n";
                    case 5 -> txtShape = txtShape + STA_23 + nameSplitRemove(p.getId()) + "\n";
                    case 6 -> txtShape = txtShape + STA_25 + nameSplitRemove(p.getId()) + "\n";
                    case 7 -> txtShape = txtShape + STA_27 + nameSplitRemove(p.getId()) + "\n";

                }
            }
        }

        //Информация об углах
        for (VertexArc v : vertexArcs) {
            if (v != null) {
                txtShape = MessageFormat.format("{0}Угол {1}= {2} гр. \n", txtShape, nameSplitRemove(v.getId()), v.getLengthAngle());
            }
        }

        //Информация об треугольниках
        for (TreangleName t : treangleNames) {
            if (t != null) {
                txtShape = MessageFormat.format("{0}{1}{2} \n", txtShape, STA_21, nameSplitRemove(t.getID()));
            }
        }
        //Информация об окружностях
        for (CircleLine p : circleLines)
            if (p != null) {
                txtShape = MessageFormat.format("{0}{1}{2} \n", txtShape, STA_29, p.getRadius());
            }
        //Передать в View для вывода
        notifyObservers("TextShapeGo");
    }

    /**
     * Метод createNameShapes().
     * Предназначен для создания объекта хранения имени геометрической фигуру.
     * Привязка свойств мышки к объекту.
     *
     * @return - возвращает созданный объект
     */
    Text createNameShapes(String name) {
        Text nameText = new Text();//создать новый объект
        nameText.setId(name);//присвоить имя
        nameText.setFont(Font.font("Alexander", FontWeight.BOLD, FontPosture.REGULAR, 14));
        nameText.setFill(Color.BLUE);//цвет букв

        //Привязка к событию мышки
        nameText.setOnMouseDragged(e -> {//перемещение
            //Найти точку с которой связано имя
            Circle circle = findCircle(nameText.getId());
            if (circle != null) {//проверить, выбрани имя точки или линии
                if (nameText.xProperty().isBound()) {//проверить на связь
                    textUnBindCircle(nameText);//снять связь для перемещения
                }
                //Максимальное расстояние при перемещении от точки
                double maxRadius = distance(e.getX(), e.getY(), circle.getCenterX(), circle.getCenterY());
                if (maxRadius < 80) {
                    //Перемещаем имя точки
                    nameText.setX(e.getX());
                    nameText.setY(e.getY());
                }
                nameUpdateXY(nameText.getId());//обновляем данные коллекции
                //устанавливаем связь с точкой
                textBindCircle(circle, nameText, (int) (nameText.getX() - circle.getCenterX()), (int) (nameText.getY() - circle.getCenterY()));
            }
            //Если выбрано имя линии
            Line line = findLines(nameText.getId());
            if (line != null) {
                //Перемещаем имя точки
                nameText.setX(e.getX());
                nameText.setY(e.getY());
                nameUpdateXY(nameText.getId());//обновляем данные коллекции
            }
        });
        //Наведение мышки на объект
        nameText.setOnMouseEntered(e -> nameText.setCursor(Cursor.HAND));
        //Уход мышки с объекта
        nameText.setOnMouseExited(e -> nameText.setCursor(Cursor.DEFAULT));
        return nameText;
    }

    /**
     * Метод nameUpdateXY(String id).
     * Предназначен для обновления мировых координат и расстояния до точки при
     * перемещении объекта Text. Вызывается из метода createNameShapes(String name).
     *
     * @param id - строка имя объекта Text.
     */
    private void nameUpdateXY(String id) {
        //Найти точку в коллекции
        Circle circle = findCircle(id);
        if (circle != null) {
            for (NamePoindLine np : namePoindLines) {
                if (np != null) {
                    if (np.getId().equals(id)) {
                        np.setDX(gridViews.revAccessX(np.getText().getX()) - gridViews.revAccessX(circle.getCenterX()));
                        np.setDY(gridViews.revAccessY(np.getText().getY()) - gridViews.revAccessY(circle.getCenterY()));
                        np.setX(gridViews.revAccessX(circle.getCenterX()));
                        np.setY(gridViews.revAccessY(circle.getCenterY()));
                    }
                }
            }
        }
        Line line = findLines(id);//Объект имя линии
        if (line != null) {
            for (NamePoindLine np : namePoindLines) {
                if (np != null) {
                    if (np.getText().getId().equals(id)) {
                        np.setX(decartX);
                        np.setY(decartY);
                    }
                }
            }

        }
    }

    /**
     * Метод nameCircleAdd().
     * Предназначен для добавления объекта Text связанного с именем точки.
     * Вызывается из метода createPoindAdd() при добавлении точек.
     *
     * @param circle - объект точка.
     */
    private void nameCircleAdd(Circle circle) {
        Text textCircle = createNameShapes(circle.getId());//создать объект текст (имя точки)
        //Добавить в коллекцию NamePoindLine
        namePoindLines.add(new NamePoindLine(textCircle, circle.getId(), -1, 1, gridViews.revAccessX(circle.getCenterX()), gridViews.revAccessY(circle.getCenterY()), showPoindName, showLineName, showAngleName, "poind"));
        textCircle.setText(circle.getId());//Имя для вывода на доску
        textX = circle.getCenterX() - 20;//место вывода Х при создании
        textY = circle.getCenterY() + 20;//место вывода Y при создании
        textCircle.setVisible(showPoindName);//показывать не показывать, зависит от меню "Настройка"
        //Передать для вывода в View
        textGo = textCircle;
        notifyObservers("TextGo");
        //Добавить в коллекцию объектов на доске
        paneBoards.getChildren().add(textCircle);
        //Односторонняя связь точки с именем объекта для перемещения
        textBindCircle(circle, textCircle, -20, 20);
    }

    /**
     * Метод nameArcAdd()
     * Предназначен для добавления имени угла на доске.
     *
     * @param circle - объект вершина угла
     * @param s      - имя угла на греческом
     * @param arc    - объект угол
     */
    private void nameArcAdd(Circle circle, String s, Arc arc) {
        Text textAngle = createNameShapes(s);//создать объект текст (имя угла)
        //Добавить в коллекцию NamePoindLine
        textAngle.setText(s);//Имя для вывода на доску
        Point2D arcXY = nameArcShow(circle, arc, textAngle);//расчитать место буквы
        namePoindLines.add(new NamePoindLine(textAngle, circle.getId(), arcXY.getX(), arcXY.getY(), gridViews.revAccessX(circle.getCenterX()), gridViews.revAccessY(circle.getCenterY()), showPoindName, showLineName, showAngleName, "arc"));
        //Добавить в коллекцию объектов на доске
        paneBoards.getChildren().add(textAngle);
    }

    /**
     * Метод nameArcShow()
     * Предназначен для расчета местоположения имени угла
     *
     * @param circle    - объект вершина угла
     * @param arc       - объект угол
     * @param textAngle - объект текст (имя угла)
     * @return - смещение координат для имени от вершины угла
     */
    private Point2D nameArcShow(Circle circle, Arc arc, Text textAngle) {
        double x = 15 * Math.cos(Math.toRadians(arc.getStartAngle() + arc.getLength() / 2));
        double y = 15 * Math.sin(Math.toRadians(arc.getStartAngle() + arc.getLength() / 2));
        Point2D arcXY = new Point2D(x, y);
        textX = circle.getCenterX() + x;//место вывода Х при создании
        textY = circle.getCenterY() - y;//место вывода Y при создании
        textAngle.setVisible(showAngleName);//показывать не показывать, зависит от меню "Настройка"
        //Передать для вывода в View
        textGo = textAngle;
        notifyObservers("TextGo");
        return arcXY;
    }

    /**
     * Метод findNameText().
     * Предназначен для поиска в коллекции NamePoindLine объекта Text связанного с именем точки.
     *
     * @param circle - объект точка
     * @return - объект Text
     */
    private Text findNameText(Circle circle) {

        for (NamePoindLine np : namePoindLines) {
            if (np != null) {
                if (np.getId().equals(circle.getId())) {
                    return np.getText();
                }
            }
        }
        return null;
    }

    /**
     * Метод findID(Line line)
     * Предназначен для поиска имени линии по объекту линия.
     *
     * @param line - объект линия
     * @return имя линии
     */
    public String findID(Line line) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(line.getId())) {
                    return p.getId();
                }
            }
        }
        return null;
    }

    /**
     * Метод nameLineAdd().
     * Предназначен для добавления имен к прямой и лучам.
     * Вызывается из контролера onMousePressed() при добавлении луча и прямой.
     *
     * @param line - объект линия
     */
    public void nameLineAdd(Line line) {
        //Создать текстовый объект
        Text nameLine = createNameShapes(line.getId());
        //Вызвать метод расчета координат перпендикуляра к середине линии
        nameLineRatchet(line, nameLine);
        namePoindLines.add(new NamePoindLine(nameLine, line.getId(), 0, 0, gridViews.revAccessX(textX), gridViews.revAccessY(textY), showPoindName, showLineName, showAngleName, "line"));
        //Связать линию с именем
        nameBindLines(line, nameLine);
        //Добавить в коллекцию объектов на доске
        paneBoards.getChildren().add(nameLine);
    }

    /**
     * Метод nameLineRatchet().
     * Предназначен для расчета координат места названия линии.
     *
     * @param line - объект линия
     */
    private void nameLineRatchet(Line line, Text nameLine) {
        //Найти точки на линии
        String[] sVer = Objects.requireNonNull(findID(line)).split("_");
        //Найти середину линии
        double aX = findCircle(sVer[0]).getCenterX();
        double aY = findCircle(sVer[0]).getCenterY();
        double bX = findCircle(sVer[1]).getCenterX();
        double bY = findCircle(sVer[1]).getCenterY();
        Point2D mP = midPoindAB(new Point2D(aX, aY), new Point2D(bX, bY));
        double cX = mP.getX();
        double cY = mP.getY();
        //Расчитать координаты перпендикуляр от середины линии на растоянии 15рх
        double dlina = sqrt((pow((aX - bX), 2)) + (pow((aY - bY), 2)));
        textX = cX - 15 * ((aY - bY) / dlina);//место вывода Х при создании
        textY = cY + 15 * ((aX - bX) / dlina);//место вывода Y при создании
        nameLine.setText(line.getId());//Имя для вывода на доску
        nameLine.setVisible(showLineName);//показывать не показывать, зависит от меню "Настройка"
        //Передать для вывода в View
        textGo = nameLine;
        notifyObservers("TextGo");
    }

    /**
     * Метод nameBindLines().
     * Предназначен для связывания имени линии с началом и концом линии.
     * Для задания перемещения имени луча и прямой.
     *
     * @param line     - объект линия.
     * @param nameLine - объект текст.
     */
    private void nameBindLines(Line line, Text nameLine) {
        line.startXProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
        line.startYProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
        line.endYProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
        line.endXProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
    }

    /**
     * Метод createPoindAdd()
     * Предназначен для создания точек и вывод на доску.
     * Для создания точки вызывается метод createPoind().
     *
     * @return новая точка
     */
    Circle createPoindAdd(boolean bMove) {
        vertex = createPoind();//Создать точку
        notifyObservers("VertexGo");//передать в View для вывода на экран
        //Добавить имя на доску
        nameCircleAdd(vertex);
        //добавить в коллекцию точек
        poindCircles.add(new PoindCircle(vertex, vertex.getId(), decartX, decartY, bMove, false, 0, null, 0.0, false));
        //Добавить в правую часть доски
        setTxtShape("");
        txtAreaOutput();
        //Привязать создаваемую точку к любой линии геометрических фигур.
        if (poindAdd && poindLineAdd) {
            double tX = (vertex.getCenterX() - line.getStartX()) / (line.getEndX() - line.getStartX());
            double tY = (vertex.getCenterY() - line.getStartY()) / (line.getEndY() - line.getStartY());
            double t = (tX + tY) / 2;

            for (PoindCircle p : poindCircles) {
                if (p != null) {
                    if (p.getCircle().getId().equals(vertex.getId())) {
                        p.setLine(line);
                        p.setT(t);
                        p.setBLine(true);
                        //circlesBindOnLine(newPoind,line,t);
                    }
                }
            }
        }
        return vertex;//возвращает точку
    }

    /**
     * Метод createPoind()
     * Предназначен для создания точек в виде кругов, а также привязке событий к данным точкам.
     * Определяет основные свойства объекта.
     *
     * @return точку.
     */
    Circle createPoind() {
        Circle newPoind = new Circle();
        newPoind.setRadius(radiusPoind);
        newPoind.setFill(circleColorFill);
        newPoind.setStroke(circleColorStroke);
        newPoind.setId(indexPoindAdd());//Индификатор узла

        //Обработка событий
        //Перемещение с нажатой клавишей
        newPoind.setOnMouseDragged(e -> {
            if (!createShape) {
                if (findPoindCircleMove(newPoind.getId())) {
                    //Найти по точке имя в коллекции
                    Text txt = findNameText(newPoind);
                    if (!Objects.requireNonNull(txt).xProperty().isBound()) {//проверить на связь, если нет связать
                        textBindCircle(newPoind, txt, (int) (txt.getX() - newPoind.getCenterX()), (int) (txt.getY() - newPoind.getCenterY()));//если нет, связать
                    }
                    //если точка принадлежит параллельной прямой, пересчитать координаты второй точки о линии
                    //изменить местоположение точки
                    newPoind.setCenterX(e.getX());
                    newPoind.setCenterY(e.getY());
                    //добавить новые координаты в коллекцию PoindCircle
                    findCirclesUpdateXY(newPoind.getId(), decartX, decartY);
                    //добавить новые координаты в коллекцию PoindLine
                    findLineUpdateXY(newPoind.getId());
                    //Добавить новые данные коллекцию VertexArc
                    findArcUpdate(newPoind.getId());
                    //Обновить координаты точки в правой части
                    setTxtShape("");
                    txtAreaOutput();
                } else {
                    setStringLeftStatus(STA_8);
                    notifyObservers("LeftStatusGo");
                }
            }
        });
        //Нажатие клавиши
        newPoind.setOnMousePressed(e -> {
            selectCircle(newPoind);
            //Проверить разрешено ли взять эту точку. Если расчетная, то запрещено
            if (findPoindAddMove(newPoind) || angleAdd) {
                poindOldAdd = true;//взять эту точку для отрезка
                timeVer = newPoind;//сохранить выбранную точку для построения
                //Вызвать метод для увелечения счетчика index в коллекции PoindCircles
                if (removeObject) { //Режим для удаления
                    removePoindAdd(newPoind);
                }
            } else {
                stringLeftStatus = STA_19;
                notifyObservers("LeftStatusGo");
            }
        });
        //Наведение на точку
        newPoind.setOnMouseEntered(e -> {
            newPoind.setCursor(Cursor.HAND);
            newPoind.setRadius(12);
            Stop[] stops = new Stop[]{
                    new Stop(0.0, Color.BLUE), new Stop(1.0, Color.WHITE)
            };
            newPoind.setFill(new RadialGradient(0.0, 0.0, 0.5, 0.5, 0.5, true,
                    CycleMethod.NO_CYCLE, stops));
            //Установить статус "Точка + выбранная точка"
            setStringLeftStatus(STA_9 + newPoind.getId());
            notifyObservers("LeftStatusGo");
        });
        //Отпускание кнопки
        newPoind.setOnMouseReleased(e -> {
        });
        //Уход с точки
        newPoind.setOnMouseExited(e -> {
            newPoind.setCursor(Cursor.DEFAULT);
            newPoind.setRadius(5);
            poindOldAdd = false;//запрет брать точку для отрезков, прямых, лучей
            //Установить статус пустая строка
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
        });
        newPoind.setOnMouseClicked(e -> {

        });

        return newPoind;//завершено создание новой точки
    }

    /**
     * Метод findPoindAddMove(Circle c).
     * Предназначен для проверки на возможность выбрать точку для геометрической фигуры.
     * Если точка расчетная, то её выбрать запрещено по условия связывания.
     *
     * @return - логическое значение bMove
     */
    private boolean findPoindAddMove(Circle c) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(c.getId())) {
                    return p.isBMove();
                }
            }
        }
        return false;//всегда запрещено
    }

    /**
     * Метод removePoindAdd(Circle c)
     * Ищет выбранную точку в коллекции PoindCircle и удаляет.
     * Вызывается из метода createPoind() из события onMousePressed().
     *
     * @param c -объект точка
     */
    private void removePoindAdd(Circle c) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(c.getId())) {
                    if (p.getIndex() == 0) {//индекс = 0 удаляем, иначе уменьшаем индекс и удаляем связанные фигуры
                        Text txt = findNameText(p.getCircle());//найти имя объекта
                        if (Objects.requireNonNull(txt).xProperty().isBound()) {//проверить на связь
                            textUnBindCircle(txt);//отменить связь
                        }
                        paneBoards.getChildren().remove(txt);//удаление имени с доски
                        removeNameText(txt);//удаление имени объекта их коллекции
                        paneBoards.getChildren().remove(p.getCircle());//удаление объекта с доски
                        poindCircles.remove(p);//удаление объекта точка из коллекции
                        removeObject = false;//сбросить статус удаления
                        //Вывод информации об объектах в правую часть доски
                        setTxtShape("");
                        txtAreaOutput();
                    } else {
                        removeObject = false;
                    }
                    return;

                }
            }
        }
    }

    /**
     * Метод removeNameText(Text txt).
     * Предназначен для удаления имени точки. Вызывается из метода removePoindAdd(Circle c).
     *
     * @param txt -объект имя точки
     */
    private void removeNameText(Text txt) {
        for (NamePoindLine pn : namePoindLines) {
            if (pn != null) {
                if (txt.getId().equals(pn.getId())) {
                    namePoindLines.remove(pn);
                    return;
                }
            }
        }
    }

    /**
     * Метод indexAdd(Circle c)
     * Метод для увелечения счетчика, когда точка принадлежит разным геометрическим фигурам.
     *
     * @param circle - объект точка
     */
    public void indexAdd(Circle circle) {
        for (PoindCircle c : poindCircles) {
            if (c != null) {
                if (c.getId().equals(circle.getId())) {
                    c.setIndex(c.getIndex() + 1);
                }
            }
        }
    }

    /**
     * Метод createCircle().
     * Предназначен для создания новой окружности и подключения событий мышки.
     *
     * @return circle - возвращает созданную окружность
     */
    Circle createCircle() {
        Circle newCircle = new Circle(screenX, screenY, 0, Color.TRANSPARENT);
        newCircle.setStroke(Color.CHOCOLATE);
        newCircle.setStrokeWidth(2.0);
        newCircle.setId(indexLineAdd());//добавить имя окружности
        //привязать события мышки
        //При наведении
        newCircle.setOnMouseEntered(e -> {
            newCircle.setCursor(Cursor.HAND);
            //Установить статус "Окружность"
            setStringLeftStatus(STA_29 + findCircleRadius(newCircle));
            notifyObservers("LeftStatusGo");
        });
        //При уходе
        newCircle.setOnMouseExited(e -> {
            newCircle.setCursor(Cursor.DEFAULT);
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
        });
        //При перемещении с нажатой кнопкой
        newCircle.setOnMouseDragged(e -> {
            if (!createShape) {
                Circle c = findCircle(findNameCircle(newCircle));
                setRadiusCircle(Math.round(distance(c.getCenterX(), c.getCenterY(), getScreenX(), getScreenY())));
                setRadiusCircleW(Math.round(distance(gridViews.revAccessX(c.getCenterX()), gridViews.revAccessY(c.getCenterY()), getDecartX(), getDecartY())));
                updateCircle(newCircle);
                circleView(newCircle);
                //Добавить в правую часть доски
                setTxtShape("");
                txtAreaOutput();
            }
        });
        return newCircle;
    }

    public void updateCircle(Circle c) {
        for (CircleLine p : circleLines) {
            if (p != null) {
                if (p.getId().equals(c.getId())) {
                    p.setRadius(getRadiusCircleW());
                    p.setCircle(c);
                    p.setX(gridViews.revAccessX(c.getCenterX()));
                    p.setY(gridViews.revAccessY(c.getCenterY()));
                }

            }
        }
    }

    /**
     * Метод findCircleRadius(Circle c).
     * Возвращает радиус окружности из коллекции
     *
     * @param c - ссылка на окружность
     * @return - радиус окружности
     */
    double findCircleRadius(Circle c) {
        for (CircleLine p : circleLines) {
            if (p != null) {
                if (p.getCircle().getId().equals(c.getId())) {
                    return p.getRadius();
                }
            }
        }
        return 0;
    }

    /**
     * Метод findCircleRadiusW(Circle c).
     * Возвращает радиус окружности в мировых координатах
     *
     * @param c - ссылка на окружность
     * @return - радиус окружности в мировых координатах
     */
    double findCircleRadiusW(Circle c) {
        for (CircleLine p : circleLines) {
            if (p != null) {
                if (p.getCircle().getId().equals(c.getId())) {
                    return p.getCircle().getRadius();
                }
            }
        }
        return 0;
    }

    /**
     * Метод indNameCircle(Circle c).
     * Возвращает имя окружности
     *
     * @param c - ссылка на окружность
     * @return - имя окружности
     */
    String findNameCircle(Circle c) {
        for (CircleLine p : circleLines) {
            if (p != null) {
                if (p.getId().equals(c.getId())) {
                    return p.getPoindID();
                }
            }
        }
        return null;
    }

    /**
     * Метод bindPoindCircle(Circle poind, Circle circle).
     * Предназначен для связывания центра окружности с окружностью для перемещения
     *
     * @param poind  - ссылка на центр окружности
     * @param circle - ссылка на окружность
     */
    public void bindPoindCircle(Circle poind, Circle circle) {
        poind.centerXProperty().addListener((obj, oldValue, newValue) -> {
            setRadiusCircle(findCircleRadiusW(circle));
            circle.setCenterX(poind.getCenterX());
            circle.setCenterY(poind.getCenterY());
            circle.setRadius(getRadiusCircle());
            updateCircle(circle);//обновить коллекцию
        });
        poind.centerYProperty().addListener((obj, oldValue, newValue) -> {
            setRadiusCircle(findCircleRadiusW(circle));
            circle.setCenterX(poind.getCenterX());
            circle.setCenterY(poind.getCenterY());
            circle.setRadius(getRadiusCircle());
            updateCircle(circle);//обновить коллекцию
        });
    }

    /**
     * Метод circleView().
     * Предназначен для передачи на вывод окружности классу View.
     */
    public void circleView(Circle c) {
        circle = c;
        notifyObservers("CircleGo");

    }

    /**
     * Метод createCircleAdd(Circle name).
     * Предназначен для добавления окружности на доску.
     *
     * @param name - объект центр окружности
     * @return - объект окружность
     */
    Circle createCircleAdd(Circle name) {
        Circle circle = createCircle();
        circleLines.add(new CircleLine(circle, gridViews.revAccessX(circle.getCenterX()), gridViews.revAccessY(circle.getCenterY()), circle.getId(), circle.getRadius(), name.getId()));
        paneBoards.getChildren().add(circle);//добавить окружность на доску
        circle.toBack();
        bindPoindCircle(findCircle(findNameCircle(circle)), circle);
        return circle;
    }

    /**
     * Метод createLine(int seg).
     * Предназначен для создания линий отрезков, лучей, прямых, сторон треугольника, медиан, биссектрис, высот.
     *
     * @param seg - определяет для коллекции, к какому объекту будет принадлежать линия.
     * @return - возвращает новый объект Line.
     */
    Line createLine(int seg) {
        line = new Line();
        line.setStrokeWidth(lineStokeWidth);//Толщина линии
        //Вид линии по умолчанию -0. Задается в переменных
        switch (inDash) {
            case 0 -> Collections.addAll(arrDash, 2.0);
            case 1 -> Collections.addAll(arrDash, 15.0, 5.0);
            case 2 -> Collections.addAll(arrDash, 5.0, 4.0, 5.0, 4.0, 5.0);
            case 3 -> Collections.addAll(arrDash, 2.0, 10.0);
            case 4 -> Collections.addAll(arrDash, 10.0, 4.0, 10.0);
        }
        line.getStrokeDashArray().addAll(arrDash); //Задаем вид линии
        //Цвет линии задается переменной ColorLine
        notifyObservers("ColorLine"); //Передаем в View для вывода
        if (seg == 0 || seg == 3) {//Отрезок или треугольник, определяем координаты
            segmentStartX = screenX;//сохраняем координаты мышки, для первой точки
            segmentStartY = screenY;
        } else {
            rayEndX = screenX;
            rayEndY = screenY;
        }
        line.setId(indexLineAdd());//Идентификатор узла
        //Привязка событий мышки
        mouseLine(line);
        return line;
    }

    /**
     * Метод mouseLine().
     * Предназначен для привязки событий мышки к объекту Line.
     *
     * @param newLine - ссылка на линию к которой привязаны события мышки
     */
    public void mouseLine(Line newLine) {
        //Перемещение линий
        newLine.setOnMouseDragged(e -> {
            //Блокировать, если режим построения
            if (!createShape) {
                //Определить, разрешено ли перемещение линии
                if (findLineMove(newLine)) {
                    String[] nameId = findID(newLine).split("_");
                    Circle A = findCircle(nameId[0]);
                    Circle B = findCircle(nameId[1]);
                    A.setCenterX(e.getX() + getDXStart());
                    A.setCenterY(e.getY() + getDYStart());
                    B.setCenterX(e.getX() + getDXEnd());
                    B.setCenterY(e.getY() + getDYEnd());
                    //добавить новые координаты в коллекцию PoindCircle
                    findCirclesUpdateXY(A.getId(), gridViews.revAccessX(A.getCenterX()), gridViews.revAccessY(A.getCenterY()));
                    findCirclesUpdateXY(B.getId(), gridViews.revAccessX(B.getCenterX()), gridViews.revAccessY(B.getCenterY()));
                    //добавить новые координаты в коллекцию PoindLine
                    findLineUpdateXY(newLine.getId());
                    updatePoindLine(newLine);
                    setTxtShape("");
                    txtAreaOutput();
                } else {
                    stringLeftStatus = STA_30;
                    notifyObservers("LeftStatusGo");
                }
            }
        });
        //Наведение на отрезок
        newLine.setOnMouseEntered(e -> {
            newLine.setCursor(Cursor.HAND);
            poindAdd = true;
            //Установить статус
            for (PoindLine p : poindLines) {
                if (p.getLine().getId().equals(newLine.getId())) {
                    switch (p.getSegment()) {
                        case 0 -> {
                            setStringLeftStatus(STA_10 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 1 -> {
                            setStringLeftStatus(STA_11 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 2 -> {
                            setStringLeftStatus(STA_12 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 3 -> {
                            setStringLeftStatus(STA_17 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 4 -> {
                            setStringLeftStatus(STA_20 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 5 -> {
                            setStringLeftStatus(STA_23 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 6 -> {
                            setStringLeftStatus(STA_25 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 7 -> {
                            setStringLeftStatus(STA_27 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                    }
                }
            }
            newLine.setStrokeWidth(selectStrokeWidth);
        });
        //уход с линии
        newLine.setOnMouseExited(e -> {
            poindAdd = false;
            //Установить статус
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
            newLine.setStrokeWidth(lineStokeWidth);
            lineOldAdd = false;
        });
        //нажата левая кнопка
        newLine.setOnMousePressed(e -> {
            timeLine = newLine;//выбрана данная линия, для построения
            lineOldAdd = true;//линия выбрана
            //Определить, разрешено ли перемещение линии
            if (findLineMove(newLine)) {
                //Вычислить смещение для перемещения всех линий
                if (!createLine) {
                    String[] nameId = findID(newLine).split("_");
                    setDXStart(findCircle(nameId[0]).getCenterX() - e.getX());
                    setDYStart(findCircle(nameId[0]).getCenterY() - e.getY());
                    setDXEnd(findCircle(nameId[1]).getCenterX() - e.getX());
                    setDYEnd(findCircle(nameId[1]).getCenterY() - e.getY());
                }
            }
        });
    }

    /**
     * Метод updatePoindLine(Line line).
     * Предназначен для обновления мировых координат при перемещении линии
     *
     * @param line - ссылка на линию
     */
    private void updatePoindLine(Line line) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (line.getId().equals(p.getLine().getId())) {
                    String[] namePoind = p.getId().split("_");
                    Circle a = findCircle(namePoind[0]);
                    Circle b = findCircle(namePoind[1]);
                    findCirclesUpdateXY(a.getId(), gridViews.revAccessX(a.getCenterX()), gridViews.revAccessY(a.getCenterY()));
                    findCirclesUpdateXY(b.getId(), gridViews.revAccessX(b.getCenterX()), gridViews.revAccessY(b.getCenterY()));
                    findLineUpdateXY(a.getId());
                    findLineUpdateXY(b.getId());
                }
            }
        }

    }

    /**
     * Метод indLineMove(Line line).
     * Предназначен для поиска разрешения на перемещение линии.
     *
     * @param line - ссылка на линию
     * @return - true - перемещение разрешено, false - перемещение запрещено
     */
    private boolean findLineMove(Line line) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(line.getId())) {
                    return p.isBMove();
                }
            }
        }
        return false;
    }

    /**
     * Метод  rayAddLine(Line newLine, int seg).
     * Предназначен для расчета окончания луча.
     *
     * @param newLine - ссылка на линию
     * @param seg     - тип линии
     */
    public void rayAddLine(Line newLine, int seg) {
        //Расчитать координаты окончания луча
        double x, y, x1, y1;
        x = getRayEndX() + (getScreenX() - getRayEndX()) * 3;
        y = getRayEndY() + (getScreenY() - getRayEndY()) * 3;
        x1 = getRayEndX() + (getScreenX() - getRayEndX()) * -3;
        y1 = getRayEndY() + (getScreenY() - getRayEndY()) * -3;
        //Добавить координаты пересчета в коллекцию
        setRayStartX(x);
        setRayStartY(y);
        //Пересчет координат в мировые
        setVerLineStartX(gridViews.revAccessX(x));
        setVerLineStartY(gridViews.revAccessY(y));
        if (seg == 2) {
            setRayEndX(x1);
            setRayEndY(y1);
            setVerLineEndX(gridViews.revAccessX(x1));
            setVerLineEndY(gridViews.revAccessY(y1));
        } else {
            setVerLineEndX(gridViews.revAccessX(getRayEndX()));
            setVerLineEndY(gridViews.revAccessY(getRayEndY()));
        }
        //Передать в View для вывода
        setLine(newLine);
        notifyObservers("RayGo");
        findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
    }

    /**
     * Метод createLineAdd(int segment).
     * Предназначен для создания линий. Вызывает метод createLine(segment). Добавляет линию на доску и в коллекцию.
     *
     * @param segment - определяет тип линии в коллекции
     * @return -возвращает объект Line.
     */
    Line createLineAdd(int segment) {
        Line newLine = createLine(segment);//добавить линию
        poindLines.add(new PoindLine(newLine, newLine.getId(), decartX, decartY, decartX, decartY, true, false, segment));
        return newLine;
    }

    /**
     * Метод lineAddPoind(Line nl, boolean poindAdd2).
     * Предназначен для приклеивания конца линии к лежащим точкам.
     *
     * @param nl        - объект линия.
     * @param poindAdd2 - режим построения второй точки для линии.
     */
    public void lineAddPoind(Line nl, boolean poindAdd2) {
        Circle pCl;
        for (PoindCircle c : poindCircles) {
            if (c != null && nl != null && poindAdd2) {
                pCl = c.getCircle();
                double d = distance(pCl.getCenterX(), pCl.getCenterY(), screenX, screenY);
                if (d < 15) {
                    poindOldAdd = true;
                    screenX = pCl.getCenterX();
                    screenY = pCl.getCenterY();
                    //Передать в View для вывода
                    line = nl;
                    notifyObservers("SideGo");

                } else {
                    setPoindOldAdd(false);
                }
            }
        }
    }

    /**
     * Метод distance(double x1, double y1, double x2, double y2).
     * Предназначен для расчета расстояния между двумя вершинами, заданные координатами.
     *
     * @param x1 - координаты вершины x1
     * @param y1 - координаты вершины y1
     * @param x2 - координаты вершины x2
     * @param y2 - координаты вершины y2
     * @return возвращает длину.
     */
    public double distance(double x1, double y1, double x2, double y2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
    }

    /**
     * Метод midPoindAB(Point2D p1,Point2D p2).
     * Предназначен для расчета координат середины между указанными точками.
     *
     * @param p1 - координаты первой точки
     * @param p2 - координаты второй точки
     * @return - возвращает точку с координатами середины между указанными точками
     */
    public Point2D midPoindAB(Point2D p1, Point2D p2) {
        return p1.midpoint(p2);
    }

    /**
     * Метод treangleAdd(Point2D v1, Point2D v2, Point2D v3)
     * Предназначен для создания треугольников.
     *
     * @param v1 - вершина А
     * @param v2 - вершина В
     * @param v3 - вершина С
     */
    public Polygon treangleAdd(Point2D v1, Point2D v2, Point2D v3, String nameTr) {
        Polygon treangle = new Polygon();
        treangle.getPoints().addAll(v1.getX(), v1.getY(), v2.getX(), v2.getY(), v3.getX(), v3.getY());
        treangle.setFill(Color.CHOCOLATE);
        treangle.setOpacity(0.2);
        String[] vertex = nameTr.split("_");
        Circle c1 = findCircle(vertex[0]);
        Circle c2 = findCircle(vertex[1]);
        Circle c3 = findCircle(vertex[2]);
        polygonBindCircles(c1, c2, c3, treangle);
        treangleNames.add(new TreangleName(treangle, nameTr));
        //привязать событие мыши
        treangle.setOnMouseEntered(e -> {
            stringLeftStatus = STA_21 + nameSplitRemove(nameTr);
            notifyObservers("LeftStatusGo");
        });
        treangle.setOnMouseExited(e -> {
            stringLeftStatus = " ";
            notifyObservers("LeftStatusGo");
        });
        return treangle;
    }


    /**
     * Метод findNameId(String poindA, String poindB, String linaA)
     * Предназначен для поиска по коллекции PoindLine для замены имени отрезка в коллекции
     *
     * @param poindA - вершина А
     * @param poindB - вершина В
     * @param linaA  - линия а
     */
    public void findNameId(String poindA, String poindB, String linaA) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getId().equals(linaA)) {
                    p.setId(poindA + "_" + poindB);
                }
            }
        }
    }

    /**
     * Метод findCircle(String c)
     * Предназначен для поиска в коллекции точек объектов Circle по имени.
     * Вызывается из метода createNameShapes() при создании объекта text, для перемещения.
     *
     * @param c - имя точки
     * @return объект Circle или null если не найден.
     */
    Circle findCircle(String c) {
        for (PoindCircle c0 : poindCircles) {
            if (c0 != null) {
                if (c0.getId().equals(String.valueOf(c))) {
                    return c0.getCircle();
                }
            }
        }
        return null;//ничего не найдено
    }

    /**
     * Метод findLine().
     * Предназначен для поиска линии в коллекции PoindLIne.
     * Вызывается из метода createNameShapes() при создании объекта text, для перемещения.
     *
     * @param s - имя линии
     * @return - возвращает объект или null
     */
    Line findLines(String s) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(s)) {
                    return p.getLine();
                }
            }
        }
        return null;
    }

    /**
     * Метод findCirclesUpdateXY(String id).
     * Предназначен для поиска объектов в коллекции PoindCircle по имени и
     * замены мировых координат
     *
     * @param id - имя объекта
     * @param x  - мировые координаты точки
     * @param y  -мировые координаты точки
     */
    public void findCirclesUpdateXY(String id, double x, double y) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setX(x);//меняем координаты X
                    p.setY(y);//меняем координаты Y
                }
            }
        }
    }


    /**
     * Метод findLinesUpdateXY(String id).
     * Предназначен для замены мировых координат при построении отрезков, лучей и прямых
     *
     * @param id - имя линии до замены
     *           Особенность метода, должен всегда вызываться до метода findNameId(Circle1, Circle2, Line), который
     *           меняет имя в коллекции PoindLines.
     */
    public void findLinesUpdateXY(String id) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                    p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                    p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                    p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                }
            }
        }

    }

    /**
     * Метод findLineUpdateXY(String s).
     * Предназначен для обновления мировых координат в коллекции PoindLine после
     * перемещения одной из точек.
     *
     * @param s - имя точки перемещения
     */
    public void findLineUpdateXY(String s) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                String name = p.getId();
                String[] chName = name.split("_");
                //Обновляем начало отрезка, луча, прямой
                if (s.equals(chName[0])) {
                    //Обновляем координаты
                    p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                    p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                    if (p.getSegment() == 1) {
                        p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                        p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                    }
                    //Обновляем координаты прямой
                    if (p.getSegment() == 2) {
                        p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                        p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                    }
                }
                if (s.equals(chName[1])) {
                    p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                    p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                    p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                    p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                }
                //Обновляем координаты прямой
                if (p.getSegment() == 2) {
                    p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                    p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                    p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                    p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                }
            }
        }
    }

    /**
     * Метод findPoindCircleMove(String id).
     * Предназначен для поиска по коллекции PoindCircle по имени точки разрешения на
     * перемещение точки по доске.
     *
     * @param id - имя точки
     * @return true- перемещение точки разрешено, false - точка расчетная, перемещение запрещено
     */
    private boolean findPoindCircleMove(String id) {
        boolean bfMove = false;//всегда запрещено
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    bfMove = p.isBMove();//определяется в коллекции
                }
            }
        }
        return bfMove;
    }

    /**
     * Метод createVertexAdd(String arc).
     * Предназначен для создания нового объекта дуги, которая задается в виде
     * трех вершин состоящих из точек. Для расчета параметров дуги и вывода на доску
     * вызывается метод arcVertex(o1,o2,o3,arcNew), о1, о2, о3 - точки вершин из класса Circle.
     * arcNew - новая дуга.
     *
     * @param arc - срока для добавления дуги, состоит из имен вершин, угол АВС
     * @return - возвращает объект дугу
     */
    Arc createVertexAdd(String arc) {
        String[] arcChar = arc.split("_");
        Circle o1 = findCircle(arcChar[0]);
        Circle o2 = findCircle(arcChar[1]);
        Circle o3 = findCircle(arcChar[2]);
        Arc arcNew = new Arc();
        arcNew.setId(String.valueOf(arcChar[1]));
        arcNew.setType(ArcType.ROUND);//тип арки
        arcNew.setOpacity(0.5);//прозрачность
        //Передать в View для вывода
        arcGo = arcNew;
        notifyObservers("ColorArc");//задаем цвет арки дуги и цвет фона
        arcVertexGo(o1, o2, o3, arcNew);//вывести на доску
        String nameAngle = indexAngledAdd();//увеличить индекс
        nameArcAdd(o2, nameAngle, arcNew);//вывести имя угла
        //добавить в коллекцию дуг
        vertexArcs.add(new VertexArc(arcNew, arc, nameAngle, gridViews.revAccessX(o2.getCenterX()), gridViews.revAccessY(o2.getCenterY()), arcRadius,
                arcRadius, angleStart, angleLength, false));
        //При наведении мышки на дугу, вывод статусной строки
        arcNew.setOnMouseEntered(e -> {
            //Установить статус "Угол + выбранный угол + длина дуги в градусах"
            setStringLeftStatus(STA_16 + arcNew.getId() + " = " + arcNew.getLength() + " гр.");
            notifyObservers("LeftStatusGo");
        });
        //При выходе мышки из дуги, сбросить статусную стоку.
        arcNew.setOnMouseExited(e -> {
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
        });
        return arcNew;
    }

    /**
     * Метод arcVertex(Circle o1, Circle o2, Circle o3, double r).
     * Предназначен для расчета угла по координатам трех точек. Построения дуги, перемещения дуги.
     *
     * @param o1 -первая точка А
     * @param o2 - вторая точка В (центр угла)
     * @param o3 - третья точка С
     *           <p>
     *           Устанавливает, для класса View, следующие переменные:
     *           angleLength - длину дуги в градусах
     *           arcRadius - радиус дуги
     *           angleStart - начальный угол в градусах
     *           screenX screenY - координаты центра дуги
     */
    public void arcVertex(Circle o1, Circle o2, Circle o3) {
        //Длина дуги в градусах
        Point2D pA = new Point2D(o1.getCenterX(), o1.getCenterY());
        Point2D pB = new Point2D(o2.getCenterX(), o2.getCenterY());
        Point2D pC = new Point2D(o3.getCenterX(), o3.getCenterY());
        double angleABC = angleTriangle(pB, pA, pC);//размер угла в градусах
        angleLength = angleABC;
        //Начальный угол в
        double arcStart = angleVector(o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        double str = areaTriangle(o2.getCenterX() + 200, o2.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        double str1 = areaTriangle(o1.getCenterX(), o1.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        if (str < 0) {
            arcStart = 360 - arcStart;
        }
        if (str1 > 0) {
            arcStart = arcStart - angleABC;
        }
        angleStart = arcStart;
    }

    /**
     * Метод arcVertexGo(Circle o1, Circle o2, Circle o3, Arc arc).
     * Предназначен для вывода на экран арки угла.
     *
     * @param o1  - первая вершина А
     * @param o2  - вторая вершина В
     * @param o3  - третья вершина С
     * @param arc - угол АВС
     */
    public void arcVertexGo(Circle o1, Circle o2, Circle o3, Arc arc) {
        arcVertex(o1, o2, o3);//расчитать угол
        //Запомнить текущие координаты мышки
        double stX = screenX;
        double stY = screenY;
        //Заменить для построения арки угла
        screenX = o2.getCenterX();
        screenY = o2.getCenterY();
        //Передать в View для вывода
        arcGo = arc;
        notifyObservers("ArcGo");
        //Восстановить текущие координаты мышки
        screenX = stX;
        screenY = stY;
    }

    /**
     * Метод findArcUpdate(String s)
     * Предназначен для поиска дуги по вершине угла и изменения в коллекции дуг
     * мировых координат после перемещения дуги.
     *
     * @param s - вершина угла
     */
    private void findArcUpdate(String s) {
        for (VertexArc v : vertexArcs) {
            if (v != null) {
                String[] arcChar = v.getId().split("_");
                if (v.getArc().getId().equals(s)) {
                    v.setCenterX(gridViews.revAccessX(screenX));
                    v.setCenterY(gridViews.revAccessY(screenY));
                    v.setStartAngle(v.getArc().getStartAngle());
                    v.setLengthAngle(v.getArc().getLength());
                } else if (s.equals(arcChar[0])) {
                    v.setCenterX(gridViews.revAccessX(v.getArc().getCenterX()));
                    v.setCenterY(gridViews.revAccessY(v.getArc().getCenterY()));
                    v.setStartAngle(v.getArc().getStartAngle());
                    v.setLengthAngle(v.getArc().getLength());
                } else if (s.equals(arcChar[2])) {
                    v.setCenterX(gridViews.revAccessX(v.getArc().getCenterX()));
                    v.setCenterY(gridViews.revAccessY(v.getArc().getCenterY()));
                    v.setStartAngle(v.getArc().getStartAngle());
                    v.setLengthAngle(v.getArc().getLength());
                }
            }
        }
    }

    /**
     * Метод angleTriangle(Point2D p1, Point2D p2, Point2D p3).
     * Предназначен для расчета угла по координатам вершин.
     *
     * @param p1 - координаты центральной вершины
     * @param p2 - координаты первой боковой вершины
     * @param p3 - координаты второй боковой вершины
     * @return угол в градусах с точностью до десятых градуса.
     */
    private double angleTriangle(Point2D p1, Point2D p2, Point2D p3) {
        return Math.round(p1.angle(p2, p3));
    }

    /**
     * Метод angleVector(double X, double Y, double X1, double Y1).
     * Предназначен для определения угла наклона вектора.
     *
     * @param X  -координата начала вектора
     * @param Y  - координата начала вектора
     * @param X1 -координата конца вектора
     * @param Y1 координата конца вектора
     * @return - возвращает угол наклона вектора
     */
    private double angleVector(double X, double Y, double X1, double Y1) {
        Point2D p1 = new Point2D(100, 0);
        Point2D p2 = new Point2D(X1 - X, Y1 - Y);
        return p1.angle(p2);
    }

    /**
     * Метод areaTriangle(double x1, double y1, double x2, double y2, double x3, double y3).
     * Предназначен для расчета площади треугольника по координатам трех вершин
     *
     * @param x1 - координаты вершины x1
     * @param y1 - координаты вершины y1
     * @param x2 - координаты вершины x2
     * @param y2 - координаты вершины y2
     * @param x3 - координаты вершины x3
     * @param y3 - координаты вершины y3
     * @return возвращает площадь треугольника.
     */
    private double areaTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        return ((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)) / 2;
    }

    /**
     * Метод lineBindCircles(Circle c1, Circle c2, Line l)
     * Метод двунаправленного связывания точек начала и конца линии с самой линией.
     * Используется при создании отрезков и треугольников.
     *
     * @param c1 - точка начала отрезка
     * @param c2 - точка конца отрезка
     * @param l  - линия между этими точками
     */
    public void lineBindCircles(Circle c1, Circle c2, Line l) {
        l.startXProperty().bindBidirectional(c1.centerXProperty());
        l.startYProperty().bindBidirectional(c1.centerYProperty());
        l.endXProperty().bindBidirectional(c2.centerXProperty());
        l.endYProperty().bindBidirectional(c2.centerYProperty());
    }

    /**
     * Метод verticalBindCircles(Circle c, Line l)
     * Предназначен для связывания перпендикуляра с прямой, перемещается только точка
     *
     * @param c1 - объект точка из которой опущен перпендикуляр
     * @param c2 - объект точка на прямой
     * @param c3 - объект точка на прямой
     * @param c4 - объект точка пересечения перпендикуляра с прямой
     * @param l  - объект линия перпендикуляр
     */
    public void verticalBindCircles(Circle c1, Circle c2, Circle c3, Circle c4, Line l) {
        l.startXProperty().bindBidirectional(c1.centerXProperty());
        l.startYProperty().bindBidirectional(c1.centerYProperty());
        l.startYProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        l.startXProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c2.centerXProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c2.centerYProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c3.centerXProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c3.centerYProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));

    }

    /**
     * Метод verticalUpdateCircle(Circle c1, Circle c2,Circle c3, Circle c4, Line l)
     * Вспомогательный метод для пересчета точки пересечения перпендикуляра с прямой.
     * Вызывается из метода verticalBindCircles().
     *
     * @param c1 - объект точка из которой опущен перпендикуляр
     * @param c2 - объект точка на прямой
     * @param c3 - объект точка на прямой
     * @param c4 - объект точка пересечения перпендикуляра с прямой
     * @param l  - объект линия перпендикуляр
     */
    private void verticalUpdateCircle(Circle c1, Circle c2, Circle c3, Circle c4, Line l) {
        Point2D A1 = new Point2D(c1.getCenterX(), c1.getCenterY());
        Point2D B1 = new Point2D(c2.getCenterX(), c2.getCenterY());
        Point2D C1 = new Point2D(c3.getCenterX(), c3.getCenterY());
        Point2D D1 = heightPoind(A1, B1, C1);
        l.setEndX(D1.getX());
        l.setEndY(D1.getY());
        c4.setCenterX(D1.getX());
        c4.setCenterY(D1.getY());
        //Обновляем мировые координаты в коллекциях
        findMedianaUpdateXY(c4, l);
    }

    /**
     * Метод textBindCircle(Circle c, Text txt, int dx, int dy).
     * Предназначен для связывания место расположения надписи с точкой.
     *
     * @param c   - объект круг.
     * @param txt - объект имя точки.
     * @param dx  - смещения имени от центра точки.
     * @param dy  - смещение имени от центра точки.
     */
    private void textBindCircle(Circle c, Text txt, int dx, int dy) {
        txt.xProperty().bind(c.centerXProperty().add(dx));
        txt.yProperty().bind(c.centerYProperty().add(dy));
    }

    /**
     * Метод parallelBindLine(Circle c, Circle d, int dx, int dy).
     * Предназначен для связывания прямой с параллельной прямой.
     *
     * @param b - точка на прямой начало
     * @param c - точка на прямой, конец вс-прямая относительно которой построена параллельная прямая
     * @param a - точка через которую проходит параллельная прямая
     * @param d - точка на параллельной прямой расчетная
     */
    public void parallelBindLine(Circle b, Circle c, Circle a, Circle d) {
        c.centerXProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = a.getCenterX() - b.getCenterX();
            double Dy = a.getCenterY() - b.getCenterY();
            d.setCenterX(c.getCenterX() + Dx);
            d.setCenterY(c.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(d.centerXProperty().get()), gridViews.revAccessY(d.centerYProperty().get()));

        });
        c.centerYProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = a.getCenterX() - b.getCenterX();
            double Dy = a.getCenterY() - b.getCenterY();
            d.setCenterX(c.getCenterX() + Dx);
            d.setCenterY(c.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(d.centerXProperty().get()), gridViews.revAccessY(d.centerYProperty().get()));

        });
        b.centerXProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = a.getCenterX() - b.getCenterX();
            double Dy = a.getCenterY() - b.getCenterY();
            d.setCenterX(c.getCenterX() + Dx);
            d.setCenterY(c.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(d.centerXProperty().get()), gridViews.revAccessY(d.centerYProperty().get()));

        });
        b.centerYProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = a.getCenterX() - b.getCenterX();
            double Dy = a.getCenterY() - b.getCenterY();
            d.setCenterX(c.getCenterX() + Dx);
            d.setCenterY(c.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(d.centerXProperty().get()), gridViews.revAccessY(d.centerYProperty().get()));
        });
        a.centerXProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = a.getCenterX() - b.getCenterX();
            double Dy = a.getCenterY() - b.getCenterY();
            d.setCenterX(c.getCenterX() + Dx);
            d.setCenterY(c.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(d.centerXProperty().get()), gridViews.revAccessY(d.centerYProperty().get()));

        });
        a.centerYProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = a.getCenterX() - b.getCenterX();
            double Dy = a.getCenterY() - b.getCenterY();
            d.setCenterX(c.getCenterX() + Dx);
            d.setCenterY(c.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(d.centerXProperty().get()), gridViews.revAccessY(d.centerYProperty().get()));
        });
        d.centerXProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = d.getCenterX() - c.getCenterX();
            double Dy = d.getCenterY() - c.getCenterY();
            a.setCenterX(b.getCenterX() + Dx);
            a.setCenterY(c.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(a.centerXProperty().get()), gridViews.revAccessY(a.centerYProperty().get()));

        });
        d.centerYProperty().addListener((obj, OldValue, newValue) -> {
            double Dx = d.getCenterX() - c.getCenterX();
            double Dy = d.getCenterY() - c.getCenterY();
            a.setCenterX(b.getCenterX() + Dx);
            a.setCenterY(b.getCenterY() + Dy);
            findCirclesUpdateXY(d.getId(), gridViews.revAccessX(a.centerXProperty().get()), gridViews.revAccessY(a.centerYProperty().get()));
        });

    }

    /**
     * Метод textUnBindCircle(Text txt).
     * Предназначен для отключения связи имени точки с текстом.
     *
     * @param txt - объект имя точки.
     */
    private void textUnBindCircle(Text txt) {
        txt.xProperty().unbind();
        txt.yProperty().unbind();
    }

    /**
     * Метод rayBindCircles(Circle cStart, Circle cEnd, Line ray)
     * Метод создания двунаправленного связывания точки начала луча и начала линии,
     * а также однонаправленного связывания второй точки на луче с окончанием линии.
     *
     * @param cStart - точка начала луча
     * @param cEnd   - вторая точка на луче
     * @param ray    -прямая от начала луча через вторую точку
     *               Вызывает метод rayLineX(cStart, cEnd) и rayLineY(cStart, cEnd) для расчета окончания прямой при построении.
     */
    public void rayBindCircles(Circle cStart, Circle cEnd, Line ray) {
        ray.startXProperty().bindBidirectional(cStart.centerXProperty());
        ray.startYProperty().bindBidirectional(cStart.centerYProperty());
        //Расчет конца луча
        ray.startYProperty().addListener((obj, oldValue, newValue) -> ray.setEndY(rayLineY(cStart, cEnd)));
        ray.startXProperty().addListener((obj, oldValue, newValue) -> ray.setEndX(rayLineX(cStart, cEnd)));
        //Точка на луче
        cEnd.centerXProperty().addListener((obj, oldValue, newValue) -> ray.setEndX(rayLineX(cStart, cEnd)));
        cEnd.centerYProperty().addListener((obj, oldValue, newValue) -> ray.setEndY(rayLineY(cStart, cEnd)));
    }

    /**
     * Метод polygonBindCircles(Circle c1, Circle c2, Circle c3, Polygon treangle).
     * Предназначен для однонаправленного связывания точек треугольника с вершинами
     * многоугольника.
     *
     * @param c1       - вершина треугольника
     * @param c2       - вершина треугольника
     * @param c3       - вершина треугольника
     * @param treangle - многоугольник в форме треугольника
     */
    private void polygonBindCircles(Circle c1, Circle c2, Circle c3, Polygon treangle) {
        c1.centerXProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(0, c1.getCenterX()));
        c1.centerYProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(1, c1.getCenterY()));
        c2.centerXProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(2, c2.getCenterX()));
        c2.centerYProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(3, c2.getCenterY()));
        c3.centerXProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(4, c3.getCenterX()));
        c3.centerYProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(5, c3.getCenterY()));
    }

    /**
     * Метод arcBindPoind(String s, Arc a)
     * Метод двунаправленного связывания центра угла с точкой, а также однонаправленного связывания
     * двух точек угла с расчетными размерами угла. А также связывания имени угла.
     * Для расчета размеров угла вызывается метод из класса Model arcVertex(Circle1, Circle2, Circle3, Arc)
     *
     * @param s   - строка содержащая название угла, типа АВС (А и С - боковые точки, В - центральная точка угла
     * @param arc - объект арка дуги угла.
     */
    public void arcBindPoind(String s, Arc arc) {
        String[] arcChar = s.split("_");
        Circle c1 = findCircle(arcChar[0]);
        Circle c2 = findCircle(arcChar[1]);
        Circle c3 = findCircle(arcChar[2]);
        c2.centerXProperty().bindBidirectional(arc.centerXProperty());
        c2.centerYProperty().bindBidirectional(arc.centerYProperty());
        arc.centerXProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);//новый угол
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
        });
        arc.centerYProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);//новый угол
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
        });
        c1.centerXProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
        });
        c3.centerXProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
        });
    }

    /**
     * Метод findArcNameAngle()
     * Предназначен для поиска имени угла по вершине. Вызывается из метода связывания угла.
     *
     * @param id - имя вершины
     * @return - объект текст (имя угла)
     */
    private Text findArcNameAngle(String id) {
        String nAngle;
        for (VertexArc p : vertexArcs) {
            if (p != null) {
                if (p.getArc().getId().equals(id)) {
                    nAngle = p.getNameAngle();
                    for (NamePoindLine n : namePoindLines) {
                        if (n != null) {
                            if (n.getText().getId().equals(nAngle)) {
                                return n.getText();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод findTypeLine(Line line).
     * Предназначен для поиска в коллекции и возвращения типа линии
     * Необходим для проверки при построении середины отрезка
     *
     * @param line - ссылка на линию
     * @return - тип линии, нужно 0 - отрезок
     */
    public int findTypeLine(Line line) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(line.getId())) {
                    return p.getSegment();
                }
            }
        }
        return -1;
    }

    /**
     * Метод circlesBindLine(Circle cStart, Circle cEnd, Line l)
     * Метод однонаправленного связывания двух точек на прямой с прямой и расчетом начала и конца прямой.
     * Для расчета начала и конца прямой вызываются методы:
     * rayLineX() и rayLineY()
     *
     * @param cStart - первая точка на прямой
     * @param cEnd   - вторая точка на прямой
     * @param line   - прямая
     */
    public void circlesBindLine(Circle cStart, Circle cEnd, Line line) {
        //Точка на прямой
        cEnd.centerXProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndX(rayLineX(cStart, cEnd));
            line.setStartX(rayLineX(cEnd, cStart));
        });
        cEnd.centerYProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndY(rayLineY(cStart, cEnd));
            line.setStartY(rayLineY(cEnd, cStart));
        });
        cStart.centerXProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndX(rayLineX(cStart, cEnd));
            line.setStartX(rayLineX(cEnd, cStart));
        });
        cStart.centerYProperty().addListener((obj, oldValue, newValue) -> {
            line.setEndY(rayLineY(cStart, cEnd));
            line.setStartY(rayLineY(cEnd, cStart));
        });
    }

    /**
     * Метод rayLineX(Circle c1, Circle c2)
     * Метод для расчета по параметрическому уравнению прямой координат начала и конца для прямой,
     * окончания линии для луча для координаты Х.
     *
     * @param c1 - объект первая точка
     * @param c2 - объект вторая точка
     * @return - координата Х
     */
    double rayLineX(Circle c1, Circle c2) {
        return c1.getCenterX() + (c2.getCenterX() - c1.getCenterX()) * 3;
    }

    /**
     * Метод rayLineY(Circle c1, Circle c2)
     * Метод для расчета по параметрическому уравнению прямой координат начала и конца для прямой,
     * окончания линии для луча для координаты Y.
     *
     * @param c1 - объект первая точка
     * @param c2 - объект вторая точка
     * @return - координата Y
     */
    double rayLineY(Circle c1, Circle c2) {
        return c1.getCenterY() + (c2.getCenterY() - c1.getCenterY()) * 3;
    }

    private void selectCircle(Circle dot) {
        //Если выбран существующий объект
        if (selected == dot) return;
        //Создан новый объект, для старого поменять цвет
        if (selected != null) selected.setFill(Color.DARKSLATEBLUE);
        selected = dot;//и установить выбранный круг
        //Если объект существует
        if (selected != null) {
            selected.requestFocus(); //установить на него фокус
            selected.setFill(Color.RED);//поменять цвет
        }
    }

    /**
     * Метод createMedianaBisectorHeight(Circle c, Circle c1, Circle c2,Point2D mc, int i).
     * Предназначен для построения отрезка медианы, биссектрисы, высоты и точки на противолежащей стороне от вершины,
     * из которой проводится медиана
     *
     * @param c  - объект вершина из которой проводится медиана, биссектриса и высота.
     * @param c1 - объект боковая вершина угла.
     * @param c2 - объект боковая вершина угла.
     * @param mc - объект точка расчетная для медианы, биссектрисы и высоты.
     * @param i  - номер объекта в коллекции PoindCircle (4- медиана, 5 - биссектриса, 6 - высота)
     */
    private Line createMedianaBisectorHeight(Circle c, Circle c1, Circle c2, Point2D mc, int i) {
        Line newLineTreangle = createLineAdd(i);//создать новую линию
        Circle newPoindTreangle = createPoindAdd(false);//создать новую расчетную точку
        screenX = mc.getX();
        screenY = mc.getY();
        //Передать в View для вывода
        vertex = newPoindTreangle;
        notifyObservers("VertexGo");
        findCirclesUpdateXY(newPoindTreangle.getId(), gridViews.revAccessX(screenX), gridViews.revAccessY(screenY));
        segmentStartX = c.getCenterX();
        segmentStartY = c.getCenterY();
        line = newLineTreangle;
        notifyObservers("SideGo");
        findLinesUpdateXY(newLineTreangle.getId());
        paneBoards.getChildren().addAll(newLineTreangle, newPoindTreangle);//добавить на доску
        newLineTreangle.toFront();
        findNameId(c.getId(), newPoindTreangle.getId(), newLineTreangle.getId());
        //Связывание созданных отрезков и точки с вершинами треугольника
        switch (i) {
            case 4 -> mbhBindCircles(c, c1, c2, newPoindTreangle, newLineTreangle, 4);
            case 5 -> mbhBindCircles(c, c1, c2, newPoindTreangle, newLineTreangle, 5);
            case 6 -> mbhBindCircles(c, c1, c2, newPoindTreangle, newLineTreangle, 6);
        }
        return newLineTreangle;
    }

    /**
     * Метод mbh(Circle c, Circle c1, Circle c2, Circle md, Line lm, int nl).
     * Предназначен для связывания медианы, биссектрисы и высоты с вершинами треугольника.
     *
     * @param c  - объект вершина треугольника
     * @param c1 - объект вершина треугольника
     * @param c2 - объект вершина треугольника
     * @param md - объект точка пересечения биссектрисы со стороной треугольника
     * @param lm - отрезок биссектриса
     * @param nl - код точки (4- медиана, 5 - биссектриса, 6 - высота)
     */
    private void mbhBindCircles(Circle c, Circle c1, Circle c2, Circle md, Line lm, int nl) {
        c.centerXProperty().bindBidirectional(lm.startXProperty());
        c.centerYProperty().bindBidirectional(lm.startYProperty());

        c.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = midPoindAB(p1, p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = midPoindAB(p1, p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });

        c1.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = midPoindAB(p1, p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c1.centerYProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = midPoindAB(p1, p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c2.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = midPoindAB(p1, p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c2.centerYProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = midPoindAB(p1, p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
    }

    /**
     * Метод findMedianaUpdateXY(Circle md, Line lm).
     * Предназначен для обновления мировых координат точки и линии окончания медианы в коллекциях.
     *
     * @param md - объект точка медианы.
     * @param lm - объект линия медианы.
     */
    private void findMedianaUpdateXY(Circle md, Line lm) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getCircle().getId().equals(md.getId())) {
                    p.setX(gridViews.revAccessX(md.getCenterX()));
                    p.setY(gridViews.revAccessY(md.getCenterY()));
                }
            }
        }
        for (PoindLine pl : poindLines) {
            if (pl != null) {
                if (pl.getLine().getId().equals(lm.getId())) {
                    pl.setEnX(gridViews.revAccessX(lm.getEndX()));
                    pl.setEnY(gridViews.revAccessY(lm.getEndY()));
                }
            }
        }
    }

    /**
     * Метод mbhLineAdd(Circle c)
     * Предназначен для проведения медианы, биссектрисы и высоты треугольника.
     *
     * @param c  - вершина треугольника, из которой надо провести высоту
     * @param nl - код линии: 4-медиана 5-биссектриса 6- высота
     * @return - объект медиана, биссектриса или высота.
     */
    public Line mbhLineAdd(Circle c, int nl) {
        Line newHeight = null;
        Point2D mc;
        for (TreangleName tn : treangleNames) {
            if (tn != null) {
                String[] vertex = tn.getID().split("_");
                if (c.getId().equals(vertex[0])) {
                    Circle c1 = findCircle(vertex[1]);
                    Circle c2 = findCircle(vertex[2]);
                    Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                    Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                    Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
                    switch (nl) {
                        case 4 -> mc = midPoindAB(p1, p2);
                        case 5 -> mc = bisectorPoind(p1, p3, p2);
                        case 6 -> mc = heightPoind(p3, p2, p1);
                        default -> throw new IllegalStateException("Неопределенно значение: " + nl);
                    }
                    newHeight = createMedianaBisectorHeight(c, c1, c2, mc, nl);
                } else if (c.getId().equals(vertex[1])) {
                    Circle c1 = findCircle(vertex[0]);
                    Circle c2 = findCircle(vertex[2]);
                    Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                    Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                    Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
                    switch (nl) {
                        case 4 -> mc = midPoindAB(p1, p2);
                        case 5 -> mc = bisectorPoind(p1, p3, p2);
                        case 6 -> mc = heightPoind(p3, p2, p1);
                        default -> throw new IllegalStateException("Неопределенно значение: " + nl);
                    }
                    newHeight = createMedianaBisectorHeight(c, c1, c2, mc, nl);
                } else if (c.getId().equals(vertex[2])) {
                    Circle c1 = findCircle(vertex[0]);
                    Circle c2 = findCircle(vertex[1]);
                    Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                    Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                    Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
                    switch (nl) {
                        case 4 -> mc = midPoindAB(p1, p2);
                        case 5 -> mc = bisectorPoind(p1, p3, p2);
                        case 6 -> mc = heightPoind(p3, p2, p1);
                        default -> throw new IllegalStateException("Неопределенно значение: " + nl);
                    }
                    newHeight = createMedianaBisectorHeight(c, c1, c2, mc, nl);
                }
            }
        }
        return newHeight;
    }

    /**
     * Метод bisectorPoind(Point2D pA, Point2D pB, Point2D pC).
     * Предназначен для определения координат пересечения биссектрисы со стороной треугольника.
     *
     * @param pA - вершина треугольника.
     * @param pB - вершина треугольника из которой проведена биссектриса.
     * @param pC - вершина треугольника.
     * @return - возвращает координаты точки пересечения.
     */
    private Point2D bisectorPoind(Point2D pA, Point2D pB, Point2D pC) {
        double ra = pA.distance(pB) / pC.distance(pB);
        double dX = (pA.getX() + ra * pC.getX()) / (1 + ra);
        double dY = (pA.getY() + ra * pC.getY()) / (1 + ra);
        return new Point2D(dX, dY);
    }

    /**
     * Метод heightPoind(Point2D p1, Point2D p2, Point2D p3)
     * Предназначен для определения координаты точки пересечения высоты со стороной треугольника.
     * А также нахождения точки пересечения перпендикуляра с прямой.
     * Вызывается из метода heightAdd(Circle c) - добавить высоту.
     *
     * @param p1 - координаты вершины А
     * @param p2 - координаты вершины В
     * @param p3 - координаты вершины С
     * @return - возвращает координаты точки пересечения высоты треугольника из вершины А к стороне ВС.
     */
    public Point2D heightPoind(Point2D p1, Point2D p2, Point2D p3) {
        double a1 = p3.getY() - p2.getY();
        double b1 = p2.getX() - p3.getX();
        double c1 = p2.getX() * p3.getY() - p3.getX() * p2.getY();
        double c2 = -p1.getX() * (p3.getX() - p2.getX()) + p1.getY() * (p2.getY() - p3.getY());
        //Вычисление главного определителя
        double o = -pow(a1, 2) - pow(b1, 2);
        return new Point2D((-c1 * a1 - c2 * b1) / o, (a1 * c2 - b1 * c1) / o);
    }

    /**
     * Метод closeLine(Line newLine).
     * Предназначен для запрета линий на перемещение от мышки.
     * К этим линиям относятся высота, медиана, биссектриса треугольника.
     *
     * @param newLine - ссылка на линию
     */
    public void closeLine(Line newLine) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(newLine.getId())) {
                    p.setBMove(false);
                }
            }
        }

    }

    //Тестовый метод для вывода информации по коллекциям
    public void ColTest() {
        //Взято из книги Кэн Коузен "Современный Java. Рецепты программирования".
        //Глава 2. Пакет java.util.function
        // стр.40 Пример 2.3
        System.out.println("Коллекция PoindCircle");
        poindCircles.forEach(System.out::println);//ссылка на метод

        System.out.println("Коллекция PoindLine");
        poindLines.forEach(System.out::println);//лямбда выражение

        System.out.println("Коллекция дуг");
        vertexArcs.forEach(System.out::println);

        System.out.println("Коллекция имен");
        namePoindLines.forEach(System.out::println);

        System.out.println("Коллекция треугольников");
        treangleNames.forEach(System.out::println);

        System.out.println("Коллекция окружностей");
        circleLines.forEach(System.out::println);

        System.out.println("Коллекция объектов");
        paneBoards.getChildren().forEach(System.out::println);
    }

    /**
     * Метод middleBindSegment(Circle newCircle, Line newLine).
     * Предназначен для связывания середины отрезка с линией
     *
     * @param newCircle - ссылка на точку
     * @param newLine   - ссылка на линию
     */
    public void middleBindSegment(Circle newCircle, Line newLine) {
        newLine.startXProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(newLine.startXProperty().get(), newLine.startYProperty().get());
            Point2D p2 = new Point2D(newLine.endXProperty().get(), newLine.endYProperty().get());
            newCircle.setCenterX(midPoindAB(p1, p2).getX());
            newCircle.setCenterY(midPoindAB(p1, p2).getY());
            findCirclesUpdateXY(newCircle.getId(), gridViews.revAccessX(newCircle.getCenterX()), gridViews.revAccessY(newCircle.getCenterY()));
        });
        newLine.startYProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(newLine.startXProperty().get(), newLine.startYProperty().get());
            Point2D p2 = new Point2D(newLine.endXProperty().get(), newLine.endYProperty().get());
            newCircle.setCenterX(midPoindAB(p1, p2).getX());
            newCircle.setCenterY(midPoindAB(p1, p2).getY());
            findCirclesUpdateXY(newCircle.getId(), gridViews.revAccessX(newCircle.getCenterX()), gridViews.revAccessY(newCircle.getCenterY()));
        });
        newLine.endXProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(newLine.startXProperty().get(), newLine.startYProperty().get());
            Point2D p2 = new Point2D(newLine.endXProperty().get(), newLine.endYProperty().get());
            newCircle.setCenterX(midPoindAB(p1, p2).getX());
            newCircle.setCenterY(midPoindAB(p1, p2).getY());
            findCirclesUpdateXY(newCircle.getId(), gridViews.revAccessX(newCircle.getCenterX()), gridViews.revAccessY(newCircle.getCenterY()));
        });
        newLine.endYProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(newLine.startXProperty().get(), newLine.startYProperty().get());
            Point2D p2 = new Point2D(newLine.endXProperty().get(), newLine.endYProperty().get());
            newCircle.setCenterX(midPoindAB(p1, p2).getX());
            newCircle.setCenterY(midPoindAB(p1, p2).getY());
            findCirclesUpdateXY(newCircle.getId(), gridViews.revAccessX(newCircle.getCenterX()), gridViews.revAccessY(newCircle.getCenterY()));
        });
    }
}