package com.example.jiayin.helpfordemo.utils.pointDialog.animation.FlipEnter;

import android.view.View;
import android.animation.ObjectAnimator;
import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;

public class FlipHorizontalEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				// ObjectAnimator.ofFloat(view, "rotationY", -90, 0));
				ObjectAnimator.ofFloat(view, "rotationY", 90, 0));
	}
}
