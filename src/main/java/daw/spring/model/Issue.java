package daw.spring.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;

@Entity
public class Issue {

    // Entity issue is a message for the admin about a notification of a issue in a home/houses

    @Id
    private long id;                // Basic info
    private String title;
    private String description;

    private Home homeIssue;         // The issue always need to have a home attached to it or a bunch
    @OneToMany()
    private ArrayList<Home> homesIssues;

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

    public Issue(long id, String title, String description, ArrayList<Home> homesIssues, boolean solved) {
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

    public ArrayList<Home> getHomesIssues() {
        return homesIssues;
    }

    public void setHomesIssues(ArrayList<Home> homesIssues) {
        this.homesIssues = homesIssues;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
