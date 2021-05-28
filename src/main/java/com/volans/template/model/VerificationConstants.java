package com.volans.template.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class VerificationConstants {

    @Getter
    @AllArgsConstructor
    public enum VerificationMethod {

        @ApiEnum("1")
        DOPA(1, "DOPA"),

        @ApiEnum("2")
        MANUAL_APPROVAL(2, "MANUAL_APPROVAL"),

        @ApiEnum("3")
        CARD_READER(3, "CARD_READER"),

        @ApiEnum("4")
        BIOMETRIC(4, "BIOMETRIC"),

        @ApiEnum("5")
        MANUAL_PASSPORT_PHOTO(5, "MANUAL_PASSPORT_PHOTO"),

        @ApiEnum("6")
        MANUAL_APPROVAL_WITH_SCANNED_IMAGE(6, "MANUAL_APPROVAL_WITH_SCANNED_IMAGE"),

        @ApiEnum("7")
        BIOMETRIC_WITH_SCANNED_IMAGE(7, "BIOMETRIC_WITH_SCANNED_IMAGE"),

        @ApiEnum("8")
        ADDITIONAL_DOCUMENT(8, "ADDITIONAL_DOCUMENT"),

        @ApiEnum("9")
        MANUAL_FAKE_CARD_DETECTION(9, "MANUAL_FAKE_CARD_DETECTION"),

        @ApiEnum("10")
        AUTO_FAKE_CARD_DETECTION(10, "AUTO_FAKE_CARD_DETECTION");

        private Integer id;
        private String name;

        public static VerificationMethod getVerificationMethodFromId(Integer id) {
            for (VerificationMethod verificationMethod : VerificationMethod
                    .values()) {
                if (id.equals(verificationMethod.getId())) {
                    return verificationMethod;
                }
            }

            return null;
        }

        public static Integer getIdFromName(String name) {

            for (VerificationMethod verificationMethod : values()) {
                if (verificationMethod.getName().equals(name)) {
                    return verificationMethod.getId();
                }
            }

            throw new IllegalArgumentException();

        }

        public static boolean isValidName(String name) {

            for (VerificationMethod verificationMethod : values()) {
                if (verificationMethod.getName().equals(name)) {
                    return true;
                }
            }

            return false;

        }

    }

    @Getter
    @AllArgsConstructor
    public enum VerificationSource {

        @ApiEnum("1")
        TRUEMONEY(1, "truemoney"),

        @ApiEnum("2")
        SEVEN_ELEVEN(2, "7-11"),

        @ApiEnum("3")
        TRUEMOVEH(3, "truemoveh"),

        @ApiEnum("4")
        NANO(4, "nano");


        private Integer id;
        private String name;

        public static boolean isValidName(String name) {

            for (VerificationSource verificationSource : values()) {
                if (verificationSource.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum VerificationStatus {

        VERIFIED("VERIFIED"),
        UNVERIFIED("UNVERIFIED");

        private String name;

    }

}
