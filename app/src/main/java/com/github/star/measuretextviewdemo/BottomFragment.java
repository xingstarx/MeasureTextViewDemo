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
    private List<Cash> mCashList = new ArrayList<>();
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
            mCashList.add(new Cash(R.drawable.ic_money_5, 200 * (i + 1), "HK$" + ((i - 1) * 300.56f + i * 500.01f + 272.00f)));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_cash, container, false);
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
                        Log.e(TAG, "getMeasuredWidth()==" + holder.cashMoney.getMeasuredWidth());
                        if (holder.cashMoney.getMeasuredWidth() > mWidth) {
                            mWidth = holder.cashMoney.getMeasuredWidth();
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

    private class Cash {
        public int resId;
        public int count;
        public String price;

        public Cash(int resId, int count, String price) {
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.cash_dialog_list_item, parent, false);
                holder = new ListHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ListHolder) convertView.getTag();
            }
            Cash cash = (Cash) getItem(position);
            holder.cashMoney.setText(cash.price);
            holder.count.setText("" + cash.count);
            if (mWidth > 0 && mForceRefresh) {
                holder.cashMoney.setMinWidth(mWidth);
            }
            holder.cashImageView.setImageResource(cash.resId);
            return convertView;
        }

        public class ListHolder {
            TextView count;
            ImageView cashImageView;
            TextView cashMoney;

            public ListHolder(View itemView) {
                count = (TextView) itemView.findViewById(R.id.item_count);
                cashImageView = (ImageView) itemView.findViewById(R.id.item_img);
                cashMoney = (TextView) itemView.findViewById(R.id.item_money);
            }
        }
    }
}