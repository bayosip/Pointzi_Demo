package de.salomax.currencies.repository

import com.github.kittinunf.fuel.core.ResponseResultOf
import de.salomax.currencies.model.ExchangeRates
import org.junit.Test
import org.junit.Assert.*

class ExchangeRatesServiceTest {

    @Test
    fun testExchangerateHost() {
        testWebservice(
            ExchangeRatesService.getRatesBlocking(ExchangeRatesService.Endpoint.EXCHANGERATE_HOST)
        )
    }

    @Test
    fun testFrankfurterApp() {
        testWebservice(
            ExchangeRatesService.getRatesBlocking(ExchangeRatesService.Endpoint.FRANKFURTER_APP)
        )
    }

    fun testWebservice(data: ResponseResultOf<ExchangeRates>) {
        // rates
        val rates = data.third.component1()

        // see there is some valid data
        assertNotNull(rates)

        // see that there is a list of exchange rates
        assertNotNull(rates!!.rates)

        // check for some currencies
        val eur = rates.rates!!.find { rate -> rate.code == "EUR" }
        assertTrue(eur != null)
        assertEquals(1.0f, eur!!.value)
        println(eur)

        val usd = rates.rates!!.find { rate -> rate.code == "USD" }
        assertTrue(usd != null)
        println(usd)

        val jpy = rates.rates!!.find { rate -> rate.code == "JPY" }
        assertTrue(jpy != null)
        println(jpy)

        val krw = rates.rates!!.find { rate -> rate.code == "KRW" }
        assertTrue(krw != null)
        println(krw)

        val chf = rates.rates!!.find { rate -> rate.code == "CHF" }
        assertTrue(chf != null)
        println(chf)
    }

}
