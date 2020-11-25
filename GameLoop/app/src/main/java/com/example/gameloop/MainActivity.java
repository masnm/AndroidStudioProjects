package com.example.gameloop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Removing android notification panel
        Window window = getWindow();
        window.setFlags (
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        //setContentView(R.layout.activity_main);
        // set the content view to game class
        setContentView(new Game(this));

    }
}