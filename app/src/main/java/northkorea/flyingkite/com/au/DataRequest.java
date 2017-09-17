package northkorea.flyingkite.com.au;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Handles requests. Ony accepts 200 responses. Enything else is rethrown as an error.
 */
public class DataRequest extends JsonObjectRequest {

    public DataRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        int statusCode = response.statusCode;

        try {
            if(statusCode == 200) {

                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new VolleyError(new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
