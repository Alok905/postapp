package com.alok.postapp.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceType;
    private final String resourceIdentifier;

    public ResourceNotFoundException(String resourceType, String resourceIdentifier) {
        super(resourceType + " not found with identifier: " + resourceIdentifier);
        this.resourceType = resourceType;
        this.resourceIdentifier = resourceIdentifier;
    }
}
