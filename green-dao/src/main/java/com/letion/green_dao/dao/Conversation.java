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

    @Property(nameInDb = "session_type")
    private int type;

    @Property(nameInDb = "extras")
    private String extras;

    @Property(nameInDb = "un_read_count")
    private int unReadCount;

    @Generated(hash = 1313877290)
    public Conversation(long id, int type, String extras, int unReadCount) {
        this.id = id;
        this.type = type;
        this.extras = extras;
        this.unReadCount = unReadCount;
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public int getUnReadCount() {
        return this.unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
