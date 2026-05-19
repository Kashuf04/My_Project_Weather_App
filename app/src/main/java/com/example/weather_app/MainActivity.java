package com.example.weather_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText cityInput;
    Button searchButton;

    TextView tempText, weatherText, humidityText, windText;

    String apiKey = "d01fdf1e78dc209cd7313c530e517adc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityInput = findViewById(R.id.cityInput);
        searchButton = findViewById(R.id.searchButton);

        tempText = findViewById(R.id.tempText);
        weatherText = findViewById(R.id.weatherText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);

        searchButton.setOnClickListener(v -> {

            String city = cityInput.getText().toString();

            if(city.isEmpty()) {
                Toast.makeText(this, "Enter city name", Toast.LENGTH_SHORT).show();
                return;
            }

            getWeather(city);

        });
    }

    private void getWeather(String city) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city +
                "&appid=" + apiKey +
                "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    try {

                        JSONObject main = response.getJSONObject("main");
                        JSONObject wind = response.getJSONObject("wind");

                        double temp = main.getDouble("temp");
                        int humidity = main.getInt("humidity");
                        double windSpeed = wind.getDouble("speed");

                        String condition = response
                                .getJSONArray("weather")
                                .getJSONObject(0)
                                .getString("main");

                        tempText.setText("Temperature: " + temp + "°C");
                        weatherText.setText("Condition: " + condition);
                        humidityText.setText("Humidity: " + humidity + "%");
                        windText.setText("Wind Speed: " + windSpeed);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },

                error -> Toast.makeText(
                        this,
                        "City not found!",
                        Toast.LENGTH_SHORT
                ).show()
        );

        queue.add(request);
    }
}