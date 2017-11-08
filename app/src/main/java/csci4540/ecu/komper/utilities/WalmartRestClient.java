package csci4540.ecu.komper.utilities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ryansummerlin on 11/5/17.
 */

public class WalmartRestClient {
    private static final String TAG = "WalmartRestClient";

    public static void query(@NonNull String query, @Nullable WalmartSearchParams params) {
        if (params == null) {
            WalmartRestClientHelper.GET("&query=" + query, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            JSONObject firstItem = (JSONObject) items.get(0);
                            Log.e(TAG, String.valueOf(firstItem.getDouble("salePrice")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e(TAG, String.valueOf(statusCode));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                    Log.e(TAG, responseString);
                }
            });
        }
    }
}

