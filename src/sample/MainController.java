package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import utils.DateUtils;

import java.io.*;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class MainController {
    private final String PNAME = "devicesname.properties";
    public Button btn_wifi;
    public Button btn_head;
    public TextArea tf_log;
    public TextField tf_name;
    public BorderPane root_layout;

    private Properties properties = new Properties();
    private String deviceId;
    private String name;

    public MainController() {
        devicesNum = 2;
    }

    @FXML public void initialize() {


        readProperties(null);

    }

    Logger logger = LoggerFactory.getLogger(MainController.class);

    private Main mainApp;
    private String arg1 = " -d ";
    private int devicesNum = 0;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void onVolumeDown(MouseEvent mouseEvent) {
        logger.debug("button use");
            Input(25);
    }

    @FXML
    public void onVolumeUp(MouseEvent mouseEvent) {
        logger.debug("button use");
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

            Runtime.getRuntime().exec(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
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

    public void onKey(KeyEvent keyEvent) {
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
        logger.debug("rm app");
        String appName = "KrobotCartoonBook";
        try {
          Process p  =  Runtime.getRuntime().exec("adb remount && adb shell rm system/app/" + appName + ".apk");
            InputStream is  =  p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder temp = new StringBuilder();
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
        logger.debug("show packages app");

        String out = adbRun("adb shell ls system/app");
//        tf_log.setText(out);

        String out2 = adbRun("adb shell pm list packages | grep -E 'krobot|kingrobot'");
        tf_log.setText("\n ::::" + out2);
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


    public void onPushLog(MouseEvent mouseEvent) throws IOException, InterruptedException {
        String date = DateUtils.dateStump();
        File dir = new File("C://Users//fdx//Desktop//logcat",date);
        if (!dir.exists()) {
           boolean a =  dir.mkdirs();
            boolean b = dir.setExecutable(true);
        }

        adbRun("adb pull data/krobot/logcat",dir);

    }

    public void writeProperties(ActionEvent actionEvent) {

        try {
            File file = new File(PNAME);
            if (file.exists()) {
                properties.clear();
                properties.load(new FileInputStream(file));
            }
            OutputStream outputStream = new FileOutputStream(file);

            boolean debug = false;
            if (debug) {
                Set<String> theKeys = properties.stringPropertyNames();

                if (!theKeys.contains(deviceId)) {
                    name = tf_name.getText();
                    properties.setProperty(name, deviceId);
                }
            }

            name = tf_name.getText();
            properties.setProperty(name, "adbded");
            tf_name.clear();

            properties.setProperty("number", "2015");
            properties.setProperty("song", "ddd");
            if (properties.getProperty("fdx")==null ) {
                System.out.println("null fdx");
                properties.setProperty("fdx", "very bad 2");
            }
            System.out.println("a:"+properties.getProperty("fdx"));
            properties.store(outputStream, "update");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readProperties(ActionEvent actionEvent) {

        try {

            File file = new File(PNAME);
            if (file.exists()) {
                System.out.println(file.getAbsoluteFile());
                InputStream fis = new FileInputStream(file);
                properties.load(fis);
            }
            logger.info(String.valueOf(file.exists()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Set<String> theKeys = properties.stringPropertyNames();
        for (String s : theKeys
                ) {
            if (properties.getProperty(s).equals("303000f10200a6e6bb0a")) {
                logger.info("I have 24");
            }

        }

        System.out.println("number:" + properties.getProperty("number") + ",song:" + properties.getProperty("song"));

        if (tf_log != null) {

            tf_log.setText(properties.getProperty("24"));
        }
    }

    public void resetProperties(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){

            try {
                File file = new File(PNAME);
                if (file.exists()) {
                    properties.clear();
                }
                OutputStream outputStream = new FileOutputStream(file,false);
                properties.setProperty("24", "303000f10200a6e6bb0a");
                properties.store(outputStream, "update");
                logger.info("update");
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void fileChoose(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Window stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) root_layout.getScene().getWindow();
        stage.close();
    }
}
