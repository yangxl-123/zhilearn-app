package com.zhilearn.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.zhilearn.app.model.CharacterData;

import java.util.List;

@Dao
public interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CharacterData> characters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CharacterData character);

    @Update
    void update(CharacterData character);

    @Query("SELECT * FROM characters ORDER BY character ASC")
    List<CharacterData> getAllCharacters();

    @Query("SELECT * FROM characters WHERE is_learned = 0 ORDER BY difficulty ASC, character ASC LIMIT :limit")
    List<CharacterData> getUnlearnedCharacters(int limit);

    @Query("SELECT * FROM characters WHERE is_learned = 1 AND next_review_time <= :currentTime ORDER BY next_review_time ASC")
    List<CharacterData> getDueForReview(long currentTime);

    @Query("SELECT * FROM characters WHERE `character` = :ch LIMIT 1")
    CharacterData getCharacter(String ch);

    @Query("UPDATE characters SET is_learned = 1 WHERE `character` = :ch")
    void markAsLearned(String ch);

    @Query("UPDATE characters SET mastery_level = :level, last_review_time = :now, next_review_time = :next, review_count = review_count + 1 WHERE `character` = :ch")
    void updateMastery(String ch, float level, long now, long next);

    @Query("SELECT COUNT(*) FROM characters")
    int getTotalCount();

    @Query("SELECT COUNT(*) FROM characters WHERE is_learned = 1")
    int getLearnedCount();

    @Query("SELECT AVG(mastery_level) FROM characters WHERE is_learned = 1")
    Float getAverageMastery();
}
