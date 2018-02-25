package daw.spring.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long precio;
    private boolean completed; // 1 for Pendind and 0 for Done

    @OneToOne
    private Home home;

    @OneToMany//(cascade = CascadeType.ALL)
    private List<Device> deviceList;

    public Order() {
    }

    public Order(long precio, boolean completed, Home home, List<Device> deviceList) {
        
        this.precio = precio;
        this.completed = completed;
        this.home = home;
        this.deviceList = deviceList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean state) {
        this.completed = state;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }
}
