package com.aitek.app.mms;

/**
 * @ProjectName: SVA
 * @ClassName: MmsTitleController
 * @Description: java类作用描述
 * @Author: liangtg
 * @CreateDate: 19-7-3 上午10:02
 * @UpdateUser: 更新者
 * @UpdateDate: 19-7-3 上午10:02
 * @UpdateRemark: 更新说明
 */
public class MmsTitleController {
    private static final TitleView defView = new EmptyTitle();
    private static TitleView titleView = defView;

    public static TitleView getTitleView() {
        return titleView;
    }

    public static void setTitleView(TitleView view) {
        titleView = view;
    }

    public static void reset() {
        titleView = defView;
    }

    public interface TitleView {
        void setTitle(String title);

        void setBackTitle(String title);
    }

    private static class EmptyTitle implements TitleView {

        @Override public void setTitle(String title) {
        }

        @Override public void setBackTitle(String title) {
        }
    }
}
