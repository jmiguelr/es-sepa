package es.virtualsw.sepa.exceptions;

/**
 * Created by
 * User: jmiguel@virtualsw.com
 * Date: 11/01/14
 * Time: 19:40
 */
public class StopProcessingException extends Exception {
    public StopProcessingException() {
        super();
    }

    public StopProcessingException(String message) {
        super(message);
    }

    public StopProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StopProcessingException(Throwable cause) {
        super(cause);
    }

}
