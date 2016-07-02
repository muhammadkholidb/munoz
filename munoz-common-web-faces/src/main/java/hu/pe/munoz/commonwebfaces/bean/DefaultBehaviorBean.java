package hu.pe.munoz.commonwebfaces.bean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultBehaviorBean extends RESTBean {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final List<String> LIST_PAGE_FOR_VIEW = Arrays.asList("index.xhtml", "view.xhtml");
	private final List<String> LIST_PAGE_FOR_MODIFY = Arrays.asList("edit.xhtml", "add.xhtml");

    @ManagedProperty(value = "#{loginBean}")
    protected LoginBean loginBean;

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
    
    public void pageCheck() {
    	log.debug("Page check ...");
    	String path = Faces.getRequestServletPath();
    	log.debug("Path: " + path);
    	for (String page : LIST_PAGE_FOR_MODIFY) {
    		if (path.endsWith(page)) {
    			log.debug("Accessing page for modify ...");
    			if (!isModifyAllowed()) {
    				log.debug("Not allowed to modify ...");
    				try {
						Faces.redirect("error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
    			}
    		}
    	}
    	for (String page : LIST_PAGE_FOR_VIEW) {
    		if (path.endsWith(page)) {
    			log.debug("Accessing page for view ...");
    			if (!isViewAllowed()) {
    				log.debug("Not allowed to view ...");
    				try {
						Faces.redirect("error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
    			}
    		}
    	}
    }

	public String gotoEdit() {
		return "edit?faces-redirect=true";
	}

	public String gotoAdd() {
		return "add?faces-redirect=true";
	}

	public String gotoIndex() {
		return "index?faces-redirect=true";
	}

    public boolean isViewAllowed() {
    	return loginBean.isViewAllowed(getMenuCode());
    }
    
    public boolean isModifyAllowed() {
    	return loginBean.isModifyAllowed(getMenuCode());
    }
    
	abstract protected String getIndexPath();
	
	abstract protected String getMenuCode();
	
}
