package at.downdrown.housekeeper.api.exception;

import java.util.function.Supplier;

/**
 * Utility class for easier exception handling and throwing.
 *
 * @author Manfred Huber
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
        // no-op, just hide
    }

    /**
     * Throws a given {@link Throwable} if the {@code condition} is {@code true}.
     *
     * @param condition the condition that controls if the {@link Throwable} should be thrown or not.
     * @param constructor a {@link Supplier} that returns the desired {@link Throwable},
     *                    typically the constructor of an {@link Exception}.
     *                    {@code ExceptionUtils.throwIf(user.isLocked(), AccountLockedException::new);}
     * @param <T> the type of the {@link Throwable} that should be thrown.
     * @throws T thrown if the condition is {@code true}.
     */
    public static <T extends Throwable> void throwIf(boolean condition, Supplier<T> constructor) throws T {
        if (condition) {
            throw constructor.get();
        }
    }

    /**
     * Throws a given {@link Throwable} if the {@code condition} resolves to {@code true}.
     *
     * @param condition the a {@link Supplier} for the condition that controls if the {@link Throwable} should be thrown or not.
     * @param constructor a {@link Supplier} that returns the desired {@link Throwable},
     *                    typically the constructor of an {@link Exception}.
     *                    {@code ExceptionUtils.throwIf(user.isLocked(), AccountLockedException::new);}
     * @param <T> the type of the {@link Throwable} that should be thrown.
     * @throws T thrown if the condition is {@code true}.
     */
    public static <T extends Throwable> void throwIf(Supplier<Boolean> condition, Supplier<T> constructor) throws T {
        if (condition != null) {
            throwIf(condition.get(), constructor);
        }
    }
}
