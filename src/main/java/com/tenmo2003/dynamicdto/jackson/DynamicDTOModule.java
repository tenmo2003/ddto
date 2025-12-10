package com.tenmo2003.dynamicdto.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tenmo2003.dynamicdto.dto.abstraction.DynamicDTO;
import com.tenmo2003.dynamicdto.registry.DynamicDTORegistry;

/**
 * @author anhvn
 * @since 2025-12-09
 */
public class DynamicDTOModule extends SimpleModule {

    public DynamicDTOModule(DynamicDTORegistry classRegistry) {
        addSerializer(DynamicDTO.class, new DynamicDTOSerializer(classRegistry));
        addDeserializer(DynamicDTO.class, new DynamicDTODeserializer(classRegistry));
    }
}
