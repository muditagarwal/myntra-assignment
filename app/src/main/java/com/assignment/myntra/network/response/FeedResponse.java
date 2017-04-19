package com.assignment.myntra.network.response;

import com.google.gson.Gson;

public class FeedResponse {

	private Item[] items;

	public Item[] getItems() {
		return items;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public class Item {

		private Media media;

		public Media getMedia() {
			return media;
		}

		@Override
		public String toString() {
			return "ClassPojo [media = " + media + "]";
		}

		public class Media {

			private String m;

			public String getM() {
				return m;
			}

			public void setM(String m) {
				this.m = m;
			}

			@Override
			public String toString() {
				return "ClassPojo [m = " + m + "]";
			}
		}
	}
}
