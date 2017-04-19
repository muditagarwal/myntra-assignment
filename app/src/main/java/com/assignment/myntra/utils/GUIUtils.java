package com.assignment.myntra.utils;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.myntra.R;

/**
 * Created by Mudit Agarwal on 18-04-2017.
 */

public class GUIUtils {

	public static void makeToast(Activity activity, @StringRes int message, int toastDuration, boolean isSuccess) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast, null);

		TextView toastMessageTextView = (TextView) layout.findViewById(R.id.text);
		toastMessageTextView.setText(message);
		toastMessageTextView.setBackgroundColor(activity.getResources().getColor(isSuccess ? R.color.success : R.color.fail));

		Toast toast = new Toast(activity);
		toast.setDuration(toastDuration);
		toast.setView(layout);
		toast.show();
	}

}
