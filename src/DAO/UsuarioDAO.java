package DAO;

import Entidad.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {

    static final String DB_URL
            = "jdbc:mysql://database-1.cpxq1relua92.us-east-1.rds.amazonaws.com:3306/prestamoequipos";
    static final String DB_DRV
            = "com.mysql.jdbc.Driver";
    static final String DB_USER
            = "admin";
    static final String DB_PASSWD
            = "4waxA687";

    public boolean validar(Usuario usuario) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            resultSet = null;
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Usuario "
                    + "WHERE Id_Usuario = BINARY '" + usuario.getId()
                    + "' AND Contraseña = BINARY '" + usuario.getContraseña() + "'");
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error en SQL" + ex);
            return false;
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
                return resultSet.next();
            } catch (SQLException ex) {

            }
        }
    }

    public Usuario leer(Usuario usuario) {
        Usuario usuarioCompleto = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            resultSet = null;
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Usuario "
                    + "WHERE Id_Usuario = BINARY '" + usuario.getId()
                    + "' AND Contraseña = BINARY '" + usuario.getContraseña() + "'");
            if (resultSet.next()) {
                usuarioCompleto = new Usuario();
                usuarioCompleto.setId(resultSet.getString(1));
                usuarioCompleto.setContraseña(resultSet.getString(2));
                usuarioCompleto.setNombres(resultSet.getString(3));
                usuarioCompleto.setApellidos(resultSet.getString(4));
                usuarioCompleto.setCarrera(resultSet.getString(5));
                usuarioCompleto.setFacultad(resultSet.getString(6));
            }
            return usuarioCompleto;
        } catch (SQLException ex) {
            System.out.println("Error en SQL" + ex);
            return usuarioCompleto;
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
                return usuarioCompleto;
            } catch (SQLException ex) {

            }
        }
    }

}
