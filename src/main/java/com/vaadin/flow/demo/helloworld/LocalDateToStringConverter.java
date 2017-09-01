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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public LocalDate toModel(String presentationValue) {
        return LocalDate.parse(presentationValue, formatter);
    }

    @Override
    public String toPresentation(LocalDate modelValue) {
        return modelValue == null ? null : modelValue.toString();
    }

}
