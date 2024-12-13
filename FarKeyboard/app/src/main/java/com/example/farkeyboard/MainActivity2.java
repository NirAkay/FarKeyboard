package com.example.farkeyboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity2 extends Activity {

    EditText myEditText;
    TextView txt, txtIp;
    DataTransfer dataTransfer;
    AutoCompleteTextView editIp;
    boolean twoFingers = false, startSelect = false;
    float currentX = 0, currentY = 0;
    String ip,suggestIp;
    int wasInMove = 0, delay = 0, select = 0;
    String[] arrayIp = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        myEditText = findViewById(R.id.textsend);
        txt = findViewById(R.id.show);
        txtIp  = findViewById(R.id.myIp);
        SharedPreferences sh = getSharedPreferences("MyData", MODE_PRIVATE);
        ip = sh.getString("ip", "0.0.0.0");
        suggestIp = sh.getString("ips", "");
        if (!suggestIp.equals("")) {
            arrayIp = suggestIp.split("\n");
        }
        txtIp.setText(ip);
        editIp = findViewById(R.id.getIp);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arrayIp);
        editIp.setAdapter(adapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            twoFingers = true;
        }
        switch (event.getActionMasked()) {
            case (MotionEvent.ACTION_DOWN):
                currentX = event.getX();
                currentY = event.getY();
                return true;
            case (MotionEvent.ACTION_MOVE):
                if (!"0.0*0.0".equals((currentX - event.getX()) +
                        "*" + (currentY - event.getY()))) {
                    if (twoFingers) {
                        dataTransfer = new DataTransfer("wheel" +
                                (event.getY() - currentY), txt, ip);
                    } else {
                        dataTransfer = new DataTransfer("move" + (currentX - event.getX())
                                + "*" + (currentY - event.getY()), txt, ip);
                    }
                    currentX = event.getX();
                    currentY = event.getY();
                    dataTransfer.execute();
                }
                wasInMove++;
                return true;
            case (MotionEvent.ACTION_UP):
                if (startSelect) {
                    dataTransfer = new DataTransfer("END_SELECT", txt, ip);
                    dataTransfer.execute();
                    startSelect = false;
                }
                if (twoFingers && wasInMove < 6) {
                    dataTransfer = new DataTransfer("Right_Click", txt, ip);
                    dataTransfer.execute();
                } else {
                    if (wasInMove < 6) {
                        select++;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (select == 2) {
                                    if (!startSelect) {
                                        dataTransfer = new DataTransfer("SELECT", txt, ip);
                                        dataTransfer.execute();
                                        startSelect = true;
                                    }
                                } else {
                                    if (!startSelect) {
                                        dataTransfer = new DataTransfer("Left_Click", txt, ip);
                                        dataTransfer.execute();
                                    }
                                }
                                select = 0;
                            }
                        }, 200);
                    }
                }
                twoFingers = false;
                wasInMove = 0;
                return true;
                default:
                    return super.onTouchEvent(event);
        }
    }

    public void sendButton(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return makesDelay(motionEvent, "key:" + myEditText.getText().toString());
            }
        });
        myEditText.setText("");
    }

    public void backButton(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return makesDelay(motionEvent, "BACK");
            }
        });
    }

    public void changeIp(View view) {
        boolean newIp = true;
        String newIps = editIp.getText().toString();
        if (!newIps.equals("")) {
            SharedPreferences sh =
                    getSharedPreferences("MyData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("ip", newIps);
            for (int i = 0; i < arrayIp.length; i++) {
                if (arrayIp[i].equals(newIps)) {
                    newIp = false;
                }
            }
            if (newIp) {
                if (sh.getString("ips", "").equals("")) {
                    arrayIp = new String[1];
                    arrayIp[0] = newIps;
                } else {
                    for (int i = 0; i < 5 && i < arrayIp.length; i++) {
                        newIps += "\n" + arrayIp[i];
                    }
                    arrayIp = newIps.split("\n");
                }
                editor.putString("ips", newIps);
            }
            editor.apply();
            txtIp.setText(editIp.getText());
            ip = editIp.getText().toString();
            editIp.setText("");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, arrayIp);
            editIp.setAdapter(adapter);
        }
    }

    public boolean makesDelay(MotionEvent motionEvent, String info) {
        delay++;
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
            dataTransfer = new DataTransfer(info, txt, ip);
            dataTransfer.execute();
            delay = 0;
            myEditText.setText("");
            return true;
        }
        if (delay > 6 && (delay % 2 == 0 || delay % 3 == 0) && motionEvent.getActionMasked() != MotionEvent.ACTION_UP) {
            dataTransfer = new DataTransfer(info, txt, ip);
            dataTransfer.execute();
        }
        return true;
    }

    public void up(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return makesDelay(motionEvent, "UP");
            }
        });
    }

    public void down(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return makesDelay(motionEvent, "DOWN");
            }
        });
    }

    public void right(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return makesDelay(motionEvent, "RIGHT");
            }
        });
    }

    public void left(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return makesDelay(motionEvent, "LEFT");
            }
        });
    }

    public void ctrlF(View view) {
        dataTransfer = new DataTransfer("CTRL_F", txt, ip);
        dataTransfer.execute();
    }

    public void ctrlC(View view) {
        dataTransfer = new DataTransfer("CTRL_C", txt, ip);
        dataTransfer.execute();
    }

    public void ctrlX(View view) {
        dataTransfer = new DataTransfer("CTRL_X", txt, ip);
        dataTransfer.execute();
    }

    public void ctrlV(View view) {
        dataTransfer = new DataTransfer("CTRL_V", txt, ip);
        dataTransfer.execute();
    }

    public void ctrlA(View view) {
        dataTransfer = new DataTransfer("CTRL_A", txt, ip);
        dataTransfer.execute();
    }

    public void ctrlZ(View view) {
        dataTransfer = new DataTransfer("CTRL_Z", txt, ip);
        dataTransfer.execute();
    }

    public void altTab(View view) {
        dataTransfer = new DataTransfer("ALT_TAB", txt, ip);
        dataTransfer.execute();
    }

    public void del(View view) {
        dataTransfer = new DataTransfer("DELETE", txt, ip);
        dataTransfer.execute();
    }

    public void esc(View view) {
        dataTransfer = new DataTransfer("ESC", txt, ip);
        dataTransfer.execute();
    }

    public void windows(View view) {
        dataTransfer = new DataTransfer("WINDOWS", txt, ip);
        dataTransfer.execute();
    }
}