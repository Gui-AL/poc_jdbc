package com.api.poc_jdbc.usuario.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
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

    /*Exemplo de excessão para utilização seria na listagem das rotas, metodo RotaDAO.listarRotas*/
    public List<Map<String, Object>> listarComCamposJson(String sql, int usrId, Object parametros) {
        List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql, new Object[]{usrId, getAsJSON(parametros)});

        for (Map<String, Object> item : lista) {
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                Object valor = entry.getValue();
                if (valor instanceof PGobject) {
                    // Lógica para processar PGobject
                    PGobject pGobject = (PGobject) valor;
                    String result = pGobject.getValue();
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<Map<String, Object>> jsonValue = mapper.readValue(result, new TypeReference<List<Map<String, Object>>>() {});
                        entry.setValue(jsonValue); // Atualiza o valor apenas se necessário
                    } catch (JsonProcessingException e) {
                        // Trate a exceção de forma adequada
                        System.err.println("Erro ao processar o JSON: " + e.getMessage());
                    }
                }
            }
        }
        return lista;
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
