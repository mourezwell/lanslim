package com.oz.lanslim.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimModel;

public class ContactTable extends JTable implements ContactView {
	
	private String[] columnToolTips = null;
	private SlimModel slimModel = null;
	private ContactTableModel contactTableModel = null;
	
	public ContactTable(SlimModel pModel, ContactPopupMenu pPopupMenu) {
		super();
		slimModel = pModel;
		
        if (slimModel.getSettings().isQuickDnd()) {
        	DragAndDropWithoutSelection.hackSingleSelectionDND(this);
    	}

		columnToolTips = new String[] { "Name", "Status" };
		setDefaultRenderer(SlimAvailabilityEnum.class, new ContactTableAvailabilityCellRenderer());
		contactTableModel = new ContactTableModel(pModel.getContacts(), 
				pModel.getSettings().isGroupHidden(), pModel.getSettings().isOfflineHidden());
		TableSorter ts = new TableSorter(contactTableModel, getTableHeader());
		ts.setColumnComparator(String.class, new Comparator() {
		        public int compare(Object o1, Object o2) {
		            return ((String) o1).compareToIgnoreCase((String)o2);
		        }
		    });
		setModel(ts);
		getColumnModel().getColumn(1).setPreferredWidth(20);
		setTransferHandler(new ContactTransferHandler());
		setDragEnabled(true);
		
        //Add listener to the table so the popup menu can come up.
        MouseListener popupListener = 
        	new ContactViewMouseListener(pPopupMenu, this, pModel);
        addMouseListener(popupListener);
        ts.setSortingStatus(0, TableSorter.ASCENDING);
		
	}

	public boolean consolidateClickPoint(MouseEvent e) {
        Point p = e.getPoint();
        int row = rowAtPoint(p);
        int column = columnAtPoint(p);
        // The autoscroller can generate drag events outside the Table's range.
        if ((column == -1) || (row == -1)) {
            return false;
        }
        if (getSelectedRowCount() == 0) {
        	changeSelection(row, column, false, false);
        }
        return true;

	}

    //Implement table header tool tips. 
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
            public String getToolTipText(MouseEvent e) {
                java.awt.Point p = e.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                int realIndex = columnModel.getColumn(index).getModelIndex();
                return columnToolTips[realIndex];
            }
        };
    }

    public SlimContact[] getSelectedContacts() {
    	
		int[] lSelectedContactIndex = getSelectedRows();
		SlimContact[] lResult = new SlimContact[lSelectedContactIndex.length];
		
		for (int j = 0 ; j < lSelectedContactIndex.length; j++) {
			int i = lSelectedContactIndex[j];
			lResult[j] = slimModel.getContacts().getContactByName(
					(String)getModel().getValueAt(i, 0));
			
		}
		return lResult;
    }
    
    
    public void filter(String pPrefix, boolean pHideGroup, boolean pHideOffline) {
    	contactTableModel.filter(pPrefix, pHideGroup, pHideOffline);
    }
    
	public String[] getSelectedCategories() {
		return new String[0];
	}

    
}
