package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class MainController {
    @FXML public Button btn_wifi;
    @FXML public Button btn_head;
    @FXML  public TextArea tf_log;

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

    public void onRemove(MouseEvent mouseEvent) {
        logger.log(Level.INFO,"rm app");
        String appName = "KrobotCartoonBook";
        try {
          Process p  =  Runtime.getRuntime().exec("adb remount && adb shell rm system/app/" + appName + ".apk");
            InputStream is  =  p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder temp = new StringBuilder();;
            while((line = reader.readLine())!= null){


                temp.append(line);
                tf_log.setText(temp.toString());
                System.out.println(line);

            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    public void onLeft(MouseEvent mouseEvent) {
        Input(21);
    }


    public void onPushApp(MouseEvent mouseEvent) {

    }

    public void onShowPackages(MouseEvent mouseEvent) throws IOException, InterruptedException {
        logger.log(Level.INFO,"show packages app");

        String out = adbRun("adb shell ls system/app");
        tf_log.setText(out);

    }

    private String adbRun(String command) throws IOException, InterruptedException {
        return adbRun(command,null);
    }

    public String adbRun(String command, File dir) throws IOException, InterruptedException {
        Process p  =  Runtime.getRuntime().exec(command,null,dir);
        InputStream is  =  p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder temp = new StringBuilder();;
        while((line = reader.readLine())!= null){
            temp.append("\n").append(line);
            System.out.println(line);
        }
        p.waitFor();
        is.close();
        reader.close();
        p.destroy();
        return  temp.toString();
    }


    public static String dateStump() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void onPushLog(MouseEvent mouseEvent) throws IOException, InterruptedException {
        String date = MainController.dateStump();
        File dir = new File("C://Users//fdx//Desktop//logcat",date);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        adbRun("adb pull data/krobot/logcat",dir);

    }
}
