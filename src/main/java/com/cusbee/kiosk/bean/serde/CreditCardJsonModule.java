package com.cusbee.kiosk.bean.serde;

import com.cusbee.kiosk.bean.dto.CreditCardDTO;
import com.cusbee.kiosk.controller.api.customer.payment.CreditCardTypeCheck;
import com.cusbee.kiosk.controller.api.customer.payment.Encryption;
import com.cusbee.kiosk.entity.CreditCards;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class CreditCardJsonModule extends AbstractJsonModuleRegistrar<CreditCards, CreditCardDTO> {

    public CreditCardJsonModule() {
        super(CreditCards.class, CreditCardDTO.class);
    }

    @Override
    protected void serialize(CreditCards entity, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        Function<Integer, String> pad = num -> StringUtils.leftPad(num.toString(), 2, '0');
        String cardNumber = entity.getPan().replaceAll("\\s+", "");
        int index = cardNumber.length() - 4;
        String formattedNumber = cardNumber.substring(0, index).replaceAll("\\d", "*") + cardNumber.substring(index);

        CreditCardDTO dto = CreditCardDTO.Builder.builder()
            .creditCardCvv(entity.getCvvCode())
            .creditCardExpMonth(pad.apply(entity.getExpirationMonth()))
            .creditCardExpYear(pad.apply(entity.getExpirationYear()))
            .creditCardLastFour(String.join(" ", Splitter.fixedLength(4).split(formattedNumber)))
            .creditCardType(CreditCardTypeCheck.getCreditCardType(cardNumber).getType())
            .creditCardHolderName(entity.getNameOnCard().toUpperCase())
            .creditCardCvv(entity.getCvvCode())
            .creditCardId(entity.getId())
            .build();

        jgen.writeObject(dto);
    }

    @Override
    protected CreditCards deserialize(
        JsonParser parser,
        DeserializationContext ctx,
        JsonDeserializer<CreditCards> delegatee) throws IOException {
        try {
            return delegatee.deserialize(parser, ctx);
        } catch (Exception ex) {
            CreditCards entity = CreditCards.Builder.builder().build();
            getAssembler().assembleEntity(parser.readValueAs(dtoType), entity, null, null);
            return entity;
        }
    }
}
