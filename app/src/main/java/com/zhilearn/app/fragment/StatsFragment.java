package com.zhilearn.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zhilearn.app.R;
import com.zhilearn.app.view.RadarView;
import com.zhilearn.app.viewmodel.StatsViewModel;

public class StatsFragment extends Fragment {

    private StatsViewModel viewModel;
    private TextView tvLearnedCount;
    private TextView tvTotalCount;
    private TextView tvMasteryPercent;
    private RadarView radarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        tvLearnedCount = view.findViewById(R.id.tv_stats_learned);
        tvTotalCount = view.findViewById(R.id.tv_stats_total);
        tvMasteryPercent = view.findViewById(R.id.tv_stats_mastery);
        radarView = view.findViewById(R.id.radar_view);

        viewModel = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);
        observeData();

        return view;
    }

    private void observeData() {
        viewModel.getLearnedCount().observe(getViewLifecycleOwner(), count -> {
            tvLearnedCount.setText(String.valueOf(count));
        });

        viewModel.getTotalCount().observe(getViewLifecycleOwner(), total -> {
            tvTotalCount.setText(String.valueOf(total));
        });

        viewModel.getAverageMastery().observe(getViewLifecycleOwner(), avg -> {
            int percent = (int) (avg * 100);
            tvMasteryPercent.setText(percent + "%");

            // 更新雷达图
            float[] values = {
                Math.max(avg, 0.3f),
                Math.max(avg, 0.4f),
                Math.max(avg, 0.35f),
                Math.max(avg * 0.9f, 0.3f),
                Math.max(avg * 0.8f, 0.25f)
            };
            radarView.setValues(values);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refresh();
        }
    }
}
