package com.kieranwoodward.Avatar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kieranwoodward on 03/10/2017.
 */

public class BootReceiver extends BroadcastReceiver {

    //Restarts the avatar once the phone has restarted
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent pushIntent = new Intent(context, Avatar.class);
        context.startService(pushIntent);

    }
}