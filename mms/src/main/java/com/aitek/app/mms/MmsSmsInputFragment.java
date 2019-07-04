package com.aitek.app.mms;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aitek.app.mms.data.Conversation;
import com.google.gson.Gson;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * @ProjectName: mms
 * @ClassName: MmsSmsInputFragment
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-1 下午3:09
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-1 下午3:09
 * @UpdateRemark: 更新说明
 */
public class MmsSmsInputFragment extends MmsBaseFragment implements ASREngine.Callback {
    private Conversation conversation;
    private ViewHolder viewHolder;
    private String message = null;

    public static MmsSmsInputFragment show(Conversation conversation) {
        MmsSmsInputFragment fragment = new MmsSmsInputFragment();
        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_KEY_EVENT, new Gson().toJson(conversation));
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversation = new Gson().fromJson(getArguments().getString(Intent.EXTRA_KEY_EVENT),
            Conversation.class);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mms_fragment_sms_input, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewHolder = new ViewHolder(view);
        try {
            DDS.getInstance().getAgent().getASREngine().disableVad();
            DDS.getInstance().getAgent().getASREngine().startListening(this);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        try {
            DDS.getInstance().getAgent().getASREngine().stopListening();
            DDS.getInstance().getAgent().getASREngine().enableVad();
        } catch (DDSNotInitCompleteException e) {
        }
    }

    @Override public void beginningOfSpeech() {
    }

    @Override public void endOfSpeech() {
    }

    @Override public void bufferReceived(byte[] bytes) {
    }

    @Override public void partialResults(String s) {
        //{"recordId":"f7523b81999164ede9b943939de29152","var":"唉","eof":0,"sessionId":"727e4c3167aaad215ec59e9ac59e61f4"}
        if (!isAdded()) return;
        try {
            JSONObject json = new JSONObject(s);
            final String text = json.optString("var");
            if (!TextUtils.isEmpty(text)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        viewHolder.inputSms.setText(text);
                        viewHolder.inputSms.setSelection(text.length());
                    }
                });
            }
        } catch (Exception e) {
            Log.d("sms", s, e);
        }
    }

    @Override public void finalResults(String s) {
    }

    @Override public void error(String s) {
    }

    @Override public void rmsChanged(float v) {
    }

    private void sendMessage() {
        if (null != message) return;
        if (viewHolder.inputSms.length() == 0) return;
        message = viewHolder.inputSms.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> list = smsManager.divideMessage(message);
        ArrayList<PendingIntent> sentIntents = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Uri uri =
                SmsSendUtil.sendMessage(getActivity().getContentResolver(), conversation.address,
                    list.get(i), Long.parseLong(conversation.threadId));
            Intent intent =
                new Intent(SmsSentReceiver.ACTION, uri, getActivity(), SmsSentReceiver.class);
            sentIntents.add(PendingIntent.getBroadcast(getActivity(), i, intent, 0));
        }
        smsManager.sendMultipartTextMessage(conversation.address, null, list, sentIntents, null);
        getFragmentManager().popBackStack();
    }

    private class ViewHolder implements View.OnClickListener {
        private View view;
        private EditText inputSms;

        public ViewHolder(View view) {
            this.view = view;
            inputSms = view.findViewById(R.id.mms_input_sms);
            view.findViewById(R.id.cancel).setOnClickListener(this);
            view.findViewById(R.id.send).setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            int id = v.getId();
            if (R.id.cancel == id) {
                getFragmentManager().popBackStack();
            } else if (R.id.send == id) {
                sendMessage();
            }
        }
    }
}
