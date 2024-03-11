package com.rnmoyasar; // replace your-apps-package-name with your app’s package name
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class MoyasarModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return  "MoyasarModule"
    }

    @ReactMethod
    fun callMoyasarFunction(promise: Promise) {
        Log.d("MoyasarModule", "Payment Event is called")
        try {
            val intent = Intent(reactApplicationContext, CheckoutActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            reactApplicationContext.startActivity(intent)
            promise.resolve("Activity started successfully")
        } catch (e: Exception) {
            promise.reject("ERROR_CODE", "Error starting activity: ${e.message}")
        }
    }

}