package daw.spring.model;

import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class OrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Double cost;
    private boolean completed; // 1 for Pendind and 0 for Done
    private Date date;

    @OneToOne
    private Home home;

    @OneToMany
    private List<Device> deviceList;

    private String observation;

    public OrderRequest() {
    }


    public OrderRequest(double cost, boolean completed, Date date, Home home, List<Device> deviceList, String observation) {

        this.cost = cost;
        this.completed = completed;
        this.home = home;
        this.deviceList = deviceList;
        this.date = date;
        this.observation = observation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    public String getDateAsString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        return format.format(date);
    }
}
