package sample;

import Model.BroadCast;
import Model.MySettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class Main extends Application {

    private List<BroadCast> xxx;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ADB tool for krobot");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void loadSettings(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(MySettings.class);
            Unmarshaller um = context.createUnmarshaller();

            MySettings settings = (MySettings) um.unmarshal(file);
            xxx.clear();
            xxx.addAll(settings.getBroadCasts());


        } catch (JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
            e.printStackTrace();
        }
    }



    public void saveSettingstoFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(MySettings.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            MySettings settings = new MySettings();
            settings.setBroadCasts(xxx);

            m.marshal(settings,file);


        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}