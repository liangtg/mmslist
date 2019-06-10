package com.aitek.app.mms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.aitek.app.mms.data.Conversation;
import com.aitek.app.mms.data.ConversationList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MmsFragment extends Fragment {
    private ViewHolder viewHolder;
    private ConversationList conversationList;
    private ConversationListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationList = new ConversationList(getActivity().getContentResolver());
        conversationList.addListener(new ConversationList.QueryListener() {
            @Override public void completed() {
                adapter.notifyDataSetChanged();
            }
        });
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

    private class ViewHolder {
        private View view;
        private ListView listView;

        public ViewHolder(View view) {
            this.view = view;
            listView = view.findViewById(R.id.listview);
        }
    }

    private class ConversationListAdapter extends BaseAdapter {
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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
                result =
                    getLayoutInflater().inflate(R.layout.item_conversation_list, parent, false);
                viewholder = new AdapterViewholder(result);
            } else {
                viewholder = (AdapterViewholder) convertView.getTag();
            }
            bindView(viewholder, position);
            return result;
        }

        private void bindView(AdapterViewholder viewholder, int index) {
            Conversation item = conversationList.getConversation(index);
            String title = item.person;
            if (TextUtils.isEmpty(title)) title = item.address;
            viewholder.itemTitle.setText(title);
            viewholder.itemText.setText(item.snippet);
            viewholder.itemDate.setText(sdf.format(new Date(item.date)));
        }
    }

    private class AdapterViewholder {
        private View itemView;
        private TextView itemTitle;
        private TextView itemText;
        private TextView itemDate;

        public AdapterViewholder(View itemView) {
            this.itemView = itemView;
            itemTitle = itemView.findViewById(R.id.item_title);
            itemText = itemView.findViewById(R.id.item_text);
            itemDate = itemView.findViewById(R.id.item_date);
        }
    }
}
