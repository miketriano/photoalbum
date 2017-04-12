package model;

public class Tag {

    String name, value;

    public Tag(String n, String v) {
        this.name = n;
        this.value = v;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
