package com.aitek.app.mms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aitek.app.mms.data.Conversation;
import com.aitek.app.mms.data.ConversationList;
import com.daimajia.swipe.SwipeLayout;

public class MmsFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
    private ViewHolder viewHolder;
    private ConversationList conversationList;
    private ConversationListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationList = new ConversationList(getActivity().getContentResolver());
        conversationList.addListener(new ConversationList.QueryListener() {
            @Override
            public void completed() {
                adapter.notifyDataSetChanged();
            }
        });
        getFragmentManager().addOnBackStackChangedListener(this);
        Context context = getActivity();
        String myPackageName = context.getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(context).equals(myPackageName)) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_mms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        adapter = new ConversationListAdapter();
        viewHolder.listView.setAdapter(adapter);
    }

    @Override
    public void onBackStackChanged() {
        if (null == getFragmentManager()) return;
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            viewHolder.listView.setVisibility(View.GONE);
        } else {
            viewHolder.listView.setVisibility(View.VISIBLE);
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        conversationList.destroy();
    }

    private class ViewHolder {
        private View view;
        private RecyclerView listView;

        public ViewHolder(View view) {
            this.view = view;
            listView = view.findViewById(R.id.listview);
        }
    }

    private class ConversationListAdapter extends RecyclerView.Adapter<AdapterViewholder> {

        @NonNull @Override
        public AdapterViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new AdapterViewholder(
                getLayoutInflater().inflate(R.layout.item_conversation_list, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull AdapterViewholder viewholder, int i) {
            int index = i;
            Conversation item = conversationList.getConversation(index);
            String title = item.person;
            if (TextUtils.isEmpty(title)) title = item.address;
            viewholder.itemTitle.setText(title);
            String body = item.snippet;
            if (TextUtils.isEmpty(body)) body = item.body;
            viewholder.itemText.setText(body);
            viewholder.itemDate.setText(TimeUtil.getChatTimeStr(item.date / 1000));
            viewholder.swipeLayout.close();
        }

        @Override
        public long getItemId(int position) {
            return Long.valueOf(conversationList.getConversation(position).threadId);
        }

        @Override public int getItemCount() {
            return conversationList.size();
        }
    }

    private class AdapterViewholder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private TextView itemTitle;
        private TextView itemText;
        private TextView itemDate;
        private SwipeLayout swipeLayout;

        public AdapterViewholder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            swipeLayout.setOnClickListener(this);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemText = itemView.findViewById(R.id.item_text);
            itemDate = itemView.findViewById(R.id.item_date);
            itemView.findViewById(R.id.item_contact).setOnClickListener(this);
            itemView.findViewById(R.id.item_delete).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (index == RecyclerView.NO_POSITION) return;
            int id = v.getId();
            if (R.id.swipe_layout == id) {
                if (SwipeLayout.Status.Close == swipeLayout.getOpenStatus()) {
                    getFragmentManager().beginTransaction()
                        .add(R.id.conversation_container,
                            ConversationDetailFragment.show(
                                conversationList.getConversation(index)))
                        .addToBackStack("conversation_detail")
                        .commit();
                }
            } else if (R.id.item_contact == id) {
            } else if (R.id.item_delete == id) {
                conversationList.removeAt(index);
            }
        }
    }
}
