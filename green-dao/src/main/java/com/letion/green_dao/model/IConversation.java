package com.letion.green_dao.model;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
public interface IConversation {
    String getId();

    String getConvTitle();

    String getConvPhoto();

    List<IUser> getUsers();

    IMessage getLastMsg();

    void setLastMsg(IMessage msg);

    int getUnreadCount();

    Date getCreatedAt();
}
