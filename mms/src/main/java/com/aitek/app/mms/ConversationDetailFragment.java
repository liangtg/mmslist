package com.aitek.app.mms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aitek.app.mms.data.Conversation;
import com.aitek.app.mms.data.Tables;
import com.google.gson.Gson;

public class ConversationDetailFragment extends Fragment {

    private Uri uri;

    private String[] projection =
        new String[] { "_id", "address", "person", "body", "type", "date" };
    private ViewHolder viewHolder;

    private Conversation conversation;

    public static ConversationDetailFragment show(Conversation conversation) {
        ConversationDetailFragment fragment = new ConversationDetailFragment();
        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_KEY_EVENT, new Gson().toJson(conversation));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversation = new Gson().fromJson(getArguments().getString(Intent.EXTRA_KEY_EVENT),
            Conversation.class);
        uri = Tables.ConversationList.MMSSMS_FULL_CONVERSATION_URI.buildUpon()
            .appendPath(conversation.threadId)
            .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mms_fragment_conversation_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewHolder = new ViewHolder(view);
    }

    private class ViewHolder implements View.OnClickListener {
        private View view;
        private RecyclerView listView;

        public ViewHolder(View view) {
            this.view = view;
            listView = view.findViewById(R.id.listview);
            listView.setAdapter(new DetailAdapter(getContext(), getActivity().getContentResolver()
                .query(uri, projection, null, null, "date DESC")));
            view.findViewById(R.id.input_sms_voice).setOnClickListener(this);
            view.findViewById(R.id.input_sms_face).setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            int id = v.getId();
            if (R.id.input_sms_face == id) {
                getFragmentManager().beginTransaction()
                    .add(R.id.conversation_container, MmsSmsInputFragment.show(conversation))
                    .addToBackStack("input_sms")
                    .commit();
            } else if (R.id.input_sms_voice == id) {
                getFragmentManager().beginTransaction()
                    .add(R.id.conversation_container, MmsSmsInputFragment.show(conversation))
                    .addToBackStack("input_sms")
                    .commit();
            }
        }
    }

    private class DetailAdapter extends RecyclerView.Adapter<AdapterViewholder> {
        Cursor cursor;

        public DetailAdapter(Context context, Cursor c) {
            this.cursor = c;
        }

        @NonNull @Override
        public AdapterViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new AdapterViewholder(LayoutInflater.from(getContext())
                .inflate(R.layout.mms_item_conversation_detail, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterViewholder viewholder, int i) {
            cursor.moveToPosition(i);
            viewholder.itemText.setText(cursor.getString(3));
            viewholder.itemDate.setText(TimeUtil.getChatTimeStr(cursor.getLong(5) / 1000));
            int type = cursor.getInt(4);
            int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                getResources().getDisplayMetrics());
            if (Telephony.Sms.MESSAGE_TYPE_INBOX == type) {
                viewholder.itemTextContainer.setPadding(0, 0, pad, 0);
                viewholder.itemText.setBackgroundResource(R.drawable.mms_detail_sender);
                viewholder.itemText.setTextColor(0xFF000000);
                viewholder.itemTextContainer.setGravity(Gravity.LEFT);
            } else {
                viewholder.itemTextContainer.setPadding(pad, 0, 0, 0);
                viewholder.itemTextContainer.setGravity(Gravity.RIGHT);
                viewholder.itemText.setBackgroundResource(R.drawable.mms_detail_receiver);
                viewholder.itemText.setTextColor(0xFFFBFBFB);
            }
        }

        @Override public int getItemCount() {
            return cursor.getCount();
        }
    }

    private class AdapterViewholder extends RecyclerView.ViewHolder {
        private TextView itemDate;
        private TextView itemText;
        private LinearLayout itemTextContainer;

        public AdapterViewholder(View view) {
            super(view);
            itemDate = view.findViewById(R.id.item_date);
            itemText = view.findViewById(R.id.item_text);
            itemTextContainer = view.findViewById(R.id.item_text_container);
        }
    }
}
