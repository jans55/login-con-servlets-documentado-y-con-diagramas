/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Aguilar Báez Javier Alejandro y Martínez Barbosa Genaro Enrique (C) Agosto 2017
 * @version 1.0
 * created on 2017-08-29
 */

@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {     
    
    /**
     * Ejecuta peticiones únicamente para obtener (Get) información. 
     * Sin embargo, es más conveniente utilizar el doPost que el doGet en este caso.
     * 
     * @param request La petición del servlet.
     * @param resp La respuesta del servlet.
     * @throws ServletException Lanza una excepción de Servlet por si ocurre un error.
     * @throws IOException Lanza una excepción por si ocurre un error en la IO.
     */
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {        
        resp.setContentType("text/html");        
        String boton1 = request.getParameter("subir"); //Botón para registrarse
        String boton2 = request.getParameter("enter"); //Botón para ingresar
        if (boton1 != null) //Al hacer clic en el botón de registro
        {
            String user = request.getParameter("usuario");     
            String pass = request.getParameter("contraseña");   
            if (!(user.equals("")) && !(pass.equals(""))) //Si ambos campos estan llenos
            {
                try
                {                    
                    //Se crea la conexión y se establece con la base; posteriormente, se ejecuta la instrucción de insertar los valores y se cierra la conexión
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/LoginServlets", "root", "n0m3l0");                    
                    Statement instruccion = conn.createStatement();
                    instruccion.executeUpdate("INSERT INTO Usuario (usuario, contraseña) VALUES ('" + user + "','" + pass + "');");                    
                    response(resp, "Usuario dado de alta exitosamente.");
                    conn.close();
                }
                catch (Exception e)
                {
                    e.getMessage();
                }  
            }
            else
            {
                response(resp, "Ingresa todos los datos.");
            }
        }
        else
        {
            if (boton2 != null) //Al hacer clic en ingresar
            {
                String user = request.getParameter("user");        
                String pass = request.getParameter("password"); 
                if (!(user.equals("")) && !(pass.equals("")))
                {
                    try
                    {
                        //De igual manera, se crea la conexión y se establece con la base; luego, se busca al usuario y, si existe, se verifica su contraseña.
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/LoginServlets", "root", "n0m3l0");   
                        Statement instruccion = conn.createStatement();
                        ResultSet rsX = instruccion.executeQuery("SELECT * FROM Usuario WHERE usuario = '" + user + "';");                        
                        if (rsX.next())
                        {
                            String cont = rsX.getString("contraseña");
                            if (pass.equals(cont))
                            {
                                response(resp, "¡Logeo correcto!");
                            }
                            else
                            {
                                if (pass.equals(""))
                                {
                                    response(resp, "Ingresa una contraseña.");
                                }
                                else
                                {
                                    response(resp, "Logeo incorrecto.");
                                }
                            }
                        }
                        else
                        {
                            response(resp, "El usuario no existe.");
                        }
                        conn.close();
                    }
                    catch(Exception e)
                    {
                        e.getMessage();
                    }
                }
                else
                {
                    response(resp, "Ingresa todos los datos.");
                }
            }
        }        
    }
    
    /**
     * La respuesta del servidor a la petición anterior.      * 
     * 
     * @param msg El mensaje enviado por el servidor para ser desplegado.
     * @param resp La respuesta del servlet.     
     * @throws IOException Lanza una excepción por si ocurre un error en la IO.
     */ 
    
    private void response(HttpServletResponse resp, String msg) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body bgcolor='aquamarine'>");
        out.println("<marquee> <b> <font face='Lucila' color='blue' size='20'>" + msg + "</font> </b> </marquee>");
        out.println("</body>");
        out.println("</html>");
    }  
}
