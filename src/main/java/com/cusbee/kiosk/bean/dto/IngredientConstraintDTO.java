package com.cusbee.kiosk.bean.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Dto
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@ApiModel(
    value = "IngredientConstraintDTO",
    description = "The data transfer object designed to hold and transfer visible information about Ingredient Constraint"
)
public class IngredientConstraintDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DtoField
    @ApiModelProperty(value = "The id of constraint")
    private Long id;

    @DtoField
    @ApiModelProperty(value = "The id of ingredient associated with given constraint")
    private long ingredientId;

    @DtoField
    @ApiModelProperty(value = "The minimum amount of ingredient")
    private Integer minAmount;

    @DtoField
    @ApiModelProperty(value = "The maximum amount of ingredient")
    private Integer maxAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
