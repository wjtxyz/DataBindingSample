package com.xxx.databindingsample3;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.xxx.databindingsample3.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        binding.setViewModel(new ViewModelProvider(this).get(ViewModel.class));
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
    }

    public static class ViewModel extends AndroidViewModel {
        public final LiveData<List<ResolveInfo>> resolveInfoLiveData = new LiveData<List<ResolveInfo>>(new ObservableArrayList<>()) {
            @Override
            protected void onActive() {
                super.onActive();
                getValue().clear();
                getValue().addAll(getApplication().getPackageManager().queryIntentActivities(Intent.makeMainActivity(null), 0));

                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_PACKAGE_ADDED);
                filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
                filter.addAction(Intent.ACTION_BATTERY_CHANGED);
                filter.addDataScheme("package");
                getApplication().registerReceiver(mBroadcastReceiver, filter);
            }

            @Override
            protected void onInactive() {
                super.onInactive();
                getApplication().unregisterReceiver(mBroadcastReceiver);
            }

            void removeByPackageName(String packageName) {
                for (int i = getValue().size() - 1; i >= 0; i--) {
                    if (getValue().get(i).activityInfo.packageName.equals(packageName)) {
                        getValue().remove(i);
                    }
                }
            }

            final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final String packageName = intent.getData().getSchemeSpecificPart();
                    if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
                        removeByPackageName(packageName);
                    } else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                        getValue().addAll(getApplication().getPackageManager().queryIntentActivities(Intent.makeMainActivity(null).setPackage(packageName), 0));
                    } else if (Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())) {
                        removeByPackageName(packageName);
                        getValue().addAll(getApplication().getPackageManager().queryIntentActivities(Intent.makeMainActivity(null).setPackage(packageName), 0));
                    }
                }
            };
        };

        public ViewModel(@NonNull Application application) {
            super(application);
        }
    }
}
