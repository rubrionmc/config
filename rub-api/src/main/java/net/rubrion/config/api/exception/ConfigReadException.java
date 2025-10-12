package net.rubrion.config.api.exception;

/**
 * Exception thrown when a configuration file cannot be read or parsed successfully.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public class ConfigReadException extends RuntimeException {

    /**
     * Constructs a new ConfigReadException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     *
     * @author LeyCM
     * @since 2.0.2
     */
    public ConfigReadException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigReadException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the underlying cause of this exception
     *
     * @author LeyCM
     * @since 2.0.2
     */
    public ConfigReadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConfigReadException with the specified cause.
     *
     * @param cause the underlying cause of this exception
     *
     * @author LeyCM
     * @since 2.0.2
     */
    public ConfigReadException(Throwable cause) {
        super(cause);
    }
}