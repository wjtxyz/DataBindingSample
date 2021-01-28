package com.xxx.databindingsample2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.ListenerUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xxx.databindingsample2.BR;
import com.xxx.databindingsample2.R;

import java.util.Arrays;
import java.util.List;

@InverseBindingMethods({
        @InverseBindingMethod(type = ViewPager.class, attribute = "currentItem")
})
public class ViewPagerBindingAdapter {

    @BindingAdapter(value = {"items", "itemTemplate"}, requireAll = true)
    public static <T> void setAdapter(ViewPager viewPager, List<T> items, @LayoutRes int itemTemplate) {
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null) {
            ((ObservableViewPagerAdapter<T>) adapter).setParams(items, itemTemplate);
        } else {
            viewPager.setAdapter(new ObservableViewPagerAdapter<>(LayoutInflater.from(viewPager.getContext()), items, itemTemplate));
        }
    }

    @BindingAdapter(value = {"items", "itemTemplate"}, requireAll = true)
    public static <T> void setAdapter(ViewPager viewPager, T[] items, @LayoutRes int itemTemplate) {
        setAdapter(viewPager, items != null ? Arrays.asList(items) : null, itemTemplate);
    }

    @BindingAdapter(value = {"onPageScrolled", "onPageSelected", "onPageScrollStateChanged", "currentItemAttrChanged"}, requireAll = false)
    public static void setOnPageChangedListener(ViewPager viewPager, final OnPageScrolled onPageScrolled, final OnPageSelected onPageSelected,
                                                final OnPageScrollStateChanged onPageScrollStateChanged, final InverseBindingListener currentItemChanged) {
        final ViewPager.OnPageChangeListener newValue;
        if (onPageScrolled == null && onPageScrollStateChanged == null && onPageSelected == null && currentItemChanged == null) {
            newValue = null;
        } else {
            newValue = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (onPageScrolled != null) {
                        onPageScrolled.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    if (onPageSelected != null) {
                        onPageSelected.onPageSelected(position);
                    }
                    if (currentItemChanged != null) {
                        currentItemChanged.onChange();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (onPageScrollStateChanged != null) {
                        onPageScrollStateChanged.onPageScrollStateChanged(state);
                    }
                }
            };
        }

        final ViewPager.OnPageChangeListener oldValue = ListenerUtil.trackListener(viewPager, newValue, R.id.viewPagerOnPageChangedListener);
        if (oldValue != null) {
            viewPager.removeOnPageChangeListener(oldValue);
        }
        if (newValue != null) {
            viewPager.addOnPageChangeListener(newValue);
        }
    }

    public interface OnPageScrolled {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    }

    public interface OnPageSelected {
        void onPageSelected(int position);
    }

    public interface OnPageScrollStateChanged {
        void onPageScrollStateChanged(int state);
    }


    static class ObservableViewPagerAdapter<T> extends PagerAdapter {
        private List<T> items;
        private int itemTemplate;
        private LayoutInflater inflater;

        final ObservableList.OnListChangedCallback mOnListChangedCallback = new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                notifyDataSetChanged();
            }
        };

        public ObservableViewPagerAdapter(LayoutInflater inflater, List<T> items, @LayoutRes int itemTemplate) {
            this.inflater = inflater;
            setParams(items, itemTemplate);
        }

        public void setParams(List<T> items, @LayoutRes int itemTemplate) {
            this.itemTemplate = itemTemplate;
            if (this.items instanceof ObservableList) {
                ((ObservableList<T>) this.items).removeOnListChangedCallback(mOnListChangedCallback);
            }
            this.items = items;
            if (this.items instanceof ObservableList) {
                ((ObservableList<T>) this.items).addOnListChangedCallback(mOnListChangedCallback);
            }
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return items != null ? items.size() : 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return ((ViewDataBinding) object).getRoot() == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, itemTemplate, container, true);
            binding.setVariable(BR.viewModel, items.get(position));
            return binding;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(((ViewDataBinding) object).getRoot());
        }
    }
}
