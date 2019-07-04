package com.aitek.app.mms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @ProjectName: SVA
 * @ClassName: MmsBaseFragment
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-2 下午4:50
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-2 下午4:50
 * @UpdateRemark: 更新说明
 */
public class MmsBaseFragment extends Fragment {
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        int t = Animation.RELATIVE_TO_SELF;
        int duration = 300;
        if (enter) {
            TranslateAnimation animation = new TranslateAnimation(t, 1, t, 0, t, 0, t, 0);
            animation.setDuration(duration);
            return animation;
        } else {
            TranslateAnimation animation = new TranslateAnimation(t, 0, t, 1, t, 0, t, 0);
            animation.setDuration(duration);
            return animation;
        }
    }
}
