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

        USER_NOT_FOUND(Category.ABSENT, "user not found"),
        USER_ID_EXISTS(Category.PRESENT, "user id is already used"),
        USER_SSN_EXISTS(Category.PRESENT, "user ssn is already used"),
        USER_INVALID_DATA(Category.INVALID, "user data is invalid"),

        PLAN_NOT_FOUND(Category.ABSENT, "plan not found"),
        PLAN_INVALID_DATA(Category.INVALID, "plan data is invalid"),
        PLAN_ID_EXISTS(Category.PRESENT, "plan id is already used"),
        PLAN_PACKAGE_NOT_FOUND(Category.ABSENT, "plan package not found"),
        PLAN_PACKAGE_INVALID_DATA(Category.INVALID, "plan package data is invalid"),
        PLAN_PACKAGE_ID_EXISTS(Category.PRESENT, "plan package id is already used");

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