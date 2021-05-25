package com.pointzi.fragdialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InstallationStamper private constructor(context: Context){
    companion object{
        private var instance : InstallationStamper?=null
        private val PREF_NAME = "Pointzi_pref"
        private val INSTALL_TIME = "install_time"

        fun newInstance(context: Context):InstallationStamper{
            val temp = instance
            if (temp!=null)return temp
            synchronized(this) {
                instance = InstallationStamper(context)
                return instance!!
            }
        }
    }

    private var appPref: SharedPreferences

    init {
        appPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun writeToInstallTimeToPointziPref(){
       with(appPref.edit()){

           putString(INSTALL_TIME, DateCalculator().getOnlyDate())
           apply()
       }
    }

    fun isNotFirstInstall():Boolean{
        with(appPref){
            return contains(INSTALL_TIME)
        }
    }

    fun getInstallTime():String{
        with(appPref){
           return appPref.getString(INSTALL_TIME,"")!!
        }
    }

    class DateCalculator(){
        private var time_in_ms:Long?= null

        constructor(time_in_ms: Long):this(){
            this.time_in_ms = time_in_ms
        }

       @SuppressLint("SimpleDateFormat")
       fun getFullDateTimeInString():String{
           val formatter = SimpleDateFormat("hh:mm:ss dd MMM yyyy")
           val date = Calendar.getInstance()
           date.timeInMillis = time_in_ms!!
           return formatter.format(date.time)
       }

        fun getOnlyDate():String{
            var strDate:String = ""
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.O) {
                val formatter = SimpleDateFormat("dd MMM yyyy")
                val date = Date()
                strDate = formatter.format(date)
            }else{
                val current = LocalDateTime.now()

                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                strDate = current.format(formatter)
            }
            return strDate
        }
    }
}