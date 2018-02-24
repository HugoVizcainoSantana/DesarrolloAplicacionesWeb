package daw.spring.model;


import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Enumerated(EnumType.STRING)
    private AnalyticsType type;
    private String title;
    private String description;

    // We have to add user too as a atribute but, for now, we dont have security implemented
    @OneToOne
    private User user;

    private ArrayList<String> domain; // And a domain in Years, Months, Weeks or Days

    private ArrayList<Integer> data; // And the data itself with the average
    private ArrayList<Integer> dataAverage;

    public Analytics() {
    }

    public Analytics(User user, AnalyticsType type, String title, String description, ArrayList<String> domain, ArrayList<Integer> data, ArrayList<Integer> dataAverage) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.description = description;
        this.domain = domain;
        this.data = data;
        this.dataAverage = dataAverage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AnalyticsType getType() {
        return type;
    }

    public void setType(AnalyticsType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getDomain() {
        return domain;
    }

    public void setDomain(ArrayList<String> domain) {
        this.domain = domain;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    public ArrayList<Integer> getDataAverage() {
        return dataAverage;
    }

    public void setDataAverage(ArrayList<Integer> dataAverage) {
        this.dataAverage = dataAverage;
    }

    public enum AnalyticsType {
        PRODUCTS, USERS, HOMES  // Different graphs for diferent entities
    }
}
