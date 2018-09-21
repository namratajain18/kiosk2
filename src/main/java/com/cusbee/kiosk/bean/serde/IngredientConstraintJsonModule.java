package com.cusbee.kiosk.bean.serde;

import com.cusbee.kiosk.bean.dto.IngredientConstraintDTO;
import com.cusbee.kiosk.entity.IngredientConstraints;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author mxmind
 * @version 1.0.0.Alpha1
 * @since 1.0.0.Alpha1
 */
@Component
public class IngredientConstraintJsonModule extends AbstractJsonModuleRegistrar<IngredientConstraints, IngredientConstraintDTO> {

    public IngredientConstraintJsonModule() {
        super(IngredientConstraints.class, IngredientConstraintDTO.class);
    }

    @Override
    protected void serialize(IngredientConstraints entity, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        IngredientConstraintDTO dto = new IngredientConstraintDTO();
        getAssembler().assembleDto(dto, entity, null, null);
        jgen.writeObject(dto);
    }

    @Override
    protected IngredientConstraints deserialize(JsonParser parser,
                                                DeserializationContext ctx,
                                                JsonDeserializer<IngredientConstraints> delegatee) throws IOException {
        try {
            return delegatee.deserialize(parser, ctx);
        } catch (Exception ex) {
            IngredientConstraints entity = new IngredientConstraints();
            getAssembler().assembleEntity(parser.readValueAs(dtoType), entity, null, null);
            return entity;
        }
    }
}
