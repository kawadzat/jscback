package io.getarrays.securecapita.exception;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 8/28/2022
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) { super(message); }
}
