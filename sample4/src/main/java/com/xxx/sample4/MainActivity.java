package com.xxx.sample4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.xxx.sample4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DataObject dataObject = new DataObject("Lee", 10);
        binding.setViewModel(dataObject);
        binding.setLifecycleOwner(this);


        binding.name.setText(dataObject.name);
        binding.age.setText(String.valueOf(dataObject.age));
    }
}
