package com.aluracursos.casadelcodigo.service;

import java.util.List;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

    <T> List<T> obtenerLista(String json, Class<T> clase);
}
