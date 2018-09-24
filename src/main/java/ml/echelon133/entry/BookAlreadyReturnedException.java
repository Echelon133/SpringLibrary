package ml.echelon133.entry;

public class BookAlreadyReturnedException extends Exception {

    public BookAlreadyReturnedException(String msg) {
        super(msg);
    }
}
