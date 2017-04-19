package com.assignment.myntra.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.assignment.myntra.R;
import com.assignment.myntra.utils.PreferencesManager;

public class StartAGameActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_agame);
		setBestScore();
	}

	private void setBestScore() {
		TextView bestScoreTextView = (TextView) findViewById(R.id.best_score);
		int bestScore = PreferencesManager.getInstance(this).getBestScore();
		if (bestScore != -1) {
			bestScoreTextView.setVisibility(View.VISIBLE);
			bestScoreTextView.setText(getString(R.string.your_best_score, bestScore));
		} else {
			bestScoreTextView.setVisibility(View.GONE);
		}
	}

	public void startAGame(View view) {
		Intent newGameIntent = new Intent(this, GameActivity.class);
		startActivityForResult(newGameIntent, 100);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == 100 && resultCode == RESULT_OK) {
			setBestScore();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
