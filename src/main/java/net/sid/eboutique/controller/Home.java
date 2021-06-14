package net.sid.eboutique.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sid.eboutique.dao.ActivityDAO;
import net.sid.eboutique.dao.CommandeDAO;
import net.sid.eboutique.dao.UserDAO;
import net.sid.eboutique.dao.UserRoleDAO;
import net.sid.eboutique.dao.VenteDAO;
import net.sid.eboutique.entities.Activity;
import net.sid.eboutique.entities.Commande;
import net.sid.eboutique.entities.Vente;

@Controller
public class Home {
	@Autowired
	public UserDAO userService;
	@Autowired
	public UserRoleDAO uroleService;
	@Autowired
	public CommandeDAO commandeService;
	@Autowired
	public VenteDAO venteService;
	@Autowired
	public ActivityDAO activityService;
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/utilisateur/home", method=RequestMethod.GET)
	public String home(Model model) throws ParseException{
		//      Sell and orders evolution in the last seven days            //
		
		Date[] dates = new Date[7];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		
		List<Commande> commandes = commandeService.orders();
		List<Vente> ventes = venteService.findAll();
		//    fill the table of dates with simple date format
		for(int i = 0; i< 7; i++){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -i);		
			Date d = calendar.getTime();		
			d = sdf.parse(sdf.format(d));			
			dates[i] = d;
		}
		double amountt1 = 0.0, amountt2 = 0.0, amountt3 = 0.0, amountt4 = 0.0, amountt5 = 0.0, amountt6 = 0.0, amountt7 = 0.0;
		double amountv1 = 0.0, amountv2 = 0.0, amountv3 = 0.0, amountv4 = 0.0, amountv5 = 0.0, amountv6 = 0.0, amountv7 = 0.0;
		
		for (Commande com : commandes) {
			Date dt = sdf.parse(sdf.format(com.getDateCom()));
			if(dt.compareTo(dates[0]) == 0){
				amountt1+=com.getMontantT();				
			}else if(dt.compareTo(dates[1]) == 0){
				amountt2+=com.getMontantT();
			}else if(dt.compareTo(dates[2]) == 0){
				amountt3+=com.getMontantT();
			}else if(dt.compareTo(dates[3]) == 0){
				amountt4+=com.getMontantT();
			}else if(dt.compareTo(dates[4]) == 0){
				amountt5+=com.getMontantT();
			}else if(dt.compareTo(dates[5]) == 0){
				amountt6+=com.getMontantT();
			}else if(dt.compareTo(dates[6]) == 0){
				amountt7+=com.getMontantT();
			}
		}
		
		for (Vente vente : ventes) {
			Date dt = sdf.parse(sdf.format(vente.getDateSell()));
			if(dt.compareTo(dates[0]) == 0){
				amountv1+=vente.getAmount();				
			}else if(dt.compareTo(dates[1]) == 0){
				amountv2+=vente.getAmount();
			}else if(dt.compareTo(dates[2]) == 0){
				amountv3+=vente.getAmount();
			}else if(dt.compareTo(dates[3]) == 0){
				amountv4+=vente.getAmount();
			}else if(dt.compareTo(dates[4]) == 0){
				amountv5+=vente.getAmount();
			}else if(dt.compareTo(dates[5]) == 0){
				amountv6+=vente.getAmount();
			}else if(dt.compareTo(dates[6]) == 0){
				amountv7+=vente.getAmount();
			}
		}
		model.addAttribute("amountt1", amountt1);
		model.addAttribute("amountp1", amountv1);
		model.addAttribute("per1", (amountv1+amountt1)/5000000);
				
		model.addAttribute("amountt2", amountt2);
		model.addAttribute("amountp2", amountv2);
		model.addAttribute("day2", day(dates[1]));
		model.addAttribute("per2", (amountv2+amountt2)/5000000);
		
		model.addAttribute("amountt3", amountt3);
		model.addAttribute("amountp3", amountv3);
		model.addAttribute("day3", day(dates[2]));
		model.addAttribute("per3", (amountv3 + amountt3)/5000000);
		
		model.addAttribute("amountt4", amountt4);
		model.addAttribute("amountp4", amountv4);
		model.addAttribute("day4", day(dates[3]));
		model.addAttribute("per4", (amountv4 + amountt4)/5000000);
		
		model.addAttribute("amountt5", amountt5);
		model.addAttribute("amountp5", amountv5);
		model.addAttribute("day5", day(dates[4]));
		model.addAttribute("per5", (amountv5 + amountt5)/5000000);
		
		model.addAttribute("amountt6", amountt6);
		model.addAttribute("amountp6", amountv6);
		model.addAttribute("day6", day(dates[5]));
		model.addAttribute("per6", (amountv6 + amountt6)/5000000);
		
		model.addAttribute("amountt7", amountt7);
		model.addAttribute("amountp7", amountv7);
		model.addAttribute("day7", day(dates[6]));
		model.addAttribute("per7", (amountv7 + amountt7)/5000000);
		
		// find incomplete order
		
		List<Commande> coms = commandeService.incompletOrder();
		int i = 0;
		for (@SuppressWarnings("unused") Commande commande : coms) {
			i++;
		}
		//                        find the 10 last movements                               //
		model.addAttribute("incomplete", i);
		model.addAttribute("usercon", userService.getOne(curentUser()));
		
		//                   check if exist incomplete sell registration                  //
		Vente vente = venteService.incompleteSell();
		if(vente != null){
			model.addAttribute("incompleteSell", "Sell X");
		}
		
		//                          Today's Activity                                    //
		List<Activity> activities = activityService.findAll();
		Date dt = new Date();
		i = 0;
		for (Activity act : activities) {
			if(act.getDateAct().getYear() == dt.getYear() && act.getDateAct().getMonth() == dt.getMonth() && act.getDateAct().getDate() == dt.getDate()){
				i++;
			}
		}
		model.addAttribute("activity", i);
		model.addAttribute("userRole", uroleService.uroleByUser(userService.getOne(new Home().curentUser()).getLogin()));
		return "/index";
	}
	@SuppressWarnings("deprecation")
	public String day(Date dt){
		int i = dt.getDay();
		if(i==0){
			return "SUN";
		}else if(i == 1){
			return "MON";
		}else if(i == 2){
			return "TUE";
		}else if(i == 3){
			return "WED";
		}else if(i == 4){
			return "THU";
		}else if(i == 5){
			return "FRI";
		}else if(i == 6){
			return "SAT";
		}else{
			return "";
		}
	}
	public String curentUser(){
		new SecurityContextHolder();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return name;
	}
	@RequestMapping("/")
	public String vide(){
		return "redirect:/utilisateur/home";
	}
	@RequestMapping("/login")	
	public String login(Model model){
		return "/login";
	}
	
	@RequestMapping("404")
	public String error404(){
		return "404";
	}
	
	@RequestMapping("403")
	public String error403(){
		return "403";
	}
	
	@RequestMapping("500")
	public String error500(){
		return "500";
	}
	
	@RequestMapping(value="/utilisateur/incomplete", method = RequestMethod.GET)
	public String incomplet(Model model){
		model.addAttribute("commandes", commandeService.incompletOrder());
		model.addAttribute("userRole", uroleService.uroleByUser(userService.getOne(new Home().curentUser()).getLogin()));
		return "commande/incomplete";
	}
}
