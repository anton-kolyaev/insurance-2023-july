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

        COMPANY_NOT_FOUND(Category.ABSENT, "company not found"),
        COMPANY_ID_EXISTS(Category.PRESENT, "company id is already exists"),

        COMPANY_FUNCTIONS_NOT_SETTED(Category.ABSENT, "company functions not setted"),
        ILLEGAL_ACCEPT(Category.INVALID, "Illegal accept value");

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
