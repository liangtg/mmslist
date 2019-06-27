package com.aitek.app.mms.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.util.SparseArrayCompat;
import java.util.ArrayList;

public class ConversationList {
    private static final Uri MMS_PHONE = Uri.parse("content://mms-sms/canonical-addresses");

    private Cursor conversationCursor;
    private SparseArrayCompat<Conversation> cacheList = new SparseArrayCompat<>();
    private QueryHandler queryHandler;
    private ArrayList<QueryListener> queryListeners = new ArrayList<>();
    private ContentResolver resolver;
    private ConversationObserver conversationObserver;

    public ConversationList(ContentResolver resolver) {
        this.resolver = resolver;
        queryHandler = new QueryHandler(resolver);
        conversationObserver = new ConversationObserver();
        resolver.registerContentObserver(Tables.ConversationList.MMSSMS_FULL_CONVERSATION_URI, true,
            conversationObserver);
        queryInternal();
    }

    public void destroy() {
        queryHandler.cancelOperation(QueryHandler.QUERY_ALL);
        if (null != conversationCursor) conversationCursor.close();
        conversationCursor = null;
        cacheList.clear();
    }

    private void queryInternal() {
        queryHandler.startQuery(QueryHandler.QUERY_ALL,
            null,
            Tables.ConversationList.CONVERSATION_URI,
            Tables.ConversationList.ALL_THREADS_PROJECTION_SIMPLE,
            null,
            null,
            "date DESC");
    }

    public void addListener(QueryListener listener) {
        queryListeners.add(listener);
    }

    public int size() {
        return null == conversationCursor ? 0 : conversationCursor.getCount();
    }

    public void removeAt(int index) {
        Conversation item = getConversation(index);
        resolver.delete(Tables.ConversationList.MMSSMS_FULL_CONVERSATION_URI, "thread_id=?",
            new String[] { item.threadId });
        queryInternal();
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
        Cursor pc = resolver.query(MMS_PHONE, new String[] { "_id", "address" }, "_id=?",
            new String[] { result.recipIDs }, null);
        if (pc.moveToFirst()) {
            result.address = pc.getString(1);
            Cursor person =
                resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    },
                    ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                    new String[] { result.address }, null);
            if (person.moveToFirst()) {
                result.person = person.getString(0);
            }
            person.close();
        }
        pc.close();
        result.threadId = String.valueOf(result.id);
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

    private class ConversationObserver extends ContentObserver {

        public ConversationObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override public void onChange(boolean selfChange) {
            queryInternal();
        }
    }

    private class QueryHandler extends AsyncQueryHandler {
        private static final int QUERY_ALL = 1;

        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (null != conversationCursor) conversationCursor.close();
            cacheList.clear();
            conversationCursor = cursor;
            dispatchListener();
        }
    }
}

