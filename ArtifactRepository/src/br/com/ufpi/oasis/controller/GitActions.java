package br.com.ufpi.oasis.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
	
	public void requisicaoInicial(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException{
		String url = Constantes.URL_AUTENTICACAO;
	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		responseCode = con.getResponseCode();
		
		response.sendRedirect(url);
	}
	
	public void requisitarAccessToken() throws IOException, JSONException{
		
		String url = Constantes.URL_BASE + "access_token";
		
		JSONObject params = new JSONObject();
		params.put("client_id", Constantes.CLIENT_ID);
		params.put("client_secret", Constantes.CLIENT_SECRET);
		params.put("code", Constantes.CODIGO_AUTORIZACAO);
		
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		
		try(OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream())) {
		   wr.write( params.toString() );
		   wr.flush();
		   wr.close();
		}
		
		responseCode = con.getResponseCode();
		System.out.println("/n#RESULTADO ACCESS TOKEN");
		System.out.println("Status: " + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer resp = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			resp.append(inputLine);
		}
		in.close();
		
		if(responseCode == HttpURLConnection.HTTP_OK){
			System.out.println("Resposta: " + resp);
			Constantes.ACCESS_TOKEN = resp.substring(resp.indexOf("=") + 1,resp.indexOf("&"));
			System.out.println(Constantes.ACCESS_TOKEN);
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
		con.setRequestProperty("Authorization", "token "+Constantes.ACCESS_TOKEN);
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
