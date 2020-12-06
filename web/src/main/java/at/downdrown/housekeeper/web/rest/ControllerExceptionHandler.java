package at.downdrown.housekeeper.web.rest;

import at.downdrown.housekeeper.api.exception.ModelConflictException;
import at.downdrown.housekeeper.api.exception.ModelNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.function.BiConsumer;

/**
 * Global exception handler that intercepts rest controller calls, checks
 * for specific Exceptions and returns corresponding {@link ResponseEntity} instances.
 *
 * @author Manfred Huber
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final BiConsumer<String, Throwable> DEBUG = log::debug;
    private static final BiConsumer<String, Throwable> WARN = log::warn;
    private static final BiConsumer<String, Throwable> ERROR = log::error;

    @ExceptionHandler(ModelConflictException.class)
    public ResponseEntity<Void> onModelConflictException(ModelConflictException exception) {
        log(WARN, exception);
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .build();
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<Void> onModelNotFoundException(ModelNotFoundException exception) {
        log(DEBUG, exception);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .build();
    }

    /** Utility method that logs that the request has been aborted with a specific level. */
    private static void log(final BiConsumer<String, Throwable> logMethod, final Throwable exception) {
        if (exception != null) {
            final String logString = String.format("%s occurred, aborting request!", exception.getClass().getSimpleName());
            logMethod.accept(logString, exception);
        }
    }
}
