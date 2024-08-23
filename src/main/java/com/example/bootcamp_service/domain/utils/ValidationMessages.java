package com.example.bootcamp_service.domain.utils;

public class ValidationMessages {
    public static final String NAME_NOT_BLANK = "El campo name no puede estar vacío";
    public static final String NAME_SIZE = "El campo name no puede tener más de 50 caracteres";
    public static final String DESCRIPTION_NOT_BLANK = "El campo description no puede estar vacío";
    public static final String DESCRIPTION_SIZE = "El campo description no puede tener más de 90 caracteres";
    public static final String BOOTCAMP_EXISTS = "Ya existe un bootcamp con el nombre proporcionado.";
    public static final String BOOTCAMP_CAPACITY = "El bootcamp debe tener entre 1 y 4 capacidades.";
    public static final String BOOTCAMP_DOESNT_EXIST = "El bootcamp no existe";

}
