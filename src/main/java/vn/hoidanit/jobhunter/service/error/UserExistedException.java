package vn.hoidanit.jobhunter.service.error;

public class UserExistedException extends Exception {
    public UserExistedException(String message) {
        super(message);
    }
}
