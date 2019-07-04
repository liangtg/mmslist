package com.aitek.app.mms;

import android.text.TextUtils;
import com.aitek.app.mms.data.Conversation;

/**
 * @ProjectName: SVA
 * @ClassName: UIUtils
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-3 上午10:20
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-3 上午10:20
 * @UpdateRemark: 更新说明
 */
public class UIUtils {
    public static String getTitle(Conversation conversation) {
        if (TextUtils.isEmpty(conversation.person)) {
            return conversation.address;
        }
        return conversation.person;
    }
}
