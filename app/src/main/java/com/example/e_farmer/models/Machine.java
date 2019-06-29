package com.example.e_farmer.models;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.e_farmer.R;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Machine extends BaseObservable {

    @Id
    private long id;
    public ToOne<User> user;

    private String name;
    private String type;
    private String registration_year;
    private String purchase_date;
    private String original_price;
    private String current_price;
    private String milage;
    private String notes;
    private String machineImage;

    public Machine() {
    }

    @Bindable
    public long getId() {
        return id;
    }

    @BindingAdapter("machineImage")
    public static void loadImage(ImageView view, String imageUrl) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_launcher_background);

        Glide.with(view.getContext())
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(view);
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getMachineImage() {
        return machineImage;
    }

    public void setMachineImage(String machineImage) {
        this.machineImage = machineImage;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getRegistration_year() {
        return registration_year;
    }

    public void setRegistration_year(String registration_year) {
        this.registration_year = registration_year;
    }

    @Bindable
    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    @Bindable
    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    @Bindable
    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    @Bindable
    public String getMilage() {
        return milage;
    }

    public void setMilage(String milage) {
        this.milage = milage;
    }

    @Bindable
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
