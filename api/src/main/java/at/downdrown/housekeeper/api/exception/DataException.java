package at.downdrown.housekeeper.api.exception;

/**
 * Base class for all data-related exceptions.
 * Data exceptions are thrown as {@link RuntimeException}s since most of the time
 * users can't really do anything to fix the causing issue.
 *
 * @author Manfred Huber
 */
abstract class DataException extends RuntimeException {
    // no-op, just provide a 'base' class for better abstraction
}
