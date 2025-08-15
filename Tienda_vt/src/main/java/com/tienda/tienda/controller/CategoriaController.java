
package com.tienda.tienda.controller;

import com.tienda.tienda.service.CategoriaService;
import com.tienda.domain.Categoria;
import com.tienda.service.FirebaseStorageService;
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
    private CategoriaService CategoriaService; // Recupero el servicio
    
    @GetMapping("/listado")
    public String listado(Model model){
        var categorias=CategoriaService.getCategorias(false); // Recuperando el array list
        model.addAttribute("categorias",categorias); // inyectando el array list para que se vea en el html
        
        return"/categoria/listado";
    }
    
    @GetMapping("/eliminar/{idCategoria}")  // Cuando se pasa un atributo en la url se crea el objeto categoría y le setea el idCategoria solo
    public String eliminar(Categoria categoria){
        CategoriaService.delete(categoria);
        return"redirect:/categoria/listado";  // Con esto se le dice a la página que se ejecute el url, en este caso el método y funciona como darle refrescar
    }                                         // Con esto se mostrará la lista de categorías por reutilizar el método de listado
    
    
    @GetMapping("/modificar/{idCategoria}") 
    public String modificar(Categoria categoria, Model model){
        categoria = CategoriaService.getCategoria(categoria);
        
        model.addAttribute("categoria", categoria);
        
        return"/categoria/modifica";
    }
    
    
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    @PostMapping("/guardar") 
    public String guardar(Categoria categoria, 
           @RequestParam("imagenFile")MultipartFile imagenFile){
        
        if(!imagenFile.isEmpty()){
            
            CategoriaService.save(categoria);
            // Sube la imagen al storage y devuelve la ruta
            String ruta = firebaseStorageService.cargaImagen(imagenFile,"categoria",categoria.getIdCategoria());
           
            categoria.setRutaImagen(ruta);
        }
        CategoriaService.save(categoria);
        
        return"redirect:/categoria/listado";
    }
}
