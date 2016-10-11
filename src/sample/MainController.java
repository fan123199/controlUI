package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import utils.DateUtils;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

import static javafx.scene.input.KeyCode.L;

public class MainController {
    private final String PropetyName = "devicesname.properties";
    public Button btn_wifi;
    public Button btn_head;
    public TextArea tf_log;
    public TextField tf_name;
    public BorderPane root_layout;
    public TitledPane tp_output;
    public Button btn_backward;
    public Button btn_home;
    public ListView<String> lv_what;
    public Button btn_focus_act;
    public Button btn_send_broadcast;

    private Properties properties = new Properties();
    private String deviceId;
    private String name;
    private List<String> allCmd = new ArrayList<>();

    public MainController() {
        devicesNum = 2;
    }

    @FXML
    public void initialize() {
        readProperties(null);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("history.data"));
            String line;
            while ((line = reader.readLine()) != null) {
                allCmd.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Logger logger = LoggerFactory.getLogger(MainController.class);

    private Main mainApp;
    private int devicesNum = 0;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void onVolumeDown(MouseEvent mouseEvent) {
        logger.debug("button use");
        adbInput(25);
    }

    @FXML
    public void onVolumeUp(MouseEvent mouseEvent) {
        logger.debug("button use");
        adbInput(24);
    }

//    private static void adbInput(KeyCode keyCode){
//        String sKeyCode = keyCode.getName();
//        adbInput(sKeyCode);
//    }

    private void adbInput(int keyCode) {
        String sKeyCode = String.valueOf(keyCode);
        this.adbInput(sKeyCode);
    }

    private void adbInput(String keyCode) {
        adbRun("adb shell input keyevent " + keyCode);

    }

    void pushFile(String path, File file) throws IOException, InterruptedException {
        adbRun("adb shell push " + file.getName() + " /system/app");
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
                adbInput(19);
                break;
            case DOWN:
                adbInput(20);
                break;
            case LEFT:
                adbInput(21);
                break;
            case RIGHT:
                adbInput(22);
                break;
            case PLUS:
                adbInput(24);
                break;
            case MINUS:
                adbInput(25);
                break;

        }
    }

    public void onHead(MouseEvent mouseEvent) {
        adbInput(118);
    }

    public void onWifi(MouseEvent mouseEvent) {
        adbInput(131);
    }

    public void onPhoto(MouseEvent mouseEvent) {
        adbInput(132);
    }

    public void onVideo(MouseEvent mouseEvent) {
        adbInput(133);
    }

    public void onUp(MouseEvent mouseEvent) {
        adbInput(19);
    }

    public void onOK(MouseEvent mouseEvent) {
        adbInput(212);
    }

    public void onDown(MouseEvent mouseEvent) {
        adbInput(20);
    }

    public void onRight(MouseEvent mouseEvent) {
        adbInput(22);
    }

    public void onHome(MouseEvent mouseEvent) {
        adbInput(3);
    }

    public void onBackward(MouseEvent mouseEvent) {
        adbInput(4);
    }


    public void onFocusActivity(MouseEvent mouseEvent) {

        String act = adbRun("adb shell dumpsys activity activities | grep -E 'Recent|mFocusedActivity'");
        tf_log.setText(act);

    }

    public void onRemove(MouseEvent mouseEvent) {
        logger.debug("rm app");
        String appName = "KrobotCartoonBook";

        String output = adbRun("adb remount && adb shell rm system/app/" + appName + ".apk");
        tf_log.setText(output);

    }

    public void onLeft(MouseEvent mouseEvent) {
        adbInput(21);
    }


    public void onPushApp(MouseEvent mouseEvent) {

    }

    public void onShowPackages(MouseEvent mouseEvent) throws IOException, InterruptedException {
        logger.debug("show packages app");

        String out = adbRun("adb shell ls system/app");

        String out2 = adbRun("adb shell pm list packages | grep -E 'krobot|kingrobot'");
        tf_log.setText("\n ::::" + out2);
        List<String> myDataStructure = new ArrayList<>();
        String[] c = out2.trim().split("\n\n");
        logger.info("trim out2 :" + Arrays.toString(c));
        myDataStructure.addAll(Arrays.asList(c));

        ObservableList<String> obList = FXCollections.observableList(myDataStructure);
        lv_what.setItems(obList);
        lv_what.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


    }

    private String adbRun(String command) {
        return adbRun(command, null);
    }

    public String adbRun(String command, File dir) {
        if (devicesNum > 1 && !command.contains("-d")) {
            command = command.replaceFirst("adb", "adb -d");
        }
        logger.info("final command: " + command);
        StringBuilder temp = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec(command, null, dir);
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                temp.append(line).append("\n");
                System.out.println(line);
            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return temp.toString();
    }


    public void onPushLog(MouseEvent mouseEvent) {
        String date = DateUtils.dateStump();
        File dir = new File("C://Users//fdx//Desktop//logcat", date);
        if (!dir.exists()) {
            boolean a = dir.mkdirs();
            boolean b = dir.setExecutable(true);
        }

        adbRun("adb pull data/krobot/logcat", dir);

    }

    public void writeProperties(ActionEvent actionEvent) {

        try {
            File file = new File(PropetyName);
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
            if (properties.getProperty("fdx") == null) {
                System.out.println("null fdx");
                properties.setProperty("fdx", "very bad 2");
            }
            System.out.println("a:" + properties.getProperty("fdx"));
            properties.store(outputStream, "update");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readProperties(ActionEvent actionEvent) {

        try {

            File file = new File(PropetyName);
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

            tf_log.setText("24: id :" + properties.getProperty("24"));
        }
    }

    public void resetProperties(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {
                File file = new File(PropetyName);
                if (file.exists()) {
                    properties.clear();
                }
                OutputStream outputStream = new FileOutputStream(file, false);
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

    public void onTest(MouseEvent actionEvent) {
        String whatIChoose = lv_what.getSelectionModel().getSelectedItem();
        int whatIChooseIndex = lv_what.getSelectionModel().getSelectedIndex();

        logger.info(",,,,,whatIchoose: " + whatIChoose);
    }


    public void onSendBroadCast(MouseEvent mouseEvent) {

        //暂时不用，这样界面太复杂了
        String argN = "-n ";
        String argA = "-a";
        String argC = "-c";

        String argESN = "-esn";
        String argES = "-es";
        String argEZ = "-ez";
        String argEI = "-ei";
        String argEL = "-el";
        String argEF = "-ef";
        String argEU = "-eu";
        String argECN = "-ecn";
        String argEIA = "-eia";
        String argELA = "-ela";

        String packageName = "";
        String activityName = "";
        StringBuilder sb = new StringBuilder();
        String componentName = argN + packageName + "/" + activityName;
        final int MAX_COMMAND_STORED = 10;
        String myInput = tf_name.getText();
        if (allCmd.size() > MAX_COMMAND_STORED) {
            allCmd.remove(MAX_COMMAND_STORED);
        }
        if (!myInput.equals(allCmd.get(0))) {
            allCmd.add(0, myInput);
        }
        adbRun("adb shell am broadcast " + myInput);

        store2file(allCmd);
    }

    private void store2file(List<String> data) {
        try {
            FileWriter fw = new FileWriter("history.data");

            for (String c :
                    data) {
                fw.append(c).append("\n");
            }
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onHint(MouseEvent mouseEvent) {
        StringBuilder sb = new StringBuilder();
        for (String s :
                allCmd) {
            sb.append(s).append("\n");
        }
        tf_log.setText(sb.toString());
    }
}
