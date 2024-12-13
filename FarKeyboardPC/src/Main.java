import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.net.*;
import java.io.*;

public class Main {

    public static void server(int port) {
        Socket socket = null;
        try {
            Robot r = new Robot();
            ServerSocket server = new ServerSocket(port);
            Boolean altTabPressed = false, heb;
            InputContext context = InputContext.getInstance();
            heb = context.getLocale().toString().equals("he_IL");
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            String line = "";
            while (true) {
                socket = server.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                try {
                    line = in.readUTF();
                    //System.out.println(line);
                    if (line.equals("ALT_TAB")) {
                        if (altTabPressed) {
                            altTabPressed = false;
                            r.keyRelease(KeyEvent.VK_ALT);
                        } else {
                            altTabPressed = true;
                            r.keyPress(KeyEvent.VK_ALT);
                            r.keyPress(KeyEvent.VK_TAB);
                            r.keyRelease(KeyEvent.VK_TAB);
                        }
                    } else {
                        r.keyRelease(KeyEvent.VK_TAB);
                        altTabPressed = false;
                    }
                    if (line.equals("SELECT")) {
                        r.mousePress(InputEvent.BUTTON1_MASK);
                        //System.out.println(line);
                    }
                    if (line.equals("END_SELECT")) {
                        r.mouseRelease(InputEvent.BUTTON1_MASK);
                        //System.out.println(line);
                    }
                    if (line.length() > 4 && line.substring(0, 4).equals("move")) {
                        line = line.substring(4);
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        r.mouseMove(p.x - (int) Float.parseFloat(line.substring(0,line.indexOf('*'))),
                                p.y - (int) Float.parseFloat(line.substring(1 + line.indexOf('*'))));
                    }
                    if (line.equals("Left_Click")) {
                        //System.out.println(line);
                        r.mousePress(InputEvent.BUTTON1_MASK);
                        r.mouseRelease(InputEvent.BUTTON1_MASK);
                    }
                    if (line.equals("Right_Click")) {
                        r.mousePress(InputEvent.BUTTON3_MASK);
                        r.mouseRelease(InputEvent.BUTTON3_MASK);
                    }
                    if(line.length() > 3 && line.substring(0,3).equals("key")) {
                        line = line.substring(4);
                        Typing hte = new Typing(r, line);
                        heb = hte.writeLine(heb);
                        continue;
                    }
                    if (line.equals("CTRL_C")) {
                        r.keyPress(KeyEvent.VK_CONTROL);
                        r.keyPress(KeyEvent.VK_C);
                        r.keyRelease(KeyEvent.VK_C);
                        r.keyRelease(KeyEvent.VK_CONTROL);
                    }
                    if (line.equals("CTRL_F")) {
                        r.keyPress(KeyEvent.VK_CONTROL);
                        r.keyPress(KeyEvent.VK_F);
                        r.keyRelease(KeyEvent.VK_F);
                        r.keyRelease(KeyEvent.VK_CONTROL);
                    }
                    if (line.equals("CTRL_V")) {
                        r.keyPress(KeyEvent.VK_CONTROL);
                        r.keyPress(KeyEvent.VK_V);
                        r.keyRelease(KeyEvent.VK_V);
                        r.keyRelease(KeyEvent.VK_CONTROL);
                    }
                    if (line.equals("CTRL_X")) {
                        r.keyPress(KeyEvent.VK_CONTROL);
                        r.keyPress(KeyEvent.VK_X);
                        r.keyRelease(KeyEvent.VK_X);
                        r.keyRelease(KeyEvent.VK_CONTROL);
                    }
                    if (line.equals("CTRL_Z")) {
                        r.keyPress(KeyEvent.VK_CONTROL);
                        r.keyPress(KeyEvent.VK_Z);
                        r.keyRelease(KeyEvent.VK_Z);
                        r.keyRelease(KeyEvent.VK_CONTROL);
                    }
                    if (line.equals("CTRL_A")) {
                        r.keyPress(KeyEvent.VK_CONTROL);
                        r.keyPress(KeyEvent.VK_A);
                        r.keyRelease(KeyEvent.VK_A);
                        r.keyRelease(KeyEvent.VK_CONTROL);
                    }
                    if (line.equals("DELETE")) {
                        r.keyPress(KeyEvent.VK_DELETE);
                        r.keyPress(KeyEvent.VK_DELETE);
                    }
                    if (line.equals("ENTER")) {
                        r.keyPress(KeyEvent.VK_ENTER);
                        r.keyRelease(KeyEvent.VK_ENTER);
                    }
                    if (line.equals("BACK")) {
                        r.keyPress(KeyEvent.VK_BACK_SPACE);
                        r.keyRelease(KeyEvent.VK_BACK_SPACE);
                    }
                    if (line.equals("UP")) {
                        r.keyPress(KeyEvent.VK_UP);
                        r.keyRelease(KeyEvent.VK_UP);
                    }
                    if (line.equals("DOWN")) {
                        r.keyPress(KeyEvent.VK_DOWN);
                        r.keyRelease(KeyEvent.VK_DOWN);
                    }
                    if (line.equals("RIGHT")) {
                        r.keyPress(KeyEvent.VK_LEFT);
                        r.keyRelease(KeyEvent.VK_LEFT);
                    }
                    if (line.equals("LEFT")) {
                        r.keyPress(KeyEvent.VK_RIGHT);
                        r.keyRelease(KeyEvent.VK_RIGHT);
                    }
                    if (line.equals("ESC")) {
                        r.keyPress(KeyEvent.VK_ESCAPE);
                        r.keyRelease(KeyEvent.VK_ESCAPE);
                    }
                    if (line.equals("WINDOWS")) {
                        r.keyPress(KeyEvent.VK_WINDOWS);
                        r.keyRelease(KeyEvent.VK_WINDOWS);
                    }
                    if (line.length() > 4 && line.substring(0, 5).equals("wheel")) {
                        line = line.substring(5);
                        int sign = 1;
                        if (Float.parseFloat(line) < 0) {
                            sign = -1;
                        }
                        double chan = Math.pow(Float.parseFloat(line) * sign, 1 / 8);
                        if (chan > 2) {
                            chan /= 2;
                        }
                        if (chan < 10) {
                            r.mouseWheel((-sign) * ((int) chan));
                            try{
                                Thread.sleep(10);
                            } catch(InterruptedException e) {
                                System.out.println(e.toString());
                            }
                        }
                    }
                    socket.close();
                    in.close();
                } catch(IOException i) {
                    System.out.println(i.toString());
                    break;
                }
            }
        } catch(IOException i) {
            System.out.println(i.toString());
        } catch (AWTException awt) {
            System.out.println(awt.toString());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        server(1755);
    }
}