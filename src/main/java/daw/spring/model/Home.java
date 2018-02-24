package daw.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long postCode;
    private String address;
    private Boolean activated;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Device> deviceList;

    //Atribute ignored by JPA (not stored in database)
    @JsonInclude()
    @Transient
    private long deviceQuantity;

    public Home() {
    }

    public Home(long postCode, String address, Boolean activated, List<Device> deviceList) {
        this.postCode = postCode;
        this.address = address;
        this.activated = activated;
        this.deviceList = deviceList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostCode() {
        return postCode;
    }

    public void setPostCode(long postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public List<Device> getDeviceList() {
        if (deviceList == null)
            deviceList = new ArrayList<>();
        setDeviceQuantity(deviceList.size());
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
        setDeviceQuantity(this.deviceList.size());
    }

    public long getDeviceQuantity() {
        return deviceQuantity;
    }

    public void setDeviceQuantity(long deviceQuantity) {
        this.deviceQuantity = deviceQuantity;
    }
}

