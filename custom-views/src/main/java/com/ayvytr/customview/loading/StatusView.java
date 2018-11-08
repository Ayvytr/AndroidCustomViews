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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayvytr.customview.R;


/**
 * 状态View，包含 {@link #LOADING}, {@link #ERROR}, {@link #EMPTY}, {@link #NONE} 4种状态，专用于状态切换的根布局，并且加入了
 * TextView设置当前状态的显示文本（注意id必须是tv_msg才可以，或者如果你的项目这几个状态提示文本固定没有变化，那就忽略这个问题）.
 * 注意：为了看到布局预览，在布局文件中看预览的时候 status 属性一定要为 CONTENT，或者不设置 status属性（默认为CONTENT属性）.
 * 并且建议在Java代码中页面创建时主动设置一次当前状态.
 * @see #showLoading()
 * @see #showLoading(String)
 * @see #showError()
 * @see #showError(String)
 * @see #showEmpty()
 * @see #showEmpty(String)
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.9.0
 */
public class StatusView extends RelativeLayout {
    public static final int NONE = NO_ID;
    public static final int CONTENT = 0;
    public static final int LOADING = 1;
    public static final int ERROR = 2;
    public static final int EMPTY = 3;

    private LayoutParams defaultLp;

    @IntDef({LOADING, ERROR, EMPTY, NONE, CONTENT})
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

    private int mStatus = NONE;

    private View loadingView;
    private View errorView;
    private View emptyView;
    private View contentView;

    private OnClickListener onLoadingClickListener;
    private OnClickListener onErrorClickListener;
    private OnClickListener onEmptyClickListener;

    private OnStatusClickListener onStatusClickListener;

    private void initView(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        getDefaultLayoutParams();

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.StatusView, defStyleAttr, defStyleRes);
        int loadingLayoutId = ta.getResourceId(R.styleable.StatusView_loadingView, R.layout.layout_loading);
        loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, this, false);
        addView(loadingView, defaultLp);
        loadingView.setVisibility(View.GONE);

        int errorLayoutId = ta.getResourceId(R.styleable.StatusView_errorView, R.layout.layout_error);
        errorView = LayoutInflater.from(getContext()).inflate(errorLayoutId, this, false);
        addView(errorView, defaultLp);
        errorView.setVisibility(View.GONE);

        int emptyLayoutId = ta.getResourceId(R.styleable.StatusView_emptyView, R.layout.layout_empty);
        emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutId, this, false);
        addView(emptyView, defaultLp);
        emptyView.setVisibility(View.GONE);

        int contentLayoutId = ta.getResourceId(R.styleable.StatusView_contentView, NO_ID);
        if(contentLayoutId > 0) {
            contentView = LayoutInflater.from(getContext()).inflate(contentLayoutId, this, false);
            LayoutParams contentLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            addView(contentView, contentLp);
        }

        mStatus = ta.getInt(R.styleable.StatusView_status, CONTENT);

        resetDefaultMsg();
        initListener();
        attachLoadingClickListener();
        attachErrorClickListener();
        attachEmptyClickListener();

        ta.recycle();
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //在onLayout调用之前，getChildCount获取的子View数量没有把布局文件中的子View算在内，所以在这里设置状态.
        if(mStatus != CONTENT) {
            setCurrentStatus(mStatus);
        }
        super.onLayout(changed, l, t, r, b);
    }

    private void hideAllViews() {
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mStatus = NONE;
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

    /**
     * 显示Loading
     *
     * @param msg Loading提示文字
     */
    public void showLoading(@Nullable String msg) {
        setMsgByTargetView(msg, loadingView);
        hideViewByStatus();
        if(loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        mStatus = LOADING;
    }

    /**
     * 根据当前状态隐藏View
     */
    private void hideViewByStatus() {
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if(mStatus == LOADING && child == loadingView) {
                continue;
            }

            if(mStatus == ERROR && child == errorView) {
                continue;
            }

            if(mStatus == EMPTY && child == emptyView) {
                continue;
            }

            if(mStatus == CONTENT && child != loadingView && child != errorView && child != emptyView) {
                continue;
            }

            child.setVisibility(View.GONE);
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
        hideViewByStatus();
        if(errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        mStatus = ERROR;
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
        hideViewByStatus();
        if(emptyView.getVisibility() != View.VISIBLE) {
            emptyView.setVisibility(View.VISIBLE);
        }
        mStatus = EMPTY;
    }

    @Status
    public int getCurrentStatus() {
        return mStatus;
    }

    /**
     * 设置当前状态
     *
     * @param status 目标状态
     */
    public void setCurrentStatus(@Status int status) {
        switch(status) {
            case NONE:
                hideAllViews();
                break;
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
     * 显示Content
     */
    public void showContent() {
        hideViewByStatus();
        if(contentView != null) {
            contentView.setVisibility(View.VISIBLE);
        }

        int childCount = getChildCount();
        if(childCount > 0) {
            for(int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if(child == loadingView || child == errorView || child == emptyView) {
                    continue;
                }

                if(child.getVisibility() != View.VISIBLE) {
                    child.setVisibility(View.VISIBLE);
                }
            }
        }

        mStatus = CONTENT;
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
        addView(this.loadingView, getDefaultLayoutParams());
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
        addView(this.errorView, getDefaultLayoutParams());
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
        addView(this.emptyView, getDefaultLayoutParams());
        attachEmptyClickListener();
    }

    /**
     * 设置Content view
     *
     * @param layoutId Content layout id
     */
    public void setContentView(@LayoutRes int layoutId) {
        setContentView(inflateView(layoutId));
    }

    /**
     * 设置Content View
     *
     * @param contentView Content view
     */
    public void setContentView(@Nullable View contentView) {
        if(this.contentView != null) {
            removeView(this.contentView);
        }

        this.contentView = contentView;
        if(this.contentView != null) {
            LayoutParams contentLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            addView(this.contentView, contentLp);

            if(mStatus != CONTENT) {
                this.contentView.setVisibility(View.GONE);
            }

        }
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
