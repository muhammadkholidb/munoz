package hu.pe.munoz.commondata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = UserEntity.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = { "username_upper" }),
    @UniqueConstraint(columnNames = { "email_upper" }) })
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

    @Column(name = "username_upper", length = 50, columnDefinition = "varchar(50) as upper(username)")
    private String usernameUpper;
    
    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "email_upper", length = 50, columnDefinition = "varchar(50) as upper(email)")
    private String emailUpper;
    
    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Column(name = "active", length = 1, nullable = false)
    private String active;

    @ManyToOne
    @JoinColumn(name = "user_group_id", nullable = false)
    private UserGroupEntity userGroup;

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

    public UserGroupEntity getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupEntity userGroup) {
        this.userGroup = userGroup;
    }

	public String getUsernameUpper() {
		return usernameUpper;
	}

	public void setUsernameUpper(String usernameUpper) {
		this.usernameUpper = usernameUpper;
	}

	public String getEmailUpper() {
		return emailUpper;
	}

	public void setEmailUpper(String emailUpper) {
		this.emailUpper = emailUpper;
	}

}