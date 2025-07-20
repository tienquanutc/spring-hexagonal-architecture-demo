package org.springframework.grpc.sample.shared.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public abstract class GenericEnumAttributeConverter<E extends Enum<E> & EnumAttribute<T>, T>
        implements AttributeConverter<E, T> {

    private final Map<T, E> cache = new HashMap<>();
    private final Class<E> enumClass;

    @SuppressWarnings("unchecked")
    public GenericEnumAttributeConverter() {
        // Lấy generic type của subclass
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        this.enumClass = (Class<E>) type.getActualTypeArguments()[0];

        for (E constant : enumClass.getEnumConstants()) {
            cache.put(constant.getValue(), constant);
        }
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        return dbData != null ? cache.get(dbData) : null;
    }
}
