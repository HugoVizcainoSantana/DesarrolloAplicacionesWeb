package daw.spring.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Double cost;
    private boolean isOk; // 1 for Pendind and 0 for Done

    @OneToOne//(cascade = CascadeType.ALL)
    private Home home;

    @OneToMany//(cascade = CascadeType.ALL)
    private List<Device> deviceList;

    public OrderRequest() {
    }


    public OrderRequest(double cost, boolean confirmed, Home home, List<Device> deviceList) {
        
        this.cost = cost;
        this.isOk = confirmed;
        this.home = home;
        this.deviceList = deviceList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isConfirmed() {
        return isOk;
    }

    public void setConfirmed(boolean state) {
        this.isOk = state;
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
