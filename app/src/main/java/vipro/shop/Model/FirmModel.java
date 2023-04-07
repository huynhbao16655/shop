package vipro.shop.Model;

import java.io.Serializable;

public class FirmModel implements Serializable {
    private String code;
    private String name;
    private String image;

    public FirmModel(String code, String name, String image) {
        this.code = code;
        this.name = name;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
