package sample;

//Класс контролер для equality.fxml

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import static ContstantString.StringWeb.*;

public class Equality extends View {

    @FXML
    public Group groupTriangles;
    public Button bntCombine;
    public Text namePr, proof, theorem, proposition_1, proposition_2;
    public Arc arcA1;
    public Arc arcA;
    public Line hatch2_2;
    public Line hatch2_1;
    public Arc arcB1_1;
    public Arc arcB1_2;
    public Polygon treagle1;
    public Group groupTr2;
    public Group groupTr3;
    public Group hatchB, hatchB1;
    public Text txtC1;
    public Text txtA1;
    public Text poindC;
    public Text poindB1;
    public Text poindB;
    @FXML
    private Arc arcB_1,arcB_2;
    @FXML
    private Line hatch_1, hatch_2;

    @FXML
    private void initialize(){
    //1 признак равенства
        if(model.getWindShow()==0) {
           arcB_1.setVisible(false);
           arcB_2.setVisible(false);
           arcB1_1.setVisible(false);
           arcB1_2.setVisible(false);
           arcA1.setVisible(true);
           arcA.setVisible(true);
           hatch_1.setVisible(true);
           hatch_2.setVisible(true);
           hatch2_1.setVisible(true);
           hatch2_2.setVisible(true);
           hatchB.setVisible(false);
           hatchB1.setVisible(false);
           poindC.setVisible(true);
           namePr.setText("Первый признак равенства треугольников.");
           model.setTextX(220);
           model.setTextY(30);
           model.TextGo(namePr);
           proof.setText(TR_PROOF_ONE);
           model.TextGo(proof);
           theorem.setText(TR_THEOREM_ONE);
           model.TextGo(theorem);
           proposition_1.setText(TR_PROPOSITION_1_ONE);
           model.TextGo(proposition_1);
           proposition_2.setText(TR_PROPOSITION_2_ONE);
           model.TextGo(proposition_2);
       }
        //2 признак равенства
        if(model.getWindShow()==1) {
            arcB_1.setVisible(true);
            arcB_2.setVisible(true);
            arcB1_1.setVisible(true);
            arcB1_2.setVisible(true);
            arcA1.setVisible(true);
            arcA.setVisible(true);
            hatch_1.setVisible(false);
            hatch_2.setVisible(false);
            hatch2_1.setVisible(false);
            hatch2_2.setVisible(false);
            hatchB.setVisible(false);
            hatchB1.setVisible(false);
            poindC.setVisible(true);
            namePr.setText("Второй признак равенства треугольников.");
            model.setTextX(220);
            model.setTextY(30);
            model.TextGo(namePr);
            proof.setText(TR_PROOF_TWO);
            model.TextGo(proof);
            theorem.setText(TR_THEOREM_TWO);
            model.TextGo(theorem);
            proposition_1.setText(TR_PROPOSITION_1_TWO);
            model.TextGo(proposition_1);
            proposition_2.setText(TR_PROPOSITION_2_TWO);
            model.TextGo(proposition_2);
        }
        //3 признак равенства
        if(model.getWindShow()==2) {
            arcB_1.setVisible(false);
            arcB_2.setVisible(false);
            arcB1_1.setVisible(false);
            arcB1_2.setVisible(false);
            arcA1.setVisible(false);
            arcA.setVisible(false);
            hatch_1.setVisible(true);
            hatch_2.setVisible(true);
            hatch2_1.setVisible(true);
            hatch2_2.setVisible(true);
            hatchB.setVisible(true);
            hatchB1.setVisible(true);
            namePr.setText("Третий признак равенства треугольников.");
            model.setTextX(220);
            model.setTextY(30);
            model.TextGo(namePr);
            proof.setText(TR_PROOF_THREE);
            model.TextGo(proof);
            theorem.setText(TR_THEOREM_THREE);
            model.TextGo(theorem);
            proposition_1.setText(TR_PROPOSITION_1_THREE);
            model.TextGo(proposition_1);
            proposition_2.setText(TR_PROPOSITION_2_THREE);

        }
    }

    //Анимация для совмещения треугольников
    public void onClickCombine() {
        //Перемещениe для 3 признака равенства треугольников
        if(model.getWindShow()==2) {
            //Перенос
            groupTr3.setVisible(false);
            TranslateTransition tr=new TranslateTransition(Duration.seconds(5.0), groupTriangles);
            tr.setFromX(0);
            tr.setFromY(0);
            tr.setToX(242);
            tr.setToY(65);
            tr.setCycleCount(1);
            tr.setInterpolator(Interpolator.LINEAR);
            //Вращение по оси Х
            RotateTransition rtX = new RotateTransition(Duration.seconds(5.0), groupTriangles);
            rtX.setAutoReverse(true);
            rtX.setAxis(Rotate.Y_AXIS);
            rtX.setFromAngle(0.0);
            rtX.setToAngle(180.0);
            rtX.setCycleCount(1);
            rtX.setInterpolator(Interpolator.LINEAR);
            //Вращение по оси Z
            RotateTransition rt2 = new RotateTransition(Duration.seconds(5.0), groupTr2);
            rt2.setAutoReverse(true);
            rt2.setAxis(Rotate.Z_AXIS);
            rt2.setFromAngle(0.0);
            rt2.setToAngle(-82.0);
            rt2.setCycleCount(1);
            rt2.setInterpolator(Interpolator.LINEAR);

            //Паралельное выполнение
            ParallelTransition pt = new ParallelTransition(groupTriangles);
            pt.getChildren().addAll(tr, rtX,rt2);
            pt.play();
            pt.setOnFinished(event -> {
                groupTr3.setVisible(true);
                poindC.setRotate(180);
                poindB.setRotate(180);
                //poindB1.setRotate(80);
            });
        }else {
            //Перемещениe для 1 и 2 признаков равенства треугольников
            TranslateTransition tr=new TranslateTransition(Duration.seconds(5.0), groupTriangles);
            tr.setFromX(0);
            tr.setFromY(0);
            tr.setToX(284);
            tr.setToY(20);
            tr.setCycleCount(1);
            tr.setInterpolator(Interpolator.LINEAR);
            //Поворот
            RotateTransition rt2 = new RotateTransition(Duration.seconds(5.0), groupTriangles);
            rt2.setAutoReverse(true);
            rt2.setFromAngle(0.0);
            rt2.setToAngle(-30.0);
            rt2.setCycleCount(1);
            rt2.setInterpolator(Interpolator.LINEAR);

            //Паралельное выполнение
            ParallelTransition pt = new ParallelTransition(groupTriangles);
            pt.getChildren().addAll(tr, rt2);
            pt.play();

        }
    }
}

