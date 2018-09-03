package com.manh.test.util.logger

import java.text.MessageFormat
import java.text.SimpleDateFormat
import java.util.logging.*

public class TestLogger {


    public static Logger getLogger(String _className)
    {
        FileHandler handler = null;
        ConsoleHandler console_handler = new ConsoleHandler();
        Date date = new Date();
        SingleLineFormatter formatter = new SingleLineFormatter();

        try {
            java.util.Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String timer = format.format(now).replace("-", "_");
            handler = new FileHandler(_className+"_"+timer+".log", true);
            handler.setFormatter(formatter);
            console_handler.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace()
        }

        Logger logger = Logger.getLogger(_className);
        logger.addHandler(handler);
        logger.addHandler(console_handler);
        logger.setUseParentHandlers(false);
        return logger;
    }

}

public class SingleLineFormatter extends Formatter {

    Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private MessageFormat formatter;
    private Object[] args = new Object[1];

    // Line separator string.  This is the value of the line.separator
    private String lineSeparator = "\n";

    /**
     * Format the given LogRecord.
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {

        StringBuilder sb = new StringBuilder();

        // Minimize memory allocations here.
        dat.setTime(record.getMillis());
        args[0] = dat;


        // Date and time
        StringBuffer text = new StringBuffer();
        if (formatter == null) {
            formatter = new MessageFormat(format);
        }
        formatter.format(args, text, null);
        sb.append(text);
        sb.append(" ");


        // Class name
        if (record.getSourceClassName() != null) {
            sb.append(record.getSourceClassName());
        } else {
            sb.append(record.getLoggerName());
        }

        // Method name
        if (record.getSourceMethodName() != null) {
            sb.append(" ");
            sb.append(record.getSourceMethodName());
        }
        sb.append(" - "); // lineSeparator



        String message = formatMessage(record);

        // Level
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");

        // Indent - the more serious, the more indented.
        int iOffset = (1000 - record.getLevel().intValue()) / 100;
        for( int i = 0; i < iOffset;  i++ ){
            sb.append(" ");
        }


        sb.append(message);
        sb.append(lineSeparator);
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

