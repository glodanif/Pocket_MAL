package com.g.pocketmal.ui.view

interface LoginView {
    fun loadWebView(url: String)
    fun displayWebView()
    fun displayLoginError()
    fun displayNoInternet()
    fun displayWebViewLoading()
    fun showProgress()
    fun hideProgress()
}