package com.aispeech.dui.dds.agent;

import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import java.util.Random;
import org.json.JSONObject;

/**
 * @ProjectName: mms
 * @ClassName: ASREngine
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-4 下午2:45
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-4 下午2:45
 * @UpdateRemark: 更新说明
 */
public class ASREngine {

    public void disableVad() throws DDSNotInitCompleteException {
    }

    public void startListening(final Callback callback) throws DDSNotInitCompleteException {
        new Thread() {
            @Override public void run() {
                StringBuffer sb = new StringBuffer();
                JSONObject obj = new JSONObject();
                try {
                    for (int i = 0; i < 30; i++) {
                        sb.append("测试消息" + new Random().nextInt(99) + ",");
                        obj.put("var", sb.toString());
                        callback.partialResults(obj.toString());
                        Thread.sleep(300);
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public void stopListening() throws DDSNotInitCompleteException {
    }

    public void enableVad() throws DDSNotInitCompleteException {
    }

    public interface Callback {
        void beginningOfSpeech();

        void endOfSpeech();

        void bufferReceived(byte[] var1);

        void partialResults(String var1);

        void finalResults(String var1);

        void error(String var1);

        void rmsChanged(float var1);
    }
}
