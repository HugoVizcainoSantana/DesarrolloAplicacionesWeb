package daw.spring.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Issue {

    // Entity issue is a message for the admin about a notification of a issue in a home/houses

    @Id
    private long id;                // Basic info
    private String title;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Home homeIssue;         // The issue always need to have a home attached to it or a bunch

    @OneToMany(cascade = CascadeType.ALL)
    private List<Home> homesIssues;

    private boolean solved = false; // The issue can be solved or not

    public Issue() {
    }

    public Issue(long id, String title, String description, boolean solved) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.solved = solved;
    }

    public Issue(long id, String title, String description, Home homeIssue, boolean solved) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.homeIssue = homeIssue;
        this.solved = solved;
    }

    public Issue(long id, String title, String description, List<Home> homesIssues, boolean solved) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.homesIssues = homesIssues;
        this.solved = solved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Home getHomeIssue() {
        return homeIssue;
    }

    public void setHomeIssue(Home homeIssue) {
        this.homeIssue = homeIssue;
    }

    public List<Home> getHomesIssues() {
        return homesIssues;
    }

    public void setHomesIssues(List<Home> homesIssues) {
        this.homesIssues = homesIssues;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
