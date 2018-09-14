package com.letion.green_dao;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.letion.green_dao.dao.Conversation;
import com.letion.green_dao.dao.Message;
import com.letion.green_dao.gen.ConversationDao;
import com.letion.green_dao.gen.DaoMaster;
import com.letion.green_dao.gen.DaoSession;
import com.letion.green_dao.gen.MessageDao;
import com.letion.green_dao.util.RandomUtil;


import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
public class DaoManager {
    private static DaoManager instance;
    private static DaoSession daoSession;

    public static synchronized DaoManager getInstance() {
        if (instance == null) {
            instance = new DaoManager();
        }
        return instance;
    }

    public void init(Context application) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application,
                "lenve.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }

    private DaoSession getDaoSession() {
        if (daoSession == null) {
            throw new NullPointerException("dao session not init");
        }
        return daoSession;
    }

    public long addOrUpdateConversation(String userId, String userName, String userAvatar, String
            userPhone) {
        Conversation conversation = new Conversation();
        conversation.setId(Long.valueOf(RandomUtil.number11()));
        conversation.setUserId(userId);
        conversation.setName(userName);
        conversation.setAvatar(userAvatar);
        conversation.setPhone(userPhone);
        conversation.setVendorId(UUID.randomUUID().toString().substring(0, 11));
        conversation.setVendorUserId(UUID.randomUUID().toString().substring(0, 11));
        conversation.setVendorUserName(UUID.randomUUID().toString().substring(0, 11));
        return getDaoSession().getConversationDao().insertOrReplace(conversation);
    }

    public List<Conversation> search() {
        return getDaoSession().getConversationDao().loadAll();
    }

    public Conversation searchConversationById(long sessionId) {
        return getDaoSession().getConversationDao().loadByRowId(sessionId);
    }

    public List<Message> searchMessage(long sessionId, int page, int pageSize) {
        return getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties
                .SessionId.eq(sessionId)).orderDesc(MessageDao.Properties.MsgTime).offset
                (page * pageSize).limit(pageSize).list();
    }

    /**
     * 插入消息数据
     *
     * @param msgId
     * @param msgTime
     * @param msgDirection
     * @param sessionId
     * @param from
     * @param to
     * @param msgType
     * @param chatType
     * @param content
     * @param extras
     * @param isRead
     * @param status
     * @return
     */
    public String insertMessage(@NonNull Long msgId, long msgTime, @IntRange(from = 0, to = 1)
    @NonNull Integer
            msgDirection,
                                @NonNull Long sessionId, @NonNull
                                        String from, @NonNull String to, @IntRange(from = 0, to
            = 9) int msgType,
                                @IntRange(from = 0, to = 1) int
                                        chatType, @Nullable String content, @Nullable String
                                        extras, @IntRange(from = 0,
            to = 1) int isRead, @IntRange(from = 0, to = 2) int status) {
        Message message = new Message();
        message.setId(msgId);
        message.setMsgTime(msgTime);
        message.setMsgDirection(msgDirection);
        message.setSessionId(sessionId);
        message.setFrom(from);
        message.setTo(to);
        message.setMsgType(msgType);
        message.setChatType(chatType);
        message.setContent(content);
        message.setExtras(extras);
        message.setIsRead(isRead);
        message.setStatus(status);

        long l = getDaoSession().getMessageDao().insertOrReplace(message);

        return l > 0 ? String.valueOf(message.getId()) : null;
    }

    /**
     * 更新消息的状态
     *
     * @param msgId
     * @param msgTime
     * @param isRead
     * @param status
     * @return
     */
    public boolean updateMessage(@NonNull Long msgId, @Nullable Long msgTime, @IntRange(from = 0,
            to = 1) int isRead, @IntRange(from = 0, to = 2) int status) {
        Message message = new Message();
        message.setId(msgId);
        if (msgTime != null) {
            message.setMsgTime(msgTime);
        }
        message.setIsRead(isRead);
        message.setStatus(status);
        getDaoSession().getMessageDao().update(message);
        return true;
    }
}
