package com.letion.test.main

import com.letion.green_dao.dao.Conversation
import com.letion.test.base.IView
import com.letion.test.bean.TestMessage

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/8 0008
 */
interface MainView : IView {
    fun notifyStatus(code: Int, content: String?)
    fun notifyConversation(data: List<Conversation>)
}