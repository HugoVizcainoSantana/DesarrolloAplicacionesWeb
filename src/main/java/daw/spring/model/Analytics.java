package daw.spring.model;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Analytics {

    @Id
    private long id;
    private AnalyticsType type;
    private String title;
    private String description;

    /*
    @OneToMany()
    private List<Object> entitiesAnalytics; // All the graphs contains a group of entities...
    private List<Product> productAnalytics;
    private List<User> userAnalytics;
    private List<Home> homeAnalytics;*/

    private List<String> domain; // And a domain in Years, Months, Weeks or Days

    private List<Integer> data; // And the data itself with the average
    private List<Integer> dataAverage;

    public Analytics() {
    }

    public Analytics(long id, AnalyticsType type, String title, String description, List<String> domain, List<Integer> data, List<Integer> dataAverage) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.domain = domain;
        this.data = data;
        this.dataAverage = dataAverage;
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

    public List<String> getDomain() {
        return domain;
    }

    public void setDomain(List<String> domain) {
        this.domain = domain;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public List<Integer> getDataAverage() {
        return dataAverage;
    }

    public void setDataAverage(List<Integer> dataAverage) {
        this.dataAverage = dataAverage;
    }

    public enum AnalyticsType{
        PRODUCTS, USERS, HOMES  // Different graphs for diferent entities
    }
}
