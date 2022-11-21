package com.huyendieu.parking.utils;

import java.lang.reflect.Type;

import org.modelmapper.ModelMapper;

public class MapperUtils {

    public static <D> D map(Object source, Type destinationType) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(source, destinationType);
    }
}
