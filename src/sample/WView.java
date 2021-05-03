package sample;

//Класс перерасчета координат
import lombok.Data;
import java.text.DecimalFormat;

@Data
//Класс перерасчета координат
public class WView {
    private double A, B, C, D ; //Коэффициенты перерасчета
    private double Wt, Wb, Wl, Wr;//Мировые координаты
    private double Vt, Vb, Vl, Vr;//Координаты окна просмотра
    private double M=20;//Сетка по умолчанию 20 рх
    private double k0 =0.2; //Нумерация по умолчанию начинается 1
    private int k1=1; //Первая точка входа для масштабирования
    private int k2=0;//Степень для масштаба
    private double k3=1;//Коэффициент масштабирования
    private double VPx, VPy;//Координаты мыши при нажатии кнопки

    //Конструктор c параметрами
    WView(double Wt, double Wb, double Wl, double Wr, double Vt, double Vb, double Vl, double Vr) {
        this.Wt=Wt;
        this.Wb=Wb;
        this.Wl=Wl;
        this.Wr=Wr;
        this.Vt=Vt;
        this.Vb=Vb;
        this.Vl=Vl;
        this.Vr=Vr;
        //Коэффициенты
        rate();
    }
    //Конструктор по умолчаниию
    WView(){
        Wt=300;
        Wb=-300;
        Wl=-400;
        Wr=400;
        Vt=0;
        Vb=600;
        Vl=0;
        Vr=800;
        rate();


    }
    //Сетка х
    double gridShowX(double tx) {return Math.round((A*tx*M)+C);} //Без масштабирования
    //Сетка Y
    double gridShowY(double ty) {return Math.round((B*ty*M)+D);}

    // Перерасчет координаты X
    double accessX(double tx) {return Math.round((A*tx*M)/(k0*k3)+C);} //С масштабом
    //Перерасчет координаты y
    double accessY(double ty) {return Math.round((B*ty*M)/(k0*k3)+D);}
    //Обратный перерасчет sx
    double revAccessX(double wx) {return Math.round((wx-C)/A)/M*k0*k3;}
    //Обратный перерасчет sy
    double revAccessY(double wy) {return Math.round((wy-D)/B)/M*k0*k3;}//С масштабом
    //Коэффициенты пересчета координат
    void rate(){
        A=(Vr-Vl)/(Wr-Wl);
        C=Vl-A*Wl;
        B=(Vb-Vt)/(Wb-Wt);
        D=Vb-B*Wb;
    }
    //Убираем лишние нули
    public String doubleString(double d){
        DecimalFormat dF = new DecimalFormat( "#.###########" );
        return String.valueOf(dF.format(d));
    }
}
