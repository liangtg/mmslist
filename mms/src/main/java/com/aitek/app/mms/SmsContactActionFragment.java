package com.aitek.app.mms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aitek.app.mms.data.Conversation;
import com.google.gson.Gson;

/**
 * @ProjectName: mms
 * @ClassName: SmsContactActionFragment
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-1 上午11:11
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-1 上午11:11
 * @UpdateRemark: 更新说明
 */
public class SmsContactActionFragment extends Fragment {
    private Conversation conversation;
    private ViewHolder viewHolder;

    public static SmsContactActionFragment show(Conversation conversation) {
        SmsContactActionFragment fragment = new SmsContactActionFragment();
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
        return inflater.inflate(R.layout.mms_fragment_conversation_contact, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewHolder = new ViewHolder(view);
        viewHolder.address.setText(conversation.address);
    }

    private class ViewHolder implements View.OnClickListener {
        private View container;
        private TextView address;

        public ViewHolder(View container) {
            this.container = container;
            address = container.findViewById(R.id.address);
            container.findViewById(R.id.call).setOnClickListener(this);
            container.findViewById(R.id.message).setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            int id = v.getId();
            if (R.id.call == id) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.fromParts("tel", conversation.address, null));
                startActivity(intent);
            } else if (R.id.message == id) {
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction()
                    .add(R.id.conversation_container, ConversationDetailFragment.show(conversation))
                    .addToBackStack("conversation_detail")
                    .commit();
            }
        }
    }
}
