package com.example.weatherapp

import android.content.DialogInterface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY : String = "Gaziantep,TR"
    val API : String = "7ddfb8a2abb04f4a9a0f219b2f377f40"
    var currentTime = Calendar.getInstance().time
    val currentDay = Calendar.DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }
    inner class weatherTask() : AsyncTask<String,Void,String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }catch (e:Exception) {
                response = null
            }
        return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val temp = main.getString("temp")+ "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+ "°C"
                val tempMax = "Max Temp: " +main.getString("temp_max")+ "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val adress = jsonObj.getString("name")+ ", " +sys.getString("country")



                findViewById<TextView>(R.id.adress).text = adress
                findViewById<TextView>(R.id.updated_at).text = "$currentTime "
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = "½$humidity"

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE


                findViewById<LinearLayout>(R.id.infoClicker)
                infoClicker.setOnClickListener{
                    val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                    dialogBuilder.setMessage("*This app is made by Ekrem Atakan KULOĞLU.                                  " +
                            "                           *All data and API is taken from https://openweathermap.org/")

                        // if the dialog is cancelable
                        .setCancelable(false)
                        .setPositiveButton("OKAY", DialogInterface.OnClickListener {
                                dialog, id ->
                            dialog.dismiss()

                        })

                    val alert = dialogBuilder.create()
                    alert.setTitle("Test")
                    alert.show()
                }


            } catch (e:Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE

            }
        }
    }
}