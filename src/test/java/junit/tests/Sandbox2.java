package junit.tests;

public class Sandbox2 {
    class Answer {

        // Change these boolean values to control whether you see 
        // the expected result and/or hints.
        public static boolean showExpectedResult = false;
        public static boolean showHints = true;
    
        // Return the total character after adding quest experience as a JSON string.
        // Return null if there is a problem with the JSON
        public static String calculateFinalExperience(String characterString, String questString) {
            try {
                return characterString;
            } catch (Exception e)
            { return null;}
            
        }
    
    }

}
