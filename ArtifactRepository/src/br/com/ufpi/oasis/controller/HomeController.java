package br.com.ufpi.oasis.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.ufpi.oasis.entidade.Login;
import br.com.ufpi.oasis.entidade.Repositorio;
import br.com.ufpi.oasis.util.Constantes;
import br.com.ufpi.oasis.util.Util;

@Controller
public class HomeController {
	String[] tags;
	String[] valores;
	String json = "";
	ArrayList<Repositorio> repositorios = new ArrayList<Repositorio>();
	ArrayList<String> repos = new ArrayList<String>();
	GitActions gitActions = new GitActions();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView showLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		if(Constantes.REPO_OWNER != null && !Constantes.REPO_OWNER.isEmpty()) {
			ModelAndView mav = new ModelAndView("index");
			repos = gitActions.listarRepositorios(request);
			mav.addObject("repositorios", repos);
			return mav;
		}
		
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("login", new Login());
		return mav;
	}

	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	public String loginProcess(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("login") Login login) throws IOException, JSONException {
//		User user = userService.validateUser(login);
//		if (null != user) {
//			mav = new ModelAndView("welcome");
//			mav.addObject("firstname", user.getFirstname());
//		} else {
//			mav = new ModelAndView("login");
//			mav.addObject("message", "Username or Password is wrong!!");
//		}
		System.out.println("LOGIN: " + login.getUsername());
		//System.out.println("PASSW: " + login.getPassword());
		
		Constantes.REPO_OWNER = login.getUsername();
		repos = gitActions.listarRepositorios(request);
		model.addAttribute("repositorios", repos);
		
		gitActions.requisicaoInicial(request, response);
		return "index";
	}

	//RETORNAR PARA A TELA INICIAL
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,Model model) throws IOException, JSONException {
		repos = gitActions.listarRepositorios(request);
		model.addAttribute("repositorios", repos);
		
		return "index";
	}
	
	@RequestMapping(value = "/access", method = RequestMethod.GET)
	public String access(HttpServletRequest request,Model model) throws IOException, JSONException {
		repos = gitActions.listarRepositorios(request);
		model.addAttribute("repositorios", repos);
		Constantes.CODIGO_AUTORIZACAO = request.getParameter("code"); 
		
		gitActions.requisitarAccessToken();
		
		return "index";
	}

	@RequestMapping(value = "/selecionaRepositorio", method = RequestMethod.GET)
	public String selecionaRepositorio(Model model,HttpServletRequest request) throws IOException, JSONException{
		String nomeRepo = request.getParameter("repositorio");

		Constantes.REPO_NAME = nomeRepo;
		json = gitActions.getArquivoMetadadosPrincipal(request, nomeRepo, "/metadados.princ");
		System.out.println("JSON: " + json);
		if(json != null){
			Constantes.UPDATE = true;
		}

		model.addAttribute("json",json);

		return "commit";
	}

	@RequestMapping(value = "/salvarArquivo", method = RequestMethod.GET)
	public String salvarArquivo(Model model,HttpServletRequest request) throws IOException, JSONException{
		int i = 0;
		String nomeSoftware,descricao,versao,autores;
		
		nomeSoftware = request.getParameter("nomeSoftware");
		descricao = request.getParameter("descricao");
		versao = request.getParameter("versao");
		autores= request.getParameter("autores");
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
			String json = Util.montarJSON(auxTags, auxValores, nomeSoftware, descricao, versao, autores);
			model.addAttribute("json",json);
			gitActions.atualizarArquivoMetadadosPrincipal(json.toString(), Constantes.REPO_NAME, Constantes.UPDATE);
		}

		return "commit";
	}
}
