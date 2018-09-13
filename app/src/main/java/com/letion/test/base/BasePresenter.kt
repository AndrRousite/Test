package com.letion.test.base

/**
 * <p>
 * @describe ...
 *
 * @author wuqi
 * @date 2018/9/13 0013
 */
 abstract class BasePresenter<V : IView> {

    var view: V? = null
        set(value) {
            field = value
        }

    open fun onCreate() {

    }

    open fun onDestroy() {
        view = null
    }


}