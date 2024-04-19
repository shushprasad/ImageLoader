package com.shushprasad.image_loader.model

data class Thumbnail(
    val domain: String,
    val basePath: String,
    val key: String
)

data class MediaCoverage(
    val thumbnails: List<Thumbnail>
)

data class MediaCoveragesWrapper(val mediaCoverages: List<MediaCoverage>)
