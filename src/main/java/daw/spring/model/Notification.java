package daw.spring.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Notification {

    // Entity Notification is about a message for the user about expenses, the service or equipment he have hired with us

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;               // Basic info

    private String title;
    private String description;
    private Date date;

    @ManyToOne
    private User userNotification; // The notification always need to have a user attached to it

    public Notification() {
    }

    public Notification(String title, String description, Date date, User userNotification) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.userNotification = userNotification;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateAsString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        return format.format(date);
    }

    public User getUserNotification() {
        return userNotification;
    }

    public void setUserNotification(User userNotification) {
        this.userNotification = userNotification;
    }
}
