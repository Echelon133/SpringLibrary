package ml.echelon133.controller;

import ml.echelon133.exception.FailedFieldValidationException;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.message.IErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    private WebApplicationContext context;

    @Autowired
    public APIExceptionHandler(WebApplicationContext context) {
        this.context = context;
    }

    private IErrorMessage getErrorMessage() {
        return context.getBean(IErrorMessage.class);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<IErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        IErrorMessage errorMessage = getErrorMessage();
        errorMessage.addSingleMessage(ex.getMessage());
        errorMessage.setTimestamp(new Date());
        errorMessage.setPath(request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedFieldValidationException.class)
    protected ResponseEntity<IErrorMessage> handleFailedFieldValidationException(FailedFieldValidationException ex, WebRequest request) {
        IErrorMessage errorMessage = getErrorMessage();
        errorMessage.setMessages(ex.getTextErrors());
        errorMessage.setTimestamp(new Date());
        errorMessage.setPath(request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
