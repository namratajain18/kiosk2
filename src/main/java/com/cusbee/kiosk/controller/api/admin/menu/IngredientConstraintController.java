package com.cusbee.kiosk.controller.api.admin.menu;

import com.cusbee.kiosk.bean.dto.IngredientConstraintDTO;
import com.cusbee.kiosk.controller.PageContainer;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.IngredientConstraints;
import com.cusbee.kiosk.repository.IngredientConstraintRepository;
import com.google.common.base.Throwables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Api("Ingredient Constraint API")
@RestController
@RequestMapping(value = "/api/admin/ingredient-constraint")
public class IngredientConstraintController {

    @Autowired
    private IngredientConstraintRepository constraintRepository;

    @ApiOperation(
        value = "Creates a new Ingredient Constraint",
        response = IngredientConstraintDTO.class
    )
    @RequestMapping(value = {"/", "*"}, method = RequestMethod.PUT)
    public ResponseContainer<IngredientConstraints> createConstraint(@RequestBody IngredientConstraints constraint) {
        try {
            IngredientConstraints saved = constraintRepository.save(constraint);
            return ResponseContainer.Builder.<IngredientConstraints>builder()
                .data(saved)
                .code(200)
                .message("Ingredient Constraint, id: {0} is saved successfully", saved.getId())
                .build();
        } catch (Exception ex) {
            String reason = Throwables.getRootCause(ex).getMessage();
            return ResponseContainer.Builder.<IngredientConstraints>builder()
                .code(500)
                .message("Unable to create ingredient constraint, reason: {0}", reason)
                .build();
        }
    }

    @ApiOperation(
        value = "Returns all Ingredient Constraints",
        response = IngredientConstraintDTO.class,
        responseContainer = "List"
    )
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseContainer<List<IngredientConstraints>> getConstraints() {
        return ResponseContainer.Builder.<List<IngredientConstraints>>builder()
            .data(constraintRepository.findAll())
            .code(200)
            .build();
    }

    @ApiOperation(
        value = "Returns the Page of Ingredient Constraints",
        response = IngredientConstraintDTO.class,
        responseContainer = "List"
    )
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseContainer<List<IngredientConstraints>> getConstraints(
        @ApiParam(required = true, value = "The number of requested page") @RequestParam("page") int page,
        @ApiParam(required = true, value = "The desired size of requested page") @RequestParam("size") int size
    ) {
        Page<IngredientConstraints> all = constraintRepository.findAll(new PageRequest(page, size));
        PageContainer container = PageContainer.Builder.builder()
            .totalElements(all.getTotalElements())
            .totalPages(all.getTotalPages())
            .build();

        return ResponseContainer.Builder.<List<IngredientConstraints>>builder()
            .data(all.getContent())
            .code(200)
            .pageContainer(container)
            .build();
    }

    @ApiOperation(
        value = "Returns the Ingredient Constraint by its ID",
        response = IngredientConstraintDTO.class
    )
    @RequestMapping(value = "/{constraintId}", method = RequestMethod.GET)
    public ResponseContainer<IngredientConstraints> getConstraint(
        @ApiParam(required = true, value = "The ID of requested constraint") @PathVariable("constraintId") long constraintId
    ) {
        if (constraintRepository.exists(constraintId)) {
            return ResponseContainer.Builder.<IngredientConstraints>builder()
                .data(constraintRepository.findOne(constraintId))
                .code(200)
                .build();
        }
        return ResponseContainer.Builder.<IngredientConstraints>builder()
            .code(404)
            .message("Ingredient Constraint, id: {0} not found", constraintId)
            .build();
    }

    @ApiOperation(
        value = "Returns the Ingredient Constraint by ingredient ID",
        response = IngredientConstraintDTO.class
    )
    @RequestMapping(value = "/by-ingredient/{ingredientId}", method = RequestMethod.GET)
    public ResponseContainer<IngredientConstraints> getConstraintByIngredientId(
        @ApiParam(required = true, value = "The Ingredient ID of requested constraint") @PathVariable("ingredientId") Long ingredientId
    ) {
        try {
            Objects.requireNonNull(ingredientId, "The ID of ingredient should not be null");
            return ResponseContainer.Builder.<IngredientConstraints>builder()
                .data(constraintRepository.findByIngredientId(ingredientId))
                .code(200)
                .build();
        } catch (Exception ex) {
            return ResponseContainer.Builder.<IngredientConstraints>builder()
                .code(404)
                .message("Ingredient Constraint, ingredient id: {0} not found", ingredientId)
                .build();
        }
    }

    @ApiOperation(
        value = "Updates the Ingredient Constraint",
        response = IngredientConstraintDTO.class
    )
    @RequestMapping(value = "/{constraintId}", method = RequestMethod.POST)
    public ResponseContainer<IngredientConstraints> updateConstraint(
        @ApiParam(required = true, value = "The ID of updated constraint") @PathVariable("constraintId") long constraintId,
        @ApiParam(required = true, value = "The updated constraint") @RequestBody IngredientConstraints constraint
    ) {
        try {
            if (constraintRepository.exists(constraintId)) {
                constraint.setId(constraintId);
                return ResponseContainer.Builder.<IngredientConstraints>builder()
                    .data(constraintRepository.save(constraint))
                    .code(200)
                    .message("Ingredient Constraint, id: {0} is updated successfully", constraintId)
                    .build();
            }
            return ResponseContainer.Builder.<IngredientConstraints>builder()
                .code(404)
                .message("Ingredient Constraint, id: {0} not found", constraintId)
                .build();
        } catch (Exception ex) {
            String reason = Throwables.getRootCause(ex).getMessage();
            return ResponseContainer.Builder.<IngredientConstraints>builder()
                .code(500)
                .message("Unable to update ingredient constraint, id: {0}, is failed to update, reason: {1}", constraintId, reason)
                .build();
        }
    }

    @ApiOperation(
        value = "Removes the Ingredient Constraint",
        response = IngredientConstraintDTO.class
    )
    @RequestMapping(value = "/{constraintId}", method = RequestMethod.DELETE)
    public ResponseContainer<Void> removeIngredientById(
        @ApiParam(required = true, value = "The ID of removed constraint") @PathVariable("constraintId") long constraintId
    ) {
        try {
            constraintRepository.delete(constraintId);
            return ResponseContainer.Builder.<Void>builder()
                .code(200)
                .message("Ingredient Constraint, id: {0}, is removed successfully", constraintId)
                .build();
        } catch (Exception ex) {
            String reason = Throwables.getRootCause(ex).getMessage();
            return ResponseContainer.Builder.<Void>builder()
                .code(500)
                .message("Unable to remove ingredient constraint, id: {0}, is failed to update, reason: {1}", constraintId, reason)
                .build();
        }
    }
}
