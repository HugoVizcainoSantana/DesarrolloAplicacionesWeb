package daw.spring.model;


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



    public Analytics() {
    }

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

        return domain;
    }

        this.domain = domain;
    }

        return data;
    }

        this.data = data;
    }

        return dataAverage;
    }

        this.dataAverage = dataAverage;
    }

    public enum AnalyticsType{
        PRODUCTS, USERS, HOMES  // Different graphs for diferent entities
    }
}
