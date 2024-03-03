package tn.esprit.utils;

public class ProfileManager {
    private static ProfileManager instance;
    private int currentUserUid;

    private ProfileManager() {
        // Private constructor to prevent instantiation
    }

    public static ProfileManager getInstance() {
        if (instance == null) {
            instance = new ProfileManager();
        }
        return instance;
    }

    public int getCurrentUserUid() {
        return currentUserUid;
    }

    public void setCurrentUserUid(int currentUserUid) {
        this.currentUserUid = currentUserUid;
    }

    public void clearSession() {
        currentUserUid = -1;
    }

    public boolean isLoggedIn() {
        return currentUserUid != -1;
    }
}
