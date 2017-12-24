package com.example.jiayin.helpfordemo.utils.pointDialog.animation.FlipExit;

import android.view.View;

import android.animation.ObjectAnimator;
import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;

public class FlipHorizontalExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotationY", 0, 90),//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0));
	}
}
