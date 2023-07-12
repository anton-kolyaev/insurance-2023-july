package pot.insurance.manager.type;

public enum UserAuthRole {

	VIEWER,
	MODERATOR,
	ADMIN;

	@Override
	public String toString() {
		return "ROLE_" + this.name().toUpperCase();
	}

}
