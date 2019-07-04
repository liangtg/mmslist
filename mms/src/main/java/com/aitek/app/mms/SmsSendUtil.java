package com.aitek.app.mms;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.Telephony;

import static android.provider.Telephony.TextBasedSmsColumns.ADDRESS;
import static android.provider.Telephony.TextBasedSmsColumns.BODY;
import static android.provider.Telephony.TextBasedSmsColumns.DATE;
import static android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT;
import static android.provider.Telephony.TextBasedSmsColumns.READ;
import static android.provider.Telephony.TextBasedSmsColumns.STATUS;
import static android.provider.Telephony.TextBasedSmsColumns.STATUS_NONE;
import static android.provider.Telephony.TextBasedSmsColumns.THREAD_ID;
import static android.provider.Telephony.TextBasedSmsColumns.TYPE;

/**
 * @ProjectName: SVA
 * @ClassName: SmsSendUtil
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-3 下午5:45
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-3 下午5:45
 * @UpdateRemark: 更新说明
 */
public class SmsSendUtil {
    public static Uri sendMessage(ContentResolver resolver, String address, String message,
        long threadId) {
        ContentValues values = new ContentValues(8);
        values.put(TYPE, MESSAGE_TYPE_SENT);
        values.put(ADDRESS, address);
        values.put(DATE, System.currentTimeMillis());
        values.put(READ, 1);
        values.put(BODY, message);
        if (threadId != -1L) {
            values.put(THREAD_ID, threadId);
        }
        values.put(STATUS, STATUS_NONE);
        return resolver.insert(Telephony.Sms.Sent.CONTENT_URI, values);
    }
}
