package com._2toficina.controller

import com._2toficina.dto.AvatarReq
import com._2toficina.dto.AvatarRes
import com._2toficina.entity.Avatar
import com._2toficina.repository.AvatarRepository
import com._2toficina.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.transaction.annotation.Transactional

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/usuarios/{usuarioId}/avatar")
class AvatarController(
    private val usuarioRepository: UsuarioRepository,
    private val avatarRepository: AvatarRepository
) {

    @GetMapping
    fun obter(@PathVariable usuarioId: Int): ResponseEntity<AvatarRes> {
        val avatar = avatarRepository.findByUsuario_Id(usuarioId) ?: return ResponseEntity.status(404).build()
        return ResponseEntity.ok(avatar.toRes())
    }

    @PostMapping
    @Transactional
    fun criar(@PathVariable usuarioId: Int, @RequestBody req: AvatarReq): ResponseEntity<AvatarRes> {
        val usuario = usuarioRepository.findById(usuarioId).orElse(null) ?: return ResponseEntity.status(404).build()
        if (avatarRepository.existsByUsuario_Id(usuarioId)) {
            return ResponseEntity.status(409).build() // já existe, usar PUT
        }
        val salvo = avatarRepository.save(
            Avatar(
                usuario = usuario,
                avatarStyle = req.avatarStyle,
                topType = req.topType,
                accessoriesType = req.accessoriesType,
                facialHairType = req.facialHairType,
                clotheType = req.clotheType,
                eyeType = req.eyeType,
                eyebrowType = req.eyebrowType,
                mouthType = req.mouthType,
                skinColor = req.skinColor
            )
        )
        return ResponseEntity.status(201).body(salvo.toRes())
    }

    @PutMapping
    @Transactional
    fun substituir(@PathVariable usuarioId: Int, @RequestBody req: AvatarReq): ResponseEntity<AvatarRes> {
        val avatar = avatarRepository.findByUsuario_Id(usuarioId) ?: return ResponseEntity.status(404).build()
        avatar.apply {
            avatarStyle = req.avatarStyle
            topType = req.topType
            accessoriesType = req.accessoriesType
            facialHairType = req.facialHairType
            clotheType = req.clotheType
            eyeType = req.eyeType
            eyebrowType = req.eyebrowType
            mouthType = req.mouthType
            skinColor = req.skinColor
        }
        return ResponseEntity.ok(avatarRepository.save(avatar).toRes())
    }

    @PatchMapping
    @Transactional
    fun atualizarParcial(@PathVariable usuarioId: Int, @RequestBody req: AvatarReq): ResponseEntity<AvatarRes> {
        val avatar = avatarRepository.findByUsuario_Id(usuarioId) ?: return ResponseEntity.status(404).build()
        req.avatarStyle?.let { avatar.avatarStyle = it }
        req.topType?.let { avatar.topType = it }
        req.accessoriesType?.let { avatar.accessoriesType = it }
        req.facialHairType?.let { avatar.facialHairType = it }
        req.clotheType?.let { avatar.clotheType = it }
        req.eyeType?.let { avatar.eyeType = it }
        req.eyebrowType?.let { avatar.eyebrowType = it }
        req.mouthType?.let { avatar.mouthType = it }
        req.skinColor?.let { avatar.skinColor = it }
        return ResponseEntity.ok(avatarRepository.save(avatar).toRes())
    }

    @DeleteMapping
    @Transactional
    fun deletar(@PathVariable usuarioId: Int): ResponseEntity<Void> {
        val avatar = avatarRepository.findByUsuario_Id(usuarioId) ?: return ResponseEntity.status(404).build()
        avatarRepository.delete(avatar)
        return ResponseEntity.status(204).build()
    }

    private fun Avatar.toRes() = AvatarRes(
        id = id,
        usuarioId = usuario?.id ?: 0,
        avatarStyle = avatarStyle,
        topType = topType,
        accessoriesType = accessoriesType,
        facialHairType = facialHairType,
        clotheType = clotheType,
        eyeType = eyeType,
        eyebrowType = eyebrowType,
        mouthType = mouthType,
        skinColor = skinColor
    )
}