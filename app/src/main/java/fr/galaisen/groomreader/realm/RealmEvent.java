package fr.galaisen.groomreader.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmEvent extends RealmObject {
    @PrimaryKey
    private String slug;
    private String name;
    private RealmPlace place;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmPlace getPlace() {
        return place;
    }

    public void setPlace(RealmPlace place) {
        this.place = place;
    }
}
