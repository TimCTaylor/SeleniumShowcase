package com.humanlegion.sandbox;

public class Sandbox2 {
    class Answer {

        // Change these boolean values to control whether you see 
        // the expected result and/or hints.
        public boolean showExpectedResult = false;
        public boolean showHints = true;

        // Return the total character after adding quest experience as a JSON string.
        // Return null if there is a problem with the JSON
        public String calculateFinalExperience(String characterString, String questString) {
            try {
                return characterString;
            } catch (Exception e) {
                return null;
            }

        }

    }

}
