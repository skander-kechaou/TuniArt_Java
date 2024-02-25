package tn.esprit.utils;

public class SessionManager {
    private static SessionManager instance;
    private int currentUserUid;

    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
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
