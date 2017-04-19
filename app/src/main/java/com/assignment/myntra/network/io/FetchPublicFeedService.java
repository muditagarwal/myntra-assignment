package com.assignment.myntra.network.io;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.assignment.myntra.R;
import com.assignment.myntra.network.response.FeedResponse;
import com.assignment.myntra.utils.NetworkUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Mudit Agarwal on 17-04-2017.
 */

public class FetchPublicFeedService {

	private static String FEED_URL = "http://api.flickr.com/services/feeds/photos_public.gne?lang=en-us&format=json&nojsoncallback=1";

	public void fetchFeed(final Activity activity) {
		if (NetworkUtils.isNetworkAvailable(activity)) {
			RequestQueue queue = Volley.newRequestQueue(activity);
			StringRequest request = new StringRequest(FEED_URL, new Response.Listener<String>() {
				@Override
				public void onResponse(final String response) {
					EventBus.getDefault().post(new Gson().fromJson(response, FeedResponse.class));
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(final VolleyError volleyError) {
					EventBus.getDefault().post(activity.getString(R.string.generic_server_error));
				}
			});
			queue.add(request);
		} else {
			EventBus.getDefault().post(activity.getString(R.string.no_internet_message));
		}
	}

}
