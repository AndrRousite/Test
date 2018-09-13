package com.letion.test.chat

import com.letion.test.base.IView
import com.letion.test.bean.TestMessage

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/13 0013
 */
interface ChatView : IView {
    fun notifyTitle(title: String)

    fun notifyData(msgId: String?,content: TestMessage)
    fun notifyData(content: List<TestMessage>)
}