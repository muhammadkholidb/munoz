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
@Table(name = SystemEntity.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = { "data_key" }))
@NamedQueries({
    @NamedQuery(name = "SYSTEM.FIND_BY_KEY", query = "SELECT e FROM SystemEntity e WHERE e.dataKey = :dataKey") })
@DynamicInsert
@DynamicUpdate
public class SystemEntity extends BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "mn_system";

    @Column(name = "data_key", length = 50, nullable = false)
    private String dataKey;

    @Column(name = "data_value", length = 50, nullable = false)
    private String dataValue;

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

}
