package wut.lang;

/**
 * Created by josephburkey on 01/05/2016.
 */
@SuppressWarnings("serial")
public class WutException extends Exception {

    public WutException(String message) {
        super(message);
    }

    public WutException(Throwable cause) {
        super(cause);
    }

    public WutException(String message, Throwable cause) {
        super(message, cause);
    }
}
