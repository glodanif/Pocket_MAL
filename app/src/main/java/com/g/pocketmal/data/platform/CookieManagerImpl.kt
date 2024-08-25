package com.g.pocketmal.data.platform

class CookieManagerImpl : CookieManager {

    override fun clearCookies() {
        android.webkit.CookieManager.getInstance().let {
            it.removeAllCookies(null)
            it.flush()
        }
    }
}
