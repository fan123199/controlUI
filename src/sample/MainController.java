package sample;

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

@SuppressWarnings("UnusedParameters")
public class MainController {
    private final String propertyName = "devicesname.properties";
    public Button btn_wifi;
    public Button btn_head;
    public TextArea tf_log;
    public TextField tf_name;
    public BorderPane root_layout;
    public TitledPane tp_output;
    public Button btn_backward;
    public Button btn_home;
    public ListView<String> lv_list;
    public Button btn_focus_act;
    public Button btn_send_broadcast;

    private Properties properties = new Properties();
    private String deviceId;
    private String deviceName;
    private List<String> allCmd = new ArrayList<>();

    boolean isRemounted = false;

    public MainController() {
        devicesNum = 2;
    }


    private void initLogcatFile() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/krobot_logcat.sh");

        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        File file = new File("krobot_logcat.sh");

        try {
            OutputStream os = new FileOutputStream(file);
            int cc;
            while ((cc = br.read()) != -1) {
                os.write(cc);
            }

            is.close();
            os.close();
            logger.info("LogcatFile DONE");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initHistoryFile() {
        BufferedReader reader;

        File file = new File("history.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                allCmd.add(line);
            }
            logger.info("HistoryFile DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        readProperties(null);
        initLogcatFile();
        initHistoryFile();

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

    public void onOK(MouseEvent mouseEvent) {
        adbInput(212);
    }

    public void onUp(MouseEvent mouseEvent) {
        adbInput(19);
    }

    public void onDown(MouseEvent mouseEvent) {
        adbInput(20);
    }

    public void onLeft(MouseEvent mouseEvent) {
        adbInput(21);
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
//        String appName = "KrobotCartoonBook";
        String aName = lv_list.getSelectionModel().getSelectedItem();
        String output = adbRun("adb shell rm system/app/" + aName);
        tf_log.setText(output);

    }


    public void onPushApp(MouseEvent mouseEvent) {

        adbRun("adb remount");

        adbRun("adb push " + "Y:/work/r58/android/out/target/product/octopus-kr/system/app/"
                + lv_list.getSelectionModel().getSelectedItem() + " /system/app");
        adbRun("adb shell chmod 644 /system/app/" + lv_list.getSelectionModel().getSelectedItem());
        adbRun("adb shell sync");

    }

    public void onShowPackages(MouseEvent mouseEvent) {
        logger.debug("show packages app");


        String out2 = adbRun("adb shell pm list packages | grep -E -i 'krobot|kingrobot'");
        tf_log.setText("\n ::::" + out2);
        List<String> myDataStructure = new ArrayList<>();
        String[] c = out2.trim().split("\n\n");
        logger.info("trim out2 :" + Arrays.toString(c));
        myDataStructure.addAll(Arrays.asList(c));

        ObservableList<String> obList = FXCollections.observableList(myDataStructure);
        lv_list.setItems(obList);
        lv_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    public void onShowApp(MouseEvent mouseEvent) {

        String out = adbRun("adb shell ls system/app | grep -E -i 'krobot|kingrobot'");
        tf_log.setText("\n ::::" + out);
        List<String> myDataStructure = new ArrayList<>();
        String[] c = out.trim().split("\n\n");
        logger.info("trim out2 :" + Arrays.toString(c));
        myDataStructure.addAll(Arrays.asList(c));
        ObservableList<String> obList = FXCollections.observableList(myDataStructure);
        lv_list.setItems(obList);
        lv_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    private String adbRun(String command) {
        return adbRun(command, null);
    }

    private String adbRun(String command, File dir) {
        if (devicesNum > 1 && !command.contains("-d")) {
            command = command.replace("adb", "adb -d");
        }

        logger.info("final command: " + command);
        StringBuilder temp = new StringBuilder();
        try {
            if (!isRemounted) {
                Runtime.getRuntime().exec("adb -d remount", null, dir);
                logger.info(" remount now");
                isRemounted = true;
            }

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


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onPullLog(MouseEvent mouseEvent) {
        String date = DateUtils.dateStump();



        //not necessary, plan B
//        File dir = new File("C://Users//fdx//Desktop//logcat", date);
//        if (!dir.exists()) {
//            dir.mkdirs();
//            dir.setExecutable(true);
//        }

        if (adbRun("adb shell ls data/krobot/logcat").contains("No such file or directory")) {
            tf_log.setText("no Log there, please push patch to xiaoyi");
            onPushLogPatch(mouseEvent);
            return;
        }

        adbRun("adb pull data/krobot/logcat "+ System.getProperty("home")+ "/Desktop/logcat/" + date + File.separator + deviceName);
//        adbRun("adb pull data/krobot/logcat/",dir);

    }

    public void writeProperties(ActionEvent actionEvent) {
//        name = tf_name.getText();
//        writeToProperties(name + "hehe", name);
    }

    private void writeToProperties(String key, String value) {
        try {
            File file = new File(propertyName);
            if (file.exists()) {
                properties.clear();
                properties.load(new FileInputStream(file));
            }
            OutputStream outputStream = new FileOutputStream(file);
            properties.setProperty(key, value);
            properties.store(outputStream, "update");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readProperties(ActionEvent actionEvent) {

        try {

            File file = new File(propertyName);
            if (!file.exists()) {
                file.createNewFile();
            }
//                System.out.println(file.getAbsoluteFile());
            InputStream fis = new FileInputStream(file);
            properties.load(fis);

//            logger.info(String.valueOf(file.exists()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
//        System.out.println("number:" + properties.getProperty("number") + ",song:" + properties.getProperty("song"));

        if (tf_log != null) {
            tf_log.clear();
            for (Object o : properties.keySet()) {
                String c = (String) o;
                tf_log.appendText(c + ":" + properties.getProperty(c) + "\n");
            }
        }
    }

    public void resetProperties(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("reset property");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            writeToProperties("24", "303000f10200a6e6bb0a");
        }


    }

    public void fileChoose(ActionEvent actionEvent) {
        //// TODO: 024,10/24  
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) root_layout.getScene().getWindow();
        stage.close();
    }

    public void onTest(MouseEvent actionEvent) {
//        String whatIChoose = lv_list.getSelectionModel().getSelectedItem();

    }


    @SuppressWarnings("unused")
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
            FileWriter fw = new FileWriter("history.txt");

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


    public void onReboot(MouseEvent mouseEvent) {

        adbRun("adb reboot");
    }

    public void onForceStop(MouseEvent mouseEvent) {
        adbRun("adb shell am force-stop " + lv_list.getSelectionModel().getSelectedItem());
    }

    public void onPushLogPatch(MouseEvent mouseEvent) {
        adbRun("adb remount");
        adbRun("adb push krobot_logcat.sh" + " /system/bin/sensors.sh");
        adbRun("adb shell chmod 644 /system/bin/sensors.sh");
        adbRun("adb shell sync");
//        adbRun("adb shell reboot");
        tf_log.setText("reboot to active the change");
    }

    public void onFirstStep(MouseEvent mouseEvent) {
        String output = adbRun("adb devices");

//        tf_log.setText(output);

        String tempID = "";
        String[] eachLine = output.split("\n");
        for (int i = 1; i < eachLine.length; i++) {
            if (eachLine[i].contains("device")) {
                tempID = eachLine[i].split("\t")[0];
//                tf_log.appendText(tempID);
            }
        }

        deviceId = tempID;

        if (!properties.containsValue(tempID)) {
            TextInputDialog dialog = new TextInputDialog("walter");
            dialog.setTitle("confirm");
            dialog.setHeaderText("设置");
            dialog.setContentText("set devices ID: ");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                logger.info(tempID + "------" + result.get());
                writeToProperties(result.get(), tempID);
                deviceName = result.get();
            }
        } else {
            for (Object key:properties.keySet()
                 ) {
                if (tempID.equals(properties.get(key))) {
                    tf_log.appendText("\nkey: " + key + ", ID: " +  tempID);
                    deviceName = (String) key;
                }
            }
        }
    }
}
