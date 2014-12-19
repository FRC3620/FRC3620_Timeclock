package org.frc3620.timeclock.gui;

import org.frc3620.timeclock.CurrentStatus;
import java.util.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/**
 *
 * @author wegscd
 */
public class TimeClockStatusModel implements TableModel {
    List<CurrentStatus> people = new ArrayList<CurrentStatus>();
    
    public void addTimeClockStatus(CurrentStatus t) {
        people.add(t);
        Collections.sort(people);
    }

    @Override
    public int getRowCount() {
        return people.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Status";
            case 2:
                return "When";
            default:
                return "" + columnIndex;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Date.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CurrentStatus s = people.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return s.getWho();
            case 1:
                return s.getWhere();
            case 2:
                return s.getWhen();
            default:
                return String.format ("column %d for person %s", columnIndex, s);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}