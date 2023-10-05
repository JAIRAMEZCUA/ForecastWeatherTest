package com.cursosant.forecastweather.common.dataAccess

import com.cursosant.forecastweather.entities.WeatherForecastEntity
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

/****
 * Project: Forecast Weather
 * From: com.cursosant.forecastweather.common.dataAccess
 * Created by Alain Nicolás Tello on 18/12/21 at 11:06
 * All rights reserved 2021.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * Web: www.alainnicolastello.com
 ***/
@RunWith(MockitoJUnitRunner::class)
class ResponseServerTest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun `read json file success`(){
        val reader = JSONFileLoader().loadJSONString("weather_forecast_response_success")
        assertThat(reader, `is`(notNullValue()))
        assertThat(reader, containsString("America/Mexico_City"))
    }

    @Test
    fun `get weatherForecast and check timezone exist`(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("weather_forecast_response_success")
                ?: "{errorCode:34}")
        mockWebServer.enqueue(response)

        assertThat(response.getBody()?.readUtf8(), containsString("\"timezone\""))
    }

    @Test
    fun `get weatherForecast and check fail response`(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("weather_forecast_response_fail")
                ?: "{errorCode:34}")
        mockWebServer.enqueue(response)

        assertThat(response.getBody()?.readUtf8(), containsString("{\"cod\":401, \"message\":" +
                " \"Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.\"}"))
    }

    @Test
    fun `get weatherForecast and check contains hourly list no empty`(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("weather_forecast_response_success")
                ?: "{errorCode:34}")
        mockWebServer.enqueue(response)
        assertThat(response.getBody()?.readUtf8(), containsString("hourly"))

        val json = Gson().fromJson(response.getBody()?.readUtf8() ?: "", WeatherForecastEntity::class.java)
        assertThat(json.hourly.isEmpty(), `is`(false))
    }
}