package com.pointzi.fragdialog

import android.content.Context
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat

@RunWith(MockitoJUnitRunner::class)
class InstallationStamperTest {

    private lateinit var calculator: InstallationStamper.DateCalculator
    private lateinit var stamper: InstallationStamper
    private lateinit var dateFormatter: SimpleDateFormat
    @Before
    fun getAppPref(){
        val context = mock(Context::class.java)
        calculator = InstallationStamper.DateCalculator()
        dateFormatter = SimpleDateFormat("hh:mm:ss dd MMM yyyy")
        //Parsing the given String to Date object

    }
    @Test
    fun isFirstInstall_CheckTest(){
        assertEquals("26 May 2021", calculator.getOnlyDate() )
        calculator = InstallationStamper.DateCalculator(System.currentTimeMillis())
        val diff = Math.abs(System.currentTimeMillis()- dateFormatter.parse(calculator.getFullDateTimeInString()).time )
        assertTrue(diff<1000)
        //assertLessThan(System.currentTimeMillis(), dateFormatter.parse(calculator.getFullDateTimeInString()).time )
    }
}