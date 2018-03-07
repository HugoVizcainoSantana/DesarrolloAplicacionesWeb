package daw.spring.model;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Device device;
    private Date date;
    private Device.StateType previousState;
    private Device.StateType newState;

    public Analytics() {
    }

    public Analytics(Device device, Date date, Device.StateType previousState, Device.StateType newState) {
        this.device = device;
        this.date = date;
        this.previousState = previousState;
        this.newState = newState;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Device.StateType getPreviousState() {
        return previousState;
    }

    public void setPreviousState(Device.StateType previousState) {
        this.previousState = previousState;
    }

    public Device.StateType getNewState() {
        return newState;
    }

    public void setNewState(Device.StateType newState) {
        this.newState = newState;
    }

    @Override
    public String toString() {
        return "Gráfica: " + getId() + " con " + getPreviousState() + getNewState() + getDevice().getType().toString() + getDate();
    }
}
