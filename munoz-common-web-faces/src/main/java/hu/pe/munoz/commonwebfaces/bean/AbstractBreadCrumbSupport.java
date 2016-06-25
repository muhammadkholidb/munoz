package hu.pe.munoz.commonwebfaces.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.omnifaces.util.Faces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;

public abstract class AbstractBreadCrumbSupport extends RESTBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBreadCrumbSupport.class);

    private MenuModel breadcrumbs = new DefaultMenuModel();

    public AbstractBreadCrumbSupport() {
        loadBreadCrumbs();
    }

    private void loadBreadCrumbs() {
    	LOGGER.info("Load breadcrumbs ...");
        DefaultMenuItem menuItem = new DefaultMenuItem();
        String languageCode = Faces.getViewRoot().getLocale().getLanguage();
        String labelHome = CommonConstants.EMPTY_STRING;
        if (CommonConstants.LANGUAGE_CODE_ENGLISH.equals(languageCode)) {
            labelHome = "Home";
        } else if (CommonConstants.LANGUAGE_CODE_INDONESIA.equals(languageCode)) {
            labelHome = "Beranda";
        }
        menuItem.setValue(labelHome);
        menuItem.setUrl("/home.xhtml");
        breadcrumbs.addElement(menuItem);

        List<JSONObject> crumbs = new ArrayList<JSONObject>();
        
        addCrumbs(crumbs, languageCode);

        for (JSONObject crumb : crumbs) {
            menuItem = new DefaultMenuItem();
            String label = (String) crumb.get("label");
            String page = (String) crumb.get("page");
            menuItem.setValue(label);
            menuItem.setUrl(page);
            breadcrumbs.addElement(menuItem);
        }
    }

    abstract protected void addCrumbs(List<JSONObject> crumbs, String languageCode);

    public MenuModel getBreadcrumbs() {
        return breadcrumbs;
    }

}
