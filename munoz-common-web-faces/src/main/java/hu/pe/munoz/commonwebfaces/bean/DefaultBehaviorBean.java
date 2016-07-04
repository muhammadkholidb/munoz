package hu.pe.munoz.commonwebfaces.bean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import org.omnifaces.util.Faces;

import hu.pe.munoz.commonwebfaces.helper.PageMode;

public abstract class DefaultBehaviorBean extends RESTBean {

	// private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final List<String> LIST_PATH_FOR_VIEW = Arrays.asList("/index.xhtml", "/view.xhtml");
	private final List<String> LIST_PATH_FOR_MODIFY = Arrays.asList("/edit.xhtml", "/add.xhtml");

	private final String PATH_INDEX = "/index.xhtml";
	
    @ManagedProperty(value = "#{loginBean}")
    protected LoginBean loginBean;

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    protected int mode = PageMode.INDEX;
    
    public DefaultBehaviorBean() {
    	if (Faces.getFlash().get("mode") != null) {
    		mode = (int) Faces.getFlash().get("mode");
    	}
    }
    
    public void pageCheck() {
    	String path = Faces.getRequestServletPath();
    	String pathFirstPart = path.substring(1, path.lastIndexOf("/"));
    	String pathLastPart = path.substring(path.lastIndexOf("/"));
    	if (!PATH_INDEX.equals(pathLastPart) && mode == PageMode.INDEX) { // Page mode is not set yet, so it is PageMode.INDEX
    		try {
				Faces.redirect(pathFirstPart + "/index.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
    		return;
    	}
    	if (LIST_PATH_FOR_MODIFY.contains(pathLastPart)) {
    		if (!isModifyAllowed()) {
				try {
					Faces.redirect("error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
    	}
    	if (LIST_PATH_FOR_VIEW.contains(pathLastPart)) {
    		if (!isViewAllowed()) {
				try {
					Faces.redirect("error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
    	}
    }

	public String gotoEdit() {
		Faces.getFlash().put("mode", PageMode.EDIT);
		return "edit?faces-redirect=true";
	}

	public String gotoAdd() {
		Faces.getFlash().put("mode", PageMode.ADD);
		return "add?faces-redirect=true";
	}

	public String gotoIndex() {
		Faces.getFlash().put("mode", PageMode.INDEX);
		return "index?faces-redirect=true";
	}

    public boolean isViewAllowed() {
    	return loginBean.isViewAllowed(getMenuCode());
    }
    
    public boolean isModifyAllowed() {
    	return loginBean.isModifyAllowed(getMenuCode());
    }
    
	abstract protected String getMenuCode();
	
}
