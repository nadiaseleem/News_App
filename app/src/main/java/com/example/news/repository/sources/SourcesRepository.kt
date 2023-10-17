package com.example.news.repository.sources

import com.example.news.data.api.sourcesModel.Source

interface SourcesRepository {
    suspend fun getSources(category: String): List<Source?>?
}