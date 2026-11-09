package cl.duoc.viviendas.controller;

import cl.duoc.viviendas.model.Vivienda;
import cl.duoc.viviendas.model.Departamento;
import cl.duoc.viviendas.model.Casa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;

import java.text.NumberFormat;
import java.util.Locale;

public class MainController {

    // Tipo de vivienda
    @FXML private ComboBox<String> cmbTipo;

    // Datos generales
    @FXML private TextField txtCodigo;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtHabitaciones;

    // Datos de Departamento
    @FXML private Label lblPiso;
    @FXML private TextField txtPiso;

    @FXML private Label lblGastosComunes;
    @FXML private ComboBox<String> cmbGastosComunes;

    @FXML private Label lblEstacionamiento;
    @FXML private ComboBox<String> cmbEstacionamiento;

    // Datos de Casa
    @FXML private Label lblPatio;
    @FXML private ComboBox<String> cmbPatio;

    // Botones
    @FXML private Button btnRegistrar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    // Tabla
    @FXML private TableView<Vivienda> tablaViviendas;
    @FXML private TableColumn<Vivienda, String> colTipo;
    @FXML private TableColumn<Vivienda, String> colCodigo;
    @FXML private TableColumn<Vivienda, String> colSuperficie;
    @FXML private TableColumn<Vivienda, String> colHabitaciones;
    @FXML private TableColumn<Vivienda, String> colCosto;

    // Mensajes
    @FXML private Label lblMensaje;

    // Lista de viviendas registradas
    private final ObservableList<Vivienda> viviendas =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        // Conectar la lista con la tabla
        tablaViviendas.setItems(viviendas);

        // Configurar columnas
        configurarTabla();

        // Detectar cambios en el tipo de vivienda
        cmbTipo.valueProperty().addListener(
                (observable, anterior, nuevoTipo) -> actualizarFormulario()
        );

        // Conectar botones
        btnRegistrar.setOnAction(event -> registrarVivienda());
        btnLimpiar.setOnAction(event -> limpiarFormulario());
        btnEliminar.setOnAction(event -> eliminarVivienda());

        actualizarFormulario();

        lblMensaje.setText("Sistema listo.");
    }

    private void configurarTabla() {

        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipo()));

        colCodigo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCodigo()));

        colSuperficie.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.valueOf(data.getValue().getSuperficie())));

        colHabitaciones.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.valueOf(data.getValue().getHabitaciones())));

        colCosto.setCellValueFactory(data -> {
            NumberFormat formato =
                    NumberFormat.getCurrencyInstance(
                            Locale.forLanguageTag("es-CL"));

            String costo = formato.format(
                    data.getValue().calcularCostoArriendo());

            return new SimpleStringProperty(costo);
        });
    }

    private void actualizarFormulario() {

        String tipo = cmbTipo.getValue();

        boolean esDepartamento = "Departamento".equals(tipo);
        boolean esCasa = "Casa".equals(tipo);

        // Campos de Departamento
        lblPiso.setVisible(esDepartamento);
        lblPiso.setManaged(esDepartamento);
        txtPiso.setVisible(esDepartamento);
        txtPiso.setManaged(esDepartamento);

        lblGastosComunes.setVisible(esDepartamento);
        lblGastosComunes.setManaged(esDepartamento);
        cmbGastosComunes.setVisible(esDepartamento);
        cmbGastosComunes.setManaged(esDepartamento);

        lblEstacionamiento.setVisible(esDepartamento);
        lblEstacionamiento.setManaged(esDepartamento);
        cmbEstacionamiento.setVisible(esDepartamento);
        cmbEstacionamiento.setManaged(esDepartamento);

        // Campos de Casa
        lblPatio.setVisible(esCasa);
        lblPatio.setManaged(esCasa);
        cmbPatio.setVisible(esCasa);
        cmbPatio.setManaged(esCasa);
    }

    private void registrarVivienda() {

        try {

            // Validar tipo de vivienda
            String tipo = cmbTipo.getValue();

            if (tipo == null) {
                throw new IllegalArgumentException(
                        "Seleccione un tipo de vivienda.");
            }

            // Leer datos generales
            String codigo = txtCodigo.getText().trim();

            double superficie = Double.parseDouble(
                    txtSuperficie.getText().trim().replace(",", "."));

            int habitaciones = Integer.parseInt(
                    txtHabitaciones.getText().trim());

            Vivienda nuevaVivienda;

            if ("Departamento".equals(tipo)) {

                int piso = Integer.parseInt(txtPiso.getText().trim());

                if (piso <= 0) {
                    throw new IllegalArgumentException(
                            "El piso debe ser mayor que cero.");
                }

                String gastos = cmbGastosComunes.getValue();

                if (gastos == null) {
                    throw new IllegalArgumentException(
                            "Seleccione el estado de los gastos comunes.");
                }

                String estacionamiento =
                        cmbEstacionamiento.getValue();

                if (estacionamiento == null) {
                    throw new IllegalArgumentException(
                            "Seleccione si tiene estacionamiento.");
                }

                boolean gastosAlDia = "Sí".equals(gastos);

                Departamento departamento = new Departamento(
                        codigo, superficie, habitaciones, piso, gastosAlDia);

                if ("Sí".equals(estacionamiento)) {
                    departamento.asignarEstacionamiento();
                }

                nuevaVivienda = departamento;

            } else {

                String patio = cmbPatio.getValue();

                if (patio == null) {
                    throw new IllegalArgumentException(
                            "Seleccione si la casa tiene patio.");
                }

                boolean tienePatio = "Sí".equals(patio);

                nuevaVivienda = new Casa(
                        codigo, superficie, habitaciones, tienePatio);
            }

            // Incorporar vivienda a la tabla
            viviendas.add(nuevaVivienda);

            lblMensaje.setText(
                    "Vivienda " + codigo + " registrada correctamente.");

            limpiarFormulario();

        } catch (NumberFormatException e) {

            lblMensaje.setText(
                    "Error: revise los campos numéricos.");

        } catch (IllegalArgumentException e) {

            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {

        cmbTipo.setValue(null);

        txtCodigo.clear();
        txtSuperficie.clear();
        txtHabitaciones.clear();
        txtPiso.clear();

        cmbGastosComunes.setValue(null);
        cmbEstacionamiento.setValue(null);
        cmbPatio.setValue(null);

        actualizarFormulario();
    }

    private void eliminarVivienda() {

        Vivienda seleccionada =
                tablaViviendas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            lblMensaje.setText(
                    "Seleccione una vivienda para eliminar.");
            return;
        }

        viviendas.remove(seleccionada);

        lblMensaje.setText(
                "Vivienda eliminada correctamente.");
    }
}
