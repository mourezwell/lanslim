package com.oz.lanslim.gui;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimContactListener;

public class ContactTableModel extends AbstractTableModel implements SlimContactListener {
	
	private static final Class[] columnTypes = new Class[] { String.class, SlimAvailabilityEnum.class};
	private static final String[] columnNames = new String[] { "Name", "S" };
	private static final int nameColumnIndex = 0;
	private static final int statusColumnIndex = 1;

	private SlimContactList list = null;
	private Object[][] contactObjectArrayModel = null;
	private String prefixNameFilter = null;
	private boolean hideGroupFilter = false;
	private boolean hideOfflineFilter = false;
	
	public ContactTableModel(SlimContactList pList, boolean pHideGroup, boolean pHideOffline) {
		list = pList;
		contactObjectArrayModel = list.getTableModelData();
		list.addContactListener(this);
		list.addCategoryListener(null);
		prefixNameFilter = "";
		hideGroupFilter = pHideGroup;
		hideOfflineFilter = pHideOffline;
	}
	
	public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Class getColumnClass(int columnIndex) {
    	return columnTypes[columnIndex];
    }
    
    public String getColumnName(int columnIndex) {
    	return columnNames[columnIndex];
    }

    
    public synchronized void updateContacts() {
		contactObjectArrayModel = list.getTableModelData();
    	fireTableDataChanged();
    }

	public int getColumnCount() {
		return columnNames.length;
	}

	public Object setValueAt(int rowIndex, int columnIndex) {
		// Not used since table is not editable
		return null;
	}

	public synchronized int getRowCount() {
		int r = 0;
		for (int i = 0; i < contactObjectArrayModel.length; i++) {
			if (contactObjectArrayModel[i][nameColumnIndex].toString().startsWith(prefixNameFilter)) {
				if (hideGroupFilter 
						&& contactObjectArrayModel[i][statusColumnIndex].equals(SlimAvailabilityEnum.UNKNOWN)) {
					continue;
				}
				if (hideOfflineFilter && 
						contactObjectArrayModel[i][statusColumnIndex].equals(SlimAvailabilityEnum.OFFLINE)) {
					continue;
				}
				r = r + 1;
			}
		}
		return r;
	}

	public synchronized Object getValueAt(int rowIndex, int columnIndex) {
		int r = 0;
		for (int i = 0; i < contactObjectArrayModel.length; i++) {
			if (contactObjectArrayModel[i][nameColumnIndex].toString().toLowerCase().startsWith(prefixNameFilter.toLowerCase())) {
				if (hideGroupFilter 
						&& contactObjectArrayModel[i][statusColumnIndex].equals(SlimAvailabilityEnum.UNKNOWN)) {
					continue;
				}
				if (hideOfflineFilter && 
						contactObjectArrayModel[i][statusColumnIndex].equals(SlimAvailabilityEnum.OFFLINE)) {
					continue;
				}
				if (r == rowIndex) {
					return contactObjectArrayModel[i][columnIndex];
				}
				r = r + 1;
			}
		}
		return null; // should never happen
	}


	public void filter(String pPrefix, boolean pHideGroup, boolean pHideOffline) {
		prefixNameFilter = pPrefix;
		hideGroupFilter = pHideGroup;
		hideOfflineFilter = pHideOffline;
		fireTableDataChanged();
	}
	
	public void notifyContactError(String pMessage) {
		JOptionPane.showMessageDialog(null,
		    pMessage,
		    "Contact Update Error",
		    JOptionPane.WARNING_MESSAGE);
    }

}
