package com.aitek.app.mms;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import static android.provider.Telephony.Sms.Inbox.ADDRESS;
import static android.provider.Telephony.Sms.Inbox.BODY;
import static android.provider.Telephony.Sms.Inbox.CONTENT_URI;
import static android.provider.Telephony.Sms.Inbox.DATE;
import static android.provider.Telephony.Sms.Inbox.READ;
import static android.provider.Telephony.Sms.Inbox.SUBJECT;

/**
 * @ProjectName: mms
 * @ClassName: SmsReceiver
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-6-27 下午4:41
 * @UpdateUser: 更新者
 * @UpdateDate: 19-6-27 下午4:41
 * @UpdateRemark: 更新说明
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        abortBroadcast();
        if (!Telephony.Sms.Intents.SMS_DELIVER_ACTION.equals(intent.getAction())) return;
        SmsMessage[] msg = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if (null == msg || msg.length == 0) return;
        for (int i = 0; i < msg.length; i++) {
            SmsMessage item = msg[i];
            ContentValues values = new ContentValues(8);

            //values.put(SUBSCRIPTION_ID, item.gets);
            values.put(ADDRESS, item.getOriginatingAddress());
            values.put(DATE, System.currentTimeMillis());
            values.put(READ, 0);
            values.put(SUBJECT, item.getPseudoSubject());
            values.put(BODY, item.getMessageBody());
            //if (deliveryReport) {
            //    values.put(STATUS, STATUS_PENDING);
            //}
            //if (threadId != -1L) {
            //    values.put(THREAD_ID, threadId);
            //}
            context.getContentResolver().insert(CONTENT_URI, values);
        }
    }
}
