package com.robinhood.wsc

import android.app.Application
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.robinhood.wsc.data.EntityAppsflyerData
import com.robinhood.wsc.interfaces.IValueListener

abstract class App : Application() {

    private val separator = "|"

    companion object {
        lateinit var instance: App
    }

    private var listener: IValueListener<EntityAppsflyerData>? = null
    private var entity: EntityAppsflyerData? = null

    abstract fun getAppsflyerAppId() : String

    fun getAppsflyerData(listener: IValueListener<EntityAppsflyerData>) {
        when {
            entity != null -> listener.value(entity)
            else -> this.listener = listener
        }
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppThemeLib)
        instance = this

        AppsFlyerLib.getInstance().init(getAppsflyerAppId(), object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(map: Map<String, Any>) {

                val status = map["af_status"]
                val namingLong = map["campaign"]
                val namingShort = map["c"]

                val isOrganic = status.toString() == "Organic"
                val naming = namingShort ?: namingLong
                var namingString = naming?.toString()
                var firstParam = ""

                val pairs = ArrayList<android.util.Pair<String, String>>()
                if(namingString != null) {
                    val hasSeparator = namingString.contains(separator)
                    val separatorIndex = namingString.indexOf(separator)

                    firstParam = if(hasSeparator) namingString.substring(0, separatorIndex) else namingString

                    if(hasSeparator) namingString = namingString.substring(separatorIndex + 1, namingString.length)

                    val namingParams = namingString.split(separator)
                    if(hasSeparator) for(param in namingParams) {
                        val data = param.split("=")
                        pairs.add(android.util.Pair(data[0], data[1]))
                    }
                }
                val value = EntityAppsflyerData(isOrganic, firstParam, pairs, map.toString())

                if(listener == null) entity = value
                else listener?.value(value)
            }
            override fun onConversionDataFail(s: String) {
                val value = EntityAppsflyerData(true, null, null, "")

                if(listener == null) entity = value
                else listener?.value(value)
            }
            override fun onAppOpenAttribution(map: Map<String, String>) {}
            override fun onAttributionFailure(s: String) {}
        }, this)
        AppsFlyerLib.getInstance().start(this)
    }



    /// TESTS ///

    val naming1 = "user1"
    val naming2 = "user1|sub1=q|sub2=4|sub3=da"
}