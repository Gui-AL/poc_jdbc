package com.api.poc_jdbc.usuario.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TemplateModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> listar(String sql, int usrId, Object parametros) {
        return jdbcTemplate.queryForList(sql, new Object[]{usrId, getAsJSON(parametros)});
    }

    public Map<String, Object> processar(String sql, int usrId, Object parametros) {

        Map<String, Object> retorno = jdbcTemplate.queryForMap(sql, new Object[]{usrId, getAsJSON(parametros)});
        if (retorno.getOrDefault("RETORNO", -1).equals(200)) {
            try {
                jdbcTemplate.getDataSource().getConnection().commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return retorno;
    }

    public static String getAsJSON(Object param) {
        String ret = "";
        try {
            ret = new ObjectMapper().writer().writeValueAsString(param);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
