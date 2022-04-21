package ua.nure.shporta.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String password;
    private String login;
    private String role;
    private String description;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<UsersTest> usersTests;

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public List<UsersTest> getUsersTests() {
        return usersTests;
    }

    public void setUsersTests(List<UsersTest> usersTests) {
        this.usersTests = usersTests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!password.equals(user.password)) return false;
        if (!login.equals(user.login)) return false;
        if (!role.equals(user.role)) return false;
        if (description != null ? !description.equals(user.description) : user.description != null) return false;
        return subscriptions != null ? subscriptions.equals(user.subscriptions) : user.subscriptions == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (subscriptions != null ? subscriptions.hashCode() : 0);
        return result;
    }
}
