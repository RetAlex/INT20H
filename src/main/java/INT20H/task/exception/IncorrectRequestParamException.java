package INT20H.task.exception;

public class IncorrectRequestParamException extends Exception {
    public IncorrectRequestParamException(String msg){
        super(msg);
    }

    public static void invalidPageNumber() throws IncorrectRequestParamException {
        throw new IncorrectRequestParamException(("Page can not lowest than 0!"));
    }
}
