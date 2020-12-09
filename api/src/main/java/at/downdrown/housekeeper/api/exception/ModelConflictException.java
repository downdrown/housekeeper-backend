package at.downdrown.housekeeper.api.exception;

/**
 * Exception that will be thrown if there has been a conflict when saving a model.
 *
 * @author Manfred Huber
 */
public class ModelConflictException extends DataException {

    public ModelConflictException() {
        this("");
    }

    public ModelConflictException(String messageKey) {
        super(messageKey);
    }
}
