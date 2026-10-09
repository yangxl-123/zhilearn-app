package com.zhilearn.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zhilearn.app.R;
import com.zhilearn.app.model.CharacterData;
import com.zhilearn.app.utils.TtsHelper;
import com.zhilearn.app.viewmodel.ReviewViewModel;

import java.util.List;

public class ReviewFragment extends Fragment {

    private ReviewViewModel viewModel;
    private TextView tvReviewCount;
    private TextView tvReviewCharacter;
    private TextView tvReviewPinyin;
    private TextView tvReviewMeaning;
    private Button btnRemembered;
    private Button btnForgot;
    private View layoutReview;
    private View layoutEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        tvReviewCount = view.findViewById(R.id.tv_review_count);
        tvReviewCharacter = view.findViewById(R.id.tv_review_character);
        tvReviewPinyin = view.findViewById(R.id.tv_review_pinyin);
        tvReviewMeaning = view.findViewById(R.id.tv_review_meaning);
        btnRemembered = view.findViewById(R.id.btn_remembered);
        btnForgot = view.findViewById(R.id.btn_forgot);
        layoutReview = view.findViewById(R.id.layout_review);
        layoutEmpty = view.findViewById(R.id.layout_empty);

        viewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);

        setupListeners();
        observeData();

        return view;
    }

    private void setupListeners() {
        btnRemembered.setOnClickListener(v -> {
            CharacterData ch = getCurrentCharacter();
            if (ch != null) {
                viewModel.markAsReviewed(ch.getCharacter(), true);
                Toast.makeText(getContext(), "太棒了！已记住 " + ch.getCharacter(), Toast.LENGTH_SHORT).show();
                showNextCharacter();
            }
        });

        btnForgot.setOnClickListener(v -> {
            CharacterData ch = getCurrentCharacter();
            if (ch != null) {
                viewModel.markAsReviewed(ch.getCharacter(), false);
                Toast.makeText(getContext(), "没关系，" + ch.getCharacter() + " 明天再复习", Toast.LENGTH_SHORT).show();
                showNextCharacter();
            }
        });
    }

    private CharacterData currentCharacter;
    private List<CharacterData> reviewList;

    private void observeData() {
        viewModel.getDueForReview().observe(getViewLifecycleOwner(), characters -> {
            if (characters != null) {
                reviewList = characters;
                tvReviewCount.setText("今天需要复习 " + characters.size() + " 个字");

                if (characters.size() > 0) {
                    showCharacter(characters.get(0));
                    layoutReview.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);
                } else {
                    layoutReview.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showCharacter(CharacterData character) {
        currentCharacter = character;
        tvReviewCharacter.setText(character.getCharacter());
        tvReviewPinyin.setText(character.getPinyin());
        tvReviewMeaning.setText(character.getMeaning());
    }

    private CharacterData getCurrentCharacter() {
        return currentCharacter;
    }

    private void showNextCharacter() {
        if (reviewList == null || reviewList.isEmpty()) {
            layoutReview.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            tvReviewCount.setText("今天的复习全部完成！🎉");
            viewModel.refresh();
            return;
        }

        // 移除第一个
        reviewList.remove(0);

        if (reviewList.size() > 0) {
            showCharacter(reviewList.get(0));
            tvReviewCount.setText("还有 " + reviewList.size() + " 个字需要复习");
        } else {
            layoutReview.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            tvReviewCount.setText("今天的复习全部完成！🎉");
            viewModel.refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refresh();
        }
    }
}
