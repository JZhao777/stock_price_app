package com.jingjuan.stock_price_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class menu_info extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String baseUrl = "https://api.api-ninjas.com/v1/stockprice?ticker=";
    //my API key below
    private String apiKey = BuildConfig.API_KEY;
    private EditText tickerEditText;
    private TextView apiResponseTextView;
    private Button getPriceButton;
    private Button clearButton;
    private TextView apiResponseTickerName;
    private TextView apiResponseName;
    private TextView apiResponseExchange;
    private TextView apiResponsePrice;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_info);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        // Initialize UI components
        tickerEditText = findViewById(R.id.tickerEditText);
        apiResponseTickerName = findViewById(R.id.apiResponseTickerName);
        apiResponseName = findViewById(R.id.apiResponseName);
        apiResponseExchange = findViewById(R.id.apiResponseExchange);
        apiResponsePrice = findViewById(R.id.apiResponsePrice);
        getPriceButton = findViewById(R.id.getPriceButton);
        clearButton = findViewById(R.id.clearButton);

        getPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = tickerEditText.getText().toString().trim();
                if (!ticker.isEmpty()) {
                    getData(ticker);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a ticker name", Toast.LENGTH_LONG).show();
                }
            }
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tickerEditText.setText("");
                apiResponseName.setText("");       // Clear the response TextView
                apiResponseTickerName.setText("");     // Clear the ticker name TextView
                apiResponseExchange.setText("");       // Clear the exchange TextView
                apiResponsePrice.setText("");          // Clear the price TextView
            }
        });



    }




    private void getData(String ticker) {
        // Combine base url and input ticker name to get full url
        String url = baseUrl + ticker;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response); // Log the full response
                try {
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(response);

                    // Extract fields from the JSON response
                    String ticker = jsonObject.getString("ticker");
                    String name = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    String exchange = jsonObject.getString("exchange");
                    long updated = jsonObject.getLong("updated");


                    // Display the response
                    //String displayText = "Ticker: " + ticker + "\nName: " + name + "\nPrice: $" + price + "\nExchange: " + exchange + "\nUpdated: " + updated;
                    String displayTickerName = ticker;
                    apiResponseTickerName.setText(displayTickerName);
                    String displayName = name;
                    apiResponseName.setText(displayName);
                    String displayExchange = exchange;
                    apiResponseExchange.setText(displayExchange);
                    String displayPrice = String.valueOf(price);
                    apiResponsePrice.setText(displayPrice);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing response: " + response, e);
                    Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.toString());

                // Detailed error message
                String errorMessage = "Error: " + error.toString();
                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    String responseBody = new String(error.networkResponse.data);
                    errorMessage += "\nStatus Code: " + statusCode;
                    errorMessage += "\nResponse Body: " + responseBody;
                    Log.e(TAG, "Status Code: " + statusCode);
                    Log.e(TAG, "Response Body: " + responseBody);
                }

                apiResponseTextView.setText(errorMessage);
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("X-Api-Key", apiKey);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        mRequestQueue.add(mStringRequest);
    }


}