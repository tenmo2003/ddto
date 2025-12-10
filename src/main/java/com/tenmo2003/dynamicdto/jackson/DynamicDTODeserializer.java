package com.tenmo2003.dynamicdto.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tenmo2003.dynamicdto.dto.abstraction.DynamicDTO;
import com.tenmo2003.dynamicdto.registry.DynamicDTORegistry;


/**
 * @author anhvn
 * @since 2025-12-09
 */
public class DynamicDTODeserializer extends StdDeserializer<DynamicDTO> {

    private static final String TYPE_FIELD = "__type";
    private final DynamicDTORegistry classRegistry;

    public DynamicDTODeserializer(DynamicDTORegistry classRegistry) {
        super(DynamicDTO.class);
        this.classRegistry = classRegistry;
    }

    @Override
    public DynamicDTO deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        JsonNode typeNode = node.get(TYPE_FIELD);
        if (typeNode == null) {
            throw new IllegalArgumentException("Missing type field");
        }

        if (!typeNode.isTextual()) {
            throw new IllegalArgumentException("Type field must be a string");
        }

        Class<?> clazz = classRegistry.getClass(typeNode.asText());
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown type: " + typeNode.asText());
        }

        ((ObjectNode) node).remove(TYPE_FIELD);

        return (DynamicDTO) ctxt.readTreeAsValue(node, clazz);
    }
}
