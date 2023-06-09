package vipro.shop.Model;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    private String username,name,password,address,phone,image, email;

    public CustomerModel(String username, String name, String password, String address, String phone, String image, String email) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.image = image;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
