package de.manuelclever.cinema.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Date;
import java.util.logging.*;

public class LogGenerator {
    private static Logger logger = Logger.getLogger("Cinema");
    public static final String ABSOLUTE_PATH = FileSystems.getDefault()
            .getPath("src", "main", "resources", "log.log").toAbsolutePath().toString();


    static {
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler(ABSOLUTE_PATH, true);

            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                @Override
                public String formatMessage(LogRecord record) {
                    return String.format(format,
                            new Date(record.getMillis()),
                            record.getLevel().getLocalizedName(),
                            record.getMessage()
                    );
                }
            });

            logger.addHandler(fileHandler);
        } catch(IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        logger.setUseParentHandlers(false);
    }

    private LogGenerator() {}

    public static void log(Level level, Class source, String string) {
        log(level, source.toString(), string);
    }

    public static void log(Level level, String source, String string) {
        logger.log(level, source + ": " + string);
    }

}
