package com.sh.michael.simple_notepad.feature_notes.data

import androidx.room.TypeConverter
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor

class BackgroundConverter {

    @TypeConverter
    fun toBackground(value: Int) = enumValues<BackgroundColor>()[value]

    @TypeConverter
    fun fromBackground(value: BackgroundColor) = value.ordinal
}