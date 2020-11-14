package Control;

import DAO.ComputadorDAO;
import DAO.ProgramaDAO;
import DAO.ProgramaSolicitudDAO;
import DAO.SolicitudDAO;
import DAO.UsuarioDAO;
import Entidad.Programa;
import Entidad.Computador;
import Entidad.Solicitud;
import Entidad.Usuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.application.Platform;

public class RealizarPrestamo {

    ComputadorDAO computadorDao = new ComputadorDAO();
    SolicitudDAO solicitudDao = new SolicitudDAO();
    ProgramaDAO programaDao = new ProgramaDAO();
    ProgramaSolicitudDAO programaSolicitudDao = new ProgramaSolicitudDAO();
    boolean estadoPrestamo = true;

    public RealizarPrestamo() {
    }

    public ArrayList<Programa> getAllPrograms() {
        ArrayList<Programa> programas = programaDao.getAllProgramsAvailable();
        return programas;
    }

    public ArrayList<String[]> getInfoComputers(ArrayList<Programa> programs) {
        ArrayList<String[]> computadores = new ArrayList<>();
        computadores = computadorDao.getInfoComputersAvailable(programs);
        return computadores;
    }

    public boolean makeBorrow(Usuario usuario, Computador computer, ArrayList<Programa> programs) {
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario(usuario);
        solicitud.setComputador(computer);
        estadoPrestamo = estadoPrestamo && solicitudDao.crear(solicitud);
        if (estadoPrestamo) {
            solicitud.setId(solicitudDao.getIdSolicitud(solicitud));
            for (int i = 0; i < programs.size(); i++) {
                estadoPrestamo = estadoPrestamo && programaSolicitudDao.crear(programs.get(i), solicitud);
            }
            return estadoPrestamo;
        } else {
            return false;
        }
    }
}
