
package com.jme3.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * More simple formatter than the default one used in Java logging.
 * Example output: <br>
 * INFO Display3D 12:00 PM: Display created.
 */
public class JmeFormatter extends Formatter {

    private final Date calendar = new Date();
    private final String lineSeparator;
    private final MessageFormat format;
    private final Object args[] = new Object[1];
    private final StringBuffer store = new StringBuffer();

    public JmeFormatter(){
        lineSeparator = System.getProperty("line.separator");
        format = new MessageFormat("{0,time}");
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        calendar.setTime(record.getMillis());
        args[0] = calendar;
        store.setLength(0);
        format.format(args, store, null);

        String clazz = null;
        try {
            clazz = Class.forName(record.getSourceClassName()).getSimpleName();
        } catch (ClassNotFoundException ex) {
        }

        sb.append(record.getLevel().getLocalizedName()).append(" ");
        sb.append(clazz).append(" ");
        sb.append(store.toString()).append(" ");
        sb.append(formatMessage(record)).append(lineSeparator);

        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
            }
        }

        return sb.toString();
    }
}
