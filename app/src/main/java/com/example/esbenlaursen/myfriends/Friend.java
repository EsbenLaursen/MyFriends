package com.example.esbenlaursen.myfriends;

import android.graphics.drawable.Drawable;
import android.media.Image;

import java.io.Serializable;

/**
 * Created by EsbenLaursen on 06-03-2017.
 */

public class Friend implements Serializable{
    public int id;
    public String name;
    public String address;
    public String email;
    public String phoneNumber;
    public String URL;
    public String image;
    public double longitude;
    public double latitude;

    public Friend(int id, String name, String address, double latitude, double longitude, String email, String phoneNumber, String URL, String image) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.URL = URL;
        this.image = image;
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getURL() {
        return URL;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}


