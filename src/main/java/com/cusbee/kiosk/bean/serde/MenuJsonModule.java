package com.cusbee.kiosk.bean.serde;

import com.cusbee.kiosk.bean.MenuBean;
import com.cusbee.kiosk.entity.Menus;
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
public class MenuJsonModule extends AbstractJsonModuleRegistrar<Menus, MenuBean> {

    public MenuJsonModule() {
        super(Menus.class, MenuBean.class);
    }

    @Override
    protected void serialize(Menus entity, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        MenuBean dto = new MenuBean();
        getAssembler().assembleDto(dto, entity, null, null);
        jgen.writeObject(dto);
    }

    @Override
    protected Menus deserialize(JsonParser parser, DeserializationContext ctx, JsonDeserializer<Menus> delegatee) throws IOException {
        try {
            return delegatee.deserialize(parser, ctx);
        } catch (Exception ex) {
            Menus entity = new Menus();
            getAssembler().assembleEntity(parser.readValueAs(dtoType), entity, null, null);
            return entity;
        }
    }
}
