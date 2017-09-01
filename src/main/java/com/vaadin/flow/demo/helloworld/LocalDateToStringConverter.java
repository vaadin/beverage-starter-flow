package com.vaadin.flow.demo.helloworld;

import java.time.LocalDate;

import com.vaadin.flow.template.model.ModelConverter;

public class LocalDateToStringConverter
        implements ModelConverter<LocalDate, String> {

    @Override
    public LocalDate toModel(String presentationValue) {
        return null;
    }

    @Override
    public String toPresentation(LocalDate modelValue) {
        return modelValue.toString();
    }

}
