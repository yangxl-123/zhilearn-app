package com.zhilearn.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhilearn.app.R;
import com.zhilearn.app.model.CharacterData;
import com.zhilearn.app.viewmodel.LearnViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private LearnViewModel viewModel;
    private TextView tvLearnedCount;
    private TextView tvTotalCount;
    private TextView tvTodayTip;
    private ProgressBar progressBar;
    private RecyclerView recyclerCharacters;
    private Button btnStartLearning;
    private CharacterAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvLearnedCount = view.findViewById(R.id.tv_learned_count);
        tvTotalCount = view.findViewById(R.id.tv_total_count);
        tvTodayTip = view.findViewById(R.id.tv_today_tip);
        progressBar = view.findViewById(R.id.progress_learning);
        recyclerCharacters = view.findViewById(R.id.recycler_characters);
        btnStartLearning = view.findViewById(R.id.btn_start_learning);

        recyclerCharacters.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new CharacterAdapter();
        recyclerCharacters.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(LearnViewModel.class);

        btnStartLearning.setOnClickListener(v -> {
            // 通知父Activity切换到学习Tab
            if (getActivity() != null) {
                // 通过回调或ViewModel通知
            }
        });

        observeData();

        return view;
    }

    private void observeData() {
        viewModel.getUnlearnedCharacters().observe(getViewLifecycleOwner(), characters -> {
            if (characters != null) {
                adapter.setCharacters(characters);
                // 更新统计（字库总字数87）
                int total = 87;
                int learned = total - characters.size();
                tvLearnedCount.setText(String.valueOf(learned));
                tvTotalCount.setText(String.valueOf(total));

                int progress = (int) ((float) learned / total * 100);
                progressBar.setProgress(progress);

                if (characters.size() > 0) {
                    tvTodayTip.setText("今天继续加油！还有 " + characters.size() + " 个字等着你");
                } else {
                    tvTodayTip.setText("恭喜！基础字库已全部学完 🎉");
                }
            }
        });
    }

    // 简单的字符适配器
    private static class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {
        private List<CharacterData> characters;

        public void setCharacters(List<CharacterData> characters) {
            this.characters = characters;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setPadding(32, 32, 32, 32);
            tv.setTextSize(40);
            tv.setGravity(android.view.Gravity.CENTER);
            return new ViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (characters != null && position < characters.size()) {
                holder.tv.setText(characters.get(position).getCharacter());
            }
        }

        @Override
        public int getItemCount() {
            return characters != null ? Math.min(characters.size(), 9) : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            ViewHolder(TextView itemView) {
                super(itemView);
                tv = (TextView) itemView;
            }
        }
    }
}
