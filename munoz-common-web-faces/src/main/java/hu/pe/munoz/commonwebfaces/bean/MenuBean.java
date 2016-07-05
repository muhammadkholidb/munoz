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

@ManagedBean
@SessionScoped
public class MenuBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String POSTFIX_FILE_MENU = ".menu.json";

    private static final String POSTFIX_FILE_SUBMENU = ".submenu.json";

    private JSONArray menus;
    private JSONArray flatMenus;

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
                return fileName.endsWith(POSTFIX_FILE_MENU);
            }
        });

        File[] submenuFiles = new File(dir).listFiles(new FileFilter() {
            
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(POSTFIX_FILE_SUBMENU);
            }
        });

        menus = new JSONArray();
        flatMenus = new JSONArray();
        JSONParser parser = new JSONParser();
        try {
            for (File file : menuFiles) {
                JSONArray array = (JSONArray) parser.parse(new FileReader(file));
                menus.addAll(array);
            }

            JSONArray submenus = new JSONArray();
            for (File file : submenuFiles) {
                JSONArray array = (JSONArray) parser.parse(new FileReader(file));
                submenus.addAll(array);
            }

            for (int i = 0; i < menus.size(); i++) {
                JSONObject json = (JSONObject) menus.get(i);
                String parentCode = (String) json.get("code");
                JSONArray children = getChildren(submenus, parentCode);
                json.put("submenus", children);
                flatMenus.add(json);
                flatMenus.addAll(children);
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

    public JSONArray getFlatMenus() {
        return flatMenus;
    }

}
