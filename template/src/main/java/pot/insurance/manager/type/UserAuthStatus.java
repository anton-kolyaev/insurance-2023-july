package pot.insurance.manager.type;

public enum UserAuthStatus {

    ACTIVE,
    DEACTIVATED,
    DELETED;

    public static UserAuthStatus getDefault() {
        return UserAuthStatus.ACTIVE;
    }
}
