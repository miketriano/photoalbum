package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Pair;
import model.*;

import java.awt.*;
import java.util.Optional;

/**
 * Controller for Tagsui.fxml
 *
 */
public class TagsController {


    @FXML
    Text captionText;

    @FXML
    Button addTagButton, deleteTagButton;

    @FXML
    ListView tagListView;


    public void start(Photo p) {

        captionText.setText(p.getCaption());

        addTagButton.setOnAction(event -> addTag(p));

        deleteTagButton.setOnAction(event -> deleteTag(p));

        updateTagsList(p);

    }

    public void addTag(Photo p) {

        Tag tag = createDialog();

        p.addTag(tag);

        updateTagsList(p);

    }

    public void deleteTag(Photo p) {

        int index = tagListView.getSelectionModel().getSelectedIndex();

        if(index < 0)
            return;

        p.getTags().remove(index);

        updateTagsList(p);

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
                        if(item != null) {
                            setText(item.getName() + " : " + item.getValue());
                        }
                    }
                };

                return cell;
            }
        });



    }

    /**
     * Creates a custom dialog popup with 2 text inputs
     *
     * Code modified from: http://code.makery.ch/blog/javafx-dialogs-official/
     */
    public Tag createDialog() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New Tag");
        dialog.setHeaderText("Enter name : value pair");

        ButtonType enterButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enterButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Tag Name");
        TextField value = new TextField();
        value.setPromptText("Tag Value");

        gridPane.add(new Label("Tag Name: "), 0, 0);
        gridPane.add(name, 1, 0);
        gridPane.add(new Label("Tag Value: "), 0, 1);
        gridPane.add(value, 1, 1);

        dialog.getDialogPane().setContent(gridPane);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enterButtonType) {
                return new Pair<>(name.getText(), value.getText());
            }
            return null;
        });


        dialog.showAndWait();

        return new Tag(name.getText(), value.getText());

    }

}
