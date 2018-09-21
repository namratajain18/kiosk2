package com.cusbee.kiosk.bean.serde;

import com.cusbee.kiosk.bean.CategoryBean;
import com.cusbee.kiosk.entity.Categories;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author mxmind
 * @version 1.0.0.Alpha1
 * @since 1.0.0.Alpha1
 */
//@Component
public class CategoryJsonModule extends AbstractJsonModuleRegistrar<Categories, CategoryBean> {

    public CategoryJsonModule() {
        super(Categories.class, CategoryBean.class);
    }

    @Override
    protected void serialize(Categories entity, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (!HashSet.class.isInstance(entity.getMenuItems())) {
            entity.setMenuItems(null);
        }

        CategoryBean dto = new CategoryBean();
        getAssembler().assembleDto(dto, entity, null, null);
        jgen.writeObject(dto);
    }

    @Override
    protected Categories deserialize(JsonParser parser,
                                     DeserializationContext ctx,
                                     JsonDeserializer<Categories> delegatee) throws IOException {
        try {
            return delegatee.deserialize(parser, ctx);
        } catch (Exception ex) {
            Categories entity = new Categories();
            getAssembler().assembleEntity(parser.readValueAs(dtoType), entity, null, null);
            return entity;
        }
    }
}
