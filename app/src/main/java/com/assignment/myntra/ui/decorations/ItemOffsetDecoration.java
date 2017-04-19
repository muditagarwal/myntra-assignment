package com.assignment.myntra.ui.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

	private Rect rect;
	private int itemsInARow;

	public ItemOffsetDecoration(Rect rect, int itemsInARow) {
		this.rect = rect;
		this.itemsInARow = itemsInARow;
	}

	public ItemOffsetDecoration(Rect rect) {
		this.rect = rect;
		this.itemsInARow = 1;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		outRect.left = rect.left;
		outRect.right = rect.right;
		outRect.bottom = rect.bottom;

		//special case for grid view
		if (itemsInARow > 1 && parent.getChildAdapterPosition(view) < itemsInARow) {
			outRect.top = rect.top;
		}
	}
}
