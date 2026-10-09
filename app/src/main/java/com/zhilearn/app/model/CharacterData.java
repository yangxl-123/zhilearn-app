package com.zhilearn.app.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "characters")
public class CharacterData {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "character")
    private String character;

    @ColumnInfo(name = "pinyin")
    private String pinyin;

    @ColumnInfo(name = "meaning")
    private String meaning;

    @ColumnInfo(name = "strokes")
    private int strokes;

    @ColumnInfo(name = "radical")
    private String radical;

    @ColumnInfo(name = "difficulty")
    private int difficulty;

    @ColumnInfo(name = "word1")
    private String word1;

    @ColumnInfo(name = "word2")
    private String word2;

    @ColumnInfo(name = "sentence")
    private String sentence;

    @ColumnInfo(name = "is_learned")
    private boolean isLearned;

    @ColumnInfo(name = "mastery_level")
    private float masteryLevel;

    @ColumnInfo(name = "last_review_time")
    private long lastReviewTime;

    @ColumnInfo(name = "next_review_time")
    private long nextReviewTime;

    @ColumnInfo(name = "review_count")
    private int reviewCount;

    // Room 使用的构造函数
    public CharacterData(@NonNull String character, String pinyin, String meaning,
                         int strokes, String radical, int difficulty,
                         String word1, String word2, String sentence) {
        this.character = character;
        this.pinyin = pinyin;
        this.meaning = meaning;
        this.strokes = strokes;
        this.radical = radical;
        this.difficulty = difficulty;
        this.word1 = word1;
        this.word2 = word2;
        this.sentence = sentence;
        this.isLearned = false;
        this.masteryLevel = 0f;
        this.lastReviewTime = 0;
        this.nextReviewTime = 0;
        this.reviewCount = 0;
    }

    // 完整构造函数（Room 忽略）
    @Ignore
    public CharacterData(@NonNull String character, String pinyin, String meaning,
                         int strokes, String radical, int difficulty,
                         String word1, String word2, String sentence,
                         boolean isLearned, float masteryLevel,
                         long lastReviewTime, long nextReviewTime, int reviewCount) {
        this.character = character;
        this.pinyin = pinyin;
        this.meaning = meaning;
        this.strokes = strokes;
        this.radical = radical;
        this.difficulty = difficulty;
        this.word1 = word1;
        this.word2 = word2;
        this.sentence = sentence;
        this.isLearned = isLearned;
        this.masteryLevel = masteryLevel;
        this.lastReviewTime = lastReviewTime;
        this.nextReviewTime = nextReviewTime;
        this.reviewCount = reviewCount;
    }

    // Getters and Setters
    @NonNull
    public String getCharacter() { return character; }
    public void setCharacter(@NonNull String character) { this.character = character; }

    public String getPinyin() { return pinyin; }
    public void setPinyin(String pinyin) { this.pinyin = pinyin; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public int getStrokes() { return strokes; }
    public void setStrokes(int strokes) { this.strokes = strokes; }

    public String getRadical() { return radical; }
    public void setRadical(String radical) { this.radical = radical; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public String getWord1() { return word1; }
    public void setWord1(String word1) { this.word1 = word1; }

    public String getWord2() { return word2; }
    public void setWord2(String word2) { this.word2 = word2; }

    public String getSentence() { return sentence; }
    public void setSentence(String sentence) { this.sentence = sentence; }

    public boolean isLearned() { return isLearned; }
    public void setLearned(boolean learned) { isLearned = learned; }

    public float getMasteryLevel() { return masteryLevel; }
    public void setMasteryLevel(float masteryLevel) { this.masteryLevel = masteryLevel; }

    public long getLastReviewTime() { return lastReviewTime; }
    public void setLastReviewTime(long lastReviewTime) { this.lastReviewTime = lastReviewTime; }

    public long getNextReviewTime() { return nextReviewTime; }
    public void setNextReviewTime(long nextReviewTime) { this.nextReviewTime = nextReviewTime; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
}
