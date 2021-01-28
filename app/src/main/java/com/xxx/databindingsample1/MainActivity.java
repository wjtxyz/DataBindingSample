package com.xxx.databindingsample1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.xxx.databindingsample1.adapter.TextViewBindingAdapter;
import com.xxx.databindingsample1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.sbStart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextViewBindingAdapter.setColorText(binding.tv6, binding.tv4.getText(), binding.sbStart.getProgress(), binding.sbEnd.getProgress(),
                        Color.rgb(binding.sbR.getProgress(), binding.sbG.getProgress(), binding.sbB.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbEnd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextViewBindingAdapter.setColorText(binding.tv6, binding.tv4.getText(), binding.sbStart.getProgress(), binding.sbEnd.getProgress(),
                        Color.rgb(binding.sbR.getProgress(), binding.sbG.getProgress(), binding.sbB.getProgress()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextViewBindingAdapter.setColorText(binding.tv6, binding.tv4.getText(), binding.sbStart.getProgress(), binding.sbEnd.getProgress(),
                        Color.rgb(binding.sbR.getProgress(), binding.sbG.getProgress(), binding.sbB.getProgress()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextViewBindingAdapter.setColorText(binding.tv6, binding.tv4.getText(), binding.sbStart.getProgress(), binding.sbEnd.getProgress(),
                        Color.rgb(binding.sbR.getProgress(), binding.sbG.getProgress(), binding.sbB.getProgress()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextViewBindingAdapter.setColorText(binding.tv6, binding.tv4.getText(), binding.sbStart.getProgress(), binding.sbEnd.getProgress(),
                        Color.rgb(binding.sbR.getProgress(), binding.sbG.getProgress(), binding.sbB.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void onTextView2Click(View v) {
        Log.d(TAG, "onTextView1Click: ");
    }
}
