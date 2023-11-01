package com.example.customviewdemo.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PaintResponse(
    val id: Int,
    val userId: String,
    var title: String,
    val createdDate: Long,
    val imagePath: String,
)


@Serializable
data class PaintPost(
    val userId: String,
    val title: String,
    val imagePath: String,
)


class ServerService {

    private val URL_BASE = "http://10.0.2.2:8080"

    // Create an instance of the Ktor HTTP client
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Resources)
    }

    // Get a list of all paints from the server
    @Throws(Exception::class)
    suspend fun getAllPaints(): List<PaintResponse> {
        Log.d("ServerService", "getAllPaints: ")
        return httpClient.get("$URL_BASE/paint").body()
    }

    // Get images created by a specific user
    @Throws(Exception::class)
    suspend fun getCurrentUserPaints(userId: String): List<PaintResponse> {
        Log.d("ServerService", "getCurrentUserPaints: $userId")
        return httpClient.get("$URL_BASE/paint/userId?userId=$userId/").body()
    }


    // Post a new paint to the server
    @Throws(Exception::class)
    suspend fun postNewPaint(paint: PaintPost) {
        Log.d("ServerService", "postNewPaint: $paint")
        return httpClient.post("$URL_BASE/paint/create") {
            contentType(ContentType.Application.Json)
            setBody(paint)
        }.body()
    }

    // Delete a paint by its ID
    @Throws(Exception::class)
    suspend fun deleteAPaintById(paintId: Int) {
        Log.d("ServerService", "deleteAPaintById: $paintId")
        return httpClient.delete("$URL_BASE/paint/delete?paintId=$paintId").body()
    }
}
