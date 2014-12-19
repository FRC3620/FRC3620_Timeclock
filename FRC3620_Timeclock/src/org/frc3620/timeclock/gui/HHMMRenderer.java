package org.frc3620.timeclock.gui;

import java.awt.Color;
import java.text.Format;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author WEGSCD
 */
public class HHMMRenderer extends DefaultTableCellRenderer {
    
    public HHMMRenderer() {
        super();
        setHorizontalAlignment(JLabel.RIGHT);
        setForeground(Color.red);
    }

    private Format formatter = new SimpleDateFormat("hh:mm");

    public void setValue(Object value) {
        //  Format the Object before setting its value in the renderer

        try {
            if (value != null) {
                value = formatter.format(value);
            }
        } catch (IllegalArgumentException e) {
        }

        super.setValue(value);
    }
    
}
