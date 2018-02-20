package fr.galaisen.groomreader.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmPlace extends RealmObject {
    @PrimaryKey
    private Integer id;
    private String address;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
