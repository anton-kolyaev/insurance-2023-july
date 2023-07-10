package pot.insurance.manager.exception;

public class ClaimNotFoundException extends RuntimeException{

        public ClaimNotFoundException(String message) {
            super(message);
        }

        public ClaimNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public ClaimNotFoundException(Throwable cause) {
            super(cause);
        }
    
}
