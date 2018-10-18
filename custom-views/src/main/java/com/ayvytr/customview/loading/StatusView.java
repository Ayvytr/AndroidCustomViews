package com.ayvytr.customview.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayvytr.customview.R;


/**
 * 状态View，包含 {@link #LOADING}, {@link #ERROR}, {@link #EMPTY}, {@link #NONE} 4种状态，专用于状态切换的根布局.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.9.0
 */
public class StatusView extends RelativeLayout {
    public static final int NONE = 0;
    public static final int LOADING = 1;
    public static final int ERROR = 2;
    public static final int EMPTY = 3;
    private LayoutParams defaultLp;

    @IntDef({LOADING, ERROR, EMPTY, NONE})
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
        initView(attrs, defStyleAttr, 0);
    }

    private int mStatus = NONE;

    private View loadingView;
    private View errorView;
    private View emptyView;

    private OnClickListener onLoadingClickListener;
    private OnClickListener onErrorClickListener;
    private OnClickListener onEmptyClickListener;

    private OnStatusClickListener onStatusClickListener;

    private void initView(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        getDefaultLayoutParams();

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.StatusView, defStyleAttr, defStyleRes);
        int loadingLayoutId = ta.getResourceId(R.styleable.StatusView_loadingView, R.layout.layout_loading);
        loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, null);
        addView(loadingView, defaultLp);

        int errorLayoutId = ta.getResourceId(R.styleable.StatusView_errorView, R.layout.layout_error);
        errorView = LayoutInflater.from(getContext()).inflate(errorLayoutId, null);
        addView(errorView, defaultLp);

        int emptyLayoutId = ta.getResourceId(R.styleable.StatusView_emptyView, R.layout.layout_empty);
        emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutId, null);
        addView(emptyView, defaultLp);

        resetDefaultMsg();
        initListener();
        attachLoadingClickListener();
        attachErrorClickListener();
        attachEmptyClickListener();

        ta.recycle();
        hideAllViews();
        showLoading();
    }

    private LayoutParams getDefaultLayoutParams() {
        if(defaultLp == null) {
            defaultLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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

    private void hideAllViews() {
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        mStatus = NONE;
    }

    private void setMsgByTargetView(@Nullable String msg, @NonNull View targetView) {
        if(TextUtils.isEmpty(msg)) {
            return;
        }

        TextView tvMsg = targetView.findViewById(R.id.tvMsg);
        if(tvMsg != null) {
            tvMsg.setText(msg);
        }
    }

    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(@Nullable String msg) {
        setMsgByTargetView(msg, loadingView);

        if(mStatus == LOADING) {
            return;
        }

        hideAllViews();
        loadingView.setVisibility(View.VISIBLE);
        mStatus = LOADING;
    }

    public void showError() {
        showError(null);
    }

    public void showError(@Nullable String msg) {
        setMsgByTargetView(msg, errorView);

        if(mStatus == ERROR) {
            return;
        }

        hideAllViews();
        errorView.setVisibility(View.VISIBLE);
        mStatus = ERROR;
    }

    public void showEmpty() {
        showEmpty(null);
    }

    public void showEmpty(@Nullable String msg) {
        setMsgByTargetView(msg, emptyView);

        if(mStatus == EMPTY) {
            return;
        }

        hideAllViews();
        emptyView.setVisibility(View.VISIBLE);
        mStatus = EMPTY;
    }

    @Status
    public int getCurrentStatus() {
        return mStatus;
    }

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
            default:
                break;
        }
    }

    public void resetDefaultMsg() {
        setMsgByTargetView(getContext().getString(R.string.loading_loading), loadingView);
        setMsgByTargetView(getContext().getString(R.string.loading_error), errorView);
        setMsgByTargetView(getContext().getString(R.string.loading_empty), emptyView);
    }

    public void setLoadingView(@NonNull View loadingView) {
        if(loadingView != null) {
            removeView(this.loadingView);
            this.loadingView = loadingView;
            if(mStatus != LOADING) {
                this.loadingView.setVisibility(View.GONE);
            }
            addView(this.loadingView, getDefaultLayoutParams());
            attachLoadingClickListener();
        }
    }

    public void setErrorView(@NonNull View errorView) {
        if(errorView != null) {
            removeView(this.errorView);
            this.errorView = errorView;
            if(mStatus != ERROR) {
                this.errorView.setVisibility(View.GONE);
            }
            addView(this.errorView, getDefaultLayoutParams());
            attachErrorClickListener();
        }
    }

    public void setEmptyView(@NonNull View emptyView) {
        if(emptyView != null) {
            removeView(this.emptyView);
            this.emptyView = emptyView;
            if(mStatus != EMPTY) {
                this.emptyView.setVisibility(View.GONE);
            }
            addView(this.emptyView, getDefaultLayoutParams());
            attachEmptyClickListener();
        }
    }

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
