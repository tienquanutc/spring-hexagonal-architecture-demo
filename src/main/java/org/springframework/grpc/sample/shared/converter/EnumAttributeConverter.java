package org.springframework.grpc.sample.shared.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public abstract class EnumAttributeConverter<E extends Enum<E> & EnumAttribute<T>, T> implements AttributeConverter<E, T> {

    private final Map<T, E> valueToEnumMap;

    public EnumAttributeConverter(Class<E> enumClass) {
        this.valueToEnumMap = buildValueToEnumMap(enumClass);
    }

    @Override
    public T convertToDatabaseColumn(E e) {
        return e.getValue();
    }

    @Override
    public E convertToEntityAttribute(T t) {
        return this.valueToEnumMap.get(t);
    }

    private Map<T, E> buildValueToEnumMap(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
            .collect(Collectors.toUnmodifiableMap(EnumAttribute::getValue, e -> e));
    }
}
