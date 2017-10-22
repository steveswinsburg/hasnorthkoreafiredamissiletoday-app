package northkorea.flyingkite.com.au;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MainActivity";
    private static final String URL = "http://hasnorthkoreafiredamissiletoday.com/data.json";

    private TextView resultsView = null;
    private SwipeRefreshLayout refreshLayout = null;

    private JsonObjectRequest request;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.resultsView = (TextView) findViewById(R.id.resultsView);
        this.refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(this);

        queue = Volley.newRequestQueue(this);

        // call manually so that initial value is set without user needing to swipe down.
        this.onRefresh();
    }

    /**
     * Convenience method to parse the response and get the string to display
     * @param response {@link JSONObject} may be null
     * @return
     */
    private String parse(JSONObject response) {

        if(response == null) {
            return getString(R.string.error);
        }

        // if response == null, this line would give error. Hence that check is above
        Log.d(TAG, response.toString());

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

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: called");
        // start progress animation
        refreshLayout.setRefreshing(true);

        // make new JsonObjectRequest
        request = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: response received");
                        String text = parse(response);
                        show(text);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error.getMessage());
                        String text = parse(null);
                        show(text);
                    }
                });

        // add request to queue
        queue.add(request);
    }

    private void show(String text) {
        resultsView.setText(text);
        resultsView.setVisibility(View.VISIBLE);

        // new result has been displayed. Cancel animation
        refreshLayout.setRefreshing(false);
    }
}
