# Date Validator

This is a Java program that validates and formats dates entered in various formats.

## Usage

This program reads input from the console, validates and formats the entered dates, and prints the formatted dates to the console. To run the program, compile the `App.java` file and then run the compiled class file. There are no external libraries other than those from the Java SDK. You can then enter dates in various formats and the program will validate and format them.

There is a test class that contains multiple tests for individual cases that violate one aspect of the specification. Looking back I would change these to input data sets with many valid and invalid dates and then test these as with the current solution there is a lot of repeated code and each case only gets one test.


>Note: This code will not compile with java versions before 15. In development I have been using Oracle Open JDK 17.

## Supported Date Formats

The program supports the following date formats:

- Day
  - One or two digits
  - Between 1 and 31
  - Supports 01 - 09
- Month
  - One or two digits
    - Between 1 and 12
    - Supports 01 - 09
  - Three Letters
    - The first three letters of the month name
    - Capitalisation:
    - MMM
    - Mmm
    - mmm
- Year
  - Two or four digit
  - Two digit
    - Between 1950 - 2049
  - Four digit
    - Between 1753 - 3000
- Separators 
  - `-`
  - `/`
  - `Space`

## Error Messages

The program generates the following error messages for invalid dates:

- `EER01: Year out of range` - The year must be between 1753 and 3000
- `EER02: Invalid date` - The input day is not formatted correctly
- `EER03: Invalid month` - The input month is not formatted correctly
- `EER04: Invalid year` - The input year is formatted correctly
- `EER05: Invalid date format` - The input month is not a valid month
- `EER06: Invalid separator` - The separators do not match
- `EER07: Invalid year` - The input day is not a valid day for the input month
- `EER08: Invalid input` - The input year is not a leap year



## License

This program is licensed under the MIT License. See the `LICENSE` file for details.
