package io.getarrays.securecapita.exception;

public class NotAuthorizedException extends RuntimeException {

    private String resourceName;

    private String fieldName;

    private Object fieldValue;

    // Constructor with resourceName and fieldName to provide more detailed information
    public NotAuthorizedException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // Constructor with a custom message
    public NotAuthorizedException(String message) {
        super(message);
    }

    // Getters for additional context if needed
    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
