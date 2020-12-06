package at.downdrown.housekeeper.api.exception;

import org.junit.jupiter.api.Test;

import static at.downdrown.housekeeper.api.exception.ExceptionUtils.throwIf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @see ExceptionUtils
 * @author Manfred Huber
 */
public class ExceptionUtilsTest {

    @Test
    void shouldThrowAnException() {
        assertThrows(IllegalArgumentException.class, () -> throwIf(true, IllegalArgumentException::new), "Should throw an IllegalArgumentException");
    }

    @Test
    void shouldNotThrowAnException() {
        throwIf(false, IllegalArgumentException::new);
    }

    @Test
    void shouldThrowAnExceptionWithConditionSupplier() {
        MyObject object = new MyObject();
        assertThrows(IllegalArgumentException.class, () -> throwIf(object::isValid, IllegalArgumentException::new), "Should throw an IllegalArgumentException");
    }

    @Test
    void shouldNotThrowAnExceptionWithConditionSupplier() {
        MyObject object = new MyObject();
        throwIf(object::isNotValid, IllegalArgumentException::new);
    }

    private static final class MyObject {

        public boolean isValid() {
            return true;
        }

        public boolean isNotValid() {
            return false;
        }
    }
}
