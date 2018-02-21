package daw.spring.model;

import javax.persistence.*;

@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String description;
    private double cost;
    @Enumerated(EnumType.STRING)
    private Device.DeviceType type;
    @Enumerated(EnumType.STRING)
    private Device.StateType status;
    private String img;
    private boolean activate;


    public Device() {
    }

    public Device( String description, double cost, DeviceType type, StateType status, String img, boolean activate) {
        this.id = id;
        this.description = description;
        this.cost = cost;
        this.type = type;
        this.status = status;
        this.img = img;
        this.activate=activate;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public StateType getStatus() {
        return status;
    }

    public void setStatus(StateType status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean active) {
        this.activate = activate;
    }

    public enum DeviceType {
        BLIND, LIGHT
    }

    public enum StateType {
        ON, OFF, UP, DOWN
    }
}
