package com.example.farkeyboard;

import android.os.AsyncTask;
import android.widget.TextView;
import java.io.DataOutputStream;
import java.net.Socket;

public class DataTransfer extends AsyncTask<Void, Void, Void> {
    private String et, ip;
    private TextView tv;

    public DataTransfer(String et, TextView tv, String ip) {
        this.et = et;
        this.tv = tv;
        this.ip = ip;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Socket socket = null;
        try {
            socket = new Socket(ip, 1755);
            socket.setSoTimeout(30000);
            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
            if (et.equals("key:")) {
                DOS.writeUTF("ENTER");
            } else {
                DOS.writeUTF(et);
            }
            socket.close();
        } catch (Exception e) {
            tv.setText(e.toString());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception ioe) {
                tv.setText(ioe.toString());
            }
        }
        return null;
    }
}
