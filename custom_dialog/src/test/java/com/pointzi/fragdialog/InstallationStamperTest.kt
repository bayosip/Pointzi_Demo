package com.pointzi.fragdialog

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InstallationStamperTest {

    private lateinit var appPref: SharedPreferences
    val PREF_NAME = "Pointzi_pref"
    @Before
    fun getAppPref(){
        val context = mock(Context::class.java)
        appPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    @Test
    fun isFirstInstall_CheckTest(){
        assert(appPref)
    }
}