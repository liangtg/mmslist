package com.aitek.app.mms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @ProjectName: mms
 * @ClassName: HideBottomScrollingBehavior
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-5 下午12:14
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-5 下午12:14
 * @UpdateRemark: 更新说明
 */
public class HideBottomScrollingBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private View footer;
    private long lastDownTime;
    private float startY;
    private int startTop;
    private boolean cancelTouch;

    public HideBottomScrollingBehavior() {
    }

    public HideBottomScrollingBehavior(Context context,
        AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child,
        @NonNull MotionEvent ev) {
        if (cancelTouch) return false;
        if (null == footer) return false;
        Log.d("sc", "intercept " + ev.getAction() + " top: " + child.getTop());
        markTouch(child, ev);
        //return shouldEndTouch(child);
        return false;
    }

    private void markTouch(@NonNull V child,
        @NonNull MotionEvent ev) {
        lastDownTime = ev.getDownTime();
        startY = ev.getY();
        startTop = child.getTop();
    }

    @Override public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child,
        @NonNull MotionEvent ev) {
        if (cancelTouch) return false;
        if (null == footer) return false;
        if (lastDownTime != ev.getDownTime()) {
            markTouch(child, ev);
        }
        int dis = Math.round(startY - ev.getY());
        dis += child.getTop() - startTop;
        //offset(child, child.getTop(), dis);
        if (shouldEndTouch(child)) {
            cancelTouch = true;
            parent.requestDisallowInterceptTouchEvent(true);
            parent.requestDisallowInterceptTouchEvent(false);
            cancelTouch = false;
        }
        return shouldEndTouch(child);
    }

    private boolean shouldEndTouch(V child) {
        if (null == footer) return true;
        return child.getTop() == 0 || child.getTop() == footer.getHeight();
    }

    @Override public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child,
        @NonNull View dependency) {
        return dependency instanceof FooterLayout;
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
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull V child,
        @NonNull View dependency) {
        footer = findFooter(parent);
        return false;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
        @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
        int type) {
        int old = child.getTop();
        if (null != footer) {
            int dis = 0;
            if (dyConsumed == 0) {
                dis = dyUnconsumed;
                //offset(child, old, dis);
                ofsetPadding(child, dis);
            } else if (dyUnconsumed == 0) {
                dis = dyConsumed;
                if (old != 0) {
                    //offset(child, old, dis);
                    ofsetPadding(child, dis);
                }
            }
        }
    }

    private void offset(@NonNull V child, int old, int dis) {
        int top = old - dis;
        top = Math.min(0, Math.max(-footer.getHeight(), top));
        ViewCompat.offsetTopAndBottom(child, top - old);
    }

    private void ofsetPadding(V child, int dis) {
        int old = child.getPaddingBottom();
        int top = old + dis;
        //top = Math.min(0, Math.max(-footer.getHeight(), top));
        top = Math.min(footer.getHeight(), Math.max(0, top));
        child.setPadding(0, 0, 0, top);
    }

    private View findFooter(CoordinatorLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof FooterLayout) return child;
        }
        return null;
    }
}
