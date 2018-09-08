package com.letion.green_dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
@Entity
public class Conversation {
    @Id
    private long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "avatar")
    private String avatar;

    @Property(nameInDb = "userinfo")
    private String user;

    @Property(nameInDb = "create_at")
    private Date createAt;

    @Generated(hash = 707155502)
    public Conversation(long id, String name, String avatar, String user,
            Date createAt) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.user = user;
        this.createAt = createAt;
    }

    @Generated(hash = 1893991898)
    public Conversation() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
