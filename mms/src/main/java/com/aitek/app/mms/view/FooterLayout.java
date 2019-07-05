package com.aitek.app.mms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @ProjectName: mms
 * @ClassName: FooterLayout
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-5 下午12:19
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-5 下午12:19
 * @UpdateRemark: 更新说明
 */
public class FooterLayout extends FrameLayout implements CoordinatorLayout.AttachedBehavior {
    public FooterLayout(@NonNull Context context) {
        super(context);
    }

    public FooterLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @NonNull @Override public CoordinatorLayout.Behavior getBehavior() {
        return new HideBottomViewOnScrollBehavior();
    }
}
