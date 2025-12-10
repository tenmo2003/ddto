package com.tenmo2003.dynamicdto.jackson;

import com.tenmo2003.dynamicdto.dto.abstraction.DynamicDTO;
import com.tenmo2003.dynamicdto.registry.DynamicDTORegistry;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.introspect.BeanPropertyDefinition;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 * @author anhvn
 * @since 2025-12-09
 */
public class DynamicDTOSerializer extends StdSerializer<DynamicDTO> {

    private static final String TYPE_FIELD = "__type";
    private final DynamicDTORegistry classRegistry;

    public DynamicDTOSerializer(DynamicDTORegistry classRegistry) {
        super(DynamicDTO.class);
        this.classRegistry = classRegistry;
    }

    @Override
    public void serialize(DynamicDTO value, JsonGenerator gen, SerializationContext provider)
        throws JacksonException {
        gen.writeStartObject();
        gen.writeStringProperty(TYPE_FIELD, classRegistry.getName(value.getClass()));

        BeanDescription beanDesc = provider.introspectBeanDescription(
            provider.getTypeFactory().constructType(value.getClass())
        );

        for (BeanPropertyDefinition prop : beanDesc.findProperties()) {
            String fieldName = prop.getName();
            Object fieldValue = prop.getAccessor().getValue(value);

            provider.defaultSerializeProperty(fieldName, fieldValue, gen);
        }

        gen.writeEndObject();
    }
}
