package com.xxx.databindingsample3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.xxx.databindingsample3.BR;

import java.util.Arrays;
import java.util.List;

public class RecyclerViewBindingAdapter {
    private static final String TAG = "RecyclerViewBindingAdap";

    @BindingAdapter(value = {"items", "itemTemplate"}, requireAll = false)
    public static <T> void setRecyclerViewAdapter(RecyclerView recyclerView, List<T> list, @LayoutRes int itemTemplate) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof RecyclerViewAdapter) {
            ((RecyclerViewAdapter<T>) adapter).setParams(list, itemTemplate);
        } else {
            recyclerView.setAdapter(new RecyclerViewAdapter<>(recyclerView.getContext(), list, itemTemplate));
        }
    }

    @BindingAdapter(value = {"items", "itemTemplate"}, requireAll = false)
    public static <T> void setRecyclerViewAdapter(RecyclerView recyclerView, T[] list, @LayoutRes int itemTemplate) {
        setRecyclerViewAdapter(recyclerView, list != null ? Arrays.asList(list) : null, itemTemplate);
    }

    static class RecyclerViewAdapter<T> extends RecyclerView.Adapter {
        private final LayoutInflater mLayoutInflater;
        List<T> mList;
        int mResourceId;

        final ObservableList.OnListChangedCallback mOnListChangedCallback = new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                for (int i = 0; i < itemCount; i++) {
                    notifyItemMoved(fromPosition + i, toPosition + i);
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        };


        public RecyclerViewAdapter(Context context, List<T> list, @LayoutRes int itemTemplate) {
            mLayoutInflater = LayoutInflater.from(context);
            setParams(list, itemTemplate);
        }

        public void setParams(List<T> list, int itemTemplate) {
            if(itemTemplate != mResourceId || list != mList){
                mResourceId = itemTemplate;

                if (mList instanceof ObservableList) {
                    ((ObservableList<T>) mList).removeOnListChangedCallback(mOnListChangedCallback);
                }
                mList = list;
                if (mList instanceof ObservableList) {
                    ((ObservableList<T>) mList).addOnListChangedCallback(mOnListChangedCallback);
                }
                notifyDataSetChanged();
            }
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding inflate = DataBindingUtil.inflate(mLayoutInflater, mResourceId, parent, false);
            return new RecyclerView.ViewHolder(inflate.getRoot()) {
            };
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            DataBindingUtil.getBinding(holder.itemView).setVariable(BR.viewModel, mList.get(position));
        }

        @Override
        public int getItemCount() {
            return (mList != null) ? mList.size() : 0;
        }
    }
}
