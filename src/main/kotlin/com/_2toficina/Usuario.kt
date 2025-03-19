package com._2toficina

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Usuario(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) var id:Int,
    var nome: String? = null,
    var email: String? = null,
    @JsonIgnore var senha: String? = null,
    var tipo: TipoUsuarioEnum = TipoUsuarioEnum.CLIENTE,
) {

}
