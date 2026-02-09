package com.passwordmanager.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerFactory {

    // Private constructor to prevent creating objects of this utility class
    private LoggerFactory() {}

    // Returns a logger instance for the given class
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
