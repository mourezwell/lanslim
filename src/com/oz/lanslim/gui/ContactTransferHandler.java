package com.oz.lanslim.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;

public class ContactTransferHandler extends TransferHandler {
	
	private SlimModel model;
	private SlimTalk talk;
	private JTabbedPane talkTabPane;
	
	// for export
	public ContactTransferHandler(SlimModel pModel) {
		model = pModel;
	}

	// for import in talkArea
	public ContactTransferHandler(SlimTalk pTalk) {
		talk = pTalk;
	}

	// for import in peopleAera
	public ContactTransferHandler(JTabbedPane pTalkTabPane) {
		talkTabPane = pTalkTabPane;
	}

	public int getSourceActions(JComponent c) {
        return COPY;
    }
	
	/**
	 * This method indicates if a component would accept an import of the given
	 * set of data flavors prior to actually attempting to import it. 
	 *
	 * @param comp  The component to receive the transfer.  This
	 *  argument is provided to enable sharing of TransferHandlers by
	 *  multiple components.
	 * @param flavors  The data formats available
	 * @return  true if the data can be inserted into the component, false otherwise.
	 */
        public boolean canImport(JComponent comp, DataFlavor[] flavors) {
        	if (talk != null || talkTabPane != null) {
	        	for (int i = 0; i< flavors.length; i++) {
	        		if (flavors[i].getRepresentationClass().equals(SlimContact.class)) {
	        			return true;
	        		}
	        	}
        	}
        	return false;
        }
        
        protected Transferable createTransferable(JComponent c) {
        	
        	if (c instanceof JTable) {
            	JTable contactTable = (JTable)c;
				int[] lSelectedContactIndex = contactTable.getSelectedRows();
				if (lSelectedContactIndex.length >= 1) {
					List cl = new ArrayList();
					for (int j = 0 ; j < lSelectedContactIndex.length; j++) {
						int i = lSelectedContactIndex[j];
						SlimContact sc = model.getContacts().getContactByName(
								(String)contactTable.getModel().getValueAt(i, 0));
						if (sc.isGroup()) {
							cl.addAll(((SlimGroupContact)sc).getOnlineMembers());
						}
						else {
							if (sc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
								cl.add(sc);
							}
						}
					}
					return new TransferableContact(cl);
				}
			}
        	else if (c instanceof JEditorPane) {
        		return new StringSelection(((JEditorPane)c).getSelectedText());
			};
        	return null; 
        }
        
    	/**
    	 * This method causes a transfer to a component from a clipboard or a 
    	 * DND drop operation.  The Transferable represents the data to be
    	 * imported into the component.  
    	 *
    	 * @param comp  The component to receive the transfer.  This
    	 *  argument is provided to enable sharing of TransferHandlers by
    	 *  multiple components.
    	 * @param t     The data to import
    	 * @return  true if the data was inserted into the component, false otherwise.
    	 */
        public boolean importData(JComponent comp, Transferable t) {
        	
        	SlimTalk st = null;
        	if (talkTabPane != null && talkTabPane.getComponents().length > 0) {
				st = ((TalkPane)talkTabPane.getSelectedComponent()).getTalk();
        	}
        	else if (talk != null) {
        		st = talk;
        	}
        	if (st != null) {
            	try {
            		List contacts = (List)t.getTransferData(new DataFlavor(SlimContact.class, "SlimContact")); 
            		st.addPeople(contacts);
            		return true;
            	}
            	catch (SlimException se) {
            		JOptionPane.showMessageDialog(comp.getTopLevelAncestor(),
						    "Unable to send Invitation message or notification",
						    "Network Error",
						    JOptionPane.ERROR_MESSAGE);                	
        		}
            	catch (IOException ioe) {
            		// should not happen
        		}
            	catch (UnsupportedFlavorException ufe) {
            		// should not happen
        		}
        	}
            return false;
    	}

            
     private class TransferableContact implements Transferable {

    	private List contacts;
    	 
    	public TransferableContact(List pContacts) {
    		contacts = pContacts;
    	}
    	 
		public Object getTransferData(DataFlavor flavor) 
			throws UnsupportedFlavorException, IOException {
			if (flavor.getRepresentationClass().equals(SlimContact.class)) {
				return contacts;
			}
			throw new UnsupportedFlavorException(flavor);
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { new DataFlavor(SlimContact.class, "SlimContact") };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (flavor.getRepresentationClass().equals(SlimContact.class)) {
				return true;
			}
			return false;
		}
		
		public List getContacts() {
			return contacts;
		}
     }
}
