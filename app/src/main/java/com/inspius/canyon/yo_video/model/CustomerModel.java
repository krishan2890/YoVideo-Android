package com.inspius.canyon.yo_video.model;

import com.google.gson.annotations.SerializedName;

import java.util.Random;

/**
 * Created by Billy on 11/23/15.
 */
public class CustomerModel {
    public int id;
    public String email;
    public String avatar;

    @SerializedName("firstname")
    public String firstName;

    @SerializedName("lastname")
    public String lastName;

    public String phone;

    @SerializedName("address")
    public String address;

    public String city;
    public String country;
    public String zip;
    public int vip;

    public boolean isLoginAsFacebook;

}
