package Manager;

public class StrangeFileException extends Exception {
	private static final long serialVersionUID = 1L;

	public StrangeFileException() {
		super("Fatal Error: Strange file came in. Use valid files please.");
	}
}
