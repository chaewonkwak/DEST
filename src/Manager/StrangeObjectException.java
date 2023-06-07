package Manager;

public class StrangeObjectException extends Exception {
	private static final long serialVersionUID = 1L;

	public StrangeObjectException() {
		super("Fatal Error: Strange object came in. Use valid key and signature please.");
	}
}
