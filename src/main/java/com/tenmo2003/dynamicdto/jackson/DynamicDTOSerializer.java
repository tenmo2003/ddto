package com.tenmo2003.dynamicdto.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tenmo2003.dynamicdto.dto.abstraction.DynamicDTO;
import com.tenmo2003.dynamicdto.registry.DynamicDTORegistry;

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
    public void serialize(DynamicDTO value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
        gen.writeStartObject();
        gen.writeStringField(TYPE_FIELD, classRegistry.getName(value.getClass()));

        BeanDescription beanDesc = provider
            .getConfig()
            .introspect(provider.constructType(value.getClass()));

        for (BeanPropertyDefinition prop : beanDesc.findProperties()) {
            String fieldName = prop.getName();
            Object fieldValue = prop.getAccessor().getValue(value);

            provider.defaultSerializeField(fieldName, fieldValue, gen);
        }

        gen.writeEndObject();
    }
}
