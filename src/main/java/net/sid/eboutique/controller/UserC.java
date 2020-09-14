package net.sid.eboutique.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import net.sid.eboutique.dao.ActivityDAO;
import net.sid.eboutique.dao.RoleDAO;
import net.sid.eboutique.dao.UserDAO;
import net.sid.eboutique.dao.UserRoleDAO;
import net.sid.eboutique.entities.Activity;
import net.sid.eboutique.entities.Role;
import net.sid.eboutique.entities.User;
import net.sid.eboutique.entities.User_role;

@Controller
public class UserC {
	@Autowired
	public UserDAO userService;
	@Autowired
	public RoleDAO roleService;
	@Autowired
	public UserRoleDAO uroleService;
	
/* ___________________________________________________________            ________________________________________________________________________________________________
______________________________________________________________     USER   ___________________________________________________________________________________________________*/
	
	@RequestMapping(value = "/admin/user", method = RequestMethod.GET)
	public String client(Model model){	
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "user/list_user";
	}
	
	@RequestMapping(value="/admin/addUser", method = RequestMethod.GET)
	public String addUser(Model model){
		model.addAttribute("user", new User());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "user/add_user";
	}
	
	// go to user's profile 
	@RequestMapping(value="/admin/profileUser", method = RequestMethod.GET)
	public String profileUser(Model model, String login){
		User user = userService.getOne(login);
		model.addAttribute("user", user);
		if(user.isActive()){
			model.addAttribute("status", "Active");
		}else{
			model.addAttribute("status", "Desactive");
		}
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "user/user_profile";
	}
	
	// active or disactive user
	@RequestMapping(value="/admin/activeDesactive", method = RequestMethod.GET)
	public String activeDesactive(Model model, String login){
		User user = userService.getOne(login);
		if(user.isActive()){
			user.setActive(false);
		}else{
			user.setActive(true);
		}
		userService.save(user);
		model.addAttribute("user", user);
		if(user.isActive()){
			model.addAttribute("status", "Active");
		}else{
			model.addAttribute("status", "Desactive");
		}
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "user/user_profile";
	}
	
//	private String path = System.getProperty("user.dir") + "/src/main/resources/static/image";
	private String path = "C:/Program Files/Apache Software Foundation/Tomcat 8.5/webapps/shope/WEB-INF/classes/static/image";
	@RequestMapping(value="/admin/saveUser", method = RequestMethod.POST)
	public String saveUser(Model model, User user, MultipartFile file, String pass) throws IllegalStateException, IOException{
		
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		user.setPassword(bc.encode(pass));
	
		if(!(file.isEmpty())){
			file.transferTo(new File(path+"/"+"USER_"+file.getOriginalFilename()));
			user.setPhoto("USER_"+file.getOriginalFilename());
		}
		userService.save(user);
		model.addAttribute("user", new User());
		return "redirect:/admin/user";
	}
	// save after edition
	@RequestMapping(value="/admin/saveUser2", method = RequestMethod.POST)
	public String saveuser2(String pass, MultipartFile file, String login, String login2,
			String nom, String phone, String email) throws IllegalStateException, IOException{
		User user = userService.getOne(login);		
		user.setNom(nom);
		user.setPhone(phone);
		user.setEmail(email);
		if(!(pass.isEmpty())){		
			BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
			user.setPassword(bc.encode(pass));
		}
		
		if(!(file.isEmpty())){
			file.transferTo(new File(path+"/"+"USER_"+file.getOriginalFilename()));
			user.setPhoto("USER_"+file.getOriginalFilename());
		}
		
		userService.save(user);
		
		
		return "redirect:/admin/user";
	}
	// edit user
	@RequestMapping(value="/admin/editUser", method = RequestMethod.GET)
	public String editUser(Model model, String login){
		model.addAttribute("user", userService.getOne(login));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "user/edit_user";
	}
	@Autowired
	public ActivityDAO actService;
	// delete user
	@RequestMapping(value="/admin/deleteUser", method = RequestMethod.GET)
	public String deleteUser(String login){
		Activity act = new Activity();
		act.setAction("Supression of user");
		act.setUser(userService.getOne(new Home().curentUser()));
		actService.save(act);
		userService.deleteById(login);
		return "redirect:/admin/user";
	}
	
/* ________________________________________________________________                   ________________________________________________________________________________________
___________________________________________________________________        ROLE       ________________________________________________________________________________________ * */

	@RequestMapping(value="/admin/role", method = RequestMethod.GET)
	public String role(Model model){
		model.addAttribute("roles", roleService.findAll());
		model.addAttribute("role", new Role());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "role/role";
	}
	@RequestMapping(value="/admin/saveRole", method = RequestMethod.POST)
	public String addRole(Model model, Role role){
		if(roleService.roleById(role.getRole()) != null){			
			model.addAttribute("roles", roleService.findAll());
			model.addAttribute("role", new Role());
			model.addAttribute("existRole", "This is already exist.");
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "role/role";
		}		
		roleService.save(role);
		return "redirect:/admin/role";
	}
	
	@RequestMapping(value="/admin/deleteRole", method = RequestMethod.GET)
	public String deleteRole(String role){
		roleService.deleteById(role);
		return "redirect:/admin/role";
	}
	
/*________________________________________________                           ___________________________________________________
__________________________________________________      USER ROLE            ___________________________________________________ */
	
	@RequestMapping(value="/admin/urole", method = RequestMethod.GET)
	public String userRole(Model model){
		model.addAttribute("uroles", uroleService.findAll());
		model.addAttribute("urole", new User_role());
		model.addAttribute("roles", roleService.findAll());
		model.addAttribute("users", userService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "role/user_role";
	}
	@RequestMapping(value="/admin/saveUrole", method = RequestMethod.POST)
	public String saveUrole(Model model, User_role urole){
		uroleService.save(urole);
		return "redirect:/admin/urole";
	}
	@RequestMapping("/admin/deleteUrole")
	public String deleteUrole(int id){
		uroleService.deleteById(id);
		return "redirect:/admin/urole";
	}
}
