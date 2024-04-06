package jp.speakbuddy.edisonandroidexercise.storage

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import jp.speakbuddy.edisonandroidexercise.FactStore
import java.io.InputStream
import java.io.OutputStream

object FactStoreSerializer : Serializer<FactStore> {

    override val defaultValue: FactStore
        get() = FactStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): FactStore {
        try {
            return FactStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: FactStore, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.factDataStore: DataStore<FactStore> by dataStore(
    fileName = "fact_store.pb", serializer = FactStoreSerializer
)
