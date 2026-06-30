using UnityEngine;

public class KKSoftAndroid : MonoBehaviour
{
    static AndroidJavaObject CurrentActivity
    {
        get
        {
            using var unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            return unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        }
    }

    static AndroidJavaClass Bridge => new AndroidJavaClass("com.kksoft.unityexample.KKSoftUnityBridge");

    public void InitializeProduction()
    {
        Bridge.CallStatic("initializeProduction", CurrentActivity, "1");
    }

    public void InitializeFull()
    {
        Bridge.CallStatic(
            "initialize",
            CurrentActivity,
            "1",
            "server-client-id",
            "google-client-id",
            "facebook-app-id",
            "facebook-client-token",
            "firebase-app-id",
            "adjust-id",
            "adjust-token",
            "tiktok-app-id",
            "tiktok-access-token",
            "facebook-display-name",
            null
        );
    }

    public void StartAuth()
    {
        Bridge.CallStatic("startAuthentication", CurrentActivity);
    }

    public void CheckForceUpdate()
    {
        Bridge.CallStatic("checkForceUpdate", CurrentActivity);
    }

    public void Logout()
    {
        Bridge.CallStatic("startLogout", CurrentActivity);
    }

    public void LinkAccount()
    {
        Bridge.CallStatic("startLinkAccount", CurrentActivity);
    }

    public void ChangeServer()
    {
        Bridge.CallStatic("startChangeServer", CurrentActivity);
    }

    public void DeactivateAccount()
    {
        Bridge.CallStatic("startDeactivateAccount", CurrentActivity);
    }

    public void StartPayment()
    {
        Bridge.CallStatic("startPayment", CurrentActivity);
    }

    public void TrackPlayGame(string gameUuid, string characterId, string characterName, string serverId, string serverName)
    {
        Bridge.CallStatic("logPlayGame", gameUuid, characterId, characterName, serverId, serverName);
    }

    public void TrackTutorialCompleted(string gameUuid, string characterId, string characterName, string serverId, string serverName)
    {
        Bridge.CallStatic("logTutorialCompletedS1", gameUuid, characterId, characterName, serverId, serverName);
    }

    public void TrackLevelUp(int level, string gameUuid, string characterId, string characterName, string serverId, string serverName)
    {
        Bridge.CallStatic("logLevelUp", level, gameUuid, characterId, characterName, serverId, serverName);
    }

    public void TrackVipLevel(int vipLevel, string gameUuid, string characterId, string characterName, string serverId, string serverName)
    {
        Bridge.CallStatic("logVipLevel", vipLevel, gameUuid, characterId, characterName, serverId, serverName);
    }

    public void TrackOnlineTime(int minutes, string gameUuid, string characterId, string characterName, int level, string serverId, string serverName)
    {
        Bridge.CallStatic("logOnlineTime", minutes, gameUuid, characterId, characterName, level, serverId, serverName);
    }

    public void OnAuthResult(string payload)
    {
        Debug.Log($"KKSoft auth result: {payload}");
    }

    public void OnPaymentResult(string payload)
    {
        Debug.Log($"KKSoft payment result: {payload}");
    }
}
