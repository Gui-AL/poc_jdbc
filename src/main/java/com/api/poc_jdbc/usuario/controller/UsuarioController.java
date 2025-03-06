package com.api.poc_jdbc.usuario.controller;

import com.api.poc_jdbc.usuario.dto.UsuarioListDTO;
import com.api.poc_jdbc.usuario.model.UsuarioModel;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioModel usuarioModel ;

    @GetMapping("/listar")
    public ResponseEntity<?> listar(@Valid @RequestBody UsuarioListDTO usuarioListDTO) {
        try {
            List<Map<String,Object>> lista = usuarioModel.listar(1, usuarioListDTO);
            return ResponseEntity.ok().body("Listar todos os usuarios");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

}
