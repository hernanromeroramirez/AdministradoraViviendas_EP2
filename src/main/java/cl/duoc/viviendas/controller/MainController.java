package cl.duoc.viviendas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {

    // Tipo de vivienda
    @FXML private ComboBox<String> cmbTipo;

    // Datos generales
    @FXML private TextField txtCodigo;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtHabitaciones;

    // Campos de Departamento
    @FXML private Label lblPiso;
    @FXML private TextField txtPiso;

    @FXML private Label lblGastosComunes;
    @FXML private ComboBox<String> cmbGastosComunes;

    @FXML private Label lblEstacionamiento;
    @FXML private ComboBox<String> cmbEstacionamiento;

    // Campos de Casa
    @FXML private Label lblPatio;
    @FXML private ComboBox<String> cmbPatio;

    // Botones
    @FXML private Button btnRegistrar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;

    // Tabla
    @FXML private TableView<?> tablaViviendas;
    @FXML private TableColumn<?, ?> colTipo;
    @FXML private TableColumn<?, ?> colCodigo;
    @FXML private TableColumn<?, ?> colSuperficie;
    @FXML private TableColumn<?, ?> colHabitaciones;
    @FXML private TableColumn<?, ?> colCosto;

    // Mensajes
    @FXML private Label lblMensaje;

    @FXML
    private void initialize() {

        lblMensaje.setText("Sistema listo.");

        // Escuchar los cambios en el tipo de vivienda
        cmbTipo.valueProperty().addListener(
                (observable, anterior, nuevoTipo) -> actualizarFormulario()
        );

        // Estado inicial
        actualizarFormulario();
    }

    private void actualizarFormulario() {

        String tipo = cmbTipo.getValue();

        boolean esDepartamento = "Departamento".equals(tipo);
        boolean esCasa = "Casa".equals(tipo);

        // Mostrar u ocultar campos de Departamento
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

        // Mostrar u ocultar campo de Casa
        lblPatio.setVisible(esCasa);
        lblPatio.setManaged(esCasa);
        cmbPatio.setVisible(esCasa);
        cmbPatio.setManaged(esCasa);
    }
}
