package com.example.mines;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout baseLayout, childLayout;
    Button button,button2;
    Tag tag;
    int buttonId = 400;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch ( event.getAction() ) {
//            case MotionEvent.ACTION_DOWN :
//                touchedOnScreenX = event.getX();
//                touchedOnScreenY = event.getY();
//                refreshButtonPosition ();
//                return true;
//        }
//        return super.onTouchEvent(event);
//    }
//
//    private void refreshButtonPosition() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setFlags (
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);
        //setContentView(new Game(this));

        initializeButtons();

        modifyLayouts();

        doSomethingMore ();

    }

    private void doSomethingMore() {
        button2 = baseLayout.findViewWithTag(324);
        button2.setWidth(100);
    }

    private void initializeButtons() {
        button = new Button(getApplicationContext());
        button.setX(100);
        button.setY(100);
        button.setWidth(300);
        button.setHeight(200);
        button.setText("BTN");
        button.setTag(324);
    }

    private void modifyLayouts() {

        baseLayout = (ConstraintLayout)findViewById(R.id.main_layout);
        childLayout = new ConstraintLayout(getApplicationContext());
        baseLayout.addView(childLayout);
        baseLayout.addView(button);
    }

}