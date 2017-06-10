package br.com.ufpi.oasis.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate.Status;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.ufpi.oasis.entidade.Repositorio;
import br.com.ufpi.oasis.util.Constantes;
import br.com.ufpi.oasis.util.Util;

@Controller
public class HomeController {
	Git repositorio;
	String[] tags;
	String[] valores;
	String json = "";
	Repositorio repo;
	File directoryRepo;
	ArrayList<Repositorio> repositorios = new ArrayList<Repositorio>();
	ArrayList<String> repos = new ArrayList<String>();
	GitActions gitActions = new GitActions();
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login(HttpServletRequest request,Model model) throws IOException, JSONException {
		
		//return "index";
		return "login";
	}
	
	//RETORNAR PARA A TELA INICIAL
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,Model model) throws IOException, JSONException {
		//model.addAttribute("repositorios", repositorios);
		Constantes.REPO_OWNER = request.getParameter("username");
		repos = gitActions.listarRepositorios(request);
		model.addAttribute("repositorios", repos);
		Constantes.AUTENTICATION_CODE = request.getParameter("code");
		return "index";
		//return "login";
	}
	
	//TESTE - MÉTODO DE AUTENTICAÇÃO
	@RequestMapping(value = "/autenticar", method = RequestMethod.GET)
	public String autenticar(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		gitActions.requisicaoInicial(request, response);
		//gitActions.listarRepositorios(request);
		return "index";
	}
	
//	//CLONAGEM DE REPOSITÓRIO
//	@RequestMapping(value = "/cloneHttp", method = RequestMethod.GET)
//	public String cloneHttp(Model model,HttpServletRequest request) throws IOException, InvalidRemoteException, TransportException, GitAPIException{
//		String url = request.getParameter("uriClone");
//
//		System.out.println(url);
//		String nomeRepo = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
//		directoryRepo = new File(System.getProperty("user.home") + "/SAR/git/" + nomeRepo);
//
//		if(directoryRepo.exists()) {
//			FileUtils.deleteDirectory(directoryRepo);
//		}
//
//		System.out.println("Cloning from " + url + " to " + directoryRepo);
//		try (Git result = Git.cloneRepository()
//				.setURI(url)
//				.setDirectory(directoryRepo)
//				.call()) {
//			// Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
//			System.out.println("Having repository: " + result.getRepository().getDirectory());
//			repositorio = new Git(result.getRepository());
//		}
//		model.addAttribute("uriClonedRepo", url);
//		model.addAttribute("localPath", directoryRepo);
//		
//		model.addAttribute("repositorios", repos);
//		
//		return "index";
//	}

	//COMMIT LOCAL E PUSH REMOTO
	@RequestMapping(value = "/commit", method = RequestMethod.GET)
	public String commit(Model model,HttpServletRequest request) throws IOException, InvalidRemoteException, TransportException, GitAPIException{
		// Stage all files in the repo including new files
		repositorio.add().addFilepattern(".").call();

		// Stage all changed files, omitting new files, and commit with one command
		repositorio.commit()
		.setAll(true)
		.setMessage("Commit changes to all files")
		.call();

		System.out.println("Committed all changes to repository at " + repositorio.getRepository().getDirectory());
		
		//model.addAttribute("repositorios", repositorios);
		model.addAttribute("repositorios", repos);
				
		CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("vinicius.v94@gmail.com", "vini88010460");
		
		Iterable<PushResult> iterable = repositorio.push()
				.setCredentialsProvider(credentialsProvider)
				.call();
		PushResult pushResult = iterable.iterator().next();
		Status status
		  = pushResult.getRemoteUpdate( "refs/heads/master" ).getStatus();
		System.out.println("Status push: " + status.toString());
		
		return "index";
	}
	
	@RequestMapping(value = "/selecionaRepositorio", method = RequestMethod.GET)
	public String selecionaRepositorio(Model model,HttpServletRequest request) throws IOException, InvalidRemoteException, TransportException, GitAPIException, JSONException{
		String nomeRepo = request.getParameter("repositorio");
		//repo = new Repositorio();
		
//		for (Repositorio item : repositorios) {
//			System.out.println(item.getName() + ":" + nomeRepo);
//			if(item.getName().equals(nomeRepo)){
//				repo = item;
//			}
//		}
//		
//		repositorio = repo.getGitObject();
		
		//json = Util.lerArquivo(repo.getLocalUri()+"/metadados.princ");
		Constantes.REPO_NAME = nomeRepo;
		json = gitActions.getArquivoMetadadosPrincipal(request, nomeRepo, "/metadados.princ");
		System.out.println("JSON: " + json);
		if(json != null){
			Constantes.UPDATE = true;
		}
		
		model.addAttribute("json",json);
		//model.addAttribute("uriClonedRepo", repo.getRemoteUri());
		//model.addAttribute("localPath", repo.getLocalUri());
		
		return "commit";
	}
	
	@RequestMapping(value = "/salvarArquivo", method = RequestMethod.GET)
	public String salvarArquivo(Model model,HttpServletRequest request) throws IOException, InvalidRemoteException, TransportException, GitAPIException, JSONException{
		int i = 0;
		tags = request.getParameterValues("nome");
		valores = request.getParameterValues("valor");
		
		if(tags != null && valores != null){
			String auxTags[] = new String[tags.length];
			String auxValores[] = new String[valores.length];
			
			for (String nome : tags) {
				if(!nome.equals("") && !nome.equals("undefined")){
					auxTags[i++] = nome;
				}
			}
			
			i = 0;
			
			for (String valor : valores) {
				if(!valor.equals("") && !valor.equals("undefined")){
					auxValores[i++] = valor;
				}
			}
			String json = Util.montarJSON(auxTags, auxValores);
			model.addAttribute("json",json);
			gitActions.atualizarArquivoMetadadosPrincipal(json.toString(), Constantes.REPO_NAME, Constantes.UPDATE);
//			Util.criarArquivo(directoryRepo.getPath(), json);
//			System.out.println("DIR: " + directoryRepo.getPath());
		}
		
//		model.addAttribute("uriClonedRepo", repo.getRemoteUri());
//		model.addAttribute("localPath", repo.getLocalUri());
		
		return "commit";
	}
}
