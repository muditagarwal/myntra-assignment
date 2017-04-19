package com.assignment.myntra.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.myntra.R;
import com.assignment.myntra.network.response.FeedResponse;
import com.assignment.myntra.ui.activities.GameActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Mudit Agarwal on 18-04-2017.
 */

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {

	private static final int GRID_SIZE = 9;

	private GameActivity gameActivity;
	private FeedResponse.Item[] items;

	private int downloadedPhotoCount = 0;
	private boolean hasPlayerTurnStarted = false;
	private Set<Integer> identifiedPhotoPositions = new HashSet<>();

	private Picasso picasso;

	public PhotoGridAdapter(final GameActivity gameActivity, final FeedResponse.Item[] items) {
		this.gameActivity = gameActivity;
		this.items = items;
		picasso = Picasso.with(gameActivity);
	}

	@Override
	public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_grid, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {

		String url = items[position].getMedia().getM();
		if (isAllPhotosReady()) {
			picasso.load(url).into(holder.photoImageView);
		}

		if (!hasPlayerTurnStarted || identifiedPhotoPositions.contains(position)) {
			holder.identifyPhotoTextView.setVisibility(View.GONE);
			holder.photoImageView.setVisibility(View.VISIBLE);
		} else {
			holder.identifyPhotoTextView.setVisibility(View.VISIBLE);
			holder.photoImageView.setVisibility(View.GONE);
		}
		holder.photoImageView.setTag(url);
		holder.identifyPhotoTextView.setTag(url);
	}

	@Override
	public int getItemCount() {
		//because we need a 3x3 grid
		return items != null ? GRID_SIZE : 0;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView identifyPhotoTextView;
		private ImageView photoImageView;

		public ViewHolder(final View itemView) {
			super(itemView);

			identifyPhotoTextView = (TextView) itemView.findViewById(R.id.identify_photo);
			photoImageView = (ImageView) itemView.findViewById(R.id.photo);

			identifyPhotoTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					gameActivity.matchPhoto(getAdapterPosition(), (String) v.getTag());
				}
			});
		}
	}

	public void setItems(final FeedResponse.Item[] items) {
		this.items = items;
		for (int i = 0; i < GRID_SIZE; i++) {
			Picasso.with(gameActivity).load(items[i].getMedia().getM()).fetch(new Callback() {
				@Override
				public void onSuccess() {
					downloadedPhotoCount++;
					//we have all the images, now show the grid to user and start the 15 second counter
					if (isAllPhotosReady()) {
						//load the images
						notifyDataSetChanged();
						//start the game counter
						gameActivity.startGame();
					}
				}

				@Override
				public void onError() {

				}
			});
		}
		notifyDataSetChanged();
	}

	public void startPlayerTurn() {
		hasPlayerTurnStarted = true;
		notifyDataSetChanged();
	}

	private boolean isAllPhotosReady() {
		return downloadedPhotoCount == GRID_SIZE;
	}

	public void updatePhotoTileWithSuccessfulIdentification(int position) {
		identifiedPhotoPositions.add(position);
		notifyItemChanged(position);
	}

	public int getPendingPhotosForIdentificationCount() {
		return GRID_SIZE - identifiedPhotoPositions.size();
	}

	public String getRandomPhotoUrl() {
		Random random = new Random();
		int position = random.nextInt(GRID_SIZE);
		if (identifiedPhotoPositions.contains(position)) {
			return getRandomPhotoUrl();
		} else {
			Log.d("Answer", "Answer => " + position);
			return items[position].getMedia().getM();
		}
	}

	public boolean isAllPhotoIdentified() {
		return GRID_SIZE == identifiedPhotoPositions.size();
	}
}
