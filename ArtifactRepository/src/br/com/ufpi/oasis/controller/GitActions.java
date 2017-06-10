package br.com.ufpi.oasis.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.ufpi.oasis.util.Constantes;
import br.com.ufpi.oasis.util.Util;

public class GitActions {
	int responseCode;
	
	public void requisicaoInicial(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String url = Constantes.URL_AUTENTICACAO;
	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		responseCode = con.getResponseCode();
		
		if(request.getParameter("code") == null || request.getParameter("code").equals("")){
			response.sendRedirect(url);
		}else{
			Constantes.CODIGO_AUTORIZACAO = request.getParameter("code");
			System.out.println("#AUTENTICAÇÃO");
			System.out.println("Código de Autorização: " + Constantes.CODIGO_AUTORIZACAO);
			System.out.println("Status: " + responseCode);
		}
	}
	
	public String getArquivoMetadadosPrincipal(HttpServletRequest request, String repo,String arquivo) throws IOException, JSONException{
		String url = Constantes.URL_API+"repos/"+Constantes.REPO_OWNER+"/"+repo+"/contents/"+arquivo;
		String conteudo;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		con.connect();
		responseCode = con.getResponseCode();
		
		System.out.println("/n#LER ARQUIVO");
		System.out.println("Status: " + responseCode);
		
		if(responseCode == HttpURLConnection.HTTP_OK){
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
			}
			in.close();
			
			System.out.println("Resposta: " + resp);
			JSONObject json = new JSONObject(resp.toString());
			conteudo = Util.decode(json.getString("content"));
			Constantes.SHA_ARQUIVO = json.getString("sha");
			return conteudo;
		}
		return null;
	}
	
	public ArrayList<String> listarRepositorios(HttpServletRequest request) throws IOException, JSONException{
		String url = Constantes.URL_API+"users/"+Constantes.REPO_OWNER+"/repos";
		ArrayList<String> listaRepositorios = new ArrayList<String>();
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		con.connect();
		responseCode = con.getResponseCode();
		
		System.out.println("/n#LISTA DE REPOSITORIOS");
		System.out.println("Status: " + responseCode);
		
		if(responseCode == HttpURLConnection.HTTP_OK){
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
			}
			in.close();
			
			System.out.println("Resposta: " + resp);
			JSONArray jsonArray = new JSONArray(resp.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				listaRepositorios.add(jsonArray.getJSONObject(i).getString("name"));
			}
			return listaRepositorios;
		}
		return null;
	}
	
	public void atualizarArquivoMetadadosPrincipal(String conteudo, String repo, boolean update) throws JSONException, IOException{
		String url = Constantes.URL_API+"repos/"+Constantes.REPO_OWNER+"/"+repo+"/contents/metadados.princ";
		
		JSONObject arquivoMeta = new JSONObject();
		arquivoMeta.put("message", "UPDATE: metadados.princ");
		arquivoMeta.put("content", Util.encode(conteudo));
		if(update){
			arquivoMeta.put("sha", Constantes.SHA_ARQUIVO);
		}
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("PUT");
		//a916a31a2701e16b3c7d319c72487833398d835f
		con.setRequestProperty("Authorization", "token a916a31a2701e16b3c7d319c72487833398d835f");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		
		try(OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream())) {
		   wr.write( arquivoMeta.toString() );
		   wr.flush();
		   wr.close();
		}	
		
		con.connect();
		responseCode = con.getResponseCode();
		System.out.println("URL: " + url);
		System.out.println("#CRIAÇÃO/UPLOAD ARQUIVO METADADOS.PRINC");
		System.out.println("Status: " + responseCode);
		
		if(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED){
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
			}
			in.close();
			
			System.out.println("Resposta: " + resp);
			JSONObject json = new JSONObject(resp.toString());
			System.out.println("/n#RESPOSTA CRIAÇÃO/UPLOAD ARQUIVO METADADOS.PRINC");
			System.out.println(json.toString());
		}
	}
}
