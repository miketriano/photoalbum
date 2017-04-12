package model;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Photo implements Serializable {

    ArrayList<Tag> tags;
    File file;
    String caption;
    Date date;

    public Photo(File file) {
        this.file = file;
        this.date = new Date(file.lastModified());
        this.caption = "No Caption";
        this.tags = new ArrayList<>();
    }

    public File getFile() {
        return file;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String c) {
        caption = c;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

   
    public void deleteTag(int index) {
        tags.remove(index);
    }
}
