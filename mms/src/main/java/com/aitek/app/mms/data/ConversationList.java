package com.aitek.app.mms.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.util.SparseArrayCompat;
import java.util.ArrayList;

public class ConversationList {
    private static final Uri MMS_PHONE = Uri.parse("content://mms-sms/canonical-addresses");

    private Cursor conversationCursor;
    private SparseArrayCompat<Conversation> cacheList = new SparseArrayCompat<>();
    private QueryHandler queryHandler;
    private ArrayList<QueryListener> queryListeners = new ArrayList<>();
    private ContentResolver resolver;

    public ConversationList(ContentResolver resolver) {
        this.resolver = resolver;
        queryHandler = new QueryHandler(resolver);
        queryHandler.startQuery(QueryHandler.QUERY_ALL, null,
            Tables.ConversationList.CONVERSATION_URI,
            Tables.ConversationList.ALL_THREADS_PROJECTION, null, null,
            "date DESC");
    }

    public void addListener(QueryListener listener) {
        queryListeners.add(listener);
    }

    public int size() {
        return null == conversationCursor ? 0 : conversationCursor.getCount();
    }

    private Conversation readConversation(Cursor cursor) {
        Conversation result = new Conversation();
        result.id = cursor.getLong(Tables.ConversationList.ID);
        result.date = cursor.getLong(Tables.ConversationList.DATE);
        result.msgCount = cursor.getLong(Tables.ConversationList.MESSAGE_COUNT);
        result.recipIDs = cursor.getString(Tables.ConversationList.RECIPIENT_IDS);
        result.snippet = cursor.getString(Tables.ConversationList.SNIPPET);
        result.snippetCS = cursor.getLong(Tables.ConversationList.SNIPPET_CS);
        result.read = cursor.getLong(Tables.ConversationList.READ);
        result.error = cursor.getLong(Tables.ConversationList.ERROR);
        result.hasAttach = cursor.getLong(Tables.ConversationList.HAS_ATTACHMENT);
        Cursor pc = resolver.query(MMS_PHONE,
            new String[] { "_id", "address" }, "_id=?",
            new String[] { result.recipIDs }, null);
        if (pc.moveToFirst()) {
            result.address = pc.getString(1);
        }
        pc.close();
        return result;
    }

    public Conversation getConversation(int index) {
        Conversation re = cacheList.get(index);
        if (null == re) {
            conversationCursor.moveToPosition(index);
            re = readConversation(conversationCursor);
            cacheList.put(index, re);
        }
        return re;
    }

    private void dispatchListener() {
        for (int i = 0; i < queryListeners.size(); i++) {
            queryListeners.get(i).completed();
        }
    }

    public interface QueryListener {
        void completed();
    }

    private class QueryHandler extends AsyncQueryHandler {
        private static final int QUERY_ALL = 1;

        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            conversationCursor = cursor;
            dispatchListener();
        }
    }
}

