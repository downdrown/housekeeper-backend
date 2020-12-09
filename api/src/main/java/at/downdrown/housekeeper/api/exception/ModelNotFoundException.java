package at.downdrown.housekeeper.api.exception;

/**
 * Exception that will be thrown if the requested model has not been found.
 *
 * @author Manfred Huber
 */
public class ModelNotFoundException extends DataException {

    public ModelNotFoundException() {
        this("");
    }

    public ModelNotFoundException(String messageKey) {
        super(messageKey);
    }
}
