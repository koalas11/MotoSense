package org.lpss.motosense.storage.room

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import org.lpss.motosense.model.DeviceData

class Converters {
    @TypeConverter
    @OptIn(ExperimentalSerializationApi::class)
    fun fromHistory(history: List<DeviceData>): ByteArray {
        return ProtoBuf.encodeToByteArray(ListSerializer(DeviceData.serializer()), history)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @TypeConverter
    fun toHistory(data: ByteArray): List<DeviceData> {
        return ProtoBuf.decodeFromByteArray(ListSerializer(DeviceData.serializer()), data)
    }
}
