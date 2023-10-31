package utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class validator extends PlainDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int limite;

	public validator(int limite) {
		super();
		this.limite = limite;
	}

	public void insertString(int ofs, String str, AttributeSet a) throws BadLocationException {
		if ((getLength() + str.length()) <= limite) {
			super.insertString(ofs, str, a);

		}

	}

}
