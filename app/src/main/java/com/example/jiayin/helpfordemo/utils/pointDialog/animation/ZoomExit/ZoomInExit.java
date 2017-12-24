package com.example.jiayin.helpfordemo.utils.pointDialog.animation.ZoomExit;

import android.view.View;

import android.animation.ObjectAnimator;
import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;

public class ZoomInExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.25f, 0),//
				ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.25f, 0),//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0, 0));//
	}
}