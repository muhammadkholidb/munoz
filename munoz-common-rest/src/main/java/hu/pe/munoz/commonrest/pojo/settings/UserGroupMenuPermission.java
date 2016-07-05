package hu.pe.munoz.commonrest.pojo.settings;

public class UserGroupMenuPermission {

	private String menuCode;
    private String view;
    private String modify;

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