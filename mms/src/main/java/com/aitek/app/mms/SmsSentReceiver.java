package com.aitek.app.mms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;

public class SmsSentReceiver extends BroadcastReceiver {
    public static final String ACTION = "aitek.sms.action.SMS_SEND";

    @Override
    public void onReceive(Context context, Intent intent) {
        int result = getResultCode();
        ContentValues values = new ContentValues(1);
        if (Activity.RESULT_OK == result) {
            values.put(Telephony.Sms.STATUS, Telephony.Sms.STATUS_COMPLETE);
        } else {
            values.put(Telephony.Sms.STATUS, Telephony.Sms.STATUS_FAILED);
        }
        context.getContentResolver().update(intent.getData(), values, null, null);
    }
}
