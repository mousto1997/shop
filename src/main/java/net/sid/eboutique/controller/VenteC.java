package net.sid.eboutique.controller;

import net.sid.eboutique.controller.Home;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sid.eboutique.dao.ClientDAO;
import net.sid.eboutique.dao.DetailSellDAO;
import net.sid.eboutique.dao.FactureSellDAO;
import net.sid.eboutique.dao.MouvDAO;
import net.sid.eboutique.dao.ActivityDAO;
import net.sid.eboutique.dao.ProductDAO;
import net.sid.eboutique.dao.UserDAO;
import net.sid.eboutique.dao.VenteDAO;
import net.sid.eboutique.entities.Activity;
import net.sid.eboutique.entities.DetailSell;
import net.sid.eboutique.entities.FactureSell;
import net.sid.eboutique.entities.Mouvement;
import net.sid.eboutique.entities.Product;
import net.sid.eboutique.entities.Vente;

@Controller
public class VenteC {
	@Autowired
	public ProductDAO productService;
	@Autowired
	public DetailSellDAO detailSService;
	@Autowired
	public MouvDAO mouvementService;
	@Autowired
	public ActivityDAO panService;
	@Autowired
	public ClientDAO clientService;
	@Autowired
	public FactureSellDAO facSellService;
	@Autowired
	public VenteDAO venteService;
	@Autowired
	public UserDAO userService;
	@Autowired
	public ActivityDAO activityService;

	
	//                 go to home page list order                //
	
	@RequestMapping(value = "/utilisateur/sell", method = RequestMethod.GET)
	public String vente(Model model){
		List<Vente> ventes = venteService.findAll();
		model.addAttribute("ventes", ventes);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/list_sell";
	}
	//                 go to print orders                        //
	
	@RequestMapping(value="/utilisateur/printSell", method = RequestMethod.GET)
	public String printsell(Model model){
		int sells = 0;
		double amount = 0.0;
		for (Vente sell : venteService.findAll()) {
			sells++;
			amount += sell.getAmount();
		}
		model.addAttribute("sells", sells);
		model.addAttribute("amount", amount);
		model.addAttribute("ventes", venteService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/print_sell";
	}
	
	//                        go to add sell page                               //
	
	@RequestMapping(value = "/utilisateur/addSell", method = RequestMethod.GET)	
	public String addSell(Model model){
		
		Vente vente = venteService.incompleteSell();
		
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		if(vente != null){
			model.addAttribute("vente", vente);
			model.addAttribute("details", detailSService.detailSellBySell(vente.getIdSell()));
			
			for (DetailSell dsell : detailSService.detailSellBySell(vente.getIdSell())) {
				items ++; 
				articles += dsell.getQtySell();
				amount += dsell.getPrixUnit() * dsell.getQtySell();
			}
		}else{
			Vente sell = new Vente();
			sell.setDateSell(new Date());
			venteService.save(sell);
			model.addAttribute("vente", sell);
			model.addAttribute("details", detailSService.detailSellBySell(sell.getIdSell()));
			
			for (DetailSell dcom : detailSService.detailSellBySell(sell.getIdSell())) {
				items ++; 
				articles += dcom.getQtySell();
				amount += dcom.getPrixUnit() * dcom.getQtySell();
			}
		}
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/add_sell";
	}
	
	//       add product in the detail order           //
	
	@RequestMapping(value="/utilisateur/addLines", method = RequestMethod.POST)
	public String addLines(Model model, int idProd, int idSell, int quantity, double price){
		DetailSell dsell = detailSService.detailSellBySellProd(idSell, idProd);
		
		if(dsell != null){
			dsell.setQtySell(quantity);
			dsell.setPrixUnit(price);
			
			detailSService.save(dsell);
		}else{
			DetailSell ds = new DetailSell();
			ds.setVente(venteService.getOne(idSell));
			ds.setPrixUnit(price);
			ds.setProduct(productService.getOne(idProd));
			ds.setQtySell(quantity);
			
			detailSService.save(ds);
		}
		return "redirect:/utilisateur/addSell";
	}
	
	
	//          pull a selected product from the details            //
	
	@RequestMapping(value="/utilisateur/pullProds", method = RequestMethod.GET)
	public String pullProds(int idDSell){
		detailSService.deleteById(idDSell);
		
		return "redirect:/utilisateur/addSell";
	}
	
	//                        save order                         //
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "utilisateur/saveSell", method = RequestMethod.POST)
	public String saveSell(Model model, int idSell, String client) throws ParseException{
		
		Vente vente = venteService.getOne(idSell);
		
		int articles = 0;
		double amount = 0.0;
		for (DetailSell ds : detailSService.detailSellBySell(idSell)) {
			articles += ds.getQtySell();
			amount += ds.getPrixUnit() * ds.getQtySell();
			Product prod = productService.getOne(ds.getProduct().getIdProd());
			prod.setQteStock(prod.getQteStock() - ds.getQtySell());
			productService.save(prod);
		}
		vente.setAmount(amount);
		vente.setDateSell(new Date());
		vente.setSujet("Sell of products");
		vente.setUser(userService.getOne(new Home().curentUser()));
		vente.setClient(client);
		String k = "";
		if(vente.getIdSell() < 10){
			k = "0000"+vente.getIdSell();
		}else if(vente.getIdSell() < 100){
			k = "000"+vente.getIdSell();
		}else if(vente.getIdSell() < 1000){
			k = "00"+vente.getIdSell();
		}else if(vente.getIdSell() < 10000){
			k = "0"+vente.getIdSell();
		}else{
			k = ""+vente.getIdSell();
		}		
		
		Date ld = new Date();
		int i = 1900 + ld.getYear();
		vente.setCodeSell("S_"+k+"/"+ i);
		
		venteService.save(vente);
		Mouvement mouvement = new Mouvement();
		mouvement.setDateMouv(new Date());
		mouvement.setMontant(amount);
		mouvement.setQte(articles);
		mouvement.setTypeMouv("out after an sell by "+userService.getOne(new Home().curentUser()).getNom());
		mouvement.setVente(vente.getCodeSell());
		mouvementService.save(mouvement);
		
		FactureSell facture = facSellService.billByVente(idSell);
		
		if(facture!=null){
			facSellService.delete(facture);
		}
		FactureSell bill = new FactureSell();
		
		bill.setVente(vente);
		bill.setDateFac(new Date());
		bill.setStatus(true);
		facSellService.save(bill);
		
		String l = "";
		if(bill.getIdFacSell() < 10){
			l = "0000"+bill.getIdFacSell();
		}else if(bill.getIdFacSell() < 100){
			l = "000"+bill.getIdFacSell();
		}else if(bill.getIdFacSell() < 1000){
			l = "00"+bill.getIdFacSell();
		}else if(bill.getIdFacSell() < 10000){
			l = "0"+bill.getIdFacSell();
		}else{
			l = ""+bill.getIdFacSell();
		}
		Date dt = new Date();
		int a = dt.getYear()+1900;
		bill.setCodeFac("B_"+l+"/"+ a);
		bill.setStatus(true);
		facSellService.save(bill);
	
		return "redirect:/utilisateur/detailSell?idSell="+idSell;
	}
	
	//          ::::::::::::::::::::::::             EDIT ORDER                 ::::::::::::::::::::::::::::::                        //
	
	@RequestMapping(value="/utilisateur/editSell", method = RequestMethod.GET)
	public String editSell(Model model, int idSell){
		Vente vente = venteService.getOne(idSell);
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		for (DetailSell dsell : detailSService.detailSellBySell(vente.getIdSell())) {
			items ++; 
			articles += dsell.getQtySell();
			amount += dsell.getPrixUnit() * dsell.getQtySell();
			if(vente.getCodeSell() != null){
				Product prod = productService.getOne(dsell.getProduct().getIdProd());
				prod.setQteStock(prod.getQteStock() + dsell.getQtySell());
			}			
		}
		
		vente.setCodeSell(null);
		venteService.save(vente);
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("vente", vente);
		model.addAttribute("details", detailSService.detailSellBySell(vente.getIdSell()));
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/edit_sell";
	}
	
	//  add product in the detail commande2           //
		
	@RequestMapping(value="/utilisateur/addLines2", method = RequestMethod.POST)
	public String addLines2(Model model, int idProd, int idSell, int quantity, double price){
		Vente vente = venteService.getOne(idSell);
		DetailSell dsell = detailSService.detailSellBySellProd(idSell, idProd);
		
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		if(dsell != null){
			dsell.setQtySell(quantity);
			dsell.setPrixUnit(price);
			
			detailSService.save(dsell);
		}else{
			DetailSell ds = new DetailSell();
			ds.setVente(venteService.getOne(idSell));
			ds.setPrixUnit(price);
			ds.setProduct(productService.getOne(idProd));
			ds.setQtySell(quantity);
			
			detailSService.save(ds);
		}
		
		for (DetailSell desell : detailSService.detailSellBySell(vente.getIdSell())) {
			items ++; 
			articles += desell.getQtySell();
			amount += desell.getPrixUnit() * desell.getQtySell();
		}
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("vente", vente);
		model.addAttribute("details", detailSService.detailSellBySell(vente.getIdSell()));
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/edit_sell";
	}
	
	
	//          pull a selected product from the details            //
	
	@RequestMapping(value="/utilisateur/pullProds2", method = RequestMethod.GET)
	public String pullProds2(Model model, int idDSell){
		Vente vente = detailSService.getOne(idDSell).getVente();
		detailSService.deleteById(idDSell);
		
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		for (DetailSell dsell : detailSService.detailSellBySell(vente.getIdSell())) {
			items ++; 
			articles += dsell.getQtySell();
			amount += dsell.getPrixUnit() * dsell.getQtySell();
		}
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("vente", vente);
		model.addAttribute("details", detailSService.detailSellBySell(vente.getIdSell()));
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/edit_sell";
	}
	
	//                                Cancel adding Order                             //
	
	@RequestMapping(value="/utilisateur/cancelSell", method = RequestMethod.GET)
	public String cancelSell(int idSell){
		List<DetailSell> details = detailSService.detailSellBySell(idSell);
		for (DetailSell ds : details) {
			detailSService.delete(ds);
		}
		venteService.deleteById(idSell);
		return "redirect:/utilisateur/sell";
	}
	
	//                               Cancel editing Order                      //
	
	
	//                                Delete sell                            //
	@Autowired
	public ActivityDAO actService;
	@RequestMapping(value="/utilisateur/deleteSell", method = RequestMethod.GET)
	public String deleteSell(int idSell){
		Activity act = new Activity();
		act.setAction("Supression of sell");
		act.setUser(userService.getOne(new Home().curentUser()));
		act.setDateAct(new Date());
		actService.save(act);
		venteService.deleteById(idSell);
		return "redirect:/utilisateur/sell";
	}

	//                              Detail sell                               //
	
	@RequestMapping(value="/utilisateur/detailSell", method = RequestMethod.GET)
	public String detailSell(Model model, int idSell){
		Vente vente = venteService.getOne(idSell);
		
		int items = 0, articles = 0;
		double amount = 0.0;
		for (DetailSell ds : detailSService.detailSellBySell(idSell)) {
			items ++;
			articles += ds.getQtySell();
			amount += ds.getPrixUnit() * ds.getQtySell();
		}
		
		model.addAttribute("vente", vente);
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("details", detailSService.detailSellBySell(idSell));
		model.addAttribute("bill", facSellService.billByVente(idSell));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/invoice";
	}
	
	//                              Bill                                //
	@RequestMapping(value="/utilisateur/bills", method = RequestMethod.GET)
	public String bills(Model model){
		model.addAttribute("bills", facSellService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "vente/bill";
	}
	
	//                         Get activities                               //
	@RequestMapping("/utilisateur/activity")
	public String activities(Model model){
		model.addAttribute("activities", activityService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "others/activity";
	}
	
	//                         Get movements                                //
	@RequestMapping("/utilisateur/movement")
	public String movement(Model model){
		int amount = (int) mouvementService.count();
		model.addAttribute("amount", amount);
		model.addAttribute("movements", mouvementService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "others/movement";
	}
}