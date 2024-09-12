package com.practice.zakatcalculator.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class NisabEntity(
    val silverPrice: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1
)
