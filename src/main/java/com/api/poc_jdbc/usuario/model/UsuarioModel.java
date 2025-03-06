package com.api.poc_jdbc.usuario.model;

import java.util.List;
import java.util.Map;


public class UsuarioModel extends TemplateModel {

    public List<Map<String, Object>> listar(int usrId, Object parametros) {
        String sql = "call usuario.listarJSON(?, ?::json) ";
        return listar(sql, usrId, parametros);
    }

    public Map<String, Object> incluir(int usrId, Object parametros) {
        String sql = "call usuario.incluirJSON(?, ?::json) ";
        return processar(sql, usrId, parametros);
    }


}
