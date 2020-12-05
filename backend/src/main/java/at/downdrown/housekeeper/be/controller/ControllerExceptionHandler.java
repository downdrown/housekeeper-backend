package at.downdrown.housekeeper.be.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that intercepts rest controller calls, checks
 * for specific Exceptions and returns corresponding {@link ResponseEntity} instances.
 *
 * @author Manfred Huber
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Void> onDataAccessException(DataAccessException exception) {
        log.error("DataAccessException occurred, aborting request!", exception);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build();
    }
}
