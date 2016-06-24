package hu.pe.munoz.commonrest.pojo.login;

public class LoginUserGroupMenuPermission {

	private Long id;

	private String menuCode;

	private String view;

	private String modify;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getModify() {
		return modify;
	}

	public void setModify(String modify) {
		this.modify = modify;
	}

}