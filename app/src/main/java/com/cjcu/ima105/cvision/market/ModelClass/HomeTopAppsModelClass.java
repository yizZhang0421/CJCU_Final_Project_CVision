package com.cjcu.ima105.cvision.market.ModelClass;

/**
 * Created by Wolf Soft on 1/17/2018.
 */

public class HomeTopAppsModelClass {

    String image, number,title,view,install, description, id, market;

    public HomeTopAppsModelClass(String image, String number, String title, String view, String install, String description, String id, String market) {
        this.image = image;
        this.number = number;
        this.title = title;
        this.view = view;
        this.install = install;
        this.description=description;
        this.id=id;
        this.market=market;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getInstall() {
        return install;
    }

    public void setInstall(String install) {
        this.install = install;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getMarket() {
        return market;
    }
}
