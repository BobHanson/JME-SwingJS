/* $RCSfile$
 * $Author: hansonr $
 * $Date: 2013-09-25 15:33:17 -0500 (Wed, 25 Sep 2013) $
 * $Revision: 18695 $
 *
 * Copyright (C) 2004-2005  The Jmol Development Team
 *
 * Contact: jmol-developers@lists.sf.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jme;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

/**
 * A simple Dropping class to allow files to be dragged onto a target. From
 * org.jmol.awt.FleDropper
 * 
 */
public class FileDropper implements DropTargetListener {
	
	public static final String PROPERTY_FILEDROPPER_INLINE = "FileDropper.inline";
	public static final String PROPERTY_FILEDROPPER_FILE = "FileDropper.file";
	
	public static DataFlavor uriDrop;
	
	private PropertyChangeSupport fd_propSupport;
	private PropertyChangeListener pcl;
	private PropertyChangeListener fileDropListener;

	public FileDropper(PropertyChangeListener fileDropListener) {
		this.fileDropListener = fileDropListener;
		fd_propSupport = new PropertyChangeSupport(this);
		addPropertyChangeListener(fileDropListener);
		addPropertyChangeListener((pcl = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("inline"))
					loadInline(evt.getNewValue());
			}
		}));
		Component receiver = (Component) fileDropListener;
		receiver.setDropTarget(new DropTarget(receiver, this));
	}

	public void dispose() {
		removePropertyChangeListener(pcl);
		pcl = null;
		fd_propSupport.removePropertyChangeListener(null);
		fd_propSupport = null;
		fileDropListener = null;
	}

	private void loadFile(String fname) {
		fd_propSupport.firePropertyChange(PROPERTY_FILEDROPPER_FILE, null, fname);
	}

	private void loadInline(Object content) {
		fd_propSupport.firePropertyChange(PROPERTY_FILEDROPPER_INLINE, null, content);
	}

//  private void loadFiles(List<File> fileList) {
//	  // return null; // not supported
//  }

	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		fd_propSupport.addPropertyChangeListener(l);
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
		fd_propSupport.removePropertyChangeListener(l);
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}

	@Override
	public void dragExit(DropTargetEvent dtde) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent dtde) {
		System.out.println("FileDropper? " + dtde.getDropTargetContext().getComponent());
		Transferable t = dtde.getTransferable();
		boolean isAccepted = false;
		if (uriDrop != null && t.isDataFlavorSupported(uriDrop)) { 
			if (doURIDrop(t, dtde, isAccepted, uriDrop))
				return;
		}
		if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			while (true) {
				Object o = null;
				try {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					o = t.getTransferData(DataFlavor.javaFileListFlavor);
					isAccepted = true;
					if (o instanceof List) {
						List<File> fileList = (List<File>) o;
						final int length = fileList.size();
						if (length >= 1) {
							String fileName = fileList.get(0).getAbsolutePath().trim();
							if (fileName.endsWith(".URL")) {
								// this is a drag drop from a link on a web page. We can do better.
								break;
							}
							dtde.getDropTargetContext().dropComplete(true);
							loadFile(fileName);
							return;
						}
						dtde.getDropTargetContext().dropComplete(true);
						return;
					}
				} catch (Exception e) {
					System.err.println("jme.FileDropper failed " + e);
					// try another
				}
				break;
			}
		}
		DataFlavor[] df = t.getTransferDataFlavors();
		if (df == null || df.length == 0)
			return;
		for (int i = 0; i < df.length; ++i) {
			DataFlavor flavor = df[i];
			Object o = null;

//			System.out.println("df " + i + " flavor " + flavor);
//			System.out.println("  class: " + flavor.getRepresentationClass().getName());
//			System.out.println("  mime : " + flavor.getMimeType());

			String mimeType = flavor.getMimeType();
			if (mimeType.startsWith("text/uri-list")
					&& flavor.getRepresentationClass().getName().equals("java.lang.String")) {
				uriDrop = flavor;
				if (doURIDrop(t, dtde, isAccepted, flavor))
					return;
			} else if (mimeType.equals("application/x-java-serialized-object; class=java.lang.String")
					|| mimeType.startsWith("text/plain;")) {
				try {
					if (!isAccepted)
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					isAccepted = true;
					o = t.getTransferData(df[i]);
				} catch (Exception e) {
					System.err.println("D.....");
				}
				if (o instanceof String) {
					String content = (String) o;
					if (content.startsWith("file:/")) {
						loadFile(content);
					} else {
						loadInline(content);
					}
					dtde.getDropTargetContext().dropComplete(true);
					return;
				}
			}
		}
		if (!isAccepted)
			dtde.rejectDrop();
	}

	private boolean doURIDrop(Transferable t, DropTargetDropEvent dtde, boolean isAccepted, DataFlavor flavor) {
		Object o;
		try {
			if (!isAccepted)
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			isAccepted = true;
			o = t.getTransferData(flavor);
			if (o instanceof String) {
				System.err.println("jme.FileDropper drop for " + o);
				loadFile(o.toString());
				dtde.getDropTargetContext().dropComplete(true);
				return true;
			}
		} catch (Exception e) {
			System.err.println("jme.FileDropper failed drop for " + flavor);
		}
		return false;
	}

}
