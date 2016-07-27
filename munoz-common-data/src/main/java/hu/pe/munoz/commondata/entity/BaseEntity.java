package hu.pe.munoz.commondata.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MappedSuperclass
public abstract class BaseEntity {

    private static final Logger LOG = LoggerFactory.getLogger(BaseEntity.class);
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Set default value using database native language, so it is database dependent. 
    // This is useful when executing database query from script which does not mention column to insert / update, 
    // so it will set default value from the database.
    @Column(name = "created_at", columnDefinition="bigint default 0")
    // Set default value from Java. This is useful when performing insert / update using 
    // JPA persist() / merge() method without having to set the values of all columns.
    private Long createdAt = 0L;

    @Column(name = "modified_at", columnDefinition="bigint default 0")
    private Long modifiedAt = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @PrePersist
    protected void onCreate() {
        LOG.debug("onCreate() triggered ...");
        modifiedAt = createdAt = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        LOG.debug("onUpdate() triggered ...");
        modifiedAt = System.currentTimeMillis();
    }
}
