package net.sid.eboutique.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sid.eboutique.dao.ActivityDAO;
import net.sid.eboutique.dao.ClientDAO;
import net.sid.eboutique.dao.UserDAO;
import net.sid.eboutique.dao.UserRoleDAO;
import net.sid.eboutique.entities.Activity;
import net.sid.eboutique.entities.Client;
import net.sid.eboutique.entities.User;

@Controller
public class ClientC {
	@Autowired
	public ClientDAO clientService;
	@Autowired
	public UserDAO userService;
	@Autowired
	public ActivityDAO activityService;
	@Autowired
	public UserRoleDAO userRole;
	
	@RequestMapping(value = "/utilisateur/client", method = RequestMethod.GET)
	public String client(Model model){	
		List<Client> clients = clientService.findAll();
		model.addAttribute("clients", clients);	
		model.addAttribute("userRole", userRole.uroleByUser(userService.getOne(new Home().curentUser()).getLogin()));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "client/list_client";
	}
	
	@RequestMapping(value = "/utilisateur/addCli", method = RequestMethod.GET)
	public String addCli(Model model){		
		model.addAttribute("client", new Client());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "client/add_client";
	}
	
//	public String path = System.getProperty("user.dir") + "/src/main/resources/static/image";
	
	@RequestMapping(value="/utilisateur/saveClient", method = RequestMethod.POST)
	public String saveClient(Model model, @Valid Client client, BindingResult br, Integer idCli) throws IOException, ParseException{
				
		if(br.hasErrors()){
			model.addAttribute("client", new Client());
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "client/add_client";
		}
		
		Client exist = clientService.clientByEmail(client.getEmail(), client.getIdCli());
		if(exist != null){
			model.addAttribute("existEmail", "This email is already used by someone !");
			return "client/add_client";
		}
		
		Client exist2 = clientService.clientByPhone(client.getPhone(), client.getIdCli());
		if(exist2 != null){
			model.addAttribute("existPhone", "This Phone number is already used by someone !");
			return "client/add_client";
		}
		
		Client exist3 = clientService.clientByNameCompany(client.getNom(), client.getCompany(), client.getIdCli());
		if(exist3 != null){
			model.addAttribute("existCli", "This client is already registered");
			return "client/add_client";
		}
		
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//		LocalDateTime now = LocalDateTime.now();
	//	String a = (dtf.format(now)).toString();
		
		clientService.save(client);
		
		return "redirect:client";
	}	
	
	@RequestMapping(value="/utilisateur/editClient", method = RequestMethod.GET)
	public String modifyCli(int idCli, Model model){
		model.addAttribute("client", clientService.getOne(idCli));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "client/edit_client";
	}
	@RequestMapping(value="/admin/deleteClient", method = RequestMethod.GET)
	public String deleteCli(int idCli){
		Activity act = new Activity();
		act.setAction("Supression of client");
		act.setDateAct(new Date());
		act.setUser(userService.getOne(new Home().curentUser()));
		activityService.save(act);
		clientService.deleteById(idCli);
		return "redirect:client";
	}

}
