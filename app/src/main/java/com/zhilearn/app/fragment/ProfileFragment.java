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
import com.zhilearn.app.viewmodel.StatsViewModel;

public class ProfileFragment extends Fragment {

    private StatsViewModel viewModel;
    private TextView tvLearnedDays;
    private TextView tvTotalLearned;
    private TextView tvAppVersion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvLearnedDays = view.findViewById(R.id.tv_learned_days);
        tvTotalLearned = view.findViewById(R.id.tv_total_learned);
        tvAppVersion = view.findViewById(R.id.tv_app_version);

        viewModel = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);

        tvAppVersion.setText("知学 v1.0.0");
        observeData();

        return view;
    }

    private void observeData() {
        viewModel.getLearnedCount().observe(getViewLifecycleOwner(), count -> {
            tvTotalLearned.setText(String.valueOf(count));
            // 简单估算学习天数：每天约5个字
            int days = Math.max(1, count / 5);
            tvLearnedDays.setText(String.valueOf(days));
        });
    }
}
