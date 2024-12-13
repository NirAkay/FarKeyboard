package com.example.farkeyboard;

import android.app.Activity;
import android.view.MotionEvent;

public class MoveTraction extends Activity {
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return super.onTouchEvent(event);
    }
}
