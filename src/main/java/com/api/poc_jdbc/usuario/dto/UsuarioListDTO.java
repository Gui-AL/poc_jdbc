package com.api.poc_jdbc.usuario.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioListDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("nome")
    private String nome;

    @NotBlank(message = "Ativo não pode ser vazio")
    @Pattern(regexp = "^[SN]$", message = "Ativo deve ser 'S' ou 'N'")
    @JsonProperty("ativo")
    private String ativo;

    @JsonProperty("dataCriacao")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dt_created_at;
}
