import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;


public class Main {
    public static void main(String[] args) {
        String text = "";
        try {
            File myObj = new File("data/" + args[0]);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                text = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        final var sentences = text.split("[.!?]").length;
        final var wordsOfText = text.replaceAll("[^\\w\\s\\d]", "").toLowerCase(Locale.ROOT).split("\\s+");
        final int words = wordsOfText.length;
        final var characters = text.replaceAll("[\\s\\n]+", "").split("").length;

        int syllables = 0;
        int polysyllables = 0;
        for (var word : wordsOfText) {
            int wordSyllables = countSyllables(word);
            syllables += wordSyllables;

            if (wordSyllables > 2) {
                polysyllables++;
            }
        }

        System.out.printf("The text is:%n%s%n"
                        + "Words: %d%n" +
                        "Sentences: %d%n" +
                        "Characters: %d%n" +
                        "Syllables: %d%n" +
                        "Polysyllables: %d%n",
                text, words, sentences, characters, syllables, polysyllables);

        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        final Scanner scanner = new Scanner(System.in);
        final String alg = scanner.next();
        System.out.println();

        if (alg.equals("ARI") || alg.equals("all")) {
            final double score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
            System.out.printf(
                    "Automated Readability Index: %.2f (about %s-year-olds).%n",
                    score,
                    calculateAge(score)
            );
        }

        if (alg.equals("FK") || alg.equals("all")) {
            final double score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
            System.out.printf(
                    "Flesch–Kincaid readability tests: %.2f (about %s-year-olds).%n",
                    score,
                    calculateAge(score)
            );
        }

        if (alg.equals("SMOG") || alg.equals("all")) {
            final double score = 1.043 * Math.sqrt(1.0 * polysyllables * 30 / sentences) + 3.1291;
            System.out.printf(
                    "Simple Measure of Gobbledygook: %.2f (about %s-year-olds).%n",
                    score,
                    calculateAge(score)
            );
        }

        if (alg.equals("CL") || alg.equals("all")) {
            double s = 1.0 * sentences / words * 100;
            double l = 1.0 * characters / words * 100;
            final double score = 0.0588 * l - 0.296 * s - 15.8;
            System.out.printf(
                    "Coleman–Liau index: %.2f (about %s-year-olds).%n",
                    score,
                    calculateAge(score)
            );
        }

    }


    private static String calculateAge(double score) {
//        final var ageGroups = new String[]{"5-6", "6-7", "7-9", "9-10", "10-11", "11-12",
//                "12-13", "13-14", "14-15", "15-16", "16-17", "17-18", "18-24", "24+"};
        final var ageGroups = new String[]{"6", "7", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "24", "24+"};
        final int level = min(14, max(1, (int) round(score))) - 1;
        return ageGroups[level];
    }


    private static int countSyllables(String word) {
        Pattern pattern = Pattern.compile("a");
        Matcher matcher = pattern.matcher(word.replaceAll("[e]$", "").replaceAll("[aeiouy]+", "a"));
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count > 0 ? count : 1;
    }
}

