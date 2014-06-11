package edu.nanoracket.npr.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import edu.nanoracket.npr.R;

public class LoadMoreNewsListView extends ListView {

    protected static final String TAG = "LoadMoreNewsListView";
    private View footerView;
    private OnScrollListener onScrollListener;
    private OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;
    private int currentScrollState;

    public LoadMoreNewsListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    public LoadMoreNewsListView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);

    }

    public LoadMoreNewsListView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        footerView = View.inflate(context, R.layout.load_more_footer, null);
        addFooterView(footerView);
        hideFooterView();
        super.setOnScrollListener(superOnScrollListener);
    }

    private void hideFooterView(){
        footerView.setVisibility(View.GONE);
    }

    private void showFooterView(){
        footerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l){
        onScrollListener = l;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }

    public void onLoadMoreComplete() {
        isLoading = false;
        hideFooterView();
    }

    private OnScrollListener superOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            currentScrollState = scrollState;

            if(onScrollListener != null) {
                onScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            if(onScrollListener != null){
                onScrollListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
            }

            if(visibleItemCount == totalItemCount){
                hideFooterView();
            }else if(!isLoading && (firstVisibleItem + visibleItemCount >= totalItemCount)
                                && currentScrollState != SCROLL_STATE_IDLE) {
                showFooterView();
                isLoading = true;
                if(onLoadMoreListener != null){
                    onLoadMoreListener.onLoadMore();
                }
            }

        }
    };

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
