import org.example.App;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatesTest {

    public static final PrintStream BACKUP_ERR = System.err;

    public static final PrintStream BACKUP_OUT = System.out;

    public static final InputStream BACKUP_IN = System.in;

    public static ByteArrayOutputStream OUTPUT_STREAM = new ByteArrayOutputStream();;

    public static ByteArrayOutputStream OUTPUT_ERROR_STREAM = new ByteArrayOutputStream();


    @BeforeAll
    public static void initialize() throws Exception {

    }

    @BeforeEach
    public void setUp() throws Exception {
        System.setOut(new PrintStream(OUTPUT_STREAM));
        System.setErr(new PrintStream(OUTPUT_ERROR_STREAM));
    }

    @AfterEach
    public void tearDown() throws Exception {
        OUTPUT_STREAM.reset();
        OUTPUT_ERROR_STREAM.reset();
        System.setIn(BACKUP_IN);
    }

    @Test
    public void lowerLimit() {
        InputStream inputStream = new ByteArrayInputStream("1-1-1753".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 1753\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void upperLimit() {
        InputStream inputStream = new ByteArrayInputStream("31-12-3000".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("31 DEC 3000\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void slashSplitter() {
        InputStream inputStream = new ByteArrayInputStream("1/1/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void dashSplitter() {
        InputStream inputStream = new ByteArrayInputStream("1-1-2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void spaceSplitter() {
        InputStream inputStream = new ByteArrayInputStream("1 1 2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void zero_dFormat() {
        InputStream inputStream = new ByteArrayInputStream("01/1/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void ddFormat() {
        InputStream inputStream = new ByteArrayInputStream("10/1/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("10 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void dFormat() {
        InputStream inputStream = new ByteArrayInputStream("1/1/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void mFormat() {
        InputStream inputStream = new ByteArrayInputStream("1/1/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void zero_mFormat() {
        InputStream inputStream = new ByteArrayInputStream("1/01/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void mmFormat() {
        InputStream inputStream = new ByteArrayInputStream("1/10/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 OCT 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void MmmFormat() {
        InputStream inputStream = new ByteArrayInputStream("1 Jan 2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void MMMFormat() {
        InputStream inputStream = new ByteArrayInputStream("1 JAN 2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void mmmFormat() {
        InputStream inputStream = new ByteArrayInputStream("1 jan 2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void yyFormatLessThan50() {
        InputStream inputStream = new ByteArrayInputStream("1/1/23".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void yyFormatGreaterThan50() {
        InputStream inputStream = new ByteArrayInputStream("1/1/65".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 1965\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void yyyyFormat() {
        InputStream inputStream = new ByteArrayInputStream("1/1/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("01 JAN 2023\n", OUTPUT_STREAM.toString());
        assertEquals("", OUTPUT_ERROR_STREAM.toString());
    }

    @Test
    public void invalidDay() {
        InputStream inputStream = new ByteArrayInputStream("123/1/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("123/1/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR02: "));
    }

    @Test
    public void mFormatInvalidMonth() {
        InputStream inputStream = new ByteArrayInputStream("1/m/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/m/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR03: "));
    }

    @Test
    public void mmFormatInvalidMonth() {
        InputStream inputStream = new ByteArrayInputStream("1/1a/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/1a/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR03: "));
    }

    @Test
    public void MMmFormatInvalidMonth() {
        InputStream inputStream = new ByteArrayInputStream("1/JAn/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/JAn/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR03: "));
    }

    @Test
    public void mmmFormatInvalidMonth() {
        InputStream inputStream = new ByteArrayInputStream("1/JANUARY/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/JANUARY/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR03: "));
    }

    @Test
    public void proceedingSeparator() {
        InputStream inputStream = new ByteArrayInputStream(" 1/JAN/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals(" 1/JAN/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR02: "));
    }

    @Test
    public void doubleSeparatorOne() {
        InputStream inputStream = new ByteArrayInputStream("1//JAN/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1//JAN/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR04: "));
    }

    @Test
    public void doubleSeparatorTwo() {
        InputStream inputStream = new ByteArrayInputStream("1/JAN//2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/JAN//2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR04: "));
    }

    @Test
    public void notADay() {
        InputStream inputStream = new ByteArrayInputStream("43/JAN/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("43/JAN/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR07: "));
    }

    @Test
    public void notAMonthText() {
        InputStream inputStream = new ByteArrayInputStream("1/JDS/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/JDS/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR05: "));
    }

    @Test
    public void notAMonthDigit() {
        InputStream inputStream = new ByteArrayInputStream("1/13/2023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/13/2023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR05: "));
    }

    @Test
    public void yearOutOfRange() {
        InputStream inputStream = new ByteArrayInputStream("1/01/3023".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("1/01/3023 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR01: "));
    }

    @Test
    public void notALeapYear() {
        InputStream inputStream = new ByteArrayInputStream("29/02/2022".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("29/02/2022 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR08: "));
    }

    @Test
    public void yyyyFormatParseError() {
        InputStream inputStream = new ByteArrayInputStream("29/02/0000".getBytes());
        System.setIn(inputStream);
        App.main(new String[]{});
        assertEquals("29/02/0000 - INVALID\n", OUTPUT_STREAM.toString());
        assertTrue(OUTPUT_ERROR_STREAM.toString().contains("ERR01: "));
    }

    @AfterAll
    public static void restore() throws Exception {
        System.setOut(BACKUP_OUT);
        System.setErr(BACKUP_ERR);
        System.setIn(BACKUP_IN);
    }
}