package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import model.Tag;
import model.User;

/**
 * Controller for view/Adminui.fxml
 * 
 * @author Michael Triano
 * @author Joseph Wolak
 *
 */

public class AdminController {
	
	@FXML
	AnchorPane adminui;
	
	@FXML
	Button addUserButton, deleteUserButton, logoutButton;
	
	/**
	 * listview for users
	 */
	@FXML
	ListView usersListView;
	List<User> users;


	
	public void start(Stage stage, ArrayList<User> u){

		users = u;

		updateUsersList();

		//Add user button listener
		addUserButton.setOnAction(event -> {
			try {
				addUser(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		

		//Delete user button listener
		deleteUserButton.setOnAction(event -> {
			try {
				deleteUser(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		
		
		
        //Logout button listener
        logoutButton.setOnAction(event -> {
            try {
                logout(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        

	}
	

	
	/**
	 * Add user to list
	 */
	private void addUser(Stage stage) {
		
		
        User user = createDialog();
        
        users.add(user);
        
        updateUsersList();

        try {
        	saveToFile();
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	
	/**
	 * Delete user from list
	 * @param stage
	 * @throws Exception
	 */
	private void deleteUser(Stage stage) throws Exception {
		
        int index = usersListView.getSelectionModel().getSelectedIndex();
        
        if(index < 0)
        	return;
        
        users.remove(index);
        
        updateUsersList();

		saveToFile();
		
	}
	
	public void updateUsersList() {
		
		ObservableList<User> userObservableList = FXCollections.observableList(users);
		
		usersListView.setItems(userObservableList);
		
		usersListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			
			@Override
			public ListCell<User> call(ListView<User> param) {
				ListCell<User> cell = new ListCell<User>() {
					@Override
					protected void updateItem(User item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null) {
							setText(item.getUsername());
						}
					}
				};
				
				return cell;
			}
		});
	}
	
	

	/**
	 * Logout user
	 * Return to login page so another user may login
	 * @param stage
	 */
    public void logout(Stage stage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Loginui.fxml"));
            Parent root = loader.load();
            LoginController loginController = loader.getController();
            stage.setScene(new Scene(root, 400, 200));
            loginController.start(stage);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    public User createDialog() {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Enter username");

        ButtonType enterButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enterButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("username");


        gridPane.add(new Label("Username: "), 0, 0);
        gridPane.add(username, 1, 0);


        dialog.getDialogPane().setContent(gridPane);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enterButtonType) {
                return new String(username.getText());
            }
            return null;
        });

        

        dialog.showAndWait();

        return new User(username.getText());

    }

    public void saveToFile() throws Exception {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("photo_data.ser"));
		objectOutputStream.writeObject(users);
		objectOutputStream.close();
	}


}
