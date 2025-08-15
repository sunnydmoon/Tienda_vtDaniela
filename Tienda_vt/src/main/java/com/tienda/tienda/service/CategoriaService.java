
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
    
    @Transactional(readOnly=true)
    public Categoria getCategoria(Categoria categoria) {
    return categoriaRepository.findById(id: categoria.getIdCategoria())
    .orElse(other:null);
    }
    
    @Transactional
    public void save(Categoria categoria) {
        categoriaRepository.save(entity: catgeoria);
        
    }
    @Transactional
    public void delate(Categoria categoria) {
        try {
        categoriaRepository.delete(entity: catgeoria);
        categoriaRepository.flush();
        
        return true;
                } catch (Exception e) {
                return false;
                }
    }
    
    
}
