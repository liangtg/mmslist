package com.aitek.app.mms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @ProjectName: mms
 * @ClassName: SmsService
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-6-27 下午4:46
 * @UpdateUser: 更新者
 * @UpdateDate: 19-6-27 下午4:46
 * @UpdateRemark: 更新说明
 */
public class SmsService extends Service {
    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }
}
