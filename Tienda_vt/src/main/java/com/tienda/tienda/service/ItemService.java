package com.tienda.tienda.service;

import com.tienda.tienda.domain.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class ItemService {

    private final List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(Item probe) {
        if (probe == null || probe.getProducto() == null) return null;
        Long id = probe.getProducto().getIdProducto();
        return items.stream()
                .filter(i -> i.getProducto() != null
                        && i.getProducto().getIdProducto().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Item item) {
        Item existente = getItem(item);
        if (existente == null) {
            if (item.getCantidad() <= 0) item.setCantidad(1);
            items.add(item);
        } else {
            existente.setCantidad(existente.getCantidad() + item.getCantidad());
        }
    }

    public void update(Item item) {
        Item existente = getItem(item);
        if (existente != null) {
            if (item.getCantidad() <= 0) {
                delete(item);
            } else {
                existente.setCantidad(item.getCantidad());
            }
        }
    }

    public void delete(Item item) {
        if (item == null || item.getProducto() == null) return;
        Long id = item.getProducto().getIdProducto();
        items.removeIf(i -> i.getProducto() != null
                && i.getProducto().getIdProducto().equals(id));
    }

    public double getTotal() {
        return items.stream().mapToDouble(Item::getSubtotal).sum();
    }

    public void clear() {
        items.clear();
    }
}

