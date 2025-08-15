
package com.tienda.tienda.controller;

import com.tienda.tienda.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    var lista = categoriaService.getCategorias(activo: false);
    
    
    @GetMapping("/listado")
    public String listado(Model model) {
    
        model.addAttribute("categorias", lista);
        model.addAttribute("totalCategorias", lista.size());
        return "/categoria/listado";
    }
    
    @PostMapping("/guardar")
    public String guardar(Categoria categoria, @RequestParam MultipartFile imagenFile) {
        categoriaService.save(categoria);
        /*var lista = categoriaService.getCategorias(activo: false);
        model.addAttribute("categorias", lista);
        model.addAttribute("totalCategorias", lista.size());
*/      //Falta algo aca
        return "redirect:/categoria/listado";
    }
    
    @PostMapping("/eliminar/{idCategoria}")
    public String eliminar(Categoria categoria, @RequestParam MultipartFile imagenFile) {
        categoriaService.delete(categoria);
        return "redirect:/categoria/listado";
    }
    
    @PostMapping("/modificar/{idCategoria}")
    public String modificar(Categoria categoria, Model model;) {
        categoria=categoriaService.delete(categoria);
        model.addAttribute(attributeName: "categoria", attributeValue: categoria);
        return "/categoria/modifica";
    }
    
    
}
