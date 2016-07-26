package com.github.star.measuretextviewdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BottomFragment extends DialogFragment {
    public static final String TAG = "BottomFragment";
    public int mWidth;
    private ListView mListView;
    private List<ShouCash> mCashList = new ArrayList<>();
    private CashAdapter mCashAdapter;
    private boolean mForceRefresh;

    public static BottomFragment newInstance() {
        Bundle args = new Bundle();
        BottomFragment fragment = new BottomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initCashList() {
        for (int i = 0; i < 8; i++) {
            mCashList.add(new ShouCash(R.drawable.ic_money_5, 200 * (i + 1), "HK$" + ((i -1 ) * 300.56f + i * 500.01f + 272.00f)));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_shoucash, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view);
        initCashList();
        mCashAdapter = new CashAdapter(getActivity());
        mListView.setAdapter(mCashAdapter);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mForceRefresh = false;
        if (mListView == null || mCashAdapter == null) {
            return;
        }
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCashAdapter != null) {
                    for (int i = 0; i < mCashAdapter.getCount(); i++) {
                        View v = mCashAdapter.getView(i, null, mListView);
                        float px = 300 * (mListView.getResources().getDisplayMetrics().density);
                        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST);
                        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        v.measure(widthMeasureSpec, heightMeasureSpec);
                        CashAdapter.ListHolder holder = (CashAdapter.ListHolder) v.getTag();
                        Log.e(TAG, "holder.shouCashMoney.getMeasuredWidth()==" + holder.shouCashMoney.getMeasuredWidth());
                        if (holder.shouCashMoney.getMeasuredWidth() > mWidth) {
                            mWidth = holder.shouCashMoney.getMeasuredWidth();
                        }
                        Log.e(TAG, "mWidth==" + mWidth);

                    }
                    mCashList.clear();
                    initCashList();
                    mForceRefresh = true;
                    mCashAdapter.notifyDataSetChanged();
                }
            }
        }, 0);
    }

    private class ShouCash {
        public int resId;
        public int count;
        public String price;

        public ShouCash(int resId, int count, String price) {
            this.resId = resId;
            this.count = count;
            this.price = price;
        }
    }

    private class CashAdapter extends BaseAdapter {

        private Context mContext;

        public CashAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mCashList.size();
        }

        @Override
        public Object getItem(int i) {
            return mCashList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, @Nullable View convertView, ViewGroup parent) {
            ListHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.shoucash_dialog_list_item, parent, false);
                holder = new ListHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ListHolder) convertView.getTag();
            }
            ShouCash shoucash = (ShouCash) getItem(position);
            holder.shouCashMoney.setText(shoucash.price);
            holder.shouCashCount.setText("" + shoucash.count);
            if (mWidth > 0 && mForceRefresh) {
                holder.shouCashMoney.setMinWidth(mWidth);
//                holder.shouCashMoney.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright));
            }
            holder.shouCashImageView.setImageResource(shoucash.resId);
            return convertView;
        }

        public class ListHolder {
            TextView shouCashCount;
            ImageView shouCashImageView;
            TextView shouCashMoney;

            public ListHolder(View itemView) {
                shouCashCount = (TextView) itemView.findViewById(R.id.shoucash_item_count);
                shouCashImageView = (ImageView) itemView.findViewById(R.id.shoucash_item_img);
                shouCashMoney = (TextView) itemView.findViewById(R.id.shoucash_item_money);
            }
        }
    }
}