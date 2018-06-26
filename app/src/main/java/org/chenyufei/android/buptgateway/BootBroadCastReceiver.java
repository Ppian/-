package org.chenyufei.android.buptgateway;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by yfchen on 2018/6/26.
 */

public class BootBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, NetworkStateService.class);
        context.startService(service);
    }
}
