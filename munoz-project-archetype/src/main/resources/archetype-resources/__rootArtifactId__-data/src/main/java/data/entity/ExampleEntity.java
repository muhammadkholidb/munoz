#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.data.entity;

import hu.pe.munoz.commondata.entity.BaseEntity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = ExampleEntity.TABLE_NAME)
@DynamicInsert
@DynamicUpdate
public class ExampleEntity extends BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME = "t_example";

    @Column(name = "example_column", length = 64, nullable = false)
    private String exampleColumn;

    public String getExampleColumn() {
        return exampleColumn;
    }

    public void setExampleColumn(String exampleColumn) {
        this.exampleColumn = exampleColumn;
    }

}
