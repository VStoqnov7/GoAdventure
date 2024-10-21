package com.example.GoAdventure.validation.validPhotos;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PhotosValidator implements ConstraintValidator<ValidPhotos, List<MultipartFile>> {

    private static final long MAX_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MAX_FILES = 6;
    private String sizeErrorMessage;
    private String countErrorMessage;
    private String minCountErrorMessage;

    @Override
    public void initialize(ValidPhotos constraintAnnotation) {
        this.sizeErrorMessage = constraintAnnotation.sizeErrorMessage();
        this.countErrorMessage = constraintAnnotation.countErrorMessage();
        this.minCountErrorMessage = constraintAnnotation.minCountErrorMessage();
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files == null || files.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(minCountErrorMessage)
                    .addConstraintViolation();
            return false;
        }

        boolean hasValidFile = false;
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                hasValidFile = true;
                break;
            }
        }

        if (!hasValidFile) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(minCountErrorMessage)
                    .addConstraintViolation();
            return false;
        }

        if (files.size() > MAX_FILES) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(countErrorMessage)
                    .addConstraintViolation();
            return false;
        }

        for (MultipartFile file : files) {
            if (file.getSize() > MAX_SIZE) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(sizeErrorMessage)
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}