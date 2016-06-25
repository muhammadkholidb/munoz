package hu.pe.munoz.commondata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = SystemEntity.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = { "key" }))
@NamedQueries({
    @NamedQuery(name = "SYSTEM.FIND_BY_KEY", query = "SELECT e FROM SystemEntity e WHERE e.key = :key") })
@DynamicInsert
@DynamicUpdate
public class SystemEntity extends BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "mn_system";

    @Column(name = "key", length = 50, nullable = false)
    private String key;

    @Column(name = "value", length = 50, nullable = false)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
