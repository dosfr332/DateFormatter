package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

import static java.util.Map.entry;

/**
 * This class validates and formats dates entered in various formats.
 */
public class App {

    //region Constants
    // Regular expression pattern used to validate date format
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^(?<day>\\d{1,2})(?<sep>[-/\\s])(?<month>\\d{1,2}|[a-z]{3}|[A-Z][a-z]{2}|[A-Z]{3})(?<sep2>[-/\\s])(?<year>\\d{2}$|\\d{4}$)"
    );

    // The minimum year allowed by the program
    private static final int MIN_YEAR = 1753;

    // The maximum year allowed by the program
    private static final int MAX_YEAR = 3000;

    // A PrintStream object for writing output to the console
    private static final PrintStream out = System.out;

    // A PrintStream object for writing error messages to the console
    private static final PrintStream err = System.err;

    // A mapping of month names to their corresponding integer values
    private static final Map<String, Integer> MONTHS_INT_MAP;

    //endregion

    static {
        // Initialize the MONTHS_INT_MAP
        MONTHS_INT_MAP = Map.ofEntries(
                entry("JAN", 1),
                entry("FEB", 2),
                entry("MAR", 3),
                entry("APR", 4),
                entry("MAY", 5),
                entry("JUN", 6),
                entry("JUL", 7),
                entry("AUG", 8),
                entry("SEP", 9),
                entry("OCT", 10),
                entry("NOV", 11),
                entry("DEC", 12)
        );
    }

    /**
     * This is the main method of the program. It reads input from the console, validates and formats the entered dates,
     * and prints the formatted dates to the console.
     *
     * @param args An array of command-line arguments that are not used by this program
     */
    public static void main(String[] args) {

        try (BufferedReader BUFFERED_READER = new BufferedReader(new InputStreamReader(System.in))){
            String line;
            while ((line = BUFFERED_READER.readLine()) != null) {
                checkDate(line);
            }
        } catch (IOException e) {
            err.println(e.getMessage());
        }
    }

    /**
     * This method checks if the date is valid and prints the formatted date to the console.
     * @param line The date entered by the user
     */
    private static void checkDate(String line) {
        Matcher matcher = DATE_PATTERN.matcher(line);
        if (matcher.matches()) {
            int day = Integer.parseInt(matcher.group("day"));
            String yearString = matcher.group("year");
            String monthString = matcher.group("month");
            Month month;

            Integer year = setYearIfValid(line, yearString);
            if (year == null) return;

            if (separatorCheck(line, matcher)) return;

            if (Character.isDigit(monthString.charAt(0))) month = setMonthFromDigit(line, monthString);
            else month = setMonthFromString(line, monthString);
            if (month == null) return;

            year = getFourDigitYearFromTwoDigit(year);

            if (year < MIN_YEAR || year > MAX_YEAR) {
                invalidDate(line, "EER01: Year out of range", "Year must be between 1753 and 3000");
            } else {
                if(parseDate(line, day, month, year) == null) {
                    return;
                }
                out.printf("%02d %s %04d\n", day, month.name().substring(0, 3), year);
            }
        } else {
            nonMatchingRegex(line);
        }
    }

    /**
     * This method prints an error message to the console if the date entered by the user does is not a possible date.
     * @param line The date entered by the user
     * @param day The day the user entered
     * @param month The month the user entered
     * @param year The year the user entered
     * @return The parsed date if it is valid else null
     */
    private static LocalDate parseDate(String line, int day, Month month, Integer year) {
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            dateParseErrorHandling(line, day, month, year);
            return null;
        }
    }

    /**
     * This method converts a two digit year to a four digit year.
     * @param year They year the user entered
     * @return The four digit year
     */
    private static Integer getFourDigitYearFromTwoDigit(Integer year) {
        if (year >= 0 && year < 100) {
            year += 2000;
            if (year >= 2050){
                year -= 100;
            }
        }
        return year;
    }

    /**
     * This method handles the errors thrown by parseDate.
     * @param line The date entered by the user
     * @param month The month the user entered
     * @param year The year the user entered
     */
    private static void dateParseErrorHandling(String line, int day, Month month, Integer year) {
        if(day == 29 && month.name().equals("FEBRUARY")){
            invalidDate(line, "ERR08: Invalid date '" + month + " " + day + "'",
        year + " is not a leap year");
            return;
        }
        invalidDate(line, "ERR07: Invalid date '" + month + " " + day + "'",
    "Day: " + day + " does not exist in month: " + month);
    }

    /**
     * This method checks if the text month entered is an actual month if not it will throw an error to stderr.
     * @param line The date entered by the user
     * @return The month that the user entered (if valid)
     */
    private static Month setMonthFromString(String line, String monthString) {
        Month month;
        Integer enteredMonth = MONTHS_INT_MAP.get(monthString.toUpperCase());
        if(enteredMonth == null) {
            invalidDate(line, "ERR05: Not a valid date", "Not a valid month");
            return null;
        }
        month = Month.of(enteredMonth);
        return month;
    }

    /**
     * This method checks if the digit month entered is an actual month if not it will throw an error to stderr.
     * @param line The date entered by the user
     * @return The month that the user entered (if valid)
     */
    private static Month setMonthFromDigit(String line, String monthString) {
        Month month;
        int monthNum = Integer.parseInt(monthString);
        if(monthNum < 1 || monthNum > 12) {
            invalidDate(line, "ERR05: Not a valid date", "Not a valid month");
            return null;
        }
        month = Month.of(monthNum);
        return month;
    }

    /**
     * This method checks if the two separators entered by the user are the same
     * @param line The date entered by the user
     * @param matcher The matcher object that matches the date entered by the user
     * @return True if the separators are invalid else false
     */
    private static boolean separatorCheck(String line, Matcher matcher) {
        if(!matcher.group("sep").equals(matcher.group("sep2"))) {
            invalidDate(line, "ERR06: Invalid date format", "Separators do not match");
            return true;
        }
        return false;
    }

    /**
     * This method checks if the year entered by the user is valid.
     * @param line The date entered by the user
     * @param yearString The year entered by the user
     */
    private static Integer setYearIfValid(String line, String yearString) {
        int year = Integer.parseInt(yearString);
        if(yearString.length() == 4) {
            if (year < 1753 || year > 3000) {
                invalidDate(line, "ERR01: Year out of range", "Year must be between 1753 and 3000");
                return null;
            }
        }
        return year;
    }

    /**
     * This method prints an error message to the console indicating that the entered date is invalid.
     * @param lineInput    The input string representing the date that was entered
     * @param errorMessage A message indicating the type of error that occurred
     * @param reason       A more detailed description of the error that occurred
     */
    private static void invalidDate(String lineInput, String errorMessage, String reason) {
        out.printf("%s - INVALID\n", lineInput);
        err.printf("%s\n%s\n", errorMessage, reason);
    }

    /**
     * This method handles a date entered by the user that does not match the regex pattern.
     * @param line The date entered by the user
     */
    private static void nonMatchingRegex(String line) {
        String dayPattern = "^\\d{1,2}[-/\\s].*";
        String monthPattern = ".*[-/\\s](\\d{1,2}|[a-z]{3}|[A-Z][a-z]{2}|[A-Z]{3})[-/\\s].*";
        String yearPattern = ".*[-/\\s]\\d{2}$|\\d{4}$";

        if(!Pattern.matches(dayPattern, line)) {
            invalidDate(line, "ERR02: Invalid date format", """
                    Day not formatted correctly
                    Must be digits in form dd, 0d, or d followed by a single separator of either "/", " ", or "-"
                    TIP: Make sure that there are no spaces in front of the first character""");

        } else if(!Pattern.matches(monthPattern, line)) {
            invalidDate(line, "ERR03: Invalid date format", """
                    Month not formatted correctly
                    Must be in form m, 0m, mm, or MMM proceeded and followed by a single separator of either "/", " ", or "-"
                    TIP: Make sure that if using text as month that all characters are in same case or with the first letter upper-case""");

        } else if (!Pattern.matches(yearPattern, line)) {
            invalidDate(line, "ERR04: Invalid date format", """
                    Year not formatted correctly
                    Must be in form yy, or yyyy proceeded by a single separator of either "/", " ", or "-"
                    TIP: Make sure that there are no spaces after the last character""");

        } else {
            invalidDate(line, "Invalid date format", "UNKNOWN REASON");
        }
    }
}