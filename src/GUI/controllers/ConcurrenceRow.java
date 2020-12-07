/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.controllers;

import Control.MostrarConcurrencia;
import Entidad.Computador;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLACK;
import javafx.stage.Stage;

/**
 *
 * @author ion
 */
public class ConcurrenceRow {

    public String idEquipo;
    public String idUsuario;
    public String nombreEdificio;
    public String codigoSala;
    public String disponibilidad;
    public Button botonInfo;

    static Computador selectcomputador = new Computador();

    public static Computador getSelectcomputador() {
        return selectcomputador;
    }

    public ConcurrenceRow(String idEquipo, String idUsuario, String nombreEdificio, String codigoSala, String disponibilidad) {
        this.idEquipo = idEquipo;
        this.idUsuario = idUsuario;
        this.nombreEdificio = nombreEdificio;
        this.codigoSala = codigoSala;
        this.disponibilidad = disponibilidad;
        if (idUsuario.equals(" ")) {
            this.disponibilidad = "Disponible";
            this.idUsuario = "N/A";
        } else {
            this.disponibilidad = "No disponible";
        }
        this.botonInfo = new Button("+");
        botonInfo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    System.out.println("El computador seleccionado es: " + idEquipo);
                    selectcomputador.setId(Integer.parseInt(idEquipo));

                    Stage primaryStage = new Stage();
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/GUI/views/equipmentInformation.fxml"));
                        Scene scene = new Scene(root);
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("Prestamo de equipos de computo");
                        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logotipo_UN_16.png")));
                        primaryStage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(ConcurrenceRow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreEdificio() {
        return nombreEdificio;
    }

    public void setNombreEdificio(String codigoEdificio) {
        this.nombreEdificio = codigoEdificio;
    }

    public String getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala) {
        this.codigoSala = codigoSala;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Button getBotonInfo() {
        return botonInfo;
    }

    public void setBotonInfo(Button botonInfo) {
        this.botonInfo = botonInfo;
    }

}
