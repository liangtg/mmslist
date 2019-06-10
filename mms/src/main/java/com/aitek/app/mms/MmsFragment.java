package com.aitek.app.mms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aitek.app.mms.data.Conversation;
import com.aitek.app.mms.data.ConversationList;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            viewHolder.listView.setVisibility(View.GONE);
        } else {
            viewHolder.listView.setVisibility(View.VISIBLE);
        }
    }

    private class ViewHolder {
        private View view;
        private ListView listView;

        public ViewHolder(View view) {
            this.view = view;
            listView = view.findViewById(R.id.listview);
        }

    }

    private class ConversationListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return conversationList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AdapterViewholder viewholder;
            View result = convertView;
            if (null == result) {
                result = getLayoutInflater().inflate(R.layout.item_conversation_list, parent, false);
                viewholder = new AdapterViewholder(result);
                result.setTag(viewholder);
            } else {
                viewholder = (AdapterViewholder) convertView.getTag();
            }
            bindView(viewholder, position);
            return result;
        }

        private void bindView(AdapterViewholder viewholder, int index) {
            viewholder.setIndex(index);
            Conversation item = conversationList.getConversation(index);
            String title = item.person;
            if (TextUtils.isEmpty(title)) title = item.address;
            viewholder.itemTitle.setText(title);
            viewholder.itemText.setText(item.snippet);
            viewholder.itemDate.setText(TimeUtil.getChatTimeStr(item.date / 1000));
        }
    }

    private class AdapterViewholder implements View.OnClickListener {
        private View itemView;
        private TextView itemTitle;
        private TextView itemText;
        private TextView itemDate;
        private int index;

        public AdapterViewholder(View itemView) {
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemText = itemView.findViewById(R.id.item_text);
            itemDate = itemView.findViewById(R.id.item_date);
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.item_view == id) {
                getFragmentManager().beginTransaction().add(R.id.conversation_container,
                        ConversationDetailFragment.show(conversationList.getConversation(index))).addToBackStack("conversation_detail").commit();
            }
        }
    }
}
