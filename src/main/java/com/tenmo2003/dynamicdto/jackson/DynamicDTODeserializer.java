package com.tenmo2003.dynamicdto.jackson;

import com.tenmo2003.dynamicdto.dto.abstraction.DynamicDTO;
import com.tenmo2003.dynamicdto.registry.DynamicDTORegistry;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.node.ObjectNode;

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
        throws JacksonException {
        JsonNode node = p.objectReadContext().readTree(p);

        JsonNode typeNode = node.get(TYPE_FIELD);
        if (typeNode == null) {
            throw new IllegalArgumentException("Missing type field");
        }

        if (!typeNode.isString()) {
            throw new IllegalArgumentException("Type field must be a string");
        }

        Class<?> clazz = classRegistry.getClass(typeNode.asString());
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown type: " + typeNode.asString());
        }

        ((ObjectNode) node).remove(TYPE_FIELD);

        return (DynamicDTO) ctxt.readTreeAsValue(node, clazz);
    }
}
