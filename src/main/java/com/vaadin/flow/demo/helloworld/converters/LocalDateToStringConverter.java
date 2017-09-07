package com.vaadin.flow.demo.helloworld.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.template.model.ModelConverter;

/**
 * Converts between DateTime-objects and their String-representations
 * 
 */

public class LocalDateToStringConverter
        implements ModelConverter<LocalDate, String> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
            .ofPattern("MM/dd/yyyy");

    @Override
    public LocalDate toModel(String presentationValue) {
        try {
            return LocalDate.parse(presentationValue, DATE_FORMAT);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    @Override
    public String toPresentation(LocalDate modelValue) {
        return modelValue == null ? null : modelValue.format(DATE_FORMAT);
    }

}
