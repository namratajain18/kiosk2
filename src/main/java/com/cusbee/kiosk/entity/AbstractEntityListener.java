package com.cusbee.kiosk.entity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;
import java.util.Date;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
public class AbstractEntityListener {

    @PrePersist
    public void prePersist(AbstractEntity entity) {
        entity.setModifiedBy(getModifierUsername());
        entity.setModifiedDate(Date.from(Instant.now()));
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        entity.setModifiedBy(getModifierUsername());
        entity.setModifiedDate(Date.from(Instant.now()));
    }

    private String getModifierUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null
            ? "anonymous"
            : authentication.getPrincipal().toString().startsWith("anonymous")
            ? "anonymous"
            : UserDetails.class.cast(authentication.getPrincipal()).getUsername();
    }
}
