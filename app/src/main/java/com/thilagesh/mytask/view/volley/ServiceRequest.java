package com.thilagesh.mytask.view.volley;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ServiceRequest {
    private Context context;
    private ServiceListener mServiceListener;
    private StringRequest stringRequest;



    public interface ServiceListener {
        void onCompleteListener(String response);
        void onErrorListener();
    }

    public ServiceRequest(Context context) {
        this.context = context;

    }

    public void cancelRequest()
    {
        if (stringRequest != null) {
            stringRequest.cancel();
        }
    }


    public void makeServiceRequest(final String url, int method, final HashMap<String, String> param,ServiceListener listener) {

        this.mServiceListener=listener;

        stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    mServiceListener.onCompleteListener(response);

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof NetworkError) {
                    } else if (error instanceof ParseError) {
                    }
                } catch (Exception e) {
                }

                 mServiceListener.onErrorListener();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };

        //to avoid repeat request Multiple Time
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
