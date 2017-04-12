package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PhotoController
 * @author Joe Wolak
 *
 */


public class PhotoController {

    final String EXAMPLE_ALBUM = "PAPA JOHNS GREATEST HITS";

    User user;

    @FXML
    Text headerText;
    
    @FXML
    ListView<Album> albumListView;

    @FXML
    Button addPhotoButton, deletePhotoButton, editCaptionButton, editTagsButton, slideshowButton;
    
    @FXML
    Button createAlbumButton, deleteAlbumButton, renameAlbumButton, searchAlbumButton, copyToAlbumButton, moveToAlbumButton;

    @FXML
    TilePane photoTilePane;

    @FXML
    Button logoutButton;

    public void start(Stage stage, User user) {

        this.user = user;

        //Set header
        stage.setTitle("Photo Gallery");

        //Just to test
        user.addAlbum(EXAMPLE_ALBUM);

        //Load users photos
        updatePhotoList();
        
        
        //Load users albums
        updateAlbumList(user);
        
        
        //Create Album button listener
        createAlbumButton.setOnAction(event -> createAlbum(stage));
        
        //Delete Album button listener
        deleteAlbumButton.setOnAction(event -> deleteAlbum(stage));

        /*
        //Rename Album button listener
        renameAlbumButton.setOnAction(event -> renameAlbum(stage));
        
        //Search Album button listener
        searchAlbumButton.setOnAction(event -> searchAlbum(stage));
        
        //Copy to Album button listener
        copyToAlbumButton.setOnAction(event -> copyToAlbum(stage));
        
        //Move to Album button listener
        moveToAlbumButton.setOnAction(event -> moveToAlbum(stage));
        */
        
        
        

        //Add Photo button listener
        addPhotoButton.setOnAction(event -> addPhoto(stage));

        //Delete Photo button listener
        deletePhotoButton.setOnAction(event -> deletePhoto(stage));

        //Edit Caption button listener
        editCaptionButton.setOnAction(event -> editCaption());

        //Edit Tags button listener
        editTagsButton.setOnAction(event -> {
            try {
                editTags();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });

        //Slideshow button listener
        slideshowButton.setOnAction(event -> {
            try{
                displaySlideshow(user.getAlbum(EXAMPLE_ALBUM), getSelectedImageIndex());
            } catch(IOException e) {
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
     * Create an album to the logged in user
     */
    public void createAlbum(Stage stage) {
    	
    	Album album = createDialog();
    	
    	user.addAlbum(album.getName());
    	
    	updateAlbumList(user);
    }


    /**
     * Adds a photo to the album
     *
     * @param stage
     */
    public void addPhoto(Stage stage) {

        //Popups and configure file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo(s)");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*")
        );

        //Can choose multiple images
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        //Adds all the files chosen to the album
        if(files != null) {
            for(File file : files) {
                user.getAlbum(EXAMPLE_ALBUM).addPhoto(file);
            }
        }

        updatePhotoList();

    }
    
    

    /**
     * Deletes the selected photo from the album
     *
     * @param stage
     */
    public void deletePhoto(Stage stage) {

        int index = getSelectedImageIndex();

        //Nothing selected
        if(index < 0)
            return;

        user.getAlbum(EXAMPLE_ALBUM).deletePhoto(index);

        updatePhotoList();
    }
    
    /**
     * Deletes a selected album from users albums
     */
    public void deleteAlbum(Stage stage) {
    	int index = albumListView.getSelectionModel().getSelectedIndex();
    	
    	//Nothing selected
    	if(index < 0)
    		return;
    	
    	user.getAlbums().remove(index);
    	
    	updateAlbumList(user);
    }


    /**
     * Edits caption for selected photo
     */
    public void editCaption() {

        int index = getSelectedImageIndex();

        //Nothing selected
        if(index < 0)
            return;

        //Popup a text input dialog
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setContentText("Enter New Caption:");

        //Get response and set caption for photo
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(name -> user.getAlbum(EXAMPLE_ALBUM).getPhotos().get(index).setCaption(name));

        updatePhotoList();

    }


    /**
     * Edit tags
     * @throws IOException
     */
    public void editTags() throws IOException {

        int index = getSelectedImageIndex();

        //Nothing selected
        if(index < 0)
            return;

        Photo p = user.getAlbum(EXAMPLE_ALBUM).getPhotos().get(index);

        Stage tagsStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tagsui.fxml"));
        Parent root = loader.load();
        TagsController tagsController = loader.getController();
        tagsController.start(p);

        tagsStage.setScene(new Scene(root, 250, 400));
        tagsStage.show();

    }


    /**
     * Populates the ListView with album's photos
     */
    public void updatePhotoList() {

        photoTilePane.getChildren().removeAll(photoTilePane.getChildren());

        ObservableList images = photoTilePane.getChildren();

        ArrayList<Photo> photos = user.getAlbum(EXAMPLE_ALBUM).getPhotos();

        //Populate the TileView with images from album
        for(Photo p : photos) {
            Label label = newImageView(p);
            images.add(label);
        }

        flushTileView();

    }
    
    
    /**
     * Populates the ListView with the user's albums
     */
    public void updateAlbumList(User u) {
    	
    	ObservableList<Album> albumObservableList = FXCollections.observableList(u.getAlbums());
    	
    	albumListView.setItems(albumObservableList);
    	
		albumListView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {
			
			@Override
			public ListCell<Album> call(ListView<Album> param) {
				ListCell<Album> cell = new ListCell<Album>() {
					@Override
					protected void updateItem(Album item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null) {
							setText(item.getName());
						}
					}
				};
				
				return cell;
			}
		});
    }
    	
    	
    	



    /**
     * Displays the album through a slideshow in a new window
     *
     * @param album
     * @throws IOException
     */
    public void displaySlideshow(Album album, int index) throws IOException {

        //Nothing selected
        if(index == -1)
            return;

        Stage slideshowStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Slideshowui.fxml"));
        Parent root = loader.load();
        SlideshowController slideshowController = loader.getController();
        slideshowController.start(slideshowStage, album, index);

        slideshowStage.setScene(new Scene(root, 800, 600));
        slideshowStage.show();

    }


    /**
     * Creates and format label for photo
     * @param p
     * @return
     */
    public Label newImageView(Photo p) {

        Image image = new Image(p.getFile().toURI().toString(), 220, 150, true, true, true);
        ImageView imageView = new ImageView(image);

        Label label = new Label(p.getCaption(), imageView);

        label.setContentDisplay(ContentDisplay.TOP);

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    flushTileView();
                    label.setStyle("-fx-scale-x: 110%; -fx-scale-y: 110%;");
                    label.setUserData("selected");
                }
            }
        });


        return label;
    }

    /**
     * For TilePane
     * @return
     */
    public int getSelectedImageIndex() {

        for(int i = 0; i < photoTilePane.getChildrenUnmodifiable().size(); i++) {
            Node child = photoTilePane.getChildren().get(i);
            if(child instanceof Label) {
                if(child.getUserData().equals("selected")) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Resets TilePane
     */
    public void flushTileView() {
        for(Node child : photoTilePane.getChildrenUnmodifiable()) {
            if(child instanceof Label) {
                child.setStyle("-fx-scale-x: 100%; -fx-scale-y: 100%;");
                child.setUserData("");
            }
        }
    }
    
    
    
    
    
    public Album createDialog() {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New Album");
        dialog.setHeaderText("Enter Album Name");

        ButtonType enterButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enterButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField albumname = new TextField();
        albumname.setPromptText("album");


        gridPane.add(new Label("Album: "), 0, 0);
        gridPane.add(albumname, 1, 0);


        dialog.getDialogPane().setContent(gridPane);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enterButtonType) {
                return new String(albumname.getText());
            }
            return null;
        });
        


        dialog.showAndWait();

        return new Album(albumname.getText());

    }

    /**
     * Logouts the user and returns them to the login screen
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

}
