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

import java.lang.ref.WeakReference;
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

        final WeakListChangedListener<T> mWeakOnListChangedCallback = new WeakListChangedListener<>(this);

        public RecyclerViewAdapter(Context context, List<T> list, @LayoutRes int itemTemplate) {
            mLayoutInflater = LayoutInflater.from(context);
            setParams(list, itemTemplate);
        }

        public void setParams(List<T> list, int itemTemplate) {
            if (itemTemplate != mResourceId || list != mList) {
                mResourceId = itemTemplate;

                if (mList instanceof ObservableList) {
                    ((ObservableList<T>) mList).removeOnListChangedCallback(mWeakOnListChangedCallback);
                }
                mList = list;
                if (mList instanceof ObservableList) {
                    //ObservableList from ViewModel MUST NOT hold strong reference of RecyclerView.Adapter,
                    //Otherwise may cause resource leak
                    ((ObservableList<T>) mList).addOnListChangedCallback(mWeakOnListChangedCallback);
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

    static class WeakListChangedListener<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {

        final WeakReference<RecyclerView.Adapter> weakReference;

        public WeakListChangedListener(RecyclerView.Adapter adapter) {
            weakReference = new WeakReference<>(adapter);
        }

        static final RecyclerView.Adapter EMPTY = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

        @NonNull
        RecyclerView.Adapter ensureGetReference(ObservableList<T> sender) {
            RecyclerView.Adapter adapter = weakReference.get();
            if (adapter == null) {
                sender.removeOnListChangedCallback(this);
            }
            return adapter != null ? adapter : EMPTY;
        }

        @Override
        public void onChanged(ObservableList<T> sender) {
            ensureGetReference(sender).notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
            ensureGetReference(sender).notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
            ensureGetReference(sender).notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
            for (int i = 0; i < itemCount; i++) {
                ensureGetReference(sender).notifyItemMoved(fromPosition + i, toPosition + i);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
            ensureGetReference(sender).notifyItemRangeRemoved(positionStart, itemCount);
        }
    }
}
