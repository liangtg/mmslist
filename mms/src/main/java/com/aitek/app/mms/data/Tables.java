package com.aitek.app.mms.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Telephony;

/**
 * @ProjectName: mms
 * @ClassName: Tables
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-6-10 下午4:51
 * @UpdateUser: 更新者
 * @UpdateDate: 19-6-10 下午4:51
 * @UpdateRemark: 更新说明
 */
public class Tables {
    public static class ConversationList implements BaseColumns, Telephony.TextBasedSmsColumns {

        public static final Uri MMSSMS_FULL_CONVERSATION_URI =
            Uri.parse("content://mms-sms/conversations");
        public static final Uri CONVERSATION_URI =
            MMSSMS_FULL_CONVERSATION_URI.buildUpon().appendQueryParameter("simple", "true").build();
        static final String[] ALL_THREADS_PROJECTION = {
            "_id", "date", "message_count", "recipient_ids", "snippet", "snippet_cs", "read",
            "error",
            "has_attachment"
        };
        static final int ID = 0;
        static final int DATE = 1;
        static final int MESSAGE_COUNT = 2;
        static final int RECIPIENT_IDS = 3;
        static final int SNIPPET = 4;
        static final int SNIPPET_CS = 5;
        static final int READ = 6;
        static final int ERROR = 7;
        static final int HAS_ATTACHMENT = 8;
    }
}
