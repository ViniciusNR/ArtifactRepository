package br.com.ufpi.oasis.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {
	public static String montarJSON(String[] nomes, String[] valores) throws JSONException{
		JSONObject metadados = new JSONObject();
		ArrayList<JSONObject> params = new ArrayList<>();
		for (int i = 0; i < nomes.length; i++) {
			if(nomes[i] != null && valores[i] != null){
				params.add(new JSONObject().put("nome",nomes[i]).accumulate("valor",valores[i]));
			}
		}
		metadados.put("metadados", new JSONArray(params));
		return metadados.toString();
	}
	
	public static String montarJSON(String[] nomes, String[] valores, String nomeSoftware, String descricao, String versao, String autores) throws JSONException{
		JSONObject metadados = new JSONObject();
		JSONObject infoBasicas = new JSONObject();
		
		ArrayList<JSONObject> params = new ArrayList<>();
		for (int i = 0; i < nomes.length; i++) {
			if(nomes[i] != null && valores[i] != null){
				params.add(new JSONObject().put("nome",nomes[i]).accumulate("valor",valores[i]));
			}
		}
		
		infoBasicas.put("nomeSoftware", nomeSoftware);
		infoBasicas.put("descricao", descricao);
		infoBasicas.put("versao", versao);
		infoBasicas.put("autores", autores);
		
		metadados.put("metadados", new JSONArray(params));
		metadados.put("info-basicas", infoBasicas);
		
		return metadados.toString();
	}

	public static void criarArquivo(String caminho, String contetudo) throws IOException{
		FileWriter arq = new FileWriter(caminho+"/metadados.princ");
		PrintWriter gravarArq = new PrintWriter(arq);
		gravarArq.print(contetudo);
		arq.close();
	}
	
	public static String encode(String s) {
		return Base64.encodeBase64String(StringUtils.getBytesUtf8(s));
	}
	
	public static String decode(String s) {
	    return StringUtils.newStringUtf8(Base64.decodeBase64(s));
	}
}
