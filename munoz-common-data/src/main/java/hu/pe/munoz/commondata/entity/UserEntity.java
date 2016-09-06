package hu.pe.munoz.commondata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = UserEntity.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"lower_username"}),
    @UniqueConstraint(columnNames = {"lower_email"})
})
public class UserEntity extends BaseEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "mn_user";

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Column(name = "active", length = 1, nullable = false)
    private String active;

    @Column(name = "lower_username", length = 50, nullable = false)
    private String lowerUsername;

    @Column(name = "lower_email", length = 50, nullable = false)
    private String lowerEmail;

    @Column(name = "user_group_id")
    private Long userGroupId;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLowerUsername() {
        return lowerUsername;
    }

    public void setLowerUsername(String lowerUsername) {
        this.lowerUsername = lowerUsername;
    }

    public String getLowerEmail() {
        return lowerEmail;
    }

    public void setLowerEmail(String lowerEmail) {
        this.lowerEmail = lowerEmail;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }
    
}
