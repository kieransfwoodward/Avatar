package com.kieranwoodward.GismatoWithoutScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kieranwoodward.gizmato.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent();
        i.setClass(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

         int[] imgarr = {
                R.drawable.gismato,
                R.drawable.gismato_2,
                R.drawable.gismato_3,
                 R.drawable.gismato_4,
                 R.drawable.gismato_5
        };


        Avatar win = new Avatar(getApplicationContext(), imgarr);

        win.setDoubleTapShortcut(i);
        win.hide();
        win.show();
        win.talk("hello", "boo");
        win.displayScreen("settings");
        win.setState(1);
    }




}