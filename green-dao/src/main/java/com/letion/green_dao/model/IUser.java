package com.letion.green_dao.model;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
public interface IUser {
    /**
     * User id.
     * @return user id, unique
     */
    String getId();

    /**
     * Display name of user
     * @return display name
     */
    String getDisplayName();

    /**
     * Get user avatar file path.
     * @return avatar file path
     */
    String getAvatarFilePath();
}
