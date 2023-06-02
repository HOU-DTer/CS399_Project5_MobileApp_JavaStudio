package com.am.aucklank;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBase extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tag", "onReceive: ");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //Self-starting app, with the parameters being the name of the application package to be launched automatically(自启动APP，参数为需要自动启动的应用包名)
            Log.d("tag", "onReceive: ");
            Intent newIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }

    }
}
