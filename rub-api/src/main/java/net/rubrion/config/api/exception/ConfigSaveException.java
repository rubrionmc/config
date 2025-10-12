package net.rubrion.config.api.exception;

/**
 * Exception thrown when a configuration file cannot be saved successfully.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public class ConfigSaveException extends RuntimeException {

    /**
     * Constructs a new ConfigSaveException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ConfigSaveException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigSaveException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the underlying cause of this exception
     */
    public ConfigSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConfigSaveException with the specified cause.
     *
     * @param cause the underlying cause of this exception
     */
    public ConfigSaveException(Throwable cause) {
        super(cause);
    }
}
