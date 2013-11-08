package ru.ifmo.Colloquim_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 08.11.13
 * Time: 0:26
 * To change this template use File | Settings | File Templates.
 */

public class UpdateBroadcastReceiver extends BroadcastReceiver {
    final static public String UpdateAction = "ru.ifmo.action.RssUpdate";
    int a;

    @Override
    public void onReceive(Context context, Intent intent) {
        a = 5;

    }
}

