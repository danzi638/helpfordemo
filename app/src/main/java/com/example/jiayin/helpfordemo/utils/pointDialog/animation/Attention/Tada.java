package com.example.jiayin.helpfordemo.utils.pointDialog.animation.Attention;

import android.animation.ObjectAnimator;
import android.view.View;

import com.example.jiayin.helpfordemo.utils.pointDialog.animation.BaseAnimatorSet;


public class Tada extends BaseAnimatorSet {
	public Tada() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),//
				ObjectAnimator.ofFloat(view, "rotation", 0, -3, -3, 3, -3, 3, -3, 3, -3, 0));//
	}
}
