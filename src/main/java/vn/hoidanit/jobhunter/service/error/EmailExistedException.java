package vn.hoidanit.jobhunter.service.error;

public class EmailExistedException extends Exception {
    public EmailExistedException(String message) {
        super(message);
    }
}
