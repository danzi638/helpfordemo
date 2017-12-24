package com.example.jiayin.helpfordemo.utils.pointDialog.animation.FadeExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;


public class FadeExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration));
	}
}
