package hu.pe.munoz.commonrest.pojo.login;

import java.util.List;

public class LoginUserGroup {

	private Long id;

	private String name;

	private String active;

	private List<LoginUserGroupMenuPermission> userGroupMenuPermissions;

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

	public List<LoginUserGroupMenuPermission> getUserGroupMenuPermissions() {
		return userGroupMenuPermissions;
	}

	public void setUserGroupMenuPermissions(List<LoginUserGroupMenuPermission> userGroupMenuPermissions) {
		this.userGroupMenuPermissions = userGroupMenuPermissions;
	}

}
