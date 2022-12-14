package com.example.weatherapp

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

import android.widget.Spinner







class MainActivity : AppCompatActivity() {
    //private var spinner = findViewById<View>(R.id.spinner) as Spinner
    //var text = spinner.selectedItem.toString()
    //var CITY: String = spinner.selectedItem.toString()
    val CITY: String = "Khulna"
    val API: String = "bb9f38871e6eaeb785f75bc764dca4f8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTask().execute()

    }


    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {

            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {

                /* Extracting JSON returns from the API*/
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val coord = jsonObj.getJSONObject("coord")
                //val visibility = jsonObj.getJSONObject("visibility")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"??C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"??C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"??C"
                val lat = coord.getString("lat")
                val long = coord.getString("lon")
                val pressure = main.getString("pressure")+" mBar"
                val humidity = main.getString("humidity")+"%"

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                //val view = visibility.getString("visibility")+" m"
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name")+", "+sys.getString("country")

                /* Populating extracted data into our views*/
                findViewById<TextView>(R.id.Location).text = address
                findViewById<TextView>(R.id.date).text =  updatedAtText
                findViewById<TextView>(R.id.Lattitute_value).text = lat
                findViewById<TextView>(R.id.Longitude_value).text =  long
                //findViewById<TextView>(R.id.eyerange_val).text =  view
                findViewById<TextView>(R.id.state).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.tem_cel).text = temp
                findViewById<TextView>(R.id.tem_far).text = temp
                //findViewById<TextView>(R.id.tem_max).text = tempMax
                findViewById<TextView>(R.id.tv_sunrise_time).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.tv_sunset_time).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                //findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure_val).text = pressure
                findViewById<TextView>(R.id.humidity_val).text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.mainContainer).visibility = View.VISIBLE
                //findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                //findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }

        }
    }
}