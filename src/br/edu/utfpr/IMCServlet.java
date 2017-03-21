package br.edu.utfpr;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet({ "/imc-calculator", "/calculadora-imc" })
public class IMCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final float ABAIXO =  18.5f;
	private static final float NORMAL =  25;
	private static final float ACIMA =  30;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		processing(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		processing(request, response);
		
		 
	}
	
	private void processing(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String weight = request.getParameter("weight");
		String height = request.getParameter("height");
		
		weight = weight.replaceAll(",", ".");
		height = height.replaceAll(",", ".");
		
		response.setContentType("text/html");	
		
		PrintWriter out = response.getWriter();
		
		double weightDouble;
		double heightDouble;
		try{
			weightDouble = Double.parseDouble(weight);
			heightDouble = Double.parseDouble(height);
			
			double imc = calculateBMI(weightDouble, heightDouble);
			
			request.getRequestDispatcher("/header.html")
			.include(request, response);
			
			out.printf("<br>Peso: %.2f", weightDouble);
			out.printf("<br>Altura: %.2f", heightDouble);
			out.printf("<br>IMC: %.2f", imc);
			
			if(imc <= ABAIXO){
				out.print("Abaixo do peso");
			}
			else if(imc <= NORMAL){
				out.print("Peso Normal");
			}
			else if(imc <= ACIMA){
				out.print("Acima do peso");
			}
			else{
				out.print("Obeso");
			}
			
			Cookie[] cookies = request.getCookies();
			for(Cookie c : cookies){
				if(c.getName().equals("login-date")){
					out.println("Data de login: " + c.getValue());
				}
			}
			
			
		}
		catch (Exception e) {
//			request.getRequestDispatcher("/error.html")
//			.forward(request, response);
			
			throw new NumberFormatException("Problema de conversão numérica");
		}		
	}
	
	private double calculateBMI(double weight, double height){
		return weight/(height * height);
	}

}
