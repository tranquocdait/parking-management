package com.huyendieu.parking.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;

public class MapperUtils {

    public static <D> D map(Object source, Type destinationType) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(source, destinationType);
    }
}
