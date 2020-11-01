package Control;

import DAO.SolicitudDAO;
import DAO.UsuarioDAO;
import Entidad.Usuario;

public class CargarDatos {

    SolicitudDAO solicitudDao = new SolicitudDAO();
    UsuarioDAO usuarioDao = new UsuarioDAO();
    private Usuario user = new Usuario();
    private boolean activo;
    private String infoIdSolicitud = "";
    private String infoIdEquipo = "";
    private String infoNombreEdificio = "";
    private String infoIdEdificio = "";
    private String infoCodigoSala = "";

    private CargarDatos() {

    }

    private static CargarDatos holder = new CargarDatos();

    public static CargarDatos getInstance() {
        return holder;
    }

    public void cargar(Usuario usuario) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cargarSolicitud(usuario);
                System.out.println("fallo1");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                cargarActivity(usuario);
                System.out.println("fallo2");
            }
        }).start();
    }

    public void cargarUsuario(Usuario usuario) {
        this.setUser(usuarioDao.leer(usuario));
    }

    public void cargarSolicitud(Usuario usuario) {
        String[] datos = solicitudDao.getInfo(usuario);
        infoIdSolicitud = datos[0];
        infoIdEquipo = datos[1];
        infoNombreEdificio = datos[2];
        infoIdEdificio = datos[3];
        infoCodigoSala = datos[4];
    }

    public void cargarActivity(Usuario usuario) {
        if (!solicitudDao.VerifyInactivity(usuario)) {
            activo = true;
        } else {
            activo = false;
        }
    }

    public SolicitudDAO getSolicitudDao() {
        return solicitudDao;
    }

    public void setSolicitudDao(SolicitudDAO solicitudDao) {
        this.solicitudDao = solicitudDao;
    }

    public UsuarioDAO getUsuarioDao() {
        return usuarioDao;
    }

    public void setUsuarioDao(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getInfoIdSolicitud() {
        return infoIdSolicitud;
    }

    public void setInfoIdSolicitud(String infoIdSolicitud) {
        this.infoIdSolicitud = infoIdSolicitud;
    }

    public String getInfoIdEquipo() {
        return infoIdEquipo;
    }

    public void setInfoIdEquipo(String infoIdEquipo) {
        this.infoIdEquipo = infoIdEquipo;
    }

    public String getInfoNombreEdificio() {
        return infoNombreEdificio;
    }

    public void setInfoNombreEdificio(String infoNombreEdificio) {
        this.infoNombreEdificio = infoNombreEdificio;
    }

    public String getInfoIdEdificio() {
        return infoIdEdificio;
    }

    public void setInfoIdEdificio(String infoIdEdificio) {
        this.infoIdEdificio = infoIdEdificio;
    }

    public String getInfoCodigoSala() {
        return infoCodigoSala;
    }

    public void setInfoCodigoSala(String infoCodigoSala) {
        this.infoCodigoSala = infoCodigoSala;
    }

    public static CargarDatos getHolder() {
        return holder;
    }

    public static void setHolder(CargarDatos holder) {
        CargarDatos.holder = holder;
    }
}
