package hu.pe.munoz.commondata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = UserGroupMenuPermissionEntity.TABLE_NAME, uniqueConstraints
        = @UniqueConstraint(columnNames = {"user_group_id", "menu_code"})) // Let hibernate give its constraint name
@NamedQueries({
    @NamedQuery(name = "USER_GROUP_MENU_PERMISSION.FIND_BY_USER_GROUP_ID", query = "SELECT e FROM UserGroupMenuPermissionEntity e WHERE e.userGroupId = :userGroupId")})
public class UserGroupMenuPermissionEntity extends BaseEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "mn_user_group_menu_permission";

    @Column(name = "menu_code", length = 50, nullable = false)
    private String menuCode;

    @Column(name = "view", length = 1, nullable = false)
    private String view;

    @Column(name = "modify", length = 1, nullable = false)
    private String modify;

    @Column(name = "user_group_id")
    private Long userGroupId;

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

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

}
