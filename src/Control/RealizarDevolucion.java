package Control;

import DAO.ComputadorDAO;
import DAO.ProgramaDAO;
import DAO.SolicitudDAO;
import Entidad.Programa;
import Entidad.Usuario;
import java.util.ArrayList;

public class RealizarDevolucion {

    SolicitudDAO solicitudDao = new SolicitudDAO();
    ComputadorDAO computadroDao = new ComputadorDAO();
    boolean estadoSolicitud = true;

    public RealizarDevolucion() {
    }

    public boolean makeReturn(Usuario usuario, int id_Equipo, boolean activo) {
        if (activo) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    estadoSolicitud = estadoSolicitud && solicitudDao.ChangeRequestStatus(usuario);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    estadoSolicitud = estadoSolicitud && computadroDao.freeComputer(id_Equipo);
                }
            }).start();
            return estadoSolicitud;
        } else {
            return false;
        }

    }
}
