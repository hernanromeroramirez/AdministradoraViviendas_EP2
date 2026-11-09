package cl.duoc.viviendas.repository;

import cl.duoc.viviendas.model.Casa;
import cl.duoc.viviendas.model.Departamento;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ViviendaRepository {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final Path archivoDepartamentos =
            Path.of("departamentos.json");

    private final Path archivoCasas =
            Path.of("casas.json");

    // Cargar departamentos desde JSON
    public List<Departamento> cargarDepartamentos()
            throws IOException {

        Type tipoLista = new TypeToken<List<Departamento>>() {}.getType();

        return cargarArchivo(archivoDepartamentos, tipoLista);
    }

    // Cargar casas desde JSON
    public List<Casa> cargarCasas()
            throws IOException {

        Type tipoLista = new TypeToken<List<Casa>>() {}.getType();

        return cargarArchivo(archivoCasas, tipoLista);
    }

    // Guardar departamentos en JSON
    public void guardarDepartamentos(List<Departamento> departamentos)
            throws IOException {

        guardarArchivo(archivoDepartamentos, departamentos);
    }

    // Guardar casas en JSON
    public void guardarCasas(List<Casa> casas)
            throws IOException {

        guardarArchivo(archivoCasas, casas);
    }

    // Método general de lectura
    private <T> List<T> cargarArchivo(Path archivo, Type tipo)
            throws IOException {

        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        String contenido = Files.readString(
                archivo, StandardCharsets.UTF_8);

        if (contenido.isBlank()) {
            return new ArrayList<>();
        }

        List<T> datos = gson.fromJson(contenido, tipo);

        return datos != null ? datos : new ArrayList<>();
    }

    // Método general de escritura
    private void guardarArchivo(Path archivo, Object datos)
            throws IOException {

        String contenido = gson.toJson(datos);

        Files.writeString(
                archivo,
                contenido,
                StandardCharsets.UTF_8
        );
    }
}
