package com.unity3d.player;

import android.util.Log;

import com.appmb.sdk.mbtracking.GameTracking;
import com.appmb.sdk.mbtracking.model.Level;
import com.appmb.sdk.mbtracking.model.OnlineTime;
import com.appmb.sdk.mbtracking.model.VIPLevel;

/**
 * Example class demonstrating how to use all GameTracking functions.
 * 
 * This class provides example methods that can be called from Unity C# scripts
 * to track in-game events. Each method demonstrates a different GameTracking function.
 * 
 * For testing purposes, you can also use the GameTrackingTestActivity UI:
 * - Launch from Unity: Call Main2Activity.startGameTrackingTestActivity() or send "GAME_TRACKING_TEST" message
 * - Launch from Java: new Intent(context, GameTrackingTestActivity.class).startActivity()
 * 
 * The GameTrackingTestActivity provides a user-friendly interface to test all tracking functions
 * with customizable parameters.
 */
public class GameTrackingExample {
    
    private static final String TAG = "GameTrackingExample";
    
    // Example game data - replace with actual game data
    private static final String EXAMPLE_GAME_UUID = "game_uuid_12345";
    private static final String EXAMPLE_CHARACTER_ID = "character_001";
    private static final String EXAMPLE_CHARACTER_NAME = "PlayerOne";
    private static final String EXAMPLE_SERVER_ID = "server_001";
    private static final String EXAMPLE_SERVER_NAME = "Server Alpha";
    
    /**
     * Example: Track when a user plays the game.
     * Call this when the game starts or when a user enters the game.
     * 
     * @param gameUUID The game UUID
     * @param characterId The character ID
     * @param characterName The character name
     * @param serverId The server ID
     * @param serverName The server name
     */
    public static void exampleLogPlayGame(
            String gameUUID,
            String characterId,
            String characterName,
            String serverId,
            String serverName) {
        try {
            Log.d(TAG, "Example: Tracking play game event");
            GameTracking.logPlayGame(
                    gameUUID,
                    characterId,
                    characterName,
                    serverId,
                    serverName
            );
            Log.d(TAG, "Successfully tracked play game event");
        } catch (Exception e) {
            Log.e(TAG, "Error tracking play game event: " + e.getMessage(), e);
        }
    }
    
    /**
     * Example: Track when a user completes tutorial S1.
     * Call this when the user completes the first tutorial.
     * 
     * @param gameUUID The game UUID
     * @param characterId The character ID
     * @param characterName The character name
     * @param serverId The server ID
     * @param serverName The server name
     */
    public static void exampleLogTutorialCompletedS1(
            String gameUUID,
            String characterId,
            String characterName,
            String serverId,
            String serverName) {
        try {
            Log.d(TAG, "Example: Tracking tutorial completed S1 event");
            GameTracking.logTutorialCompletedS1(
                    gameUUID,
                    characterId,
                    characterName,
                    serverId,
                    serverName
            );
            Log.d(TAG, "Successfully tracked tutorial completed S1 event");
        } catch (Exception e) {
            Log.e(TAG, "Error tracking tutorial completed S1 event: " + e.getMessage(), e);
        }
    }
    
    /**
     * Example: Track when a user levels up.
     * Call this when the user reaches a milestone level (10, 20, 30, etc.).
     * 
     * @param levelValue The level value (10, 20, 30, 40, 50, 60, 70, 80, 90, or 100)
     * @param gameUUID The game UUID
     * @param characterId The character ID
     * @param characterName The character name
     * @param serverId The server ID
     * @param serverName The server name
     */
    public static void exampleLogLevelUp(
            int levelValue,
            String gameUUID,
            String characterId,
            String characterName,
            String serverId,
            String serverName) {
        try {
            Log.d(TAG, "Example: Tracking level up event for level " + levelValue);
            
            // Convert level value to Level enum
            Level level = getLevelFromValue(levelValue);
            if (level == null) {
                Log.w(TAG, "Invalid level value: " + levelValue + ". Valid values are: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100");
                return;
            }
            
            GameTracking.logLevelUp(
                    level,
                    gameUUID,
                    characterId,
                    characterName,
                    serverId,
                    serverName
            );
            Log.d(TAG, "Successfully tracked level up event for level " + levelValue);
        } catch (Exception e) {
            Log.e(TAG, "Error tracking level up event: " + e.getMessage(), e);
        }
    }
    
    /**
     * Example: Track when a user reaches a VIP level.
     * Call this when the user reaches a VIP milestone (1-10).
     * 
     * @param vipLevelValue The VIP level value (1-10)
     * @param gameUUID The game UUID
     * @param characterId The character ID
     * @param characterName The character name
     * @param serverId The server ID
     * @param serverName The server name
     */
    public static void exampleLogVIPLevel(
            int vipLevelValue,
            String gameUUID,
            String characterId,
            String characterName,
            String serverId,
            String serverName) {
        try {
            Log.d(TAG, "Example: Tracking VIP level event for VIP level " + vipLevelValue);
            
            // Convert VIP level value to VIPLevel enum
            VIPLevel vipLevel = getVIPLevelFromValue(vipLevelValue);
            if (vipLevel == null) {
                Log.w(TAG, "Invalid VIP level value: " + vipLevelValue + ". Valid values are: 1-10");
                return;
            }
            
            GameTracking.logVIPLevel(
                    vipLevel,
                    gameUUID,
                    characterId,
                    characterName,
                    serverId,
                    serverName
            );
            Log.d(TAG, "Successfully tracked VIP level event for VIP level " + vipLevelValue);
        } catch (Exception e) {
            Log.e(TAG, "Error tracking VIP level event: " + e.getMessage(), e);
        }
    }
    
    /**
     * Example: Track user's online time.
     * Call this when the user has been online for a specific duration (5, 10, 30, or 60 minutes).
     * 
     * @param onlineTimeMinutes The online time in minutes (5, 10, 30, or 60)
     * @param gameUUID The game UUID
     * @param characterId The character ID
     * @param characterName The character name
     * @param currentLevelValue The current level value (10, 20, 30, 40, 50, 60, 70, 80, 90, or 100)
     * @param serverId The server ID
     * @param serverName The server name
     */
    public static void exampleLogOnlineTime(
            int onlineTimeMinutes,
            String gameUUID,
            String characterId,
            String characterName,
            int currentLevelValue,
            String serverId,
            String serverName) {
        try {
            Log.d(TAG, "Example: Tracking online time event for " + onlineTimeMinutes + " minutes");
            
            // Convert online time minutes to OnlineTime enum
            OnlineTime onlineTime = getOnlineTimeFromMinutes(onlineTimeMinutes);
            if (onlineTime == null) {
                Log.w(TAG, "Invalid online time: " + onlineTimeMinutes + ". Valid values are: 5, 10, 30, 60");
                return;
            }
            
            // Convert level value to Level enum
            Level level = getLevelFromValue(currentLevelValue);
            if (level == null) {
                Log.w(TAG, "Invalid level value: " + currentLevelValue + ". Valid values are: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100");
                return;
            }
            
            GameTracking.logOnlineTime(
                    onlineTime,
                    gameUUID,
                    characterId,
                    characterName,
                    level,
                    serverId,
                    serverName
            );
            Log.d(TAG, "Successfully tracked online time event for " + onlineTimeMinutes + " minutes");
        } catch (Exception e) {
            Log.e(TAG, "Error tracking online time event: " + e.getMessage(), e);
        }
    }
    
    // ========== Helper Methods ==========
    
    /**
     * Converts a level value to Level enum.
     * 
     * @param value The level value
     * @return Level enum or null if invalid
     */
    private static Level getLevelFromValue(int value) {
        switch (value) {
            case 10: return Level.Level10;
            case 20: return Level.Level20;
            case 30: return Level.Level30;
            case 40: return Level.Level40;
            case 50: return Level.Level50;
            case 60: return Level.Level60;
            case 70: return Level.Level70;
            case 80: return Level.Level80;
            case 90: return Level.Level90;
            case 100: return Level.Level100;
            default: return null;
        }
    }
    
    /**
     * Converts a VIP level value to VIPLevel enum.
     * 
     * @param value The VIP level value
     * @return VIPLevel enum or null if invalid
     */
    private static VIPLevel getVIPLevelFromValue(int value) {
        switch (value) {
            case 1: return VIPLevel.Level1;
            case 2: return VIPLevel.Level2;
            case 3: return VIPLevel.Level3;
            case 4: return VIPLevel.Level4;
            case 5: return VIPLevel.Level5;
            case 6: return VIPLevel.Level6;
            case 7: return VIPLevel.Level7;
            case 8: return VIPLevel.Level8;
            case 9: return VIPLevel.Level9;
            case 10: return VIPLevel.Level10;
            default: return null;
        }
    }
    
    /**
     * Converts online time minutes to OnlineTime enum.
     * 
     * @param minutes The online time in minutes
     * @return OnlineTime enum or null if invalid
     */
    private static OnlineTime getOnlineTimeFromMinutes(int minutes) {
        switch (minutes) {
            case 5: return OnlineTime.OL5minutes;
            case 10: return OnlineTime.OL10minutes;
            case 30: return OnlineTime.OL30minutes;
            case 60: return OnlineTime.OL60minutes;
            default: return null;
        }
    }
    
    // ========== Example Usage Methods (for testing) ==========
    
    /**
     * Demonstrates all GameTracking functions with example data.
     * This method can be called for testing purposes.
     */
    public static void demonstrateAllTrackingFunctions() {
        Log.d(TAG, "========== Starting GameTracking Examples ==========");
        
        // Example 1: Play Game
        exampleLogPlayGame(
                EXAMPLE_GAME_UUID,
                EXAMPLE_CHARACTER_ID,
                EXAMPLE_CHARACTER_NAME,
                EXAMPLE_SERVER_ID,
                EXAMPLE_SERVER_NAME
        );
        
        // Example 2: Tutorial Completed S1
        exampleLogTutorialCompletedS1(
                EXAMPLE_GAME_UUID,
                EXAMPLE_CHARACTER_ID,
                EXAMPLE_CHARACTER_NAME,
                EXAMPLE_SERVER_ID,
                EXAMPLE_SERVER_NAME
        );
        
        // Example 3: Level Up (Level 50)
        exampleLogLevelUp(
                50,
                EXAMPLE_GAME_UUID,
                EXAMPLE_CHARACTER_ID,
                EXAMPLE_CHARACTER_NAME,
                EXAMPLE_SERVER_ID,
                EXAMPLE_SERVER_NAME
        );
        
        // Example 4: VIP Level (VIP Level 5)
        exampleLogVIPLevel(
                5,
                EXAMPLE_GAME_UUID,
                EXAMPLE_CHARACTER_ID,
                EXAMPLE_CHARACTER_NAME,
                EXAMPLE_SERVER_ID,
                EXAMPLE_SERVER_NAME
        );
        
        // Example 5: Online Time (30 minutes)
        exampleLogOnlineTime(
                30,
                EXAMPLE_GAME_UUID,
                EXAMPLE_CHARACTER_ID,
                EXAMPLE_CHARACTER_NAME,
                50, // Current level
                EXAMPLE_SERVER_ID,
                EXAMPLE_SERVER_NAME
        );
        
        Log.d(TAG, "========== Completed GameTracking Examples ==========");
    }
}

