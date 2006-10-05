package trainer;

public class CacheException extends RuntimeException {
// Exception for reporting such cache errors that prevent
// the program to proceed

    public CacheException(String msg) {
       super("CACHE ERROR "+ msg);
    }
}
