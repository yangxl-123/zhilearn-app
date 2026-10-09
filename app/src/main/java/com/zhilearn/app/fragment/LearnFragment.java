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
import com.zhilearn.app.view.TianZiGeView;
import com.zhilearn.app.viewmodel.LearnViewModel;

public class LearnFragment extends Fragment {

    private LearnViewModel viewModel;

    // 阶段指示器
    private TextView tvPhase1;
    private TextView tvPhase2;
    private TextView tvPhase3;

    // 认字阶段
    private View layoutRecognize;
    private TextView tvBigChar;
    private TextView tvPinyin;
    private TextView tvStrokes;
    private TextView tvRadical;
    private TextView tvMeaning;
    private TextView tvWord1;
    private TextView tvWord2;
    private TextView tvSentence;
    private Button btnPlaySound;
    private Button btnToWrite;

    // 书写阶段
    private View layoutWrite;
    private TianZiGeView tianZiGeView;
    private TextView tvScoreDisplay;
    private Button btnClear;
    private Button btnSubmit;
    private Button btnToPractice;

    // 练习阶段
    private View layoutPractice;
    private TextView tvQuestion;
    private Button btnAnswer1;
    private Button btnAnswer2;
    private Button btnAnswer3;
    private TextView tvPracticeResult;
    private Button btnFinish;

    // 完成阶段
    private View layoutComplete;
    private TextView tvCompleteMessage;
    private Button btnLearnMore;

    // 当前阶段
    private enum Phase { RECOGNIZE, WRITE, PRACTICE, COMPLETE }
    private Phase currentPhase = Phase.RECOGNIZE;

    // 评分记录
    private float writingScore = 0;
    private float practiceScore = 0;
    private int practiceCorrectCount = 0;
    private int practiceTotalCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        initViews(view);
        viewModel = new ViewModelProvider(requireActivity()).get(LearnViewModel.class);
        observeData();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        // 阶段指示器
        tvPhase1 = view.findViewById(R.id.tv_phase1);
        tvPhase2 = view.findViewById(R.id.tv_phase2);
        tvPhase3 = view.findViewById(R.id.tv_phase3);

        // 认字
        layoutRecognize = view.findViewById(R.id.layout_recognize);
        tvBigChar = view.findViewById(R.id.tv_big_char);
        tvPinyin = view.findViewById(R.id.tv_pinyin);
        tvStrokes = view.findViewById(R.id.tv_strokes);
        tvRadical = view.findViewById(R.id.tv_radical);
        tvMeaning = view.findViewById(R.id.tv_meaning);
        tvWord1 = view.findViewById(R.id.tv_word1);
        tvWord2 = view.findViewById(R.id.tv_word2);
        tvSentence = view.findViewById(R.id.tv_sentence);
        btnPlaySound = view.findViewById(R.id.btn_play_sound);
        btnToWrite = view.findViewById(R.id.btn_to_write);

        // 书写
        layoutWrite = view.findViewById(R.id.layout_write);
        tianZiGeView = view.findViewById(R.id.tianzige_view);
        tvScoreDisplay = view.findViewById(R.id.tv_score_display);
        btnClear = view.findViewById(R.id.btn_clear);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnToPractice = view.findViewById(R.id.btn_to_practice);

        // 练习
        layoutPractice = view.findViewById(R.id.layout_practice);
        tvQuestion = view.findViewById(R.id.tv_question);
        btnAnswer1 = view.findViewById(R.id.btn_answer1);
        btnAnswer2 = view.findViewById(R.id.btn_answer2);
        btnAnswer3 = view.findViewById(R.id.btn_answer3);
        tvPracticeResult = view.findViewById(R.id.tv_practice_result);
        btnFinish = view.findViewById(R.id.btn_finish);

        // 完成
        layoutComplete = view.findViewById(R.id.layout_complete);
        tvCompleteMessage = view.findViewById(R.id.tv_complete_message);
        btnLearnMore = view.findViewById(R.id.btn_learn_more);
    }

    private void observeData() {
        viewModel.getCurrentCharacter().observe(getViewLifecycleOwner(), character -> {
            if (character != null) {
                displayCharacter(character);
            }
        });

        viewModel.isLearnComplete().observe(getViewLifecycleOwner(), complete -> {
            if (complete != null && complete) {
                showComplete();
            }
        });
    }

    private void displayCharacter(CharacterData character) {
        tvBigChar.setText(character.getCharacter());
        tvPinyin.setText(character.getPinyin());
        tvStrokes.setText("笔画：" + character.getStrokes() + "画");
        tvRadical.setText("部首：" + character.getRadical());
        tvMeaning.setText(character.getMeaning());
        tvWord1.setText(character.getWord1());
        tvWord2.setText(character.getWord2());
        tvSentence.setText(character.getSentence());
    }

    private void setupClickListeners() {
        // 认字阶段
        btnPlaySound.setOnClickListener(v -> {
            CharacterData ch = viewModel.getCurrentCharacter().getValue();
            if (ch != null) {
                TtsHelper.getInstance(requireContext()).speak(ch.getCharacter());
                TtsHelper.getInstance(requireContext()).speak(ch.getPinyin());
            }
        });

        btnToWrite.setOnClickListener(v -> {
            switchPhase(Phase.WRITE);
        });

        // 书写阶段
        btnClear.setOnClickListener(v -> {
            tianZiGeView.clear();
            tvScoreDisplay.setText("");
        });

        btnSubmit.setOnClickListener(v -> {
            int score = tianZiGeView.evaluateScore(
                viewModel.getCurrentCharacter().getValue().getCharacter()
            );
            writingScore = score;
            tvScoreDisplay.setText("本次得分：" + score + "分");
            Toast.makeText(getContext(), "书写评分：" + score + "分", Toast.LENGTH_SHORT).show();
        });

        btnToPractice.setOnClickListener(v -> {
            if (!tianZiGeView.hasContent()) {
                Toast.makeText(getContext(), "请先在田字格中书写", Toast.LENGTH_SHORT).show();
                return;
            }
            switchPhase(Phase.PRACTICE);
            startPractice();
        });

        // 练习阶段
        View.OnClickListener answerListener = v -> {
            Button clicked = (Button) v;
            String answer = clicked.getText().toString();

            CharacterData ch = viewModel.getCurrentCharacter().getValue();
            if (ch == null) return;

            String correct = ch.getPinyin();
            practiceTotalCount++;

            if (answer.equals(correct)) {
                practiceCorrectCount++;
                tvPracticeResult.setText("✓ 正确！" + ch.getCharacter() + " 的读音是 " + correct);
                tvPracticeResult.setTextColor(0xFF4CAF50);
            } else {
                tvPracticeResult.setText("✗ 不对哦，" + ch.getCharacter() + " 的读音是 " + correct);
                tvPracticeResult.setTextColor(0xFFF44336);
            }

            // 计算练习得分
            practiceScore = practiceTotalCount > 0
                ? (float) practiceCorrectCount / practiceTotalCount * 100f
                : 0f;

            // 显示下一步按钮
            btnFinish.setVisibility(View.VISIBLE);
        };

        btnAnswer1.setOnClickListener(answerListener);
        btnAnswer2.setOnClickListener(answerListener);
        btnAnswer3.setOnClickListener(answerListener);

        btnFinish.setOnClickListener(v -> {
            // 保存学习结果
            CharacterData ch = viewModel.getCurrentCharacter().getValue();
            if (ch != null) {
                viewModel.saveLearningResult(ch.getCharacter(), writingScore, practiceScore);
            }
            switchPhase(Phase.COMPLETE);
        });

        // 完成阶段
        btnLearnMore.setOnClickListener(v -> {
            resetLearning();
            viewModel.moveToNext();
        });
    }

    private void startPractice() {
        CharacterData ch = viewModel.getCurrentCharacter().getValue();
        if (ch == null) return;

        tvQuestion.setText("「" + ch.getCharacter() + "」怎么读？");

        // 生成选项
        String correct = ch.getPinyin();
        String[] options = generateOptions(correct);

        btnAnswer1.setText(options[0]);
        btnAnswer2.setText(options[1]);
        btnAnswer3.setText(options[2]);

        tvPracticeResult.setText("");
        btnFinish.setVisibility(View.GONE);
        practiceCorrectCount = 0;
        practiceTotalCount = 0;
    }

    private String[] generateOptions(String correct) {
        // 从常见拼音中随机选两个作为干扰项
        String[] distractors = {
            "bā", "pā", "mā", "fā", "dā", "tā", "nā", "lā",
            "gē", "kē", "hē", "jī", "qī", "xī", "zhī", "chī",
            "shī", "rì", "zī", "cī", "sī", "yī", "wū", "yǔ"
        };

        String[] result = new String[3];
        result[0] = correct;

        // 随机选两个不同的干扰项
        int idx1 = (int) (Math.random() * distractors.length);
        int idx2 = (int) (Math.random() * distractors.length);
        while (idx2 == idx1 || distractors[idx2].equals(correct)) {
            idx2 = (int) (Math.random() * distractors.length);
        }
        result[1] = distractors[idx1];
        result[2] = distractors[idx2];

        // 打乱顺序
        for (int i = 0; i < 3; i++) {
            int j = (int) (Math.random() * 3);
            String temp = result[i];
            result[i] = result[j];
            result[j] = temp;
        }

        return result;
    }

    private void switchPhase(Phase phase) {
        currentPhase = phase;
        layoutRecognize.setVisibility(View.GONE);
        layoutWrite.setVisibility(View.GONE);
        layoutPractice.setVisibility(View.GONE);
        layoutComplete.setVisibility(View.GONE);

        tvPhase1.setBackgroundResource(R.drawable.phase_inactive);
        tvPhase2.setBackgroundResource(R.drawable.phase_inactive);
        tvPhase3.setBackgroundResource(R.drawable.phase_inactive);

        switch (phase) {
            case RECOGNIZE:
                layoutRecognize.setVisibility(View.VISIBLE);
                tvPhase1.setBackgroundResource(R.drawable.phase_active);
                break;
            case WRITE:
                layoutWrite.setVisibility(View.VISIBLE);
                tvPhase1.setBackgroundResource(R.drawable.phase_active);
                tvPhase2.setBackgroundResource(R.drawable.phase_active);
                break;
            case PRACTICE:
                layoutPractice.setVisibility(View.VISIBLE);
                tvPhase1.setBackgroundResource(R.drawable.phase_active);
                tvPhase2.setBackgroundResource(R.drawable.phase_active);
                tvPhase3.setBackgroundResource(R.drawable.phase_active);
                break;
            case COMPLETE:
                layoutComplete.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showComplete() {
        CharacterData ch = viewModel.getCurrentCharacter().getValue();
        if (ch != null) {
            int avgScore = (int) ((writingScore + practiceScore) / 2);
            tvCompleteMessage.setText(
                "🎉 「" + ch.getCharacter() + "」学习完成！\n" +
                "书写得分：" + (int) writingScore + "分\n" +
                "练习得分：" + (int) practiceScore + "分\n" +
                "综合评分：" + avgScore + "分"
            );
        }
        switchPhase(Phase.COMPLETE);
    }

    private void resetLearning() {
        tianZiGeView.clear();
        tvScoreDisplay.setText("");
        writingScore = 0;
        practiceScore = 0;
        switchPhase(Phase.RECOGNIZE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getContext() != null) {
            TtsHelper.getInstance(requireContext()).shutdown();
        }
    }
}
