package org.rent.app.controller;

import org.hibernate.StaleStateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
/**
 * ErrorHandler
 * <p>
 *     Change http status in case of optimistic locking and entity not found
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(StaleStateException.class)
    public void handleOptimisticlLock(StaleStateException exception,
                                                     HttpServletResponse response) throws IOException {
        String msg = exception.getMessage();
        if (Objects.nonNull(msg) && msg.startsWith("Batch update returned unexpected row count from update")) {
            response.sendError(HttpStatus.PRECONDITION_FAILED.value(), "Optimistic lock. Try again");
        } else {
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
        }
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public void handleEntityNotFound(EntityNotFoundException exception,
                                      HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
