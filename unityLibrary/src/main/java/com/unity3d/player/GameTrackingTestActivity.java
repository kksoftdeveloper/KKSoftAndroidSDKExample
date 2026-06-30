package com.unity3d.player;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appmb.sdk.mbtracking.GameTracking;
import com.appmb.sdk.mbtracking.model.Level;
import com.appmb.sdk.mbtracking.model.OnlineTime;
import com.appmb.sdk.mbtracking.model.VIPLevel;
import com.unity3d.player.CrashExample;

/**
 * Activity for testing GameTracking functions.
 * Provides a UI to test all in-game tracking events with customizable parameters.
 */
public class GameTrackingTestActivity extends Activity {
    
    private static final String TAG = "GameTrackingTest";
    
    private EditText etGameUUID;
    private EditText etCharacterId;
    private EditText etCharacterName;
    private EditText etServerId;
    private EditText etServerName;
    private EditText etLevel;
    private EditText etVIPLevel;
    private EditText etOnlineTime;
    
    private Button btnPlayGame;
    private Button btnTutorialCompleted;
    private Button btnLevelUp;
    private Button btnVIPLevel;
    private Button btnOnlineTime;
    private Button btnTestAll;
    private Button btnLogNonFatalException;
    private Button btnTriggerTestCrash;
    private Button btnTriggerNullPointerCrash;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_tracking_test);
        
        initializeViews();
        setupClickListeners();
    }
    
    private void initializeViews() {
        etGameUUID = findViewById(R.id.etGameUUID);
        etCharacterId = findViewById(R.id.etCharacterId);
        etCharacterName = findViewById(R.id.etCharacterName);
        etServerId = findViewById(R.id.etServerId);
        etServerName = findViewById(R.id.etServerName);
        etLevel = findViewById(R.id.etLevel);
        etVIPLevel = findViewById(R.id.etVIPLevel);
        etOnlineTime = findViewById(R.id.etOnlineTime);
        
        btnPlayGame = findViewById(R.id.btnPlayGame);
        btnTutorialCompleted = findViewById(R.id.btnTutorialCompleted);
        btnLevelUp = findViewById(R.id.btnLevelUp);
        btnVIPLevel = findViewById(R.id.btnVIPLevel);
        btnOnlineTime = findViewById(R.id.btnOnlineTime);
        btnTestAll = findViewById(R.id.btnTestAll);
        btnLogNonFatalException = findViewById(R.id.btnLogNonFatalException);
        btnTriggerTestCrash = findViewById(R.id.btnTriggerTestCrash);
        btnTriggerNullPointerCrash = findViewById(R.id.btnTriggerNullPointerCrash);
    }
    
    private void setupClickListeners() {
        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackPlayGame();
            }
        });
        
        btnTutorialCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackTutorialCompletedS1();
            }
        });
        
        btnLevelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackLevelUp();
            }
        });
        
        btnVIPLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackVIPLevel();
            }
        });
        
        btnOnlineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackOnlineTime();
            }
        });
        
        btnTestAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAllFunctions();
            }
        });
        
        btnLogNonFatalException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logNonFatalException();
            }
        });
        
        btnTriggerTestCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerTestCrash();
            }
        });
        
        btnTriggerNullPointerCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerNullPointerCrash();
            }
        });
    }
    
    private String getGameUUID() {
        String value = etGameUUID.getText().toString().trim();
        return TextUtils.isEmpty(value) ? "game_uuid_12345" : value;
    }
    
    private String getCharacterId() {
        String value = etCharacterId.getText().toString().trim();
        return TextUtils.isEmpty(value) ? "character_001" : value;
    }
    
    private String getCharacterName() {
        String value = etCharacterName.getText().toString().trim();
        return TextUtils.isEmpty(value) ? "PlayerOne" : value;
    }
    
    private String getServerId() {
        String value = etServerId.getText().toString().trim();
        return TextUtils.isEmpty(value) ? "server_001" : value;
    }
    
    private String getServerName() {
        String value = etServerName.getText().toString().trim();
        return TextUtils.isEmpty(value) ? "Server Alpha" : value;
    }
    
    private int getLevelValue() {
        try {
            String value = etLevel.getText().toString().trim();
            return TextUtils.isEmpty(value) ? 50 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 50;
        }
    }
    
    private int getVIPLevelValue() {
        try {
            String value = etVIPLevel.getText().toString().trim();
            return TextUtils.isEmpty(value) ? 5 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 5;
        }
    }
    
    private int getOnlineTimeMinutes() {
        try {
            String value = etOnlineTime.getText().toString().trim();
            return TextUtils.isEmpty(value) ? 30 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 30;
        }
    }
    
    private Level getLevelFromValue(int value) {
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
    
    private VIPLevel getVIPLevelFromValue(int value) {
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
    
    private OnlineTime getOnlineTimeFromMinutes(int minutes) {
        switch (minutes) {
            case 5: return OnlineTime.OL5minutes;
            case 10: return OnlineTime.OL10minutes;
            case 30: return OnlineTime.OL30minutes;
            case 60: return OnlineTime.OL60minutes;
            default: return null;
        }
    }
    
    private void trackPlayGame() {
        try {
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            GameTracking.logPlayGame(gameUUID, characterId, characterName, serverId, serverName);
            showToast("Play Game event tracked successfully!");
            Log.d(TAG, "Play Game tracked: gameUUID=" + gameUUID + ", characterId=" + characterId);
        } catch (Exception e) {
            showToast("Error tracking Play Game: " + e.getMessage());
            Log.e(TAG, "Error tracking Play Game", e);
        }
    }
    
    private void trackTutorialCompletedS1() {
        try {
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            GameTracking.logTutorialCompletedS1(gameUUID, characterId, characterName, serverId, serverName);
            showToast("Tutorial Completed S1 event tracked successfully!");
            Log.d(TAG, "Tutorial Completed S1 tracked: gameUUID=" + gameUUID + ", characterId=" + characterId);
        } catch (Exception e) {
            showToast("Error tracking Tutorial Completed S1: " + e.getMessage());
            Log.e(TAG, "Error tracking Tutorial Completed S1", e);
        }
    }
    
    private void trackLevelUp() {
        try {
            int levelValue = getLevelValue();
            Level level = getLevelFromValue(levelValue);
            
            if (level == null) {
                showToast("Invalid level value. Valid values: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100");
                return;
            }
            
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            GameTracking.logLevelUp(level, gameUUID, characterId, characterName, serverId, serverName);
            showToast("Level Up event tracked successfully! Level: " + levelValue);
            Log.d(TAG, "Level Up tracked: level=" + levelValue + ", gameUUID=" + gameUUID);
        } catch (Exception e) {
            showToast("Error tracking Level Up: " + e.getMessage());
            Log.e(TAG, "Error tracking Level Up", e);
        }
    }
    
    private void trackVIPLevel() {
        try {
            int vipLevelValue = getVIPLevelValue();
            VIPLevel vipLevel = getVIPLevelFromValue(vipLevelValue);
            
            if (vipLevel == null) {
                showToast("Invalid VIP level value. Valid values: 1-10");
                return;
            }
            
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            GameTracking.logVIPLevel(vipLevel, gameUUID, characterId, characterName, serverId, serverName);
            showToast("VIP Level event tracked successfully! VIP Level: " + vipLevelValue);
            Log.d(TAG, "VIP Level tracked: vipLevel=" + vipLevelValue + ", gameUUID=" + gameUUID);
        } catch (Exception e) {
            showToast("Error tracking VIP Level: " + e.getMessage());
            Log.e(TAG, "Error tracking VIP Level", e);
        }
    }
    
    private void trackOnlineTime() {
        try {
            int onlineTimeMinutes = getOnlineTimeMinutes();
            OnlineTime onlineTime = getOnlineTimeFromMinutes(onlineTimeMinutes);
            
            if (onlineTime == null) {
                showToast("Invalid online time. Valid values: 5, 10, 30, 60 minutes");
                return;
            }
            
            int levelValue = getLevelValue();
            Level level = getLevelFromValue(levelValue);
            
            if (level == null) {
                showToast("Invalid level value. Valid values: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100");
                return;
            }
            
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            GameTracking.logOnlineTime(onlineTime, gameUUID, characterId, characterName, level, serverId, serverName);
            showToast("Online Time event tracked successfully! Time: " + onlineTimeMinutes + " minutes");
            Log.d(TAG, "Online Time tracked: minutes=" + onlineTimeMinutes + ", level=" + levelValue + ", gameUUID=" + gameUUID);
        } catch (Exception e) {
            showToast("Error tracking Online Time: " + e.getMessage());
            Log.e(TAG, "Error tracking Online Time", e);
        }
    }
    
    private void testAllFunctions() {
        showToast("Testing all tracking functions...");
        Log.d(TAG, "========== Starting All Tracking Tests ==========");
        
        // Small delay between each call to see individual results
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                trackPlayGame();
            }
        }, 100);
        
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                trackTutorialCompletedS1();
            }
        }, 300);
        
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                trackLevelUp();
            }
        }, 500);
        
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                trackVIPLevel();
            }
        }, 700);
        
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                trackOnlineTime();
                showToast("All tracking functions tested!");
                Log.d(TAG, "========== Completed All Tracking Tests ==========");
            }
        }, 900);
    }
    
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    private void logNonFatalException() {
        try {
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            // Log game context before exception
            CrashExample.demonstrateCrashLoggingWithGameContext(
                gameUUID, characterId, characterName, getLevelValue(), serverId, serverName
            );
            
            // Log non-fatal exception
            CrashExample.logNonFatalException(
                "Test non-fatal exception from GameTrackingTestActivity"
            );
            
            showToast("Non-fatal exception logged successfully!");
            Log.d(TAG, "Non-fatal exception logged");
        } catch (Exception e) {
            showToast("Error logging non-fatal exception: " + e.getMessage());
            Log.e(TAG, "Error logging non-fatal exception", e);
        }
    }
    
    private void triggerTestCrash() {
        try {
            // Log game context before crash
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            CrashExample.demonstrateCrashLoggingWithGameContext(
                gameUUID, characterId, characterName, getLevelValue(), serverId, serverName
            );
            
            // Show warning toast
            Toast.makeText(this, "App will crash in 2 seconds!", Toast.LENGTH_LONG).show();
            
            // Delay crash to allow toast to show
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CrashExample.triggerTestCrash("Test crash triggered from GameTrackingTestActivity");
                }
            }, 2000);
        } catch (Exception e) {
            Log.e(TAG, "Error triggering test crash", e);
        }
    }
    
    private void triggerNullPointerCrash() {
        try {
            // Log game context before crash
            String gameUUID = getGameUUID();
            String characterId = getCharacterId();
            String characterName = getCharacterName();
            String serverId = getServerId();
            String serverName = getServerName();
            
            CrashExample.demonstrateCrashLoggingWithGameContext(
                gameUUID, characterId, characterName, getLevelValue(), serverId, serverName
            );
            
            // Show warning toast
            Toast.makeText(this, "App will crash with NullPointerException in 2 seconds!", Toast.LENGTH_LONG).show();
            
            // Delay crash to allow toast to show
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CrashExample.triggerNullPointerCrash();
                }
            }, 2000);
        } catch (Exception e) {
            Log.e(TAG, "Error triggering null pointer crash", e);
        }
    }
}

