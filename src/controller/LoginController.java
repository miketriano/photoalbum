package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for view/Loginui.fxml
 * 
 * @author Michael Triano
 * @author Joseph Wolak
 */
public class LoginController {

    @FXML
    AnchorPane loginui;

    @FXML
    TextField userNameField;

    @FXML
    Button loginButton;

    ArrayList<User> users;

    public void start(Stage stage) throws Exception {

        users = new ArrayList<>();


        //Saves user data to file
        File file = new File("photo_data.ser");
        if(file.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            users = (ArrayList<User>) objectInputStream.readObject();
            objectInputStream.close();
        } else {
            User user = createStock();
            users.add(user);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(users);
            objectOutputStream.close();

        }


        //Login button listener
        loginButton.setOnAction(event -> {
        	try {
        		login(stage, users);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        });

    }

    /**
     * Login button listener
     */
    private void login(Stage stage, ArrayList<User> users) throws Exception {

        //Username field is blank
        if(userNameField.getText().isEmpty())
            return;


        //Switch over to view/Admin.fxml scene
        if(userNameField.getText().equals("admin")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Adminui.fxml"));
                Parent root = loader.load();
                AdminController adminController = loader.getController();
                adminController.start(stage, users);
                stage.setTitle("Admin Home");
                stage.setScene(new Scene(root, 280, 400));
                stage.setResizable(false);
                stage.show();
            } catch(IOException e) {
        	    e.printStackTrace();
            }

        }

        //Switch over to view/Photoui.fxml scene
        else {

            User user = null;

            for(User u : users) {
                if(u.getUsername().equals(userNameField.getText())) {
                    user = u;
                }
            }

            if(user == null)
                return;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Photoui.fxml"));
                Parent root = loader.load();
                PhotoController photoController = loader.getController();
                stage.setScene(new Scene(root, 1280, 720));
                photoController.start(stage, user);
            } catch(IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Sets up the sample user with stock photos
     * @return user
     */
    public User createStock() {

        User user = new User("stock");

        user.addAlbum("stock");

        for(int i = 1; i <= 10; i++) {
            user.getAlbum("stock").addPhoto(new File("stock_photos/" + i + ".jpg"));
        }


        return user;
    }

}
