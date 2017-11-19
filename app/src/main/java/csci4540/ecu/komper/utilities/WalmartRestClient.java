package csci4540.ecu.komper.utilities;

import android.content.Context;
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

    public static void query(Context context, @NonNull String query, @Nullable WalmartSearchParams params,
                             @NonNull JsonHttpResponseHandler handler) {
        if (params == null) {
            WalmartRestClientHelper.GET(context, "query=" + query, null, handler);
        }
    }
}

