package com.unity3d.player;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * Example class demonstrating how to use Firebase Crashlytics for crash reporting.
 * 
 * This class provides example methods that can be called from Unity C# scripts
 * to test crash reporting functionality. These methods demonstrate various ways
 * to log crashes and exceptions for testing and debugging purposes.
 * 
 * WARNING: Some methods in this class will cause the app to crash!
 * Use only for testing purposes in development builds.
 */
public class CrashExample {
    
    private static final String TAG = "CrashExample";
    
    /**
     * Triggers a test crash by throwing a RuntimeException.
     * This will cause the app to crash immediately.
     * 
     * WARNING: This will crash the app! Use only for testing.
     * 
     * @param message Optional crash message
     */
    public static void triggerTestCrash(String message) {
        String crashMessage = message != null && !message.isEmpty() 
            ? message 
            : "Test crash triggered from CrashExample";
        
        Log.e(TAG, "Triggering test crash: " + crashMessage);
        
        // Log custom key before crash
        FirebaseCrashlytics.getInstance().setCustomKey("crash_type", "test_crash");
        FirebaseCrashlytics.getInstance().setCustomKey("crash_source", "unity_library");
        FirebaseCrashlytics.getInstance().log("About to trigger test crash: " + crashMessage);
        
        // Throw exception to trigger crash
        throw new RuntimeException(crashMessage);
    }
    
    /**
     * Triggers a test crash with a NullPointerException.
     * This simulates a common crash scenario.
     * 
     * WARNING: This will crash the app! Use only for testing.
     */
    public static void triggerNullPointerCrash() {
        Log.e(TAG, "Triggering NullPointerException crash");
        
        FirebaseCrashlytics.getInstance().setCustomKey("crash_type", "null_pointer");
        FirebaseCrashlytics.getInstance().setCustomKey("crash_source", "unity_library");
        FirebaseCrashlytics.getInstance().log("About to trigger NullPointerException");
        
        // Intentionally cause NullPointerException
        String nullString = null;
        int length = nullString.length(); // This will throw NullPointerException
    }
    
    /**
     * Triggers a test crash with an ArrayIndexOutOfBoundsException.
     * This simulates an array access error.
     * 
     * WARNING: This will crash the app! Use only for testing.
     */
    public static void triggerArrayIndexCrash() {
        Log.e(TAG, "Triggering ArrayIndexOutOfBoundsException crash");
        
        FirebaseCrashlytics.getInstance().setCustomKey("crash_type", "array_index_out_of_bounds");
        FirebaseCrashlytics.getInstance().setCustomKey("crash_source", "unity_library");
        FirebaseCrashlytics.getInstance().log("About to trigger ArrayIndexOutOfBoundsException");
        
        // Intentionally cause ArrayIndexOutOfBoundsException
        int[] array = new int[5];
        int value = array[10]; // This will throw ArrayIndexOutOfBoundsException
    }
    
    /**
     * Logs a non-fatal exception without crashing the app.
     * This is useful for logging errors that don't cause the app to crash.
     * 
     * @param exceptionMessage The exception message
     */
    public static void logNonFatalException(String exceptionMessage) {
        try {
            Log.w(TAG, "Logging non-fatal exception: " + exceptionMessage);
            
            // Create an exception
            Exception exception = new Exception(exceptionMessage);
            
            // Log custom keys
            FirebaseCrashlytics.getInstance().setCustomKey("exception_type", "non_fatal");
            FirebaseCrashlytics.getInstance().setCustomKey("exception_source", "unity_library");
            FirebaseCrashlytics.getInstance().log("Non-fatal exception occurred: " + exceptionMessage);
            
            // Record exception without crashing
            FirebaseCrashlytics.getInstance().recordException(exception);
            
            Log.d(TAG, "Non-fatal exception logged successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error logging non-fatal exception", e);
        }
    }
    
    /**
     * Logs a custom message to Crashlytics.
     * This message will appear in crash reports.
     * 
     * @param message The message to log
     */
    public static void logMessage(String message) {
        try {
            Log.d(TAG, "Logging message to Crashlytics: " + message);
            FirebaseCrashlytics.getInstance().log(message);
            Log.d(TAG, "Message logged successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error logging message", e);
        }
    }
    
    /**
     * Sets a custom key-value pair in Crashlytics.
     * These keys will appear in crash reports.
     * 
     * @param key The key name
     * @param value The value (as String)
     */
    public static void setCustomKeyString(String key, String value) {
        try {
            Log.d(TAG, "Setting custom key: " + key + " = " + value);
            FirebaseCrashlytics.getInstance().setCustomKey(key, value);
            Log.d(TAG, "Custom key set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting custom key", e);
        }
    }
    
    /**
     * Sets a custom key-value pair in Crashlytics (integer value).
     * 
     * @param key The key name
     * @param value The value (as int)
     */
    public static void setCustomKeyInt(String key, int value) {
        try {
            Log.d(TAG, "Setting custom key: " + key + " = " + value);
            FirebaseCrashlytics.getInstance().setCustomKey(key, value);
            Log.d(TAG, "Custom key set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting custom key", e);
        }
    }
    
    /**
     * Sets a custom key-value pair in Crashlytics (boolean value).
     * 
     * @param key The key name
     * @param value The value (as boolean)
     */
    public static void setCustomKeyBool(String key, boolean value) {
        try {
            Log.d(TAG, "Setting custom key: " + key + " = " + value);
            FirebaseCrashlytics.getInstance().setCustomKey(key, value);
            Log.d(TAG, "Custom key set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting custom key", e);
        }
    }
    
    /**
     * Sets a custom key-value pair in Crashlytics (double value).
     * 
     * @param key The key name
     * @param value The value (as double)
     */
    public static void setCustomKeyDouble(String key, double value) {
        try {
            Log.d(TAG, "Setting custom key: " + key + " = " + value);
            FirebaseCrashlytics.getInstance().setCustomKey(key, value);
            Log.d(TAG, "Custom key set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting custom key", e);
        }
    }
    
    /**
     * Sets the user identifier for crash reports.
     * This helps identify which user experienced the crash.
     * 
     * @param userId The user ID
     */
    public static void setUserId(String userId) {
        try {
            Log.d(TAG, "Setting user ID: " + userId);
            FirebaseCrashlytics.getInstance().setUserId(userId);
            Log.d(TAG, "User ID set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting user ID", e);
        }
    }
    
    /**
     * Example: Logs game state information before a potential crash.
     * This demonstrates how to add context before an operation that might crash.
     * 
     * @param gameUUID The game UUID
     * @param characterId The character ID
     * @param level The current level
     * @param serverId The server ID
     */
    public static void logGameStateBeforeOperation(
            String gameUUID,
            String characterId,
            int level,
            String serverId) {
        try {
            Log.d(TAG, "Logging game state before operation");
            
            // Set custom keys for context
            FirebaseCrashlytics.getInstance().setCustomKey("game_uuid", gameUUID != null ? gameUUID : "unknown");
            FirebaseCrashlytics.getInstance().setCustomKey("character_id", characterId != null ? characterId : "unknown");
            FirebaseCrashlytics.getInstance().setCustomKey("current_level", level);
            FirebaseCrashlytics.getInstance().setCustomKey("server_id", serverId != null ? serverId : "unknown");
            
            // Log message
            FirebaseCrashlytics.getInstance().log("Game state: UUID=" + gameUUID + 
                ", Character=" + characterId + 
                ", Level=" + level + 
                ", Server=" + serverId);
            
            Log.d(TAG, "Game state logged successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error logging game state", e);
        }
    }
    
    /**
     * Example: Wraps an operation with crash logging.
     * If the operation throws an exception, it will be logged to Crashlytics.
     * 
     * @param operationName The name of the operation being performed
     */
    public static void performOperationWithCrashLogging(String operationName) {
        try {
            Log.d(TAG, "Performing operation: " + operationName);
            
            // Log operation start
            FirebaseCrashlytics.getInstance().setCustomKey("operation_name", operationName);
            FirebaseCrashlytics.getInstance().log("Starting operation: " + operationName);
            
            // Simulate an operation that might fail
            // In real usage, this would be your actual game logic
            if (operationName != null && operationName.equals("risky_operation")) {
                throw new RuntimeException("Operation failed: " + operationName);
            }
            
            Log.d(TAG, "Operation completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Operation failed: " + operationName, e);
            
            // Log the exception to Crashlytics without crashing
            FirebaseCrashlytics.getInstance().setCustomKey("operation_failed", true);
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    
    /**
     * Demonstrates all crash logging functions with example data.
     * This method can be called for testing purposes.
     * 
     * WARNING: This method includes examples that will crash the app!
     */
    public static void demonstrateAllCrashFunctions() {
        Log.d(TAG, "========== Starting Crash Example Demonstrations ==========");
        
        // Example 1: Set user ID
        setUserId("test_user_12345");
        
        // Example 2: Set custom keys
        setCustomKeyString("game_version", "1.0.0");
        setCustomKeyInt("player_level", 50);
        setCustomKeyBool("is_premium", true);
        setCustomKeyDouble("player_score", 12345.67);
        
        // Example 3: Log a message
        logMessage("Testing Crashlytics integration");
        
        // Example 4: Log game state
        logGameStateBeforeOperation(
            "game_uuid_12345",
            "character_001",
            50,
            "server_001"
        );
        
        // Example 5: Perform operation with crash logging
        performOperationWithCrashLogging("safe_operation");
        
        // Example 6: Log non-fatal exception
        logNonFatalException("This is a test non-fatal exception");
        
        Log.d(TAG, "========== Completed Crash Example Demonstrations ==========");
        Log.d(TAG, "Note: To test actual crashes, call triggerTestCrash(), triggerNullPointerCrash(), or triggerArrayIndexCrash()");
    }
    
    /**
     * Demonstrates crash logging with game context.
     * This shows how to add game-specific information before logging crashes.
     * 
     * @param gameUUID The game UUID
     * @param characterId The character ID
     * @param characterName The character name
     * @param level The current level
     * @param serverId The server ID
     * @param serverName The server name
     */
    public static void demonstrateCrashLoggingWithGameContext(
            String gameUUID,
            String characterId,
            String characterName,
            int level,
            String serverId,
            String serverName) {
        try {
            Log.d(TAG, "Demonstrating crash logging with game context");
            
            // Set user identifier
            if (characterId != null) {
                setUserId(characterId);
            }
            
            // Set custom keys with game information
            setCustomKeyString("game_uuid", gameUUID != null ? gameUUID : "unknown");
            setCustomKeyString("character_id", characterId != null ? characterId : "unknown");
            setCustomKeyString("character_name", characterName != null ? characterName : "unknown");
            setCustomKeyInt("current_level", level);
            setCustomKeyString("server_id", serverId != null ? serverId : "unknown");
            setCustomKeyString("server_name", serverName != null ? serverName : "unknown");
            
            // Log game context
            logMessage("Game context: UUID=" + gameUUID + 
                ", Character=" + characterName + 
                " (ID: " + characterId + ")" +
                ", Level=" + level + 
                ", Server=" + serverName + 
                " (ID: " + serverId + ")");
            
            Log.d(TAG, "Game context logged successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error demonstrating crash logging with game context", e);
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}

