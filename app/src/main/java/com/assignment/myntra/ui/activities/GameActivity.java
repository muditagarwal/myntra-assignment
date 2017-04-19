package com.assignment.myntra.ui.activities;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.myntra.R;
import com.assignment.myntra.network.io.FetchPublicFeedService;
import com.assignment.myntra.network.response.FeedResponse;
import com.assignment.myntra.ui.adapters.PhotoGridAdapter;
import com.assignment.myntra.ui.decorations.ItemOffsetDecoration;
import com.assignment.myntra.utils.PreferencesManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GameActivity extends AppCompatActivity {

	private int gameTimerInSeconds = 15;
	private int turns = 0;

	private PhotoGridAdapter photoGridAdapter;

	private ImageView currentPhotoForIdentificationImageView;
	private TextView timerTextView, resultTextView;
	private RelativeLayout gameAreaLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_game);

		timerTextView = (TextView) findViewById(R.id.timer);
		gameAreaLayout = (RelativeLayout) findViewById(R.id.game_area_layout);
		currentPhotoForIdentificationImageView = (ImageView) findViewById(R.id.current_photo_for_identification);
		resultTextView = (TextView) findViewById(R.id.result);

		RecyclerView photoGridRecyclerView = (RecyclerView) findViewById(R.id.photo_grid);
		photoGridRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
		photoGridAdapter = new PhotoGridAdapter(this, null);
		photoGridRecyclerView.setAdapter(photoGridAdapter);
		int gapDimen = getResources().getDimensionPixelOffset(R.dimen.gap_8);
		photoGridRecyclerView.addItemDecoration(new ItemOffsetDecoration(new Rect(0, gapDimen, gapDimen, gapDimen), 3));

		fetchImages();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void fetchImages() {
		showProgress();
		new FetchPublicFeedService().fetchFeed(this);
	}

	private void showProgress() {
		findViewById(R.id.progress_layout).setVisibility(View.VISIBLE);
	}

	private void hideProgress() {
		findViewById(R.id.progress_layout).setVisibility(View.GONE);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void handleSuccessFetchImageResponse(FeedResponse feedResponse) {
		photoGridAdapter.setItems(feedResponse.getItems());
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void handleFailureFetchImageResponse(String errorMessage) {
		Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		finish();
	}

	public void startGame() {
		hideProgress();
		timerTextView.setVisibility(View.VISIBLE);
		startGameTimer();
	}

	private void startGameTimer() {
		setTime();
		timerCounterHandler.postDelayed(runnable, 1000);
	}

	private void setTime() {
		timerTextView.setText(gameTimerInSeconds + "");
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			timerCounterHandler.sendEmptyMessage(-1);
		}
	};

	private Handler timerCounterHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			gameTimerInSeconds--;
			if (gameTimerInSeconds != 0) {
				setTime();
				timerCounterHandler.postDelayed(runnable, 1000);
			} else {
				timerCounterHandler.removeCallbacks(runnable);
				startPlayerTurn();
			}
		}
	};

	private void startPlayerTurn() {
		timerTextView.setVisibility(View.GONE);
		gameAreaLayout.setVisibility(View.VISIBLE);
		photoGridAdapter.startPlayerTurn();
		updateGameStats();
		showRandomImage();
	}

	private void showRandomImage() {
		String randomUrl = photoGridAdapter.getRandomPhotoUrl();
		Picasso.with(this).load(randomUrl).into(currentPhotoForIdentificationImageView);
		currentPhotoForIdentificationImageView.setTag(randomUrl);
	}

	public void matchPhoto(final int position, final String photoIdentifier) {
		turns++;
		resultTextView.setVisibility(View.VISIBLE);
		String currentPhotoUrl = (String) currentPhotoForIdentificationImageView.getTag();
		if (photoIdentifier.equals(currentPhotoUrl)) {
			//yay, user has identified correctly
			photoGridAdapter.updatePhotoTileWithSuccessfulIdentification(position);
			if (photoGridAdapter.isAllPhotoIdentified()) {
				new AlertDialog.Builder(this).setTitle(R.string.yay)
						.setMessage(getString(R.string.congrats_message, turns))
						.setCancelable(false).setPositiveButton(R.string.great, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						PreferencesManager.getInstance(GameActivity.this).setBestScore(turns);
						setResult(RESULT_OK);
						finish();
					}
				}).show();
			} else {
				resultTextView.setText(R.string.success_message);
				resultTextView.setTextColor(getResources().getColor(R.color.success));
				showRandomImage();
			}
		} else {
			resultTextView.setTextColor(getResources().getColor(R.color.fail));
			resultTextView.setText(R.string.fail_message);
		}
		updateGameStats();
	}

	private void updateGameStats() {
		((TextView) findViewById(R.id.score)).setText(getString(R.string.score, turns));
		((TextView) findViewById(R.id.pending_photo_count)).setText(getString(R.string.pending, photoGridAdapter.getPendingPhotosForIdentificationCount()));
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage(R.string.abort_game_message).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				finish();
			}
		}).setNegativeButton(android.R.string.no, null).show();
	}
}
