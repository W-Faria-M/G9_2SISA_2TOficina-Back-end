package com._2toficina.dto

data class AvatarRes(
    val id: Int,
    val usuarioId: Int,
    val avatarStyle: String,
    val topType: String,
    val accessoriesType: String,
    val facialHairType: String,
    val clotheType: String,
    val eyeType: String,
    val eyebrowType: String,
    val mouthType: String,
    val skinColor: String
)