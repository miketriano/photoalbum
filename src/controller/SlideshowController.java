package controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;


public class SlideshowController {

    @FXML
    Text captionText, dateText;

    @FXML
    Label photoLabel;

    @FXML
    Button slideshowButton, leftButton, rightButton;

    @FXML
    ListView tagListView;

    int index;

    public void start(Stage stage, Album album, int i) {

        index = i;

        stage.setTitle("Slideshow");

        Photo currentPhoto = album.getPhotos().get(index);

        captionText.setText(currentPhoto.getCaption());
        dateText.setText(currentPhoto.getDate().toString());

        slideshowButton.setOnAction(event -> {
            try {
                startSlideshow(album);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        leftButton.setOnAction(event -> left(album));

        rightButton.setOnAction(event -> right(album));

        photoLabel.setGraphic(new ImageView(new Image(currentPhoto.getFile().toURI().toString(), 560, 520, true, true, true)));

        updateTagsList(currentPhoto);

    }


    public void left(Album album) {

        index--;
        int fixed = index % album.getPhotos().size();

        photoLabel.setGraphic(new ImageView(new Image(album.getPhotos().get(fixed).getFile().toURI().toString(), 560, 520, true, true, true)));

        updateTagsList(album.getPhotos().get(fixed));
    }

    public void right(Album album) {

        index++;
        int fixed = index % album.getPhotos().size();

        photoLabel.setGraphic(new ImageView(new Image(album.getPhotos().get(fixed).getFile().toURI().toString(), 560, 520, true, true, true)));
        captionText.setText(album.getPhotos().get(fixed).getCaption());
        dateText.setText(album.getPhotos().get(fixed).getDate().toString());

        updateTagsList(album.getPhotos().get(fixed));
    }


    /**
     * Automatic slideshow. DOESN'T STOP. NOT FINISHED.
     *
     * @param album
     * @throws Exception
     */
    public void startSlideshow(Album album) throws Exception {

        System.out.println(album.getPhotos().size());

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {


                try {
                    while(true) {
                        Thread.sleep(2500);
                        for(Photo p : album.getPhotos()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    photoLabel.setGraphic(new ImageView(new Image(p.getFile().toURI().toString(), 560, 520, true, true, true)));
                                }
                            });

                            Thread.sleep(2500);
                        }
                    }
                } catch(InterruptedException e) {
                    return null;
                }

            }

        };

        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
            }
        });

        new Thread(sleeper).start();
    }


    public void updateTagsList(Photo p) {

        ObservableList<Tag> tagObservableList = FXCollections.observableList(p.getTags());

        tagListView.setItems(tagObservableList);

        tagListView.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>() {

            @Override
            public ListCell<Tag> call(ListView<Tag> param) {
                ListCell<Tag> cell = new ListCell<Tag>() {
                    @Override
                    protected void updateItem(Tag item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName() + " : " + item.getValue());
                        }
                    }
                };

                return cell;
            }
        });

    }
}
