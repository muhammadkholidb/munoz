package hu.pe.munoz.commonrest.pojo.settings;

import java.util.List;

public class UserGroupWithMenuPermissions {

	private Long id;
    private String name;
    private String active;
    private List<UserGroupMenuPermission> userGroupMenuPermissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public List<UserGroupMenuPermission> getUserGroupMenuPermissions() {
		return userGroupMenuPermissions;
	}

	public void setUserGroupMenuPermissions(List<UserGroupMenuPermission> userGroupMenuPermissions) {
		this.userGroupMenuPermissions = userGroupMenuPermissions;
	}

}
