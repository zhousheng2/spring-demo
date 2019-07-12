package com.example.springframework.beans.converter;

public interface TypeConverter {

	boolean isType(Class<?> clazz);
	
	Object convert(String source);
}
