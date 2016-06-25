package hu.pe.munoz.commonwebfaces.bean;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@SessionScoped
public class MenuBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBean.class);

    private static final String MENU_FILE_NAME_POSTFIX = ".menu.json";

    private static final String SUBMENU_FILE_NAME_POSTFIX = ".submenu.json";

    private JSONArray menus;

    @PostConstruct
    public void init() {
        loadMenus();
    }

    @SuppressWarnings("unchecked")
    private void loadMenus() {
        String dir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/");
        
        File[] menuFiles = new File(dir).listFiles(new FileFilter() {

            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(MENU_FILE_NAME_POSTFIX);
            }
        });
//        LOGGER.debug("File menus: " + menuFiles.length);
        File[] submenuFiles = new File(dir).listFiles(new FileFilter() {
            
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(SUBMENU_FILE_NAME_POSTFIX);
            }
        });
//        LOGGER.debug("File submenus: " + submenuFiles.length);
        menus = new JSONArray();
        JSONParser parser = new JSONParser();
        try {
            for (File file : menuFiles) {
                JSONArray array = (JSONArray) parser.parse(new FileReader(file));
                menus.addAll(array);
            }
//            LOGGER.debug("Menus: " + menus);
            JSONArray submenus = new JSONArray();
            for (File file : submenuFiles) {
                JSONArray array = (JSONArray) parser.parse(new FileReader(file));
                submenus.addAll(array);
            }
//            LOGGER.debug("Submenus: " + submenus);
            for (int i = 0; i < menus.size(); i++) {
                JSONObject json = (JSONObject) menus.get(i);
                String parentCode = (String) json.get("code");
                JSONArray children = getChildren(submenus, parentCode);
                json.put("submenus", children);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private JSONArray getChildren(JSONArray submenus, String parentCode) {
        JSONArray children = new JSONArray();
        for (int i = 0; i < submenus.size(); i++) {
            JSONObject json = (JSONObject) submenus.get(i);
            if (parentCode.equals(json.get("parentCode"))) {
                children.add(json);
            }
        }
        return children;
    }
    
    public JSONArray getMenus() {
        return menus;
    }

    public void setMenus(JSONArray menus) {
        this.menus = menus;
    }
}
