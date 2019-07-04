package com.aispeech.dui.dds.agent;

import android.appwidget.AppWidgetHost;

/**
 * @ProjectName: mms
 * @ClassName: Agent
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-4 下午2:47
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-4 下午2:47
 * @UpdateRemark: 更新说明
 */
public class Agent {
    private ASREngine engine = new ASREngine();
    public ASREngine getASREngine() {
        return engine;
    }
}
