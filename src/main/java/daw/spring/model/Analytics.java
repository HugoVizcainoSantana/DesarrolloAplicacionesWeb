package daw.spring.model;


import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    /*
    @OneToOne
    private User user;

    private ArrayList<String> domain; // And a domain in Years, Months, Weeks or Days

    private ArrayList<Integer> data; // And the data itself with the average
    private ArrayList<Integer> dataAverage;

    */
    @ManyToOne(fetch = FetchType.EAGER)
    private Device device;
    private Date date;
    private Device.StateType previousState;
    private Device.StateType newState;
    @OneToOne(fetch = FetchType.EAGER)
    private Analytics previousRecord;


    public Analytics() {
    }

    public Analytics(Device device, Device.StateType previousState, Device.StateType newState, Analytics previousRecord) {
        this.device = device;
        this.date = Date.from(Instant.now()); // in days, minutes and secs
        this.previousState = previousState;
        this.newState = newState;
        this.previousRecord = previousRecord;
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
}
