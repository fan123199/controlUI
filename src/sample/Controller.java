package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.scene.input.KeyCode.*;

public class Controller {
    @FXML public Button btn_wifi;
    @FXML public Button btn_head;

    private Logger logger = Logger.getLogger("hehe");

    private Main mainApp;
    private String arg1 = " -d ";
    private int devicesNum = 0;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void onVolumeDown(MouseEvent mouseEvent) {
        logger.log(Level.INFO, "button use");
            Input(25);
    }

    @FXML
    public void onVolumeUp(MouseEvent mouseEvent) {
        logger.log(Level.INFO, "button use");
        Input(24);
    }

//    private static void Input(KeyCode keyCode){
//        String sKeyCode = keyCode.getName();
//        Input(sKeyCode);
//    }

    private  void Input(int keyCode){
        String sKeyCode = String.valueOf(keyCode);
        this.Input(sKeyCode);
    }
    private  void Input(String keyCode) {
        try {

            StringBuilder sb = new StringBuilder("adb");
            if (devicesNum > 1) {
                sb.append(arg1);
            }
            sb.append(" shell input keyevent ").append(keyCode);

//            Runtime.getRuntime().exec(sb.toString());
            Runtime.getRuntime().exec("adb "+ arg1 +" shell input keyevent " + keyCode);
            System.out.print(1);
        } catch (IOException e) {
            e.printStackTrace(); System.out.print(-1);

        }
    }

    void pushFile(String path, File file) {

        final String tempPath = "/system/app";


        try {
            Runtime.getRuntime().exec("adb "+ arg1 +" shell push " +file.getName()+ " "+ tempPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML public void doLog() {
        logger.log(Level.INFO, "always do it");
    }

    @FXML public void onKey(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case A:
                break;
            case K:
                break;
            case M:
                break;

            //上下左右
            case UP:
                Input(19);
                break;
            case DOWN:
                Input(20);
                break;
            case LEFT:
                Input(21);
                break;
            case RIGHT:
                Input(22);
                break;
            case PLUS:
                Input(24);
                break;
            case MINUS:
                Input(25);
                break;

        }
    }

    public void onHead(MouseEvent mouseEvent) {
        Input(118);
    }
    public void onWifi(MouseEvent mouseEvent) {
        Input(131);
    }
    public void onPhoto(MouseEvent mouseEvent) {
        Input(132);
    }
    public void onVideo(MouseEvent mouseEvent) {
        Input(133);
    }

    @FXML void oneee(TouchEvent dd, int c ,int d) {

    }

    public void onUp(MouseEvent mouseEvent) {
        Input(19);
    }

    public void onOK(MouseEvent mouseEvent) {
        Input(212);
    }

    public void onDown(MouseEvent mouseEvent) {
        Input(20);
    }


    public void onRight(MouseEvent mouseEvent) {
        Input(22);
    }

    public void onLeft(MouseEvent mouseEvent) {
        Input(21);
    }
}
