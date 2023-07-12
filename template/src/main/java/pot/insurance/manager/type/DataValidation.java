package pot.insurance.manager.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class DataValidation {

    public enum Category {
        VALID,
        INVALID,
        ABSENT,
        PRESENT;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    @RequiredArgsConstructor
    public enum Status {
        SUCCESSFUL(Category.VALID, "success"),
        MISSING_DATA(Category.ABSENT, "data is missing"),
        MALFORMED_DATA(Category.INVALID, "data is malformed"),

        USER_NOT_FOUND(Category.ABSENT, "user not found"),
        USER_ID_EXISTS(Category.PRESENT, "user id is already exists"),
        USER_SSN_EXISTS(Category.PRESENT, "user ssn is already used"),

        USER_AUTH_NOT_FOUND(Category.PRESENT, "user auth not found"),
        USER_AUTH_ID_EXISTS(Category.PRESENT, "user auth id is already used"),
        USER_AUTH_USERNAME_EXISTS(Category.PRESENT, "user auth username is already used"),

        COMPANY_NOT_FOUND(Category.ABSENT, "company not found"),
        COMPANY_ID_EXISTS(Category.PRESENT, "company id is already exists");

        @Getter
        private final Category category;

        @Getter
        private final String description;

        @Override
        public String toString() {
            return this.getDescription();
        }

        // Method for future-proofing.
        public int getCode() {
            return this.ordinal();
        }
    }


}
