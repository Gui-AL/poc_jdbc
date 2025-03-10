package com.api.poc_jdbc.usuario.controller;

import com.api.poc_jdbc.usuario.dto.UsuarioListDTO;
import com.api.poc_jdbc.usuario.model.UsuarioModel;
import com.api.poc_jdbc.usuario.pdf.UsuarioPdf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/usuario")
@Tag(name = "UsuarioController", description = "Operações relacionadas a usuários")
public class UsuarioController {

    @Autowired
    private UsuarioModel usuarioModel;

    @Operation(
            summary = "Listar usuários",
            description = "Lista usuarios esperando um objeto UsuarioListDTO"
    )
    @PostMapping("/listar")
    public ResponseEntity<?> listar(@Valid @RequestBody UsuarioListDTO usuarioListDTO) {
        try {
            List<Map<String,Object>> lista = usuarioModel.listar(1, usuarioListDTO);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/listarAtributo")
    public ResponseEntity<?> listarAtributo(@Valid @RequestBody UsuarioListDTO usuarioListDTO, HttpServletResponse resp) {
        try {
            List<Map<String,Object>> lista = usuarioModel.listarAtribuicoes(1, usuarioListDTO);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pdf")
    public void gerarPdf(@Valid @RequestBody UsuarioListDTO usuarioListDTO, HttpServletResponse response) {
        try {
            List<Map<String,Object>> lista = usuarioModel.listar(1, usuarioListDTO);
            UsuarioPdf usuarioPdf = new UsuarioPdf().gerarPdf(response, lista);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
