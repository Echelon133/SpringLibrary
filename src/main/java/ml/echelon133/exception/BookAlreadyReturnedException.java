package ml.echelon133.exception;

public class BookAlreadyReturnedException extends Exception {

    public BookAlreadyReturnedException(String msg) {
        super(msg);
    }
}
