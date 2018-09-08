package com.letion.test

import com.letion.green_dao.model.IUser

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/8 0008
 */
data class TestUser(val uid: String?, val name: String?, val avatar: String?)
    : IUser {
    override fun getId(): String? {
        return uid
    }

    override fun getDisplayName(): String? {
        return name
    }

    override fun getAvatarFilePath(): String? {
        return avatar
    }
}