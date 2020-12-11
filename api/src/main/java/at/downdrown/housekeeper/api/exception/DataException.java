package at.downdrown.housekeeper.api.exception;

import lombok.Getter;

/**
 * Base class for all data-related exceptions.
 * Data exceptions are thrown as {@link RuntimeException}s since most of the time
 * users can't really do anything to fix the causing issue.
 *
 * @author Manfred Huber
 */
@Getter
abstract class DataException extends RuntimeException {

    /** The error message key that can be evaluated on the client side. */
    private final String messageKey;

    protected DataException(String messageKey) {
        this.messageKey = messageKey;
    }

}
