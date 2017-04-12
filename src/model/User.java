package model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

    String username;
    ArrayList<Album> albums;


    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
    }
    

    public String getUsername() {
        return username;
    }

    
    
    public void addAlbum(String name) {
        albums.add(new Album(name));
    }
    
    public void deleteAlbum(int index) {
    	albums.remove(index);
    }
    
    

    public Album getAlbum(String name) {
        for(Album album : albums) {
            if(album.getName().equals(name))
                return album;
        }

        return null;
    }
    
    
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    
}
