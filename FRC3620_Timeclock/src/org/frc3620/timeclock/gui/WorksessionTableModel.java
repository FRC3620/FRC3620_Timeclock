package org.frc3620.timeclock.gui;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.table.*;
import org.frc3620.timeclock.Person;
import org.frc3620.timeclock.Worksession;
import org.frc3620.timeclock.db.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

/**
 *
 * @author wegscd
 */
@Component
public class WorksessionTableModel extends AbstractTableModel {

    Logger logger = LoggerFactory.getLogger(getClass());
    List<Worksession> worksessions = new ArrayList<>();

    SimpleDateFormat hhmmssFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dayFormat = new SimpleDateFormat("MMM dd, yyyy");

    public TableColumnModel getTableColumnModel() {
        TableColumnModel rv = new DefaultTableColumnModel();
        rv.addColumn(new TableColumn(0, 200));
        rv.getColumn(0).setHeaderValue("Date");
        rv.addColumn(new TableColumn(1, 50));
        rv.getColumn(1).setHeaderValue("In");
        rv.addColumn(new TableColumn(2, 50));
        rv.getColumn(2).setHeaderValue("Out");
        DefaultTableCellRenderer rRenderer = new DefaultTableCellRenderer();
        rRenderer.setHorizontalAlignment(JLabel.CENTER);
        // rv.getColumn(0).setCellRenderer(rRenderer);
        rv.getColumn(1).setCellRenderer(rRenderer);
        rv.getColumn(2).setCellRenderer(rRenderer);
        return rv;
    }

    public void reload(Person person) {
        logger.info("{} using dao {}", this, dao);
        worksessions = dao.fetchWorksessionsForPerson(person.getPersonId());
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        // logger.info("personstatus = {}", personsStatus);
        return worksessions.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
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
        Object o = null;
        Worksession s = worksessions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                if (s.getStartDate() == null) {
                    o = "";
                } else {
                    synchronized (dayFormat) {
                        o = dayFormat.format(s.getStartDate());
                        logger.info ("format {} to {}", s.getStartDate(), o);
                    }
                }
                break;
            case 1:
                if (s.getStartDate() == null) {
                    o = "";
                } else {
                    synchronized (hhmmssFormat) {
                        o = hhmmssFormat.format(s.getStartDate());
                    }
                }
                break;
            case 2:
                if (s.getEndDate() == null) {
                    o = "";
                } else {
                    synchronized (hhmmssFormat) {
                        o = hhmmssFormat.format(s.getEndDate());
                    }
                }
                break;
            default:
                o = String.format("column %d for person %s", columnIndex, s);
        }
        logger.info("Data for {} {}: {}", rowIndex, columnIndex, o);
        return o;
    }
    
    public Worksession getLastWorksession() {
        if (worksessions.size() > 0) {
            return worksessions.get(0);
        } else {
            return null;
        }
    }

    DAO dao;

    @Autowired
    @Required
    public void setDao(DAO dao) {
        logger.info("{} got dao {}", this, dao);
        this.dao = dao;
    }
}
