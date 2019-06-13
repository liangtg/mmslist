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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aitek.app.mms.data.Conversation;
import com.aitek.app.mms.data.Tables;

public class ConversationDetailFragment extends Fragment {

    private Uri uri;

    private String[] projection = new String[]{"_id", "address", "person", "body", "type", "date"};
    private ViewHolder viewHolder;

    public static ConversationDetailFragment show(Conversation conversation) {
        ConversationDetailFragment fragment = new ConversationDetailFragment();
        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_KEY_EVENT, conversation.threadId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uri = Tables.ConversationList.MMSSMS_FULL_CONVERSATION_URI.buildUpon().appendPath(getArguments().getString(Intent.EXTRA_KEY_EVENT)).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mms_fragment_conversation_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewHolder = new ViewHolder(view);
    }

    private class ViewHolder {
        private View view;
        private ListView listView;

        public ViewHolder(View view) {
            this.view = view;
            listView = view.findViewById(R.id.listview);
            listView.setAdapter(new DetailAdapter(getContext(), getActivity().getContentResolver().query(uri, projection, null, null, "date DESC")));
        }
    }

    private class DetailAdapter extends CursorAdapter {
        public DetailAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mms_item_conversation_detail, parent, false);
            AdapterViewholder viewholder = new AdapterViewholder(view);
            view.setTag(viewholder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            AdapterViewholder viewholder = (AdapterViewholder) view.getTag();
            int type = cursor.getInt(4);
            int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
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
            viewholder.itemText.setText(cursor.getString(3));
            viewholder.itemDate.setText(TimeUtil.getChatTimeStr(cursor.getLong(5) / 1000));
        }
    }

    private class AdapterViewholder {
        private View view;
        private TextView itemDate;
        private TextView itemText;
        private LinearLayout itemTextContainer;

        public AdapterViewholder(View view) {
            this.view = view;
            itemDate = view.findViewById(R.id.item_date);
            itemText = view.findViewById(R.id.item_text);
            itemTextContainer = view.findViewById(R.id.item_text_container);
        }
    }
}
