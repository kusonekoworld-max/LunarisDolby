/*
 * Copyright (C) 2024-2026 Lunaris AOSP
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lunaris.dolby.data.autoeq

import android.content.Context
import org.json.JSONArray
import org.lunaris.dolby.DolbyConstants
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoEqRepository(private val context: Context) {

    private var index: List<IndexEntry> = emptyList()

    suspend fun initialize() = withContext(Dispatchers.IO) {
        if (index.isNotEmpty()) return@withContext
        try {
            val json = fetch(INDEX_URL)
            val array = JSONArray(json)
            val entries = ArrayList<IndexEntry>(array.length())
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                entries.add(
                    IndexEntry(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        source = obj.optString("source", ""),
                        measurementRig = obj.optString("measurementRig", ""),
                    )
                )
            }
            index = entries
        } catch (e: Exception) {
            DolbyConstants.dlog(TAG, "Failed to fetch AutoEQ index: ${e.message}")
        }
    }

    suspend fun search(query: String): List<IndexEntry> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext index
        index.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.source.contains(query, ignoreCase = true) ||
                it.measurementRig.contains(query, ignoreCase = true)
        }
    }

    suspend fun getProfile(id: String): AutoEqProfile? = withContext(Dispatchers.IO) {
        try {
            val json = fetch(profileUrl(id))
            val obj = org.json.JSONObject(json)
            AutoEqProfile(
                name = obj.getString("name"),
                graphicEq = obj.getString("graphicEq"),
            )
        } catch (e: Exception) {
            DolbyConstants.dlog(TAG, "Failed to fetch AutoEQ profile $id: ${e.message}")
            null
        }
    }

    private fun fetch(urlString: String): String {
        val connection = URL(urlString).openConnection() as HttpURLConnection
        connection.connectTimeout = CONNECT_TIMEOUT_MS
        connection.readTimeout = READ_TIMEOUT_MS
        try {
            return connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private fun profileUrl(id: String) = "$PROFILES_BASE_URL/$id.json"

    companion object {
        private const val TAG = "AutoEqRepository"
        private const val CONNECT_TIMEOUT_MS = 10_000
        private const val READ_TIMEOUT_MS = 10_000
        private const val INDEX_URL =
            "https://raw.githubusercontent.com/Pong-Development/DolbyProfiles/main/index.json"
        private const val PROFILES_BASE_URL =
            "https://raw.githubusercontent.com/Pong-Development/DolbyProfiles/main/profiles"
    }
}
