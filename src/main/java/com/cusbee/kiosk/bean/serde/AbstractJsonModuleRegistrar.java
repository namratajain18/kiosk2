package com.cusbee.kiosk.bean.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author mxmind
 * @version 1.0.0.Alpha
 * @since 1.0.0.Alpha
 */
public abstract class AbstractJsonModuleRegistrar<E, D> implements InitializingBean {

    /**
     * Holds reference on GEDA bean assembler.
     */
    private final Assembler assembler;


    /**
     * The Entity type.
     */
    protected final Class<E> entityType;

    /**
     * The Dto type.
     */
    protected final Class<D> dtoType;

    /**
     * Injectable resource that contains reference on default object mapper configured via Spring Framework.
     */
    //@Resource(name = "jacksonObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    /**
     * Instantiates a new Abstract json module registrar.
     * @param entityType
     * @param dtoType
     */
    protected AbstractJsonModuleRegistrar(Class<E> entityType, Class<D> dtoType) {
        this.entityType = entityType;
        this.dtoType = dtoType;
        this.assembler = DTOAssembler.newAssembler(dtoType, entityType, AbstractJsonModuleRegistrar.class.getClassLoader());
    }

    public Assembler getAssembler() {
        return assembler;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Setups factory to declare specific data bindings between entity and dto.
     * Also, this method is eligible to declare data bindings for supported classes.
     */
    @PostConstruct
    public void configure() {
        objectMapper = springMvcJacksonConverter.getObjectMapper();
    }

    /**
     * Registers the module to object mapper modules registry after configuring all parts of given module.
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        SimpleModule module = new SimpleModule(this.getClass().getSimpleName());
        module.addSerializer(entityType, new EntitySerializer());
        module.setDeserializerModifier(createModifier());

        objectMapper.registerModule(module);
    }

    /**
     * Setup custom deserializer as object mapper modifier.
     * That allows to have full control on deserialization process.
     *
     * @return the modifier
     */
    @Nonnull
    private BeanDeserializerModifier createModifier() {
        return new BeanDeserializerModifier() {

            /**
             * {@inheritDoc}
             */
            @Override
            @SuppressWarnings("unchecked")
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
                                                          BeanDescription beanDesc,
                                                          JsonDeserializer<?> delegatee) {
                // if object is not dto or entity the delegatee will be used as primary deserializer;
                return beanDesc.getBeanClass().equals(entityType)
                    ? new EntityDeserializer((JsonDeserializer<E>) delegatee)
                    : delegatee;
            }
        };
    }

    /**
     * Performs serialization of entity to JSON.
     *
     * @param entity   the entity to be serialized
     * @param jgen     the JSON generator
     * @param provider the provider
     *
     * @throws IOException
     */
    protected abstract void serialize(E entity, JsonGenerator jgen, SerializerProvider provider) throws IOException;

    /**
     * Performs deserialization entity from JSON.
     *
     * @param parser    the JSON parser
     * @param ctx       the current deserialization context
     * @param delegatee the fallback deserializer to delegate custom deserialization, in case, if received object is dto
     * @return deserialized entity
     *
     * @throws IOException
     */
    protected abstract E deserialize(JsonParser parser, DeserializationContext ctx, JsonDeserializer<E> delegatee)
        throws IOException;

    /**
     * The proxy entity serializer.
     * Calls #serialize method of root class to perform serialization.
     */
    private class EntitySerializer extends JsonSerializer<E> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(E value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            AbstractJsonModuleRegistrar.this.serialize(value, jgen, provider);
        }
    }

    /**
     * The proxy entity deserializer.
     * Calls #deserialize method of root class to perform deserialization.
     */
    private class EntityDeserializer extends StdDeserializer<E> implements ResolvableDeserializer {

        /**
         * The fallback deserializer to perform custom deserialization, in case, if received object is dto.
         */
        protected final JsonDeserializer<E> delegatee;

        /**
         * Instantiates a new Entity deserializer.
         *
         * @param delegatee the delegatee
         */
        public EntityDeserializer(JsonDeserializer<E> delegatee) {
            super(entityType);
            this.delegatee = delegatee;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public E deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
            return AbstractJsonModuleRegistrar.this.deserialize(parser, ctx, delegatee);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resolve(DeserializationContext ctx) throws JsonMappingException {
            ((ResolvableDeserializer) delegatee).resolve(ctx);
        }
    }
}
