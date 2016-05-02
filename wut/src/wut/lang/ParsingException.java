package wut.lang;

/**
 * Created by josephburkey on 02/05/2016.
 */
public class ParsingException extends WutException{

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, String file, int line) {
        super(String.format("%s in %s:%d", message, file, line));
    }

}

