package pot.insurance.manager.entity.user;

public enum BasicUserRole {
	VIEWER,
	MODERATOR,
	ADMIN;

	@Override
	public String toString() {
		return "ROLE_" + this.name().toUpperCase();
	}
}
