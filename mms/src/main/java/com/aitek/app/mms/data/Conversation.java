package com.aitek.app.mms.data;

public class Conversation {
    public long id;
    public long date;
    public long msgCount;
    public String recipIDs;
    public String snippet;
    public long snippetCS;
    public long read;
    public long error;
    public long hasAttach;
    public String address;
    public String person;
    public String body;
    public String threadId;
    public int type;

    @Override public String toString() {
        return "Conversation{" +
            "id=" + id +
            ", date=" + date +
            ", msgCount=" + msgCount +
            ", recipIDs='" + recipIDs + '\'' +
            ", snippet='" + snippet + '\'' +
            ", snippetCS=" + snippetCS +
            ", read=" + read +
            ", error=" + error +
            ", hasAttach=" + hasAttach +
            ", address='" + address + '\'' +
            ", person='" + person + '\'' +
            ", body='" + body + '\'' +
            ", threadId='" + threadId + '\'' +
            ", type=" + type +
            '}';
    }
}
