package com.tenmo2003.dynamicdto.registry;

import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

/**
 * @author anhvn
 * @since 2025-12-09
 */
public class DynamicDTORegistry {

    private final Map<String, Class<?>> nameToClass = new HashMap<>();
    private final Map<Class<?>, String> classToName = new HashMap<>();

    public DynamicDTORegistry(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(TypeName.class)) {
            TypeName typeName = clazz.getAnnotation(TypeName.class);
            if (typeName.value().isEmpty()) {
                nameToClass.put(clazz.getSimpleName(), clazz);
                classToName.put(clazz, clazz.getSimpleName());
            } else {
                nameToClass.put(typeName.value(), clazz);
                classToName.put(clazz, typeName.value());
            }
            for (String alias : typeName.aliases()) {
                nameToClass.put(alias, clazz);
            }
        }
    }

    public Class<?> getClass(String name) {
        return nameToClass.get(name);
    }

    public String getName(Class<?> clazz) {
        return classToName.get(clazz);
    }
}
