package core.webloader;

import java.io.IOException;
import java.io.OutputStream; import java.io.PrintStream;
import java.util.Locale;

import org.apache.log4j.Logger;

public class Log4jPrintStream extends PrintStream {

    private Logger log = Logger.getLogger("SystemOut");

    private static PrintStream instance = new Log4jPrintStream(System.out);

    private Log4jPrintStream(OutputStream out) {
        super(out);
    }

    public static void redirectSystemOut() {
        System.setOut(instance);
    }

    public PrintStream append(char c) {
        // Ignore
        return this;
    }

    public PrintStream append(CharSequence csq, int start, int end) {
        // Ignore
        return this;
    }

    public PrintStream append(CharSequence csq) {
        // Ignore
        return this;
    }

    public boolean checkError() {
        // Ignore
        return false;
    }

    protected void clearError() {
        // Ignore
    }

    public void close() {
        // Ignore
    }

    public void flush() {
        // Ignore
    }

    public PrintStream format(Locale l, String format, Object... args) {
        // Ignore
        return this;
    }

    public PrintStream format(String format, Object... args) {
        // Ignore
        return this;
    }

    public void print(boolean b) {
        println(b);
    }

    public void print(char c) {
        println(c);
    }

    public void print(char[] s) {
        println(s);
    }

    public void print(double d) {
        println(d);
    }

    public void print(float f) {
        println(f);
    }

    public void print(int i) {
        println(i);
    }

    public void print(long l) {
        println(l);
    }

    public void print(Object obj) {
        println(obj);
    }

    public void print(String s) {
        println(s);
    }

    public PrintStream printf(Locale l, String format, Object... args) {
        // Ignore
        return this;
    }

    public PrintStream printf(String format, Object... args) {
        // Ignore
        return this;
    }

    public void println() {
        // Ignore
    }

    public void println(boolean x) {
        log.debug(Boolean.valueOf(x));
    }

    public void println(char x) {
        log.debug(Character.valueOf(x));
    }

    public void println(char[] x) {
        log.debug(x == null ? null : new String(x));
    }

    public void println(double x) {
        log.debug(Double.valueOf(x));
    }

    public void println(float x) {
        log.debug(Float.valueOf(x));
    }

    public void println(int x) {
        log.debug(Integer.valueOf(x));
    }

    public void println(long x) {
        log.debug(x);
    }

    public void println(Object x) {
        log.debug(x);
    }

    public void println(String x) {
        log.debug(x);
    }

    protected void setError() {
        // Ignore
    }

    public void write(byte[] buf, int off, int len) {
        // Ignore
    }

    public void write(int b) {
        // Ignore
    }

    public void write(byte[] b) throws IOException {
        // Ignore
    }
}
