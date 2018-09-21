package com.cusbee.kiosk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "ingredient_constraints")
public class IngredientConstraints extends AbstractEntity {

    @Column(name = "ingredient_id", nullable = false, unique = true)
    private long ingredientId;

    @Column(name = "min_amount")
    private Integer minAmount;

    @Column(name = "max_amount")
    private Integer maxAmount;

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public static final class Builder {
        private Long id;
        private long ingredientId;
        private Integer minAmount;
        private Integer maxAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder ingredientId(long ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public Builder minAmount(Integer minAmount) {
            this.minAmount = minAmount;
            return this;
        }

        public Builder maxAmount(Integer maxAmount) {
            this.maxAmount = maxAmount;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public IngredientConstraints build() {
            final IngredientConstraints entity = new IngredientConstraints();
            entity.setId(id);
            entity.setIngredientId(ingredientId);
            entity.setMinAmount(minAmount);
            entity.setMaxAmount(maxAmount);

            return entity;
        }
    }
}
