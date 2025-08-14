
package com.tienda.tienda.service;

import com.tienda.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import java.awt.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CategoriaService {
    //Permite crear ua unica instancia de CategoriaRepository, y 
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly=true)
    public List<Categoria> getCategorias(boolean activo) {
    var lista = categoriaRepository.findAll();
    //Se valida si solo se quieren las categorias activas
    if (activo){
    //Solo activos...
    lista.removeIf(c -> !c.isActivo());
    }
    return lista;
        
    }
}
