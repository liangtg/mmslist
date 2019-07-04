package com.aitek.app.mms;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aitek.app.mms.data.Conversation;
import com.aitek.app.mms.data.Tables;
import com.google.gson.Gson;

public class ConversationDetailFragment extends MmsBaseFragment {

    private DetailAdapter adapter;
    private Uri uri;
    private String[] projection =
        new String[] { "_id", "address", "person", "body", "type", "date" };
    private ViewHolder viewHolder;
    private Conversation conversation;
    private QueryHandler queryHandler;
    private ContentObserver observer = new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override public void onChange(boolean selfChange) {
            query();
        }
    };

    public static ConversationDetailFragment show(Conversation conversation) {
        ConversationDetailFragment fragment = new ConversationDetailFragment();
        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_KEY_EVENT, new Gson().toJson(conversation));
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MmsTitleController.getTitleView().setBackTitle(UIUtils.getTitle(conversation));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();
        queryHandler = new QueryHandler(resolver);
        conversation = new Gson().fromJson(getArguments().getString(Intent.EXTRA_KEY_EVENT),
            Conversation.class);
        uri = Tables.ConversationList.MMSSMS_FULL_CONVERSATION_URI.buildUpon()
            .appendPath(conversation.threadId)
            .build();
        resolver.registerContentObserver(uri, false, observer);
        query();
    }

    private void query() {
        queryHandler.startQuery(0, null, uri, projection, null, null, "date DESC");
    }

    @Override public void onDestroy() {
        super.onDestroy();
        getActivity().getContentResolver().unregisterContentObserver(observer);
        adapter.cursor.close();
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
            adapter = new DetailAdapter(getContext(), new MatrixCursor(new String[0]));
            listView.setAdapter(adapter);
            view.findViewById(R.id.input_sms_voice).setOnClickListener(this);
            view.findViewById(R.id.input_sms_face).setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            int id = v.getId();
            if (R.id.input_sms_face == id) {
                getFragmentManager().beginTransaction()
                    .add(R.id.mms_fragment_container, MmsSmsInputFragment.show(conversation))
                    .addToBackStack("input_sms")
                    .commit();
            } else if (R.id.input_sms_voice == id) {
                getFragmentManager().beginTransaction()
                    .add(R.id.mms_fragment_container, MmsSmsInputFragment.show(conversation))
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

        private void swipeCursor(Cursor cursor) {
            if (null != this.cursor) {
                this.cursor.close();
            }
            this.cursor = cursor;
            notifyDataSetChanged();
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

    private class QueryHandler extends AsyncQueryHandler {

        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (isAdded()) {
                adapter.swipeCursor(cursor);
            } else {
                cursor.close();
            }
        }
    }
}
