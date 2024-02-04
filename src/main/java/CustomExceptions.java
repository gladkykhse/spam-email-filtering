/**
 * The CustomExceptions class contains custom exception classes used in the spam email filtering project.
 */
public class CustomExceptions {

    /**
     * The InvalidFileFormatException class is an exception thrown when a file has an invalid format.
     */
    static class InvalidFileFormatException extends Exception {

        /**
         * Constructs a new InvalidFileFormatException with no detail message.
         */
        public InvalidFileFormatException() {
            super();
        }

        /**
         * Constructs a new InvalidFileFormatException with the specified detail message.
         *
         * @param message the detail message (which is saved for later retrieval by the getMessage() method)
         */
        public InvalidFileFormatException(String message) {
            super(message);
        }

        /**
         * Constructs a new InvalidFileFormatException with the specified detail message and cause.
         *
         * @param message the detail message (which is saved for later retrieval by the getMessage() method)
         * @param cause   the cause (which is saved for later retrieval by the getCause() method)
         */
        public InvalidFileFormatException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
