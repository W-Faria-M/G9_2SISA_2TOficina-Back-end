package com._2toficina.avatar

import com._2toficina.entity.Avatar
import com._2toficina.entity.Usuario

object DefaultAvatarFactory {
    fun create(usuario: Usuario) = Avatar(
        usuario = usuario,
        avatarStyle = "Transparent",
        topType = "NoHair",
        accessoriesType = "Blank",
        facialHairType = "Blank",
        clotheType = "BlazerShirt",
        eyeType = "Default",
        eyebrowType = "Default",
        mouthType = "Default",
        skinColor = "Light"
    )
}