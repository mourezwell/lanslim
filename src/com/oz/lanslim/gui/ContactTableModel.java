package com.oz.lanslim.gui;

import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.StringConstants;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimContactListener;

public class ContactTableModel extends AbstractTableModel implements SlimContactListener {
	
	private static final Class[] columnTypes = new Class[] { String.class, SlimContact.class};
	private static final String[] columnNames = new String[] { Externalizer.getString("LANSLIM.7"), Externalizer.getString("LANSLIM.104") }; //$NON-NLS-1$ //$NON-NLS-2$
	private static final int nameColumnIndex = 0;
	private static final int statusColumnIndex = 1;

	private SlimContactList list = null;
	private Object[][] contactObjectArrayModel = null;
	private String prefixNameFilter = null;
	private boolean hideGroupFilter = false;
	private boolean hideOfflineFilter = false;
	
	public ContactTableModel(SlimContactList pList, boolean pHideGroup, boolean pHideOffline) {
		list = pList;
		
		contactObjectArrayModel = getTableModelData();
		list.registerContactListener(this);
		list.registerCategoryListener(null);
		prefixNameFilter = StringConstants.EMPTY;
		hideGroupFilter = pHideGroup;
		hideOfflineFilter = pHideOffline;
	}
	
	public Object[][] getTableModelData() {
		List lAll = list.getAllContacts();
		Object[][] datas = new Object[lAll.size()][2];
		int i = 0;
		for (Iterator it = lAll.iterator(); it.hasNext();) {
			SlimContact c = (SlimContact)it.next();
			if (!list.isSettingsUser(c)) {
				datas[i] = new Object[] { c.getName(), c };
				i++;
			}
		}
		return datas;
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
		contactObjectArrayModel = getTableModelData();
    	fireTableDataChanged();
    }

	public int getColumnCount() {
		return columnNames.length;
	}

	public synchronized int getRowCount() {
		int r = 0;
		for (int i = 0; i < contactObjectArrayModel.length; i++) {
			if (contactObjectArrayModel[i][nameColumnIndex].toString().toLowerCase().startsWith(prefixNameFilter)) {
				if (hideGroupFilter 
						&& ((SlimContact)contactObjectArrayModel[i][statusColumnIndex]).getAvailability().equals(SlimAvailabilityEnum.UNKNOWN)) {
					continue;
				}
				if (hideOfflineFilter && 
						((SlimContact)contactObjectArrayModel[i][statusColumnIndex]).getAvailability().equals(SlimAvailabilityEnum.OFFLINE)) {
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
			if (contactObjectArrayModel[i][nameColumnIndex].toString().toLowerCase().startsWith(prefixNameFilter)) {
				if (hideGroupFilter 
						&& ((SlimContact)contactObjectArrayModel[i][statusColumnIndex]).getAvailability().equals(SlimAvailabilityEnum.UNKNOWN)) {
					continue;
				}
				if (hideOfflineFilter && 
						((SlimContact)contactObjectArrayModel[i][statusColumnIndex]).getAvailability().equals(SlimAvailabilityEnum.OFFLINE)) {
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
		prefixNameFilter = pPrefix.toLowerCase();
		hideGroupFilter = pHideGroup;
		hideOfflineFilter = pHideOffline;
		fireTableDataChanged();
	}
}
