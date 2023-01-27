/**
 * 
 */
package jme.io;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;





/**
 * Credit: http://www.javapractices.com/topic/TopicAction.do?Id=82
 */
public class TextTransfer implements ClipboardOwner, ActionListener {
	

	
	
	public interface PasteAction {
		void paste(String clipboardContent);
	}

	private PasteAction pasteAction;

	public void test() {
		TextTransfer textTransfer = new TextTransfer();

		// display what is currently on the clipboard
		System.out.println("Clipboard contains:"
				+ textTransfer.getClipboardContents());

		// change the contents and then re-display
		textTransfer.setClipboardContents("blah, blah, blah");
		System.out.println("Clipboard contains:"
				+ textTransfer.getClipboardContents());
	}

	/**
	 * Empty implementation of the ClipboardOwner interface.
	 */
	public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
		// do nothing
	}

	/**
	 * Place a String on the clipboard, and make this class the owner of the
	 * Clipboard's contents.
	 */
	public void setClipboardContents(String aString) {
		/**
		 * @j2sNative
		 * 
		 * navigator.clipboard.writeText(aString);
		 */
		{
		
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
		}
	}

	/**
	 * Get the String residing on the clipboard.
	 * 
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public String getClipboardContents() {
		String result = null;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(this);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents
						.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Get the String residing on the clipboard and when available paste it.
	 * 
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public void getAsyncClipboardContents(PasteAction pasteAction) {
		this.pasteAction = pasteAction;
		String content = this.getClipboardContents();
		if(content != null) {
			pasteAction.paste(content); //happens when synchronous, for Java applet and JavaScript IE applets
		} else {
			//pasting will happen in actionPerformed()
		}
	}

	/*
	 * This method will be called by a pasting operation that is asynchronous such as from a JavaSvript window.
	 * This is a hack for non IE browsers that do not support clipboard handling.
	 * See the Clipboard implementation .
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		this.pasteAction.paste(e.getActionCommand());
	}

}
