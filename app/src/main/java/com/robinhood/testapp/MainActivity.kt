package com.robinhood.testapp

import com.robinhood.wsc.StartActivity

class MainActivity : StartActivity() {

    override fun getPlaceholderStartActivity(): Class<*> = ExampleActivity::class.java

    override fun getAlartReceiver() = AlartReceiver::class.java

    override fun getPackageName() = BuildConfig.APPLICATION_ID
}