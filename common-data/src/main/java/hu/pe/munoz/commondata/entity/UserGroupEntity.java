package hu.pe.munoz.commondata.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = UserGroupEntity.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQueries({
    @NamedQuery(name = "USER_GROUP.FIND_BY_NAME", query = "SELECT e FROM UserGroupEntity e WHERE e.name = :name") })
@DynamicInsert
@DynamicUpdate
public class UserGroupEntity extends BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "mn_user_group";
    
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "active", length = 1, nullable = false)
    private String active;

    @OneToMany(mappedBy = "userGroup")
    private List<UserEntity> users;

    @OneToMany(mappedBy = "userGroup")
    private List<UserGroupMenuPermissionEntity> userGroupMenuPermissions;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	public List<UserGroupMenuPermissionEntity> getUserGroupMenuPermissions() {
		return userGroupMenuPermissions;
	}

	public void setUserGroupMenuPermissions(List<UserGroupMenuPermissionEntity> userGroupMenuPermissions) {
		this.userGroupMenuPermissions = userGroupMenuPermissions;
	}

}