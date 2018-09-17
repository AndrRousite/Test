package com.letion.green_dao.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/11 0011
 */
@Entity(nameInDb = "tb_message")
public class Message {
    @Id
    @Property(nameInDb = "msg_id")
    private long id;

    @Property(nameInDb = "msg_time")
    private long msgTime;

    @Property(nameInDb = "msg_direction")
    private int msgDirection;

    @NotNull
    @Property(nameInDb = "conversation")
    private long sessionId;

    @NotNull
    @Property(nameInDb = "from")
    private String from;

    @Property(nameInDb = "to")
    private String to;

    @Property(nameInDb = "msg_type")
    private int msgType;

    @Property(nameInDb = "chat_type")
    private int chatType;

    @Property(nameInDb = "content")
    private String content;

    @Property(nameInDb = "extras")
    private String extras;

    @Property(nameInDb = "is_read")
    private int isRead;

    @Property(nameInDb = "status")
    private int status;

    @Generated(hash = 1323170568)
    public Message(long id, long msgTime, int msgDirection, long sessionId,
            @NotNull String from, String to, int msgType, int chatType,
            String content, String extras, int isRead, int status) {
        this.id = id;
        this.msgTime = msgTime;
        this.msgDirection = msgDirection;
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.chatType = chatType;
        this.content = content;
        this.extras = extras;
        this.isRead = isRead;
        this.status = status;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMsgTime() {
        return this.msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public int getMsgDirection() {
        return this.msgDirection;
    }

    public void setMsgDirection(int msgDirection) {
        this.msgDirection = msgDirection;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getChatType() {
        return this.chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
