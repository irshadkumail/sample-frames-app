package com.example.perftester.client

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("image") var image: String? = null,
)

/*

data class ArticlesResponse(
    @SerializedName("results") var articleList: List<Article> = arrayListOf()
)



data class Article(
    @SerializedName("uri") var uri: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("asset_id") var assetId: Int? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("published_date") var publishedDate: String? = null,
    @SerializedName("updated") var updated: String? = null,
    @SerializedName("section") var section: String? = null,
    @SerializedName("subsection") var subsection: String? = null,
    @SerializedName("nytdsection") var nytdsection: String? = null,
    @SerializedName("adx_keywords") var adxKeywords: String? = null,
    @SerializedName("column") var column: String? = null,
    @SerializedName("byline") var byline: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("abstract") var abstract: String? = null,
    @SerializedName("des_facet") var desFacet: ArrayList<String> = arrayListOf(),
    @SerializedName("org_facet") var orgFacet: ArrayList<String> = arrayListOf(),
    @SerializedName("per_facet") var perFacet: ArrayList<String> = arrayListOf(),
    @SerializedName("geo_facet") var geoFacet: ArrayList<String> = arrayListOf(),
    @SerializedName("media") var media: ArrayList<Media> = arrayListOf(),
    @SerializedName("eta_id") var etaId: Int? = null
)

data class Media(
    @SerializedName("type") var type: String? = null,
    @SerializedName("subtype") var subtype: String? = null,
    @SerializedName("caption") var caption: String? = null,
    @SerializedName("copyright") var copyright: String? = null,
    @SerializedName("approved_for_syndication") var approvedForSyndication: Int? = null,
    @SerializedName("media-metadata") var mediaMetadata: ArrayList<MediaMetadata> = arrayListOf()
)

data class MediaMetadata(
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("format")
    var format: String? = null,
    @SerializedName("height")
    var height: Int? = null,
    @SerializedName("width")
    var width: Int? = null
)*/
