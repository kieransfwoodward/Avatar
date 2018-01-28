package com.kieranwoodward.Avatar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.kieranwoodward.gizmato.R;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kieranwoodward on 02/10/2017.
 */

public class Avatar extends Service {

    Context context;
    public WindowManager wm;
    ImageView img;
    boolean doubleclick = false;
    int i = 0;
    WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
            100, 100, WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    double lastx;
    double lasty;
    TextView description;
    TextView title;
    SharedPreferences sp;
    private float x1, x2, x3, y3;
    Activity activity;
    private static final String PREFERENCE_NAME = "MyPreferenceFileName";
    View myview;
    LayoutInflater li;
    float transparency;
    String option;
    String option1 = "monster";
    AppPreferences appPreferences;
    int checked;
    int state1 = 1;
    Intent intent;
    int[] images;
    boolean vis = true;
    String option2;

    public Avatar(){

    }

    public void OnCreate(){
        startWindow();
    }





    public Avatar(Context con, int[] imgs){
        context = con;
        images = imgs;
        appPreferences = new AppPreferences(context);
        transparency = appPreferences.getFloat("transparency", 0.6f);
        option = appPreferences.getString("option", "New Test");
        option1 = appPreferences.getString("option1", "monster");
        checked = appPreferences.getInt("checked", 0);
        state1 = appPreferences.getInt("state", 1);
        option2 = appPreferences.getString("option2", "Medium");


        //Check version of Android, if 23 or less ask for overlay permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(context)) {
                startWindow();  //start the floating window
            } else {
                //ask for the required permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Gismato avatar_settings").setMessage("This app needs to draw over other apps.") //instructions to user
                        .setPositiveButton("Go to avatar_settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));
                                activity.startActivityForResult(intent, 1234);
                            }
                        }).create().show();

                if (Settings.canDrawOverlays(this)) { //check to see if the permission has been granted
                    startWindow();  //start the floating window
                }
            }
        } else {
            startWindow(); //if Android version is below 23 the permission is not required
        }

    }

    public void setTapShortcut(Intent i){
        intent = i;
    }

    public void setImages(){



    }

    public void setState(int i){
        state1 = i;
        if (state1 > (images.length - 1)){
        }
        else{
            setIMG(img);
        }


    }

    public void displayScreen(String layout, int width, int height){
        wm.removeView(myview);
        doubleclick = true;
        myview = li.inflate(
                context.getResources().getIdentifier(
                        layout,
                        "layout",
                        context.getPackageName()),
                null); //new view to display the message
        img = new ImageView(context);

        LinearLayout picLL = new LinearLayout(context);
        picLL.setOrientation(LinearLayout.VERTICAL);
        picLL.addView(img);
        ((ViewGroup) myview).addView(picLL);

        setIMG(img);
        updateIMG(img, myview, parameters);

        if(appPreferences.getString("option2", "Medium").equals("Medium") ){
            height = height + context.getResources().getDimensionPixelSize(R.dimen.fifty);

        }
        else if(appPreferences.getString("option2", "Medium").equals("Small") ){

            height = height +  context.getResources().getDimensionPixelSize(R.dimen.thirty);

        }
        else if(appPreferences.getString("option2", "Medium").equals("Large")){
            height = height +  context.getResources().getDimensionPixelSize(R.dimen.seventy);


        }

        parameters = new WindowManager.LayoutParams(
                width,
                height,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.x = (int) lastx;
        parameters.y = (int) lasty;

        updateIMG(img, myview, parameters);
        wm.addView(myview, parameters);

    }




    private void startWindow() { //start the floating window

        wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        li = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        myview = li.inflate(R.layout.avatar, null); //View only showing the avatar
        img = (ImageView) myview.findViewById(R.id.avatarView); //ImageView within this layout

        if(appPreferences.getString("option2", "Medium").equals("Medium") )
        {
            parameters = new WindowManager.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.fifty),  context.getResources().getDimensionPixelSize(R.dimen.fifty), WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);


        }
        else if(appPreferences.getString("option2", "Medium").equals("Small") ){
            parameters = new WindowManager.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.thirty),  context.getResources().getDimensionPixelSize(R.dimen.thirty), WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        else if(appPreferences.getString("option2", "Medium").equals("Large") ){
            parameters = new WindowManager.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.seventy),  context.getResources().getDimensionPixelSize(R.dimen.seventy), WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        parameters.gravity = Gravity.TOP | Gravity.LEFT;
        parameters.x = 0;
        parameters.y = 0;

        if(appPreferences.getString("option2", "Medium").equals("Medium") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.fifty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.fifty);

        }
        else if(appPreferences.getString("option2", "Medium").equals("Small") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.thirty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.thirty);


        }
        else if(appPreferences.getString("option2", "Medium").equals("Large")){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.seventy);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.seventy);


        }

        setIMG(img); //set the current avatar into the image view
        myview.animate().alpha(transparency); //set the transparency chose by the user
        wm.addView(myview, parameters); //add the view into the window
        updateIMG(img, myview, parameters); //Allow the avatar to be clickable and movable

    }

    //Method to set the correct avatar icon
    private void setIMG(ImageView img) {
        if(appPreferences.getString("option2", "Medium").equals("Medium") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.fifty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.fifty);

        }
        else if(appPreferences.getString("option2", "Medium").equals("Small") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.thirty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.thirty);


        }
        else if(appPreferences.getString("option2", "Medium").equals("Large")){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.seventy);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.seventy);


        }
        Drawable image = context.getResources().getDrawable(images[state1]);
        img.setImageDrawable(image);
    }

    public void hide(){
        if (vis == true){
            wm.removeView(myview);
        }
        vis = false;
    }

    public void show(){
        if (vis == true){
            wm.removeView(myview);
        }

        vis = true;


        setIMG(img); //set the current avatar into the image view
        myview.animate().alpha(transparency);
        wm.addView(myview, parameters); //add the view into the window
        updateIMG(img, myview, parameters); //Allow the avatar to be clickable and movable
    }

    //Method to make the avatar image clickable and movable
    private void updateIMG(ImageView img, final View myview, final WindowManager.LayoutParams parameters) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                WindowManager.LayoutParams parameters1 = parameters;
                double x1 = parameters1.x;
                double y1 = parameters1.y;


                if (i == 1) {
                    //Single click


                    handler.postDelayed(r, 350);


                    if (x1 > x3) {
                        if (y1 > y3) {
                            if ((x1 - x3 < 15) & (y1 - y3 < 15)) { //15 is the number of pixels the x and y cordinates can change but stil be consideres a long touch not a drag
                                if (option.equals("New Test")) { //intent to New Test screen

                                    context.startActivity(intent);
                                }
                                doubleclick = true;
                            }
                        } else {
                            if ((x1 - x3 < 15) & (y3 - y1 < 15)) {
                                if (option.equals("New Test")) { //intent to New Test screen

                                    context.startActivity(intent);
                                }
                                doubleclick = true;
                            }
                        }
                    } else {
                        if (y1 > y3) {
                            if ((x3 - x1 < 15) & (y1 - y3 < 15)) {
                                if (option.equals("New Test")) { //intent to New Test screen

                                    context.startActivity(intent);
                                }
                                doubleclick = true;
                            }
                        } else {
                            if ((x3 - x1 < 15) & (y3 - y1 < 15)) {
                                if (option.equals("New Test")) { //intent to New Test screen

                                    context.startActivity(intent);
                                }
                                doubleclick = true;
                            }
                        }
                    }
                } else if (i == 2) {
                    i = 0;
                    // hide();
                }
            }
        });

        //create on touch listener on the image so the icon can be moved around the screen
        img.setOnTouchListener(new View.OnTouchListener() {
            double x;
            double y;
            double pressedX;
            double pressedY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sp = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
                WindowManager.LayoutParams parameters1 = parameters;


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = parameters1.x;
                        y = parameters1.y;

                        x3 = parameters1.x;
                        y3 = parameters1.y;

                        x1 = event.getX();
                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (doubleclick) {

                            updateIcon(state1, wm, myview);
                            doubleclick = false;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //Move the window
                        parameters1.x = (int) (x + (event.getRawX() - pressedX));
                        parameters1.y = (int) (y + (event.getRawY() - pressedY));

                        lasty = y + (event.getRawY() - pressedY);
                        lastx = x + (event.getRawX() - pressedX);
                        wm.updateViewLayout(myview, parameters1);


                    default:
                        break;

                }
                return false;
            }

        });

        img.setOnLongClickListener(new View.OnLongClickListener() { //long click listener to open menu
            @Override
            public boolean onLongClick(View view) {
                WindowManager.LayoutParams parameters1 = parameters;
                double x1 = parameters1.x;
                double y1 = parameters1.y;

                if (x1 > x3) {
                    if (y1 > y3) {
                        if ((x1 - x3 < 15) & (y1 - y3 < 15)) { //15 is the number of pixels the x and y cordinates can change but stil be consideres a long touch not a drag
                            wm.removeView(myview);
                            showSettings();
                        }
                    } else {
                        if ((x1 - x3 < 15) & (y3 - y1 < 15)) {
                            wm.removeView(myview);
                            showSettings();
                        }
                    }
                } else {
                    if (y1 > y3) {
                        if ((x3 - x1 < 15) & (y1 - y3 < 15)) {
                            wm.removeView(myview);
                            showSettings();
                        }
                    } else {
                        if ((x3 - x1 < 15) & (y3 - y1 < 15)) {
                            wm.removeView(myview);
                            showSettings();
                        }
                    }
                }
                return false;
            }
        });
    }

    //Updates the icon to different states
    private void updateIcon(int state, WindowManager wm1, View myview1) {

        if(appPreferences.getString("option2", "Medium").equals("Medium") )
        {
            parameters = new WindowManager.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.fifty),  context.getResources().getDimensionPixelSize(R.dimen.fifty), WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        else if(appPreferences.getString("option2", "Medium").equals("Small") ){
            parameters = new WindowManager.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.thirty),  context.getResources().getDimensionPixelSize(R.dimen.thirty), WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        else if(appPreferences.getString("option2", "Medium").equals("Large") ){
            parameters = new WindowManager.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.seventy),  context.getResources().getDimensionPixelSize(R.dimen.seventy), WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }


        //sets the avatar window to be smaller to just show the avatar
        parameters.x = (int) lastx; //positions the avatar where it last was
        parameters.y = (int) lasty; //positions the avatar where it last was
        state1 = state;
        appPreferences.put("state", state);
        myview = li.inflate(R.layout.avatar, null); //sets layout to be just the avatar
        img = (ImageView) myview.findViewById(R.id.avatarView);


        if(appPreferences.getString("option2", "Medium").equals("Medium") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.fifty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.fifty);

        }
        else if(appPreferences.getString("option2", "Medium").equals("Small") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.thirty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.thirty);


        }
        else if(appPreferences.getString("option2", "Medium").equals("Large")){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.seventy);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.seventy);


        }

        Drawable image = context.getResources().getDrawable(images[state1]);
        img.setImageDrawable(image);
        updateIMG(img, myview, parameters);
        myview.animate().alpha(transparency);
        wm1.removeView(myview1);
        wm.addView(myview, parameters);

    }



    //show a message above the avatar
    public void talk(String titleText, String message) {

        if (appPreferences.getInt("checked", 0) == 0) { //if showing alerts
            if (appPreferences.getInt("checked", 0) == 0) { //if showing alerts
                show();
                doubleclick = true;
                wm.removeView(myview);
                myview = li.inflate(R.layout.text, null); //new view to display the message
                img = (ImageView) myview.findViewById(R.id.avatarView);


                if (appPreferences.getString("option2", "Medium").equals("Medium")) {
                    img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.fifty);
                    img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.fifty);

                } else if (appPreferences.getString("option2", "Medium").equals("Small")) {
                    img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.thirty);
                    img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.thirty);


                } else if (appPreferences.getString("option2", "Medium").equals("Large")) {
                    img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.seventy);
                    img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.seventy);


                }

                setIMG(img);
                updateIMG(img, myview, parameters);

                //Set the title
                title = (TextView) myview.findViewById(R.id.title);
                title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(titleText);
                title.setTextColor(Color.WHITE);
                title.setBackgroundResource(R.drawable.top);
                title.setTextSize(20);
                title.setGravity(10);

                //Set the description
                description = (TextView) myview.findViewById(R.id.description);
                description.setText(message);
                description.setBackgroundResource(R.drawable.bottom);

                //Make the size of the window larger to fit the text
                parameters = new WindowManager.LayoutParams(
                        context.getResources().getDimensionPixelSize(R.dimen.twoTen),
                        context.getResources().getDimensionPixelSize(R.dimen.two),
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                parameters.x = (int) lastx;
                parameters.y = (int) lasty;

                updateIMG(img, myview, parameters);
                wm.addView(myview, parameters);
            }
        }
    }


    //Method to display the avatar avatar_settings
    private void showSettings() {
        parameters = new WindowManager.LayoutParams(
                context.getResources().getDimensionPixelSize(R.dimen.twoTen), context.getResources().getDimensionPixelSize(R.dimen.four), WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.x = (int) lastx;
        parameters.y = (int) lasty;
        doubleclick = true;
        myview = li.inflate(R.layout.avatar_settings, null); //display the avatar_settings view
        img = (ImageView) myview.findViewById(R.id.avatarView);

        if(appPreferences.getString("option2", "Medium").equals("Medium") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.fifty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.fifty);

        }
        else if(appPreferences.getString("option2", "Medium").equals("Small") ){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.thirty);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.thirty);


        }
        else if(appPreferences.getString("option2", "Medium").equals("Large")){
            img.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.seventy);
            img.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.seventy);


        }

        setIMG(img);
        updateIMG(img, myview, parameters);


        Spinner spinner2 = (Spinner) myview.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2;
        List<String> list2;

        //Add avatar options
        list2 = new ArrayList<>();
        list2.add("Small");
        list2.add("Medium");
        list2.add("Large");

        adapter2 = new ArrayAdapter<String>(context, R.layout.spinner_item, list2);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(adapter2);

        int spinnerPosition2 = adapter2.getPosition(option2);
        spinner2.setSelection(spinnerPosition2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                option2 = item.toString();
                appPreferences.put("option2", option2); //store chosen option

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        //Seekbar to adjust the transparency of the icon
        SeekBar seekBar = (SeekBar) myview.findViewById(R.id.seekbar);
        float progress = transparency * 10;
        int progress1 = (int) progress;
        progress1 = progress1 - 1;
        seekBar.setProgress(progress1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                float progress1 = (float) progress + 1;
                progress1 = progress1 / 10;
                transparency = progress1;
                appPreferences.put("transparency", progress1); //store the transparency
                img.animate().alpha(transparency); // adjust the icon to the new transparency
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Checkbox to silence alerts from the avatar
        CheckBox ChkBx = (CheckBox) myview.findViewById(R.id.checkbox);
        if (checked == 0) {
            appPreferences.put("checked", 0);
            ChkBx.setChecked(false); //display current choice
        } else {
            appPreferences.put("checked", 1);
            ChkBx.setChecked(true); //display current choice
        }

        //On check box changed listener
        ChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = 1;
                    appPreferences.put("checked", checked); //store new choice
                } else {
                    checked = 0;
                    appPreferences.put("checked", checked); //store new choice
                }
            }
        });

        wm.addView(myview, parameters); //display avatar_settings in the window

        Button hideButton = (Button) myview.findViewById(R.id.button4);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vis == true){
                    updateIcon(state1, wm, myview);
                    wm.removeView(myview);

                }
                vis = false;
            }
        });
    }

    //restarts the icon even when the app is closed
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //restarts the avatar quicker once the app has been forced closed from recent apps
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);
    }



    @Override
    public void onDestroy() {
        wm.removeViewImmediate(myview);
        super.onDestroy();
    }






}