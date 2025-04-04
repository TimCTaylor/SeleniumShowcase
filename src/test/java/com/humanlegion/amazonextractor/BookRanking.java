package com.humanlegion.amazonextractor;
// Object to hold ranking and review data. This will eventually be serialized to JSON.

import java.util.HashMap;
import java.util.Map;

// It provides serveral static methods to parse the data on the html page.

public class BookRanking {
    String ASIN;
    int reviewCount;
    float reviewAverage;
    int overallRanking; // ie best selling rank in overall Kindle Store. Sub-charts are handled in subRankings
    HashMap<String, Integer> subRankings; // Map to hold best-seller chart names and their positions
    boolean error;
    String errorMsg;

    public BookRanking() {
        // Initialize the object properties so they aren't null.
        subRankings = new HashMap<>();
        ASIN = "";
        errorMsg = "";
        error = false;
    }

    public void addSubRanking(String chartName, int position) {
        subRankings.put(chartName, position);
    }

    public Integer getSubRanking(String chartName) {
        return subRankings.get(chartName);
    }

    public Map<String, Integer> getAllSubRankings() {
        return subRankings;
    }

    // pass in the raw Kindle Store ranking text. Return the Kindle Store bestseller rank.
    public static int getRankingFromText(String rankingRawText) {
        try {
            // Split text by spaces, take the first chunk, remove any non-numeric characters and parse the result as an int
            String numericText = rankingRawText.split(" ")[0].replaceAll("[^\\d]", "");
            return Integer.parseInt(numericText);
        } catch (NumberFormatException e) {
            // If parsing fails, return -1 to indicate an error
            return -1;
        }
    }
}
