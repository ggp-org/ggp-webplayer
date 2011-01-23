package util.logging;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.match.Match;

/**
 * GamerLogger is a customized logger designed for long-running game players.
 * Logs are written to directories on a per-game basis. Each logfile represents
 * a single logical component of the game playing program, identified whenever
 * the logger is called.
 * 
 * TODO: More details about specific use examples.
 * 
 * @author Sam Schreiber
 */
public class GamerLogger {
    public static void startFileLogging(Match m, String roleName) {
        log("Logger", "Started logging to files at: " + new Date());
        log("Logger", "Game rules: " + m.getGame().getRules());
        log("Logger", "Start clock: " + m.getStartClock());
        log("Logger", "Play clock: " + m.getPlayClock());        
    }
    
    public static final int LOG_LEVEL_DATA_DUMP = 0;
    public static final int LOG_LEVEL_ORDINARY = 3;
    public static final int LOG_LEVEL_IMPORTANT = 6;
    public static final int LOG_LEVEL_CRITICAL = 9;
    
    public static void logError(String toFile, String message) {
        logEntry(System.err, toFile, message, LOG_LEVEL_CRITICAL);
        logEntry(System.err, "Errors", "(in " + toFile + ") " + message, LOG_LEVEL_CRITICAL);
    }
        
    public static void log(String toFile, String message) {
        log(toFile, message, LOG_LEVEL_ORDINARY);
    }
    
    public static void log(String toFile, String message, int nLevel) {
        logEntry(System.out, toFile, message, nLevel);
    }    
    
    public static void logStackTrace(String toFile, Exception ex) {
        StringWriter s = new StringWriter();
        ex.printStackTrace(new PrintWriter(s));
        logError(toFile, s.toString());
    }
    
    public static void logStackTrace(String toFile, Error ex) {
        StringWriter s = new StringWriter();
        ex.printStackTrace(new PrintWriter(s));
        logError(toFile, s.toString());
    }    
    
    // Private Implementation    
    private static void logEntry(PrintStream ordinaryOutput, String toFile, String message, int logLevel) {
        String logMessage = logFormat(logLevel, ordinaryOutput == System.err, message);
        Logger.getLogger(toFile).log(Level.INFO, logMessage);       
    }   
    
    private static String logFormat(int logLevel, boolean isError, String message) {
        String logMessage = "LOG " + System.currentTimeMillis() + " [L" + logLevel + "]: " + (isError ? "<ERR> " : "") + message;            
        if(logMessage.charAt(logMessage.length() - 1) != '\n') {
            logMessage += '\n';     // All log lines must end with a newline.
        }
        return logMessage;
    }
}