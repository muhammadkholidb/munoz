package hu.pe.munoz.commonrest.pojo.login;

public class LoginUser {

	private Long id;

	private String firstName;

	private String lastName;

	private String username;

	private String email;

	private String active;

	private LoginUserGroup userGroup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public LoginUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(LoginUserGroup userGroup) {
		this.userGroup = userGroup;
	}

}