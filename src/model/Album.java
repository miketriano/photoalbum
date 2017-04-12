package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

    ArrayList<Photo> photos;
    String name;

    public Album(String name) {
        this.name = name;
        photos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto(File file) {
        photos.add(new Photo(file));
    }

    public void deletePhoto(int index) {
        photos.remove(index);
    }
}
