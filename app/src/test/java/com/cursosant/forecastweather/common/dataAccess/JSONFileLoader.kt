package com.cursosant.forecastweather.common.dataAccess

import com.cursosant.forecastweather.entities.WeatherForecastEntity
import com.google.gson.Gson
import java.io.InputStreamReader

/****
 * Project: Forecast Weather
 * From: com.cursosant.forecastweather.common.dataAccess
 * Created by Alain Nicolás Tello on 18/12/21 at 10:52
 * All rights reserved 2021.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * Web: www.alainnicolastello.com
 ***/
class JSONFileLoader {
    private var jsonStr: String? = null

    fun loadJSONString(file: String): String?{
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return jsonStr
    }

    fun loadWeatherForecastEntity(file: String): WeatherForecastEntity?{
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, WeatherForecastEntity::class.java)
    }
}