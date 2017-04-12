package app;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Loginui.fxml"));
        Parent root = loader.load();

        //Start with Login screen
        LoginController loginController = loader.getController();
        loginController.start(stage);

        stage.setTitle("Photo Gallery Login");
        stage.setScene(new Scene(root, 400, 200));
        stage.setResizable(false);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
