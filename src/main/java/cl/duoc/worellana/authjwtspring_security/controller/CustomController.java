package cl.duoc.worellana.authjwtspring_security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class CustomController {

    @GetMapping("/first")
    @PreAuthorize("hasAuthority('READ')")
    public String indexSec() {
        return "hola mundo ¡CON seguridad!";
    }
}
