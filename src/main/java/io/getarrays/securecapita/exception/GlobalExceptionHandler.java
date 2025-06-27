package io.getarrays.securecapita.exception;

import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * Global exception handler
 * <p>
 * This class handles all exceptions thrown by the controller methods.
 * It returns a ResponseEntity with a message in the body.
 * The message is a CustomMessage object converted to a String.
 * </p>
 *
 * @see CustomMessage
 * @see ResponseEntity
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handle SignatureException thrown by JwtTokenUtil
     *
     * @param ex Exception object
     * @return ResponseEntity with a message in the body
     */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException ex) {
        CustomMessage errorResponse = new CustomMessage("Invalid token, please login again");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Exception handlers
     * <p>
     * These methods handle exceptions thrown by the controller methods
     * and return a ResponseEntity with a message in the body.
     * The message is a CustomMessage object converted to a String.
     * </p>
     *
     * @param ex Exception object
     * @return ResponseEntity with a message in the body
     * @see CustomMessage
     * @see ResponseEntity
     */

//    @ExceptionHandler({RuntimeException.class})
//    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
//        ex.printStackTrace();
//        return ResponseEntity.badRequest().body(new CustomMessage("Server error, consider contacting the admin"));
//    }

    //MethodArgumentTypeMismatchException
    @ExceptionHandler({org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        ex.printStackTrace();
        CustomMessage errorResponse = new CustomMessage("Please check the type of the parameters you have passed");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle MethodArgumentNotValidException
     * <p>
     * This method handles MethodArgumentNotValidException thrown by the controller methods
     * and returns a ResponseEntity with a message in the body.
     * The message is a HashMap with field names as keys and error messages as values.
     * </p>
     *
     * @param ex Exception object
     * @return ResponseEntity with a message in the body
     * @see ResponseEntity
     * @see org.springframework.web.bind.MethodArgumentNotValidException
     * @see ExceptionHandler
     */
    @ExceptionHandler({org.springframework.web.bind.MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    //InvalidFormatException
    @ExceptionHandler({com.fasterxml.jackson.databind.exc.InvalidFormatException.class})
    public ResponseEntity<Object> handleInvalidFormatException(com.fasterxml.jackson.databind.exc.InvalidFormatException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put(ex.getPath().get(0).getFieldName(), "Invalid format");
        return ResponseEntity.badRequest().body(errors);
    }

    // UsernameNotFoundException
    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        CustomMessage errorResponse = new CustomMessage("User not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //IllegalArgumentException
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ex.printStackTrace();
        CustomMessage errorResponse = new CustomMessage("Please check the type of the parameters you have passed");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //.MalformedJwtException
    @ExceptionHandler({io.jsonwebtoken.MalformedJwtException.class})
    public ResponseEntity<Object> handleMalformedJwtException(io.jsonwebtoken.MalformedJwtException ex) {
        CustomMessage errorResponse = new CustomMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //HttpMessageNotReadableException
    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(new CustomMessage("Please check the type of the parameters you have passed"));
    }

    //.UnexpectedTypeException
    @ExceptionHandler({UnexpectedTypeException.class})
    public ResponseEntity<Object> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        HashMap<String, String> errors = new HashMap<>();
        ex.printStackTrace();
        errors.put("message", "Please check the type of the parameters you have passed");
        errors.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    //.SizeLimitExceededException
    @ExceptionHandler({org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException.class})
    public ResponseEntity<Object> handleSizeLimitExceededException(org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "File size is too large");
        errors.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    //MaxUploadSizeExceededException
    @ExceptionHandler({org.springframework.web.multipart.MaxUploadSizeExceededException.class})
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(org.springframework.web.multipart.MaxUploadSizeExceededException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "File size is too large");
        errors.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errors);
    }

//    //DefaultHandlerExceptionResolver
//    @ExceptionHandler({Exception.class})
//    public ResponseEntity<Object> handleException(Exception ex) {
//        ex.printStackTrace();
//        return ResponseEntity.badRequest().body(new CustomMessage("Server error, consider contacting the admin"));
//    }

    //IncorrectResultSizeDataAccessException
    @ExceptionHandler({org.springframework.dao.IncorrectResultSizeDataAccessException.class})
    public ResponseEntity<Object> handleIncorrectResultSizeDataAccessException(org.springframework.dao.IncorrectResultSizeDataAccessException ex) {
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(new CustomMessage("Incorrect result, consider contacting the admin"));
    }

    //TypeMismatchException
    @ExceptionHandler({org.springframework.beans.TypeMismatchException.class})
    public ResponseEntity<Object> handleTypeMismatchException(org.springframework.beans.TypeMismatchException ex) {
        HashMap<String, String> errors = new HashMap<>();
        ex.printStackTrace();
        errors.put("message", "Please check the type of the parameters you have passed");
        errors.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleTypeMismatchException(ResourceNotFoundException ex) {
        HashMap<String, String> errors = new HashMap<>();
        ex.printStackTrace();
        errors.put("message", ex.getMessage());
        errors.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({NotAuthorizedException.class})
    public ResponseEntity<Object> handleNotAuthorizedException(NotAuthorizedException ex) {
        HashMap<String, String> errors = new HashMap<>();
        ex.printStackTrace();
        errors.put("message", ex.getMessage());
        errors.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        HashMap<String, String> errors = new HashMap<>();
        ex.printStackTrace();
        errors.put("message", ex.getMessage());
        errors.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errors);
    }
    //jasper error;
//    @ExceptionHandler({net.sf.jasperreports.engine.JRException.class})
//    public ResponseEntity<Object> handleJRException(net.sf.jasperreports.engine.JRException ex) {
//        HashMap<String, String> errors = new HashMap<>();
//        ex.printStackTrace();
//        errors.put("message", "Could not generate report");
//        errors.put("error", ex.getLocalizedMessage());
//        return ResponseEntity.badRequest().body(errors);
//    }

    //IOException
//    @ExceptionHandler({java.io.IOException.class})
//    public ResponseEntity<Object> handleIOException(java.io.IOException ex) {
//        HashMap<String, String> errors = new HashMap<>();
//        ex.printStackTrace();
//        errors.put("message", "Something went wrong with the server");
//        errors.put("error", ex.getLocalizedMessage());
//        return ResponseEntity.badRequest().body(errors);
//    }
}
