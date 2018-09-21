package com.cusbee.kiosk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author mxmind
 * @version 1.0.0.Alpha1
 * @since 1.0.0.Alpha1
 */
@MappedSuperclass
@EntityListeners(AbstractEntityListener.class)
public class AbstractEntity implements Identifiable<Long>, Serializable {

    /**
     * Auto-generated entity identifier.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username of user, which made the last changes to this entity.
     */
    @Column(name = "modifiedBy")
    @JsonIgnore
    private String modifiedBy;

    /**
     * Date the last changes to this entity.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonIgnore
    private Date modifiedDate;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) obj;
        return Objects.equals(id, that.id)
            && Objects.equals(modifiedBy, that.modifiedBy)
            && Objects.equals(modifiedDate, that.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modifiedBy, modifiedDate);
    }
}
