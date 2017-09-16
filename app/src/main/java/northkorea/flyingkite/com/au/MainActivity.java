package northkorea.flyingkite.com.au;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView resultsView = (TextView) findViewById(R.id.resultsView);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://hasnorthkoreafiredamissiletoday.com/data.json";

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        resultsView.setText(parse(response));
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getLocalizedMessage());
                        resultsView.setText(parse(null));
                    }
                });
        queue.add(request);
    }

    /**
     * Convenience method to parse the response and get the string to display
     * @param response {@link JSONObject} may be null
     * @return
     */
    private String parse(JSONObject response) {

        Log.d(TAG, response.toString());

        if(response == null) {
            return getString(R.string.error);
        }

        try {
            String result = response.getString("today");
            switch(result) {
                case "yes": {
                    return getString(R.string.yes);
                }
                case "no": {
                    return getString(R.string.no);
                }
                default: {
                    Log.e(TAG, "Data is bad: " + result);
                    return getString(R.string.error);
                }

            }

        } catch (JSONException e) {
            Log.e(TAG, "Error fetching data", e);
            return getString(R.string.error);
        }

    }
}
