package ua.nure.shporta.exception;

public class DBException extends AppException {
    private static final long serialVersionUID = -5787274581266409085L;

    public DBException() {
        super();
    }

    public DBException(String message){ super(message);}

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
