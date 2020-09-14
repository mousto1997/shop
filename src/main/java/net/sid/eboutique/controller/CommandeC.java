package net.sid.eboutique.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sid.eboutique.dao.ClientDAO;
import net.sid.eboutique.dao.CommandeDAO;
import net.sid.eboutique.dao.DCommandeDAO;
import net.sid.eboutique.dao.DetailSellDAO;
import net.sid.eboutique.dao.FactureDAO;
import net.sid.eboutique.dao.MouvDAO;
import net.sid.eboutique.dao.PayeDAO;
import net.sid.eboutique.dao.ActivityDAO;
import net.sid.eboutique.dao.ProductDAO;
import net.sid.eboutique.dao.UserDAO;
import net.sid.eboutique.dao.VenteDAO;
import net.sid.eboutique.entities.Activity;
import net.sid.eboutique.entities.Commande;
import net.sid.eboutique.entities.DetailCommande;
import net.sid.eboutique.entities.DetailSell;
import net.sid.eboutique.entities.Facture;
import net.sid.eboutique.entities.Mouvement;
import net.sid.eboutique.entities.Pay;
import net.sid.eboutique.entities.Product;
import net.sid.eboutique.entities.Vente;

@Controller
public class CommandeC {
	@Autowired
	public ProductDAO productService;
	@Autowired
	public CommandeDAO commandeService;
	@Autowired
	public DCommandeDAO dCommandeService;
	@Autowired
	public MouvDAO mouvementService;
	@Autowired
	public ActivityDAO panService;
	@Autowired
	public ClientDAO clientService;
	@Autowired
	public FactureDAO factureService;
	@Autowired
	public UserDAO userService;
	@Autowired
	public VenteDAO venteService;
	@Autowired
	public PayeDAO payService;
	@Autowired
	public DetailSellDAO dSellService;
	
	//                 go to home page list order                //
	
	@RequestMapping(value = "/utilisateur/commande", method = RequestMethod.GET)
	public String commande(Model model){
		model.addAttribute("commandes", commandeService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/list_commande";
	}
	//                 go to print orders                        //
	
	@RequestMapping(value="/utilisateur/printOrder", method = RequestMethod.GET)
	public String printorder(Model model){
		List<Commande> coms = commandeService.orders();
		int amount = 0;
		double payed = 0.0, rest = 0.0;		
		for (Commande com : coms) {
			amount ++;
			payed += com.getMontantP();
			rest += com.getMontantR();
		}
		model.addAttribute("amount", amount);
		model.addAttribute("payed", payed);
		model.addAttribute("rest", rest);
		model.addAttribute("commandes", coms);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/print_com";
	}
	
	//                        go to add order page                               //
	
	@RequestMapping(value = "/utilisateur/makeOrder", method = RequestMethod.GET)	
	public String product(Model model, int idCli){
		Commande commande = commandeService.incompleteOrder(idCli);
		
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		if(commande != null){
			model.addAttribute("commande", commande);
			model.addAttribute("details", dCommandeService.detailComsByCom(commande.getIdCom()));
			
			for (DetailCommande dcom : dCommandeService.detailComsByCom(commande.getIdCom())) {
				items ++; 
				articles += dcom.getQteCom();
				amount += dcom.getPrixUnit() * dcom.getQteCom();
			}
			
		}else{
			Commande com = new Commande();		
			com.setClient(clientService.getOne(idCli));
			commandeService.save(com);
			model.addAttribute("commande", com);
			model.addAttribute("details", dCommandeService.detailComsByCom(com.getIdCom()));
			
			for (DetailCommande dcom : dCommandeService.detailComsByCom(com.getIdCom())) {
				items ++; 
				articles += dcom.getQteCom();
				amount += dcom.getPrixUnit() * dcom.getQteCom();
			}
		}
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("clients", clientService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/add_commande";
	}
	
	//       add product in the detail order           //
	
	@RequestMapping(value="/utilisateur/addLine", method = RequestMethod.POST)
	public String addLine(Model model, int idProd, int idCom, int quantity, double price){
		DetailCommande dcom = dCommandeService.detailComsByComProd(idCom, idProd);
		
		if(dcom != null){
			dcom.setQteCom(quantity);
			dcom.setPrixUnit(price);
			
			dCommandeService.save(dcom);
		}else{
			DetailCommande dc = new DetailCommande();
			dc.setCommande(commandeService.getOne(idCom));
			dc.setPrixUnit(price);
			dc.setProduct(productService.getOne(idProd));
			dc.setQteCom(quantity);
			
			dCommandeService.save(dc);
		}
		return "redirect:/utilisateur/makeOrder?idCli="+commandeService.getOne(idCom).getClient().getIdCli()+"";
	}
	
	
	//          pull a selected product from the details            //
	
	@RequestMapping(value="/utilisateur/pullProd", method = RequestMethod.GET)
	public String pullProd(int idDCom){
		Commande com = dCommandeService.getOne(idDCom).getCommande();
		dCommandeService.deleteById(idDCom);
		
		return "redirect:/utilisateur/makeOrder?idCli="+com.getClient().getIdCli()+"";
	}
	
	//                        save order                         //
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "utilisateur/saveOrder", method = RequestMethod.POST)
	public String saveOrder(Model model, int idCom, int client) throws ParseException{
		
		Commande commande = commandeService.getOne(idCom);
		
		int articles = 0;
		double amount = 0.0;
		for (DetailCommande dc : dCommandeService.detailComsByCom(idCom)) {
			articles += dc.getQteCom();
			amount += dc.getPrixUnit() * dc.getQteCom();
			Product prod = productService.getOne(dc.getProduct().getIdProd());
			prod.setQteStock(prod.getQteStock() - dc.getQteCom());
			productService.save(prod);
		}
		commande.setClient(clientService.getOne(client));
		commande.setMontantT(amount);
		commande.setMontantP(0);
		commande.setMontantR(commande.getMontantT() - commande.getMontantP());
		commande.setDateCom(new Date());
		commande.setSujet("Order of products");
		commande.setUser(userService.getOne(new Home().curentUser()));
		
		String k = "";
		if(commande.getIdCom() < 10){
			k = "0000"+commande.getIdCom();
		}else if(commande.getIdCom() < 100){
			k = "000"+commande.getIdCom();
		}else if(commande.getIdCom() < 1000){
			k = "00"+commande.getIdCom();
		}else if(commande.getIdCom() < 10000){
			k = "0"+commande.getIdCom();
		}else{
			k = ""+commande.getIdCom();
		}		
		
		Date ld = new Date();
		int i = 1900 + ld.getYear();
		commande.setCodeCom("O_"+k+"/"+ i);
		
		commandeService.save(commande);
		Mouvement mouvement = new Mouvement();
		mouvement.setCommande(commande.getCodeCom());
		mouvement.setDateMouv(new Date());
		mouvement.setMontant(amount);
		mouvement.setQte(articles);
		mouvement.setTypeMouv("out after an order by "+userService.getOne(new Home().curentUser()).getNom());
		mouvementService.save(mouvement);
		
		Facture facture = factureService.billnopayed(idCom);
		
		if(facture!=null){
			factureService.delete(facture);
		}
		Facture bill = new Facture();
		
		bill.setCommande(commande);
		bill.setDateFac(new Date());
		factureService.save(bill);
		
		String l = "";
		if(bill.getIdFacture() < 10){
			l = "0000"+bill.getIdFacture();
		}else if(bill.getIdFacture() < 100){
			l = "000"+bill.getIdFacture();
		}else if(bill.getIdFacture() < 1000){
			l = "00"+bill.getIdFacture();
		}else if(bill.getIdFacture() < 10000){
			l = "0"+bill.getIdFacture();
		}else{
			l = ""+bill.getIdFacture();
		}
		Date dt = new Date();
		int a = dt.getYear()+1900;
		bill.setCodeFac("B_"+l+"/"+ a);
		bill.setStatus(false);
		factureService.save(bill);
	
		return "redirect:/utilisateur/detailOrder?idCom="+idCom;
	}
	
	//          ::::::::::::::::::::::::             EDIT ORDER                 ::::::::::::::::::::::::::::::                        //
	
	@RequestMapping(value="/utilisateur/editOrder", method = RequestMethod.GET)
	public String editOrder(Model model, int idCom){
		Commande commande = commandeService.getOne(idCom);
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		for (DetailCommande dcom : dCommandeService.detailComsByCom(commande.getIdCom())) {
			items ++; 
			articles += dcom.getQteCom();
			amount += dcom.getPrixUnit() * dcom.getQteCom();
			if(commande.getCodeCom() != null){
				Product prod = productService.getOne(dcom.getProduct().getIdProd());
				prod.setQteStock(prod.getQteStock() + dcom.getQteCom());
			}			
		}
		
		commande.setCodeCom(null);
		commandeService.save(commande);
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("commande", commande);
		model.addAttribute("details", dCommandeService.detailComsByCom(commande.getIdCom()));
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("clients", clientService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/edit_commande";
	}
	
	//  add product in the detail commande2           //
		
	@RequestMapping(value="/utilisateur/addLine2", method = RequestMethod.POST)
	public String addLine2(Model model, int idProd, int idCom, int quantity, double price){
		Commande commande = commandeService.getOne(idCom);
		DetailCommande dcom = dCommandeService.detailComsByComProd(idCom, idProd);
		
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		if(dcom != null){
			dcom.setQteCom(quantity);
			dcom.setPrixUnit(price);
			
			dCommandeService.save(dcom);
		}else{
			DetailCommande dc = new DetailCommande();
			dc.setCommande(commandeService.getOne(idCom));
			dc.setPrixUnit(price);
			dc.setProduct(productService.getOne(idProd));
			dc.setQteCom(quantity);
			
			dCommandeService.save(dc);
		}
		
		for (DetailCommande dcomm : dCommandeService.detailComsByCom(commande.getIdCom())) {
			items ++; 
			articles += dcomm.getQteCom();
			amount += dcomm.getPrixUnit() * dcomm.getQteCom();
		}
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("commande", commande);
		model.addAttribute("details", dCommandeService.detailComsByCom(commande.getIdCom()));
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("clients", clientService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/edit_commande";
	}
	
	
	//          pull a selected product from the details            //
	
	@RequestMapping(value="/utilisateur/pullProd2", method = RequestMethod.GET)
	public String pullProd2(Model model, int idDCom){
		Commande commande = dCommandeService.getOne(idDCom).getCommande();
		dCommandeService.deleteById(idDCom);
		
		int items = 0;
		int articles = 0; 
		double amount = 0.0;
		
		for (DetailCommande dcomm : dCommandeService.detailComsByCom(commande.getIdCom())) {
			items ++; 
			articles += dcomm.getQteCom();
			amount += dcomm.getPrixUnit() * dcomm.getQteCom();
		}
		
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("commande", commande);
		model.addAttribute("details", dCommandeService.detailComsByCom(commande.getIdCom()));
		List<Product> products = productService.disponibleProd();
		model.addAttribute("products", products);
		model.addAttribute("clients", clientService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/edit_commande";
	}
	
	//                                Cancel adding Order                             //
	
	@RequestMapping(value="/utilisateur/cancelOrder", method = RequestMethod.GET)
	public String cancelOrder(int idCom){
		List<DetailCommande> details = dCommandeService.detailComsByCom(idCom);
		for (DetailCommande dc : details) {
			dCommandeService.delete(dc);
		}
		commandeService.deleteById(idCom);
		return "redirect:/utilisateur/commande";
	}
	
	//                               Cancel editing Order                      //
	
	
	//                                Delete order                            //
	@Autowired
	public ActivityDAO actService;
	@RequestMapping(value="/utilisateur/deleteOrder", method = RequestMethod.GET)
	public String deleteOrder(int idCom){
		Activity act = new Activity();
		act.setAction("Supression of order");
		act.setDateAct(new Date());
		act.setUser(userService.getOne(new Home().curentUser()));
		actService.save(act);
		commandeService.deleteById(idCom);
		return "redirect:/utilisateur/commande";
	}

	//                              Detail order                               //
	
	@RequestMapping(value="/utilisateur/detailOrder", method = RequestMethod.GET)
	public String detailOrders(Model model, int idCom){
		Commande commande = commandeService.getOne(idCom);
		
		int items = 0, articles = 0;
		double amount = 0.0;
		for (DetailCommande dc : dCommandeService.detailComsByCom(idCom)) {
			items ++;
			articles += dc.getQteCom();
			amount += dc.getPrixUnit() * dc.getQteCom();
		}
		
		model.addAttribute("commande", commande);
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("client", commande.getClient());
		model.addAttribute("details", dCommandeService.detailComsByCom(idCom));
		model.addAttribute("bill", factureService.billnopayed(idCom));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/invoice";
	}
	
	//                                   Pay Order                            //
	
	@RequestMapping(value="/utilisateur/payBill", method = RequestMethod.GET)
	public String payOrder(Model model, int idFacture){
		Commande commande = commandeService.getOne(factureService.getOne(idFacture).getCommande().getIdCom());
		model.addAttribute("commade", commande);
		model.addAttribute("bill", factureService.getOne(idFacture));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/pay_bill";
	}
	
	//                                 save pay                                 //
	@RequestMapping(value="/utilisateur/savePay", method = RequestMethod.POST)
	public String savePaye(Model model, int idFacture, double amount){
		Facture bill = factureService.getOne(idFacture);
		Commande commande = commandeService.getOne(bill.getCommande().getIdCom());
		
		commande.setMontantP(commande.getMontantP() + amount);
		commande.setMontantR(commande.getMontantT() - commande.getMontantP());
		
		Pay pay = new Pay();
		pay.setDatePaye(new Date());
		pay.setFacture(factureService.getOne(idFacture));
		pay.setMontantP(amount);
		pay.setUser(userService.getOne(new Home().curentUser()));
		payService.save(pay);
		
		Mouvement mouv = new Mouvement();
		mouv.setCommande(commande.getCodeCom());
		mouv.setDateMouv(new Date());
		mouv.setMontant(amount);
		mouv.setTypeMouv("in after bill payement by "+userService.getOne(new Home().curentUser()).getNom());
		mouvementService.save(mouv);
		
		commandeService.save(commande);
		
		if(commande.getMontantR()>0){
			bill.setStatus(true);
			Facture fac = new Facture();
			fac.setCommande(commande);
			fac.setDateFac(new Date());
			
			factureService.save(bill);
			
			String l = "";
			if(bill.getIdFacture() < 10){
				l = "0000"+bill.getIdFacture();
			}else if(bill.getIdFacture() < 100){
				l = "000"+bill.getIdFacture();
			}else if(bill.getIdFacture() < 1000){
				l = "00"+bill.getIdFacture();
			}else if(bill.getIdFacture() < 10000){
				l = "0"+bill.getIdFacture();
			}else{
				l = ""+bill.getIdFacture();
			}
			Date dt = new Date();
			@SuppressWarnings("deprecation")
			int a = dt.getYear()+1900;
			bill.setCodeFac("B_"+l+"/"+ a);
			bill.setStatus(false);
			factureService.save(bill);
			model.addAttribute("bill", bill);
		}else{
			commande.setStatus(true);
			commandeService.save(commande);
			return "redirect:/utilisateur/commande";
		}
		return "redirect:/utilisateur/detailOrder?idCom="+commande.getIdCom()+"";
	}
	
	//                              Bill                                //
	@RequestMapping(value="/utilisateur/bill", method = RequestMethod.GET)
	public String bill(Model model){
		model.addAttribute("bills", factureService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/bill";
	}
	@RequestMapping(value="utilisateur/report", method = RequestMethod.GET)
	public String orderr(Model model){
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/raport";
	}
	
	//                              Order make per day                            //		
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/utilisateur/orderDay", method = RequestMethod.POST)
	public String orderday(Model model, String date) throws ParseException{
		
		ArrayList<Commande> commandes = new ArrayList<>();
		ArrayList<Vente> ventes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		double amounts = 0.0, amounto = 0.0;
		int orders = 0, sells = 0;
		int qtys = 0;
		int qtyo = 0;
		Date dt = sdf.parse(date);
		
		for (Commande c : commandeService.orders()) {
			if(c.getDateCom().getYear() == dt.getYear() && c.getDateCom().getMonth() == dt.getMonth() && c.getDateCom().getDate() == dt.getDate()){
			 commandes.add(c);
			 orders++;
			 amounto += c.getMontantT();
			 for (DetailCommande dc : dCommandeService.detailComsByCom(c.getIdCom())) {
				qtyo += dc.getQteCom();
			}
			}		
		}
		
		for (Vente vente : venteService.findAll()) {
			if(vente.getDateSell().getYear() == dt.getYear() && vente.getDateSell().getMonth() == dt.getMonth() && vente.getDateSell().getDate() == dt.getDate()){
				ventes.add(vente);
				sells++;
				amounts+=vente.getAmount();
				for (DetailSell ds : dSellService.detailSellBySell(vente.getIdSell())) {
					qtys += ds.getQtySell();
				}
			}
		}		
		model.addAttribute("qtys", qtys);
		model.addAttribute("qtyo", qtyo);
		model.addAttribute("commandes", commandes);
		model.addAttribute("ventes", ventes);
		model.addAttribute("amounts", amounts);
		model.addAttribute("amounto", amounto);
		model.addAttribute("sells", sells);
		model.addAttribute("orders", orders);
		model.addAttribute("date", dt);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/raport";
	}
	
	//                                orders make per month                                      //
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/utilisateur/orderMonth", method = RequestMethod.POST)
	public String orderMonth(Model model, String date) throws ParseException{
		
		ArrayList<Commande> commandes = new ArrayList<>();
		ArrayList<Vente> ventes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		double amounts = 0.0, amounto = 0.0;
		int orders = 0, sells = 0;
		
		Date dt = sdf.parse(date);
		
		for (Commande c : commandeService.orders()) {
			if(c.getDateCom().getYear() == dt.getYear() && c.getDateCom().getMonth() == dt.getMonth()){
			 commandes.add(c);
			 orders++;
			 amounto += c.getMontantT();
			}		
		}
		
		for (Vente vente : venteService.findAll()) {
			if(vente.getDateSell().getYear() == dt.getYear() && vente.getDateSell().getMonth() == dt.getMonth()){
				ventes.add(vente);
				sells++;
				amounts+=vente.getAmount();
			}
		}
		model.addAttribute("commandes", commandes);
		model.addAttribute("ventes", ventes);
		model.addAttribute("amounts", amounts);
		model.addAttribute("amounto", amounto);
		model.addAttribute("sells", sells);
		model.addAttribute("orders", orders);
		model.addAttribute("date", dt);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/raport";
	}
	
	//                          order between two dates                               //
	@RequestMapping(value="/utilisateur/orderC", method = RequestMethod.POST)
	public String orderc(Model model, String date1, String date2) throws ParseException{
		
		ArrayList<Commande> commandes = new ArrayList<>();
		ArrayList<Vente> ventes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		double amounts = 0.0, amounto = 0.0;
		int orders = 0, sells = 0;
		
		Date dt1 = sdf.parse(date1);
		Date dt2 = sdf.parse(date2);
		
		for (Commande c : commandeService.findAll()) {
			Date od = sdf.parse(sdf.format(c.getDateCom()));
			if((dt1.before(od) || dt1.compareTo(od)==0) && (dt2.after(od) || dt2.compareTo(od)==0 )){
			 commandes.add(c);
			 orders++;
			 amounto += c.getMontantT();
			}		
		}
		
		for (Vente vente : venteService.findAll()) {
			Date vd = sdf.parse(sdf.format(vente.getDateSell()));
			if((dt1.before(vd) || dt1.compareTo(vd)==0) && (dt2.after(vd) || dt2.compareTo(vd)==0 )){
				ventes.add(vente);
				sells++;
				amounts+=vente.getAmount();
			}
		}
		model.addAttribute("commandes", commandes);
		model.addAttribute("ventes", ventes);
		model.addAttribute("amounts", amounts);
		model.addAttribute("amounto", amounto);
		model.addAttribute("sells", sells);
		model.addAttribute("orders", orders);
		model.addAttribute("date", dt1+" - "+dt2);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/raport";
	}
	
	//                                  List of pays                                    //
	@RequestMapping("/utilisateur/pay")
	public String pays(Model model){
		model.addAttribute("pays", payService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/pay";
	}
	//                                 Edit pay                                       //
	@RequestMapping("/utilisateur/editPay")
	public String editpay(Model model, int idPaye){
		model.addAttribute("bill", payService.getOne(idPaye).getFacture());
		model.addAttribute("commande", payService.getOne(idPaye).getFacture().getCommande());
		model.addAttribute("pay", payService.getOne(idPaye));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/edit_pay";
	}
	
	//                      save pay after edit
	@RequestMapping(value="/utilisateur/savePay2", method = RequestMethod.POST)
	public String savePaye2(Model model, int idFacture, int idPaye, double amount1, double amount2){
		Facture bill = factureService.getOne(idFacture);
		Commande commande = commandeService.getOne(bill.getCommande().getIdCom());
		
		commande.setMontantP((commande.getMontantP() + amount1) - amount2);
		commande.setMontantR(commande.getMontantT() - commande.getMontantP());
		
		Pay pay = payService.getOne(idPaye);
		pay.setDatePaye(new Date());
		pay.setFacture(factureService.getOne(idFacture));
		pay.setMontantP(amount1);
		payService.save(pay);
		
		Mouvement mouv = new Mouvement();
		mouv.setCommande(commande.getCodeCom());
		mouv.setDateMouv(new Date());
		mouv.setMontant(amount1);
		mouv.setTypeMouv("in after editint bill payement by "+userService.getOne(new Home().curentUser()).getNom());
		mouvementService.save(mouv);
		commandeService.save(commande);
		
		if(commande.getMontantR()>0){
			bill.setStatus(true);
			Facture fac = new Facture();
			fac.setCommande(commande);
			fac.setDateFac(new Date());
			
			factureService.save(bill);
			
			String l = "";
			if(bill.getIdFacture() < 10){
				l = "0000"+bill.getIdFacture();
			}else if(bill.getIdFacture() < 100){
				l = "000"+bill.getIdFacture();
			}else if(bill.getIdFacture() < 1000){
				l = "00"+bill.getIdFacture();
			}else if(bill.getIdFacture() < 10000){
				l = "0"+bill.getIdFacture();
			}else{
				l = ""+bill.getIdFacture();
			}
			Date dt = new Date();
			@SuppressWarnings("deprecation")
			int a = dt.getYear()+1900;
			bill.setCodeFac("B_"+l+"/"+ a);
			bill.setStatus(false);
			factureService.save(bill);
			model.addAttribute("bill", bill);
		}else{
			commande.setStatus(true);
			commandeService.save(commande);
			return "redirect:/utilisateur/commande";
		}
		return "redirect:/utilisateur/detailOrder?idCom="+commande.getIdCom()+"";
	}
	
	//                        incomplete orders                              //
	@RequestMapping("/utilisateur/incomplete")
	public String incompletOrder(Model model){
		model.addAttribute("commandes", commandeService.incompleteOrders());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "commande/incomplete";
	}
}