package com.example.GoAdventure.validation.validPhotos;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhotosValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhotos {
    String message() default "Invalid photos upload.";
    String sizeErrorMessage() default "Each file size must be less than 10MB!";
    String countErrorMessage() default "You can upload a maximum of 6 photos!";
    String minCountErrorMessage() default "At least one photo is required!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}