package org.frc3620.timeclock.app;

import com.Ostermiller.util.CSVPrint;
import com.Ostermiller.util.ExcelCSVPrinter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.frc3620.timeclock.workhistory.HistoryPeriod;
import org.frc3620.timeclock.workhistory.PersonWithHistory;

/**
 *
 * @author wegscd
 */
public class CSVReport {
    
    static SimpleDateFormat sdf = new SimpleDateFormat("MMMM DD, YYYY");

    public static void generateReport(List<PersonWithHistory> personWithHistoryList, Writer w) throws IOException {
        CSVPrint csv = new ExcelCSVPrinter(w);
        generateReport (personWithHistoryList, csv);
    }

    public static void generateReport(List<PersonWithHistory> personWithHistoryList, CSVPrint csv) throws IOException {
        Collections.sort(personWithHistoryList, new Comparator<PersonWithHistory>() {
            @Override
            public int compare(PersonWithHistory o1, PersonWithHistory o2) {
                return o1.getPerson().getName().compareToIgnoreCase(o2.getPerson().getName());
            }
        });
        
        csv.writeln(new String[] { "name", "start date", "length", "hours", "days", "forgot to checkout"} );
        
        for (PersonWithHistory p : personWithHistoryList) {
            List<HistoryPeriod> hl = new ArrayList<>(p.getHistoryPeriods().values());
            Collections.sort(hl, new Comparator<HistoryPeriod>() {

                @Override
                public int compare(HistoryPeriod o1, HistoryPeriod o2) {
                    return - o1.getStart().compareTo(o2.getStart());
                }
                
            });
            for (HistoryPeriod h : hl) {
                csv.write(p.getPerson().getName());
                csv.write(sdf.format(h.getStart()));
                csv.write(h.getPeriod().toString());
                csv.write(String.format("%.1f", h.getHours()));
                csv.write(Integer.toString(h.getDays().size()));
                csv.write(Integer.toString(h.getOpenWorksessions().size()));
                csv.writeln();
            }
        }
        csv.close();
    }

}
