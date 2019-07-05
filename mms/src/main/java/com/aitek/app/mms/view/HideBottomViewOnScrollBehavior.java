package com.aitek.app.mms.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * @ProjectName: mms
 * @ClassName: HideBottomViewOnScrollBehavior
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-5 上午11:22
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-5 上午11:22
 * @UpdateRemark: 更新说明
 */
public class HideBottomViewOnScrollBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    public static final TimeInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    public static final TimeInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR =
        new FastOutSlowInInterpolator();
    public static final TimeInterpolator FAST_OUT_LINEAR_IN_INTERPOLATOR =
        new FastOutLinearInInterpolator();
    public static final TimeInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR =
        new LinearOutSlowInInterpolator();
    public static final TimeInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    protected static final int ENTER_ANIMATION_DURATION = 225;
    protected static final int EXIT_ANIMATION_DURATION = 175;
    private static final int STATE_SCROLLED_DOWN = 1;
    private static final int STATE_SCROLLED_UP = 2;
    private int height = 0;
    private int currentState = STATE_SCROLLED_UP;
    private ViewPropertyAnimator currentAnimator;

    public HideBottomViewOnScrollBehavior() {
    }

    public HideBottomViewOnScrollBehavior(Context context,
        AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        height = child.getMeasuredHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(
        CoordinatorLayout coordinatorLayout,
        V child,
        View directTargetChild,
        View target,
        int nestedScrollAxes,
        int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(
        CoordinatorLayout coordinatorLayout,
        V child,
        View target,
        int dxConsumed,
        int dyConsumed,
        int dxUnconsumed,
        int dyUnconsumed,
        int type) {
        if (currentState != STATE_SCROLLED_UP && dyConsumed > 0) {
            slideUp(child);
        } else if (currentState != STATE_SCROLLED_DOWN && dyConsumed < 0) {
            slideDown(child);
        }
    }

    protected void slideUp(V child) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
            child.clearAnimation();
        }
        currentState = STATE_SCROLLED_UP;
        animateChildTo(
            child, 0, ENTER_ANIMATION_DURATION, LINEAR_OUT_SLOW_IN_INTERPOLATOR);
    }

    protected void slideDown(V child) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
            child.clearAnimation();
        }
        currentState = STATE_SCROLLED_DOWN;
        animateChildTo(
            child, height, EXIT_ANIMATION_DURATION, FAST_OUT_LINEAR_IN_INTERPOLATOR);
    }

    private void animateChildTo(V child, int targetY, long duration,
        TimeInterpolator interpolator) {
        currentAnimator = child.animate()
            .translationY(targetY)
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        currentAnimator = null;
                    }
                });
    }
}
