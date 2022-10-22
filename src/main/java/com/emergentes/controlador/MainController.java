
package com.emergentes.controlador;

import com.emergentes.modelo.Productos;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       // Verificar si la coleccion de personas existe en el objeto session:
        HttpSession ses = request.getSession();
        
        if (ses.getAttribute("listapro") == null){
            // Crear la coleccion y almacenarla en el objeto session:
            ArrayList<Productos> listaux = new ArrayList<Productos>();
            // Colocar listaux como atributo de session:
            ses.setAttribute("listapro", listaux);
        }
        
        ArrayList<Productos> lista = (ArrayList<Productos>)ses.getAttribute("listapro");
        
        String op = request.getParameter("op");
        String opcion = (op != null) ? request.getParameter("op") : "view";
        
        Productos obj1 = new Productos();
        int id;
        int pos;
        switch (opcion){
            //Inserta nuevo Registro
            case "nuevo":
                request.setAttribute("miProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "editar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request,id);
                obj1 = lista.get(pos);
                request.setAttribute("miProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "eliminar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request,id);
                lista.remove(pos);
                response.sendRedirect("index.jsp");
                break;
            case "view":
                //Colocamos Como un atributo:
                ses.setAttribute("listapro", lista);
                response.sendRedirect("index.jsp");
                break;
                
        }
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession ses = request.getSession();
        ArrayList<Productos> lista = (ArrayList<Productos>) ses.getAttribute("listapro");
        
        Productos obj1 = new Productos();
        int idt;
        
        obj1.setId(Integer.parseInt(request.getParameter("id")));
        obj1.setDescripcion(request.getParameter("descripcion"));
        obj1.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        obj1.setPrecio(Integer.parseInt(request.getParameter("precio")));
        
       idt = obj1.getId();
       if(idt == 0){
        
        obj1.setId(ultimoId(request));
        lista.add(obj1); 
       }
       else{
           lista.set(buscarIndice(request,idt), obj1);
       }
        
        response.sendRedirect("index.jsp");
    }
    private int ultimoId(HttpServletRequest request){
        HttpSession ses = request.getSession();
        ArrayList<Productos> lista = (ArrayList<Productos>)ses.getAttribute("listapro");
        
        int idaux = 0;
        for (Productos item:lista){
            idaux = item.getId();
        }
        return idaux + 1;
    }

    private int buscarIndice(HttpServletRequest request, int id)
    {
        HttpSession ses = request.getSession();
        ArrayList<Productos> lista = (ArrayList<Productos>)ses.getAttribute("listapro");
        int i = 0;
        if (lista.size()>0){
            while (i< lista.size()){
                if (lista.get(i).getId() == id){
                    break;
                }else{
                    i++;
                }
            }    
        }
        return i;
    
    }

}
