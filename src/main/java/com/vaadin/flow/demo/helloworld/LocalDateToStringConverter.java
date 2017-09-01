package com.vaadin.flow.demo.helloworld;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Converts between Date-objects and their String-representations 
 * 
 */

import com.vaadin.flow.template.model.ModelConverter;

public class LocalDateToStringConverter
        implements ModelConverter<LocalDate, String> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
            .ofPattern("MM/dd/yyyy");

    @Override
    public LocalDate toModel(String presentationValue) {
        return LocalDate.parse(presentationValue, DATE_FORMAT);
    }

    @Override
    public String toPresentation(LocalDate modelValue) {
        return modelValue == null ? null : modelValue.toString();
    }

}
