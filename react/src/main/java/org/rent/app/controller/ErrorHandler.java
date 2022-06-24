package org.rent.app.controller;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;
/**
 * ErrorHandler
 * <p>
 *     Change http status in case of optimistic locking
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Mono<Void> handleOptimisticLock(OptimisticLockingFailureException exception) {
        return Mono.error(exception);
    }
}
