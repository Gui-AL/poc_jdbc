package com.api.poc_jdbc.usuario.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder/*Utilizado para poder colocar um valor default no parametro*/
@AllArgsConstructor/*Utilizado para poder colocar um valor default no parametro*/
@NoArgsConstructor/*Utilizado para poder colocar um valor default no parametro*/
public class UsuarioListDTO {

    @Builder.Default/*Utilizado para poder colocar um valor default no parametro*/
    @JsonProperty("id")
    private Integer id = 0;

    @Builder.Default
    @JsonProperty("nome")
    private String nome = "";

    @NotBlank(message = "Ativo não pode ser vazio")
    @Pattern(regexp = "^[SN]$", message = "Ativo deve ser 'S' ou 'N'")
    @JsonProperty("ativo")
    private String ativo;
}
