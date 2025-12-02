package org.lpss.motosense.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.lpss.motosense.model.DeviceData

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val history: List<DeviceData>
)
