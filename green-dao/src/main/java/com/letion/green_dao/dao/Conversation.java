package com.letion.green_dao.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
@Entity(nameInDb = "tb_conversation")
public class Conversation {
    @Id
    @Property(nameInDb = "session_id")
    private long id;

    @Unique
    @NotNull
    @Property(nameInDb = "user_id")
    private String userId;

    @NotNull
    @Property(nameInDb = "user_name")
    private String name;

    @Property(nameInDb = "user_avatar")
    @NotNull
    private String avatar;

    @Property(nameInDb = "user_phone")
    @NotNull
    private String phone;

    @Property(nameInDb = "vendor_id")
    @NotNull
    private String vendorId;

    @Property(nameInDb = "vendor_user_id")
    @NotNull
    private String vendorUserId;

    @Property(nameInDb = "vendor_user_name")
    @NotNull
    private String vendorUserName;

    @Generated(hash = 1165643662)
    public Conversation(long id, @NotNull String userId, @NotNull String name,
                        @NotNull String avatar, @NotNull String phone, @NotNull String vendorId,
                        @NotNull String vendorUserId, @NotNull String vendorUserName) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.phone = phone;
        this.vendorId = vendorId;
        this.vendorUserId = vendorUserId;
        this.vendorUserName = vendorUserName;
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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorUserId() {
        return this.vendorUserId;
    }

    public void setVendorUserId(String vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public String getVendorUserName() {
        return this.vendorUserName;
    }

    public void setVendorUserName(String vendorUserName) {
        this.vendorUserName = vendorUserName;
    }

}
