package com.aitek.app.mms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aitek.app.mms.data.Conversation;
import com.google.gson.Gson;

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
public class MmsSmsInputFragment extends Fragment {
    private Conversation conversation;
    private ViewHolder viewHolder;

    public static MmsSmsInputFragment show(Conversation conversation) {
        MmsSmsInputFragment fragment = new MmsSmsInputFragment();
        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_KEY_EVENT, new Gson().toJson(conversation));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mms_fragment_sms_input, container, false);
    }

    private class ViewHolder implements View.OnClickListener {
        private View view;

        public ViewHolder(View view) {
            this.view = view;
            view.findViewById(R.id.cancel).setOnClickListener(this);
        }

        @Override public void onClick(View v) {
        }
    }
}
