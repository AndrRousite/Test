package com.letion.green_dao.inputs;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
public interface OnMenuItemClickListener {

    /**
     * Fires when send button is on click.
     *
     * @param input Input content
     * @return boolean
     */
    boolean onSendTextMessage(CharSequence input);

    /**
     * Fires when emoji button is on click.
     */
    void switchToEmojiMode();
}
