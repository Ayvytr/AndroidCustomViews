package com.ayvytr.customview.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayvytr.customview.R;


/**
 * 状态View，包含 {@link #LOADING}, {@link #ERROR}, {@link #EMPTY}, {@link #CONTENT} 4种状态，专用于状态切换的根布局，并且加入了
 * TextView设置当前状态的显示文本（注意id必须是tv_msg才可以，或者如果你的项目这几个状态提示文本固定没有变化，那就忽略这个问题）.
 * 注意：为了看到布局预览，在布局文件中看预览的时候 status 属性一定要为 CONTENT，或者不设置 status属性（默认为CONTENT属性）.
 * 并且建议在Java代码中页面创建时主动设置一次当前状态.
 * 注意：addView时，请别添加View到0-2的索引，内部View占用了.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @see #showLoading()
 * @see #showLoading(String)
 * @see #showError()
 * @see #showError(String)
 * @see #showEmpty()
 * @see #showEmpty(String)
 * @since 0.9.0
 * @since 1.3.0 修改了View管理失效问题
 */
public class StatusView extends RelativeLayout {
    public static final int LOADING = 0;
    public static final int ERROR = 1;
    public static final int EMPTY = 2;
    public static final int CONTENT = -1;

    private LayoutParams defaultLp;

    @IntDef({LOADING, ERROR, EMPTY, CONTENT})
    private @interface Status {}

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr, R.style.LoadingViewDefaultStyle);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs, defStyleAttr, defStyleRes);
    }

    private int mStatus = CONTENT;

    private View loadingView;
    private View errorView;
    private View emptyView;

    private OnClickListener onLoadingClickListener;
    private OnClickListener onErrorClickListener;
    private OnClickListener onEmptyClickListener;

    private OnStatusClickListener onStatusClickListener;

    private void initView(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        getDefaultLayoutParams();

        TypedArray ta = getContext()
                .obtainStyledAttributes(attrs, R.styleable.StatusView, defStyleAttr, defStyleRes);
        int loadingLayoutId = ta
                .getResourceId(R.styleable.StatusView_loadingView, R.layout.layout_loading);
        loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, this, false);
        addView(loadingView, LOADING, defaultLp);
        loadingView.setVisibility(View.GONE);

        int errorLayoutId = ta
                .getResourceId(R.styleable.StatusView_errorView, R.layout.layout_error);
        errorView = LayoutInflater.from(getContext()).inflate(errorLayoutId, this, false);
        addView(errorView, ERROR, defaultLp);
        errorView.setVisibility(View.GONE);

        int emptyLayoutId = ta
                .getResourceId(R.styleable.StatusView_emptyView, R.layout.layout_empty);
        emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutId, this, false);
        addView(emptyView, EMPTY, defaultLp);
        emptyView.setVisibility(View.GONE);

        mStatus = ta.getInt(R.styleable.StatusView_status, CONTENT);

        ta.recycle();

        initStatus();
        resetDefaultMsg();
        initListener();
        attachLoadingClickListener();
        attachErrorClickListener();
        attachEmptyClickListener();
    }

    private void initStatus() {
        switch(mStatus) {
            case LOADING:
                showLoading();
                break;
            case ERROR:
                showError();
                break;
            case EMPTY:
                showEmpty();
                break;
            case CONTENT:
                showContent();
                break;
            default:
                break;
        }
    }

    /**
     * 必须是MATCH_PARENT，不然显示/隐藏View的时候View显示不全了.
     *
     * @return {@link #defaultLp}
     */
    private LayoutParams getDefaultLayoutParams() {
        if(defaultLp == null) {
            defaultLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            defaultLp.addRule(CENTER_IN_PARENT);
        }
        return defaultLp;
    }

    private void attachEmptyClickListener() {
        emptyView.setOnClickListener(onEmptyClickListener);
    }

    private void attachErrorClickListener() {
        errorView.setOnClickListener(onErrorClickListener);
    }

    private void attachLoadingClickListener() {
        loadingView.setOnClickListener(onLoadingClickListener);
    }

    private void initListener() {
        onLoadingClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onStatusClickListener != null) {
                    onStatusClickListener.onLoading(StatusView.this);
                }
            }
        };
        onErrorClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onStatusClickListener != null) {
                    onStatusClickListener.onError(StatusView.this);
                }
            }
        };
        onEmptyClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onStatusClickListener != null) {
                    onStatusClickListener.onEmpty(StatusView.this);
                }
            }
        };
    }

    private void setMsgByTargetView(@Nullable String msg, @NonNull View targetView) {
        if(TextUtils.isEmpty(msg)) {
            return;
        }

        TextView tvMsg = targetView.findViewById(R.id.tv_msg);
        if(tvMsg != null) {
            tvMsg.setText(msg);
        }
    }

    /**
     * 显示Loading
     */
    public void showLoading() {
        showLoading(null);
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getErrorView() {
        return errorView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    /**
     * 显示Loading
     *
     * @param msg Loading提示文字
     */
    public void showLoading(@Nullable String msg) {
        Log.e("tag", "showLoading");
        setMsgByTargetView(msg, loadingView);
        if(mStatus != LOADING) {
            mStatus = LOADING;
            getEmptyView().setVisibility(View.GONE);
            getErrorView().setVisibility(View.GONE);
            if(loadingView.getVisibility() != View.VISIBLE) {
                loadingView.setVisibility(View.VISIBLE);
            }
            hideContentView();
        }
    }

    private void showContentView() {
        for(int i = EMPTY + 1; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(child.getVisibility() != View.VISIBLE) {
                child.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideContentView() {
        for(int i = EMPTY + 1; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE) {
                child.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示Error
     */
    public void showError() {
        showError(null);
    }

    /**
     * 显示Error
     *
     * @param msg Error提示文字
     */
    public void showError(@Nullable String msg) {
        setMsgByTargetView(msg, errorView);
        if(mStatus != ERROR) {
            mStatus = ERROR;
            getLoadingView().setVisibility(View.GONE);
            getEmptyView().setVisibility(View.GONE);
            if(errorView.getVisibility() != View.VISIBLE) {
                errorView.setVisibility(View.VISIBLE);
            }
            hideContentView();
        }
    }

    /**
     * 显示Empty
     */
    public void showEmpty() {
        showEmpty(null);
    }

    /**
     * 显示Empty
     *
     * @param msg Empty提示文字
     */
    public void showEmpty(@Nullable String msg) {
        setMsgByTargetView(msg, emptyView);
        if(mStatus != EMPTY) {
            mStatus = EMPTY;
            getLoadingView().setVisibility(View.GONE);
            getErrorView().setVisibility(View.GONE);
            if(emptyView.getVisibility() != View.VISIBLE) {
                emptyView.setVisibility(View.VISIBLE);
            }
            hideContentView();
        }
    }

    @Status
    public int getCurrentStatus() {
        return mStatus;
    }


    /**
     * 显示Content
     */
    public void showContent() {
        if(mStatus != CONTENT) {
            mStatus = CONTENT;
            getLoadingView().setVisibility(View.GONE);
            getErrorView().setVisibility(View.GONE);
            getEmptyView().setVisibility(View.GONE);
            showContentView();
        }
    }

    /**
     * 重置默认提示文字
     */
    public void resetDefaultMsg() {
        setMsgByTargetView(getContext().getString(R.string.loading_loading), loadingView);
        setMsgByTargetView(getContext().getString(R.string.loading_error), errorView);
        setMsgByTargetView(getContext().getString(R.string.loading_empty), emptyView);
    }

    private View inflateView(int layoutId) {
        return LayoutInflater.from(getContext()).inflate(layoutId, this, false);
    }

    /**
     * 设置Loading View
     *
     * @param layoutId Loading layout id
     */
    public void setLoadingView(@LayoutRes int layoutId) {
        setLoadingView(inflateView(layoutId));
    }

    /**
     * 设置Loading View
     *
     * @param loadingView Loading View
     */
    public void setLoadingView(@NonNull View loadingView) {
        removeView(this.loadingView);
        this.loadingView = loadingView;
        if(mStatus != LOADING) {
            this.loadingView.setVisibility(View.GONE);
        }
        addView(this.loadingView, LOADING, getDefaultLayoutParams());
        attachLoadingClickListener();
    }

    /**
     * 设置Error View
     *
     * @param layoutId Error layout id
     */
    public void setErrorView(@LayoutRes int layoutId) {
        setErrorView(inflateView(layoutId));
    }

    /**
     * 设置Error View
     *
     * @param errorView Error View
     */
    public void setErrorView(@NonNull View errorView) {
        removeView(this.errorView);
        this.errorView = errorView;
        if(mStatus != ERROR) {
            this.errorView.setVisibility(View.GONE);
        }
        addView(this.errorView, ERROR, getDefaultLayoutParams());
        attachErrorClickListener();
    }

    /**
     * 设置Empty View
     *
     * @param layoutId Empty layout id
     */
    public void setEmptyView(@LayoutRes int layoutId) {
        setEmptyView(inflateView(layoutId));
    }

    /**
     * 设置Empty View
     *
     * @param emptyView Empty View
     */
    public void setEmptyView(@NonNull View emptyView) {
        removeView(this.emptyView);
        this.emptyView = emptyView;
        if(mStatus != EMPTY) {
            this.emptyView.setVisibility(View.GONE);
        }
        addView(this.emptyView, EMPTY, getDefaultLayoutParams());
        attachEmptyClickListener();
    }


    /**
     * 设置状态View点击监听
     *
     * @param onStatusClickListener {@link OnStatusClickListener}
     */
    public void setOnStatusClickListener(OnStatusClickListener onStatusClickListener) {
        this.onStatusClickListener = onStatusClickListener;
    }

    public interface OnStatusClickListener {
        /**
         * 加载中的点击事件
         *
         * @param statusView {@link StatusView}
         */
        void onLoading(StatusView statusView);

        /**
         * 加载错误的点击事件
         *
         * @param statusView {@link StatusView}
         */
        void onError(StatusView statusView);

        /**
         * 空数据时的点击事件
         *
         * @param statusView {@link StatusView}
         */
        void onEmpty(StatusView statusView);
    }
}
