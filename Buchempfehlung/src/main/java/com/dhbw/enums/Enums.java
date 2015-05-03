package com.dhbw.enums;

public class Enums
{
    public enum Age
    {
        A("<18"),
        B("19-24"),
        C("25-35"),
        D("36-49"),
        E("50-65"),
        F(">65");

        private String text;

        Age(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Age fromString(String text) {
            if (text != null) {
                for (Age b : Age.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public enum Sex {
        M("m"),
        W("w");

        private String text;

        Sex(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Sex fromString(String text) {
            if (text != null) {
                for (Sex b : Sex.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public enum Married {
        Y("ja"),
        N("nein");
        private String text;

        Married(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Married fromString(String text) {
            if (text != null) {
                for (Married b : Married.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public enum Children {
        ZERO("0"),
        ONE("1"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        MORE(">4");

        private String text;

        Children(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Children fromString(String text) {
            if (text != null) {
                for (Children b : Children.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public enum Degree {
        GYM("Gymnasium"),
        HAUPT("Hauptschule"),
        HOCH("Hochschule"),
        NO("keiner"),
        PROMO("Promotion"),
        REAL("Realschule");

        private String text;

        Degree(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Degree fromString(String text) {
            if (text != null) {
                for (Degree b : Degree.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public enum Occupation {
        EMPLOYEE("Angestellter"),
        WORK("Arbeiter"),
        NONE("Arbeitslos"),
        FUHRER("Fuehrungskraft"),
        WIFE("Hausfrau"),
        TEACHER("Lehrer"),
        RETIREE("Rentner"),
        INDEP("Selbstaendig");

        private String text;

        Occupation(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Occupation fromString(String text) {
            if (text != null) {
                for (Occupation b : Occupation.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public enum Salary {
        A("<1000"),
        B("1000-1999"),
        C("2000-2999"),
        D("3000-3999"),
        E("4000-4999"),
        F("5000 und mehr");

        private String text;

        Salary(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Salary fromString(String text) {
            if (text != null) {
                for (Salary b : Salary.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public enum Book {
        A("Buch_A"),
        B("Buch_B"),
        C("Buch_C");

        private String text;

        Book(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static Book fromString(String text) {
            if (text != null) {
                for (Book b : Book.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }


}
