package com.api.poc_jdbc.usuario.model;

import com.api.poc_jdbc.usuario.dto.UsuarioListDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import jakarta.validation.Valid;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class UsuarioModel extends TemplateModel {

    public List<Map<String, Object>> listar(int usrId, Object parametros) {
        String sql = "select * from usuario.listarJSON(?, ?::json) ";
        return listar(sql, usrId, parametros);
    }

    public Map<String, Object> incluir(int usrId, Object parametros) {
        String sql = "select * from usuario.incluirJSON(?, ?::json) ";
        return processar(sql, usrId, parametros);
    }

    public List<Map<String, Object>> listarAtribuicoes(int usrId, Object parametros) {
        String sql = "select * from usuario.listarAtributojson(?, ?::json)";
        return listarComCamposJson(sql, usrId, parametros);
    }
}
