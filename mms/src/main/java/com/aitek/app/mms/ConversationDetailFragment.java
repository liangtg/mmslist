package com.aitek.app.mms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.aitek.app.mms.data.Conversation;

public class ConversationDetailFragment extends Fragment {

    private Uri uri = Uri.parse("content://sms/");

    private String[] projection = new String[]{"_id", "address", "person", "body", "type", "date"};

    public static ConversationDetailFragment show(Conversation conversation) {
        ConversationDetailFragment fragment = new ConversationDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    private class ViewHolder {
        private View view;
        private ListView listView;

        public ViewHolder(View view) {
            this.view = view;
            listView = view.findViewById(R.id.listview);
//            listView.setAdapter(new DetailAdapter(getContext(),getActivity().getContentResolver().query(uri,projection,"thread_id=?",new String[]{},"date DESC")));
        }
    }

    private class DetailAdapter extends CursorAdapter {
        public DetailAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }


}
