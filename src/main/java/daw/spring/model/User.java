package daw.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private long id;

    @JsonView(Basic.class)
    @NotEmpty
    private String firstName;

    @JsonView(Basic.class)
    @NotEmpty
    private String lastName;

    @JsonView(Basic.class)
    @NotEmpty
    @Email
    private String email;

    @JsonView(Basic.class)
    @NotEmpty
    private String passwordHash;

    //@JsonView(Home.class)
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @OneToMany
    private List<Home> homeList;

    @JsonView(Basic.class)
    private String phone;

    //@JsonView(Notification.class)
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @OneToMany
    private List<Notification> notificationList;

    @JsonView(Basic.class)
    private String photo;


    //@JsonView(Basic.class)
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    //@JsonView(OrderRequest.class)
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderRequest> orderList;


    public User() {
    }

    public User(String firstName, String lastName, String email, String passwordHash, List<Home> homeList, String phone, List<Notification> notificationList, String photo, List<OrderRequest> orderList, String... roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.homeList = homeList;
        this.phone = phone;
        this.notificationList = notificationList;
        this.photo = photo;
        this.roles = new HashSet<>(Arrays.asList(roles));
        this.orderList = orderList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<Home> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<Home> homeList) {
        this.homeList = homeList;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(String... roles) {
        this.roles = new HashSet<>(Arrays.asList(roles));
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<OrderRequest> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderRequest> orderList) {
        this.orderList = orderList;
    }
}

