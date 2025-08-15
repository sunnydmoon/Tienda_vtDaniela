package com.tienda.tienda.service;

import com.tienda.tienda.domain.Factura;
import com.tienda.tienda.domain.Item;
import com.tienda.tienda.domain.Usuario;
import com.tienda.tienda.domain.Venta;
import com.tienda.tienda.repository.FacturaRepository;
import com.tienda.tienda.repository.ProductoRepository;
import com.tienda.tienda.repository.UsuarioRepository;
import com.tienda.tienda.repository.VentaRepository;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    //Permite crear una única instancia de ItemRepository, y la crea automáticamente
    @Autowired
    private HttpSession session;
    @Autowired
    private ProductoRepository productoRepository;

    public List<Item> getItems() {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        return lista;
    }

    public Item getItem(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        if (lista != null) {
            for (Item i : lista) {
                if (Objects.equals(i.getIdProducto(), item.getIdProducto())) {
                    return i;
                }
            }
        }
        return null;
    }

    public void save(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        if (lista == null) {
            lista = new ArrayList<>();
        }
        var existe = false;
        for (Item i : lista) {
            if (Objects.equals(i.getIdProducto(), item.getIdProducto())) {
                existe = true;
                if (i.getCantidad() < i.getExistencias()) {
                    i.setCantidad(i.getCantidad() + 1);
                }
                break;
            }
        }
        if (!existe) {
            item.setCantidad(1);
            lista.add(item);
        }
        session.setAttribute("listaItems", lista);
    }

    public void delete(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        if (lista != null) {
            var posición = -1;
            var existe = false;
            for (Item i : lista) {
                posición++;
                if (Objects.equals(i.getIdProducto(), item.getIdProducto())) {
                    existe = true;
                    break;
                }
            }
            if (existe) {
                lista.remove(posición);
                session.setAttribute("listaItems", lista);
            }
        }
    }

    public void update(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        if (lista != null) {
            for (Item i : lista) {
                if (Objects.equals(i.getIdProducto(), item.getIdProducto())) {
                    i.setCantidad(item.getCantidad());
                    session.setAttribute("listaItems", lista);
                    break;
                }
            }
        }
    }

    public double getTotal() {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        double total = 0.0;
        if (lista != null) {
            for (Item i : lista) {
                total += i.getCantidad() * i.getPrecio();
            }
        }
        return total;
    }

    @Autowired
    private UsuarioRepository usuarioRepository;
   
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private VentaRepository ventaRepository;

    public void facturar() {
        //Se debe recuperar el usuario autenticado y obtener su idUsuario
        String username = "";
        var principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            if (principal != null) {
                username = principal.toString();
            }
        }

        if (username.isBlank()) {
            System.out.println("username en blanco...");
            return;
        }

        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            System.out.println("Usuario no existe en usuarios...");
            return;
        }

        //Se debe registrar la factura incluyendo el usuario
        Factura factura = new Factura(usuario.getIdUsuario());
        factura = facturaRepository.save(factura);

        //Se debe registrar las ventas de cada producto -actualizando existencias-
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List) session.getAttribute("listaItems");
        if (listaItems != null) {
            double total = 0;
            for (Item i : listaItems) {
                var producto = productoRepository.getReferenceById(i.getIdProducto());
                if (producto.getExistencias() >= i.getCantidad()) {
                    Venta venta = new Venta(factura.getIdFactura(),
                            i.getIdProducto(),
                            i.getPrecio(),
                            i.getCantidad());
                    ventaRepository.save(venta);
                    producto.setExistencias(producto.getExistencias() - i.getCantidad());
                    productoRepository.save(producto);
                    total += i.getCantidad() * i.getPrecio();
                }
            }

            //Se debe registrar el total de la venta en la factura
            factura.setTotal(total);
            facturaRepository.save(factura);

            //Se debe limpiar el carrito la lista...
            listaItems.clear();
        }

    }
}

