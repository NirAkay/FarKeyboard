import java.awt.*;
import java.awt.event.KeyEvent;

public class Typing {
    private String line;
    private Robot r;

    public Typing(Robot r, String line) {
        this.r = r;
        this.line = line;

    }

    private char convertNote(char note) {
        switch (note) {
            case 'א':
                return 't';
            case 'ב':
                return 'c';
            case 'ג':
                return 'd';
            case 'ד':
                return 's';
            case 'ה':
                return 'v';
            case 'ו':
                return 'u';
            case 'ז':
                return 'z';
            case 'ח':
                return 'j';
            case 'ט':
                return 'y';
            case 'י':
                return 'h';
            case 'כ':
                return 'f';
            case 'ל':
                return 'k';
            case 'מ':
                return 'n';
            case 'נ':
                return 'b';
            case 'ס':
                return 'x';
            case 'ע':
                return 'g';
            case 'פ':
                return 'p';
            case 'צ':
                return 'm';
            case 'ק':
                return 'e';
            case 'ר':
                return 'r';
            case 'ש':
                return 'a';
            case 'ת':
                return ',';
            default:
                return note;
        }
    }

    private void pressAscii(int note, Robot r) {
        if (note == 0) {
            return;
        }
        pressAscii(note / 10, r);
        r.keyPress(KeyEvent.VK_NUMPAD0 + (note % 10));
        r.keyRelease(KeyEvent.VK_NUMPAD0 + (note %10));
    }

    private void writeWord(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            if (Character.isUpperCase(c)) {
                r.keyPress(KeyEvent.VK_SHIFT);
            }
            r.keyPress(Character.toUpperCase(c));
            r.keyRelease(Character.toUpperCase(c));
            if (Character.isUpperCase(c)) {
                r.keyRelease(KeyEvent.VK_SHIFT);
            }
        } else {
            r.keyPress(KeyEvent.VK_ALT);
            pressAscii(c, r);
            r.keyRelease(KeyEvent.VK_ALT);
        }
    }

    public boolean writeLine(boolean heb) {
        for (int i = 0; i < this.line.length(); i++) {
            char note = convertNote(line.charAt(i));
            if (line.charAt(i) < 256) {
                if (heb) {
                    this.r.keyPress(KeyEvent.VK_ALT);
                    this.r.keyPress(KeyEvent.VK_SHIFT);
                    this.r.keyRelease(KeyEvent.VK_SHIFT);
                    this.r.keyRelease(KeyEvent.VK_ALT);
                    heb = false;
                }
            } else {
                if (!heb) {
                    this.r.keyPress(KeyEvent.VK_ALT);
                    this.r.keyPress(KeyEvent.VK_SHIFT);
                    this.r.keyRelease(KeyEvent.VK_SHIFT);
                    this.r.keyRelease(KeyEvent.VK_ALT);
                    heb = true;
                }
            }
            writeWord(note);
        }
        return heb;
    }
}
