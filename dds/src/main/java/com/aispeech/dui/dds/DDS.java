package com.aispeech.dui.dds;

import com.aispeech.dui.dds.agent.Agent;

/**
 * @ProjectName: mms
 * @ClassName: DDS
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-4 下午2:46
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-4 下午2:46
 * @UpdateRemark: 更新说明
 */
public class DDS {
    private static DDS instance = new DDS();
    private Agent agent = new Agent();

    public static DDS getInstance() {
        return instance;
    }

    public Agent getAgent() {
        return agent;
    }
}
