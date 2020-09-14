package net.sid.eboutique.controller;

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
import net.sid.eboutique.dao.CategoryDAO;
import net.sid.eboutique.dao.MouvDAO;
import net.sid.eboutique.dao.ProductDAO;
import net.sid.eboutique.dao.UniteDAO;
import net.sid.eboutique.dao.UserDAO;
import net.sid.eboutique.entities.Activity;
import net.sid.eboutique.entities.Categorie;
import net.sid.eboutique.entities.Mouvement;
import net.sid.eboutique.entities.Product;
import net.sid.eboutique.entities.Unite;

@Controller
public class ProductC {
	
//::::::::::::::::::::::::::::::::::::::     PRODUCT      :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	@Autowired
	public ProductDAO productService;
	@Autowired
	public MouvDAO mouvementService;
	@Autowired
	public UserDAO userService;
	@Autowired
	public ActivityDAO activityService;
	
	@RequestMapping(value="/utilisateur/product", method = RequestMethod.GET)
	public String product(Model model){
		List<Product> products = productService.products();
		model.addAttribute("products", products);
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "product/list_product";
	}
	
	//                             add product                                 //
	
	@RequestMapping(value="/utilisateur/addProd", method = RequestMethod.GET)
	public String addProduct(Model model){
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("unites", uniteService.findAll());
		model.addAttribute("product", new Product());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "product/add_prod";		
	}
	// Save product
//	String path = System.getProperty("user.dir") + "/src/main/resources/static/image";
	@RequestMapping(value="/utilisateur/saveProduct", method = RequestMethod.POST)
	public String saveProd(Model model, Product product, BindingResult br){
		
		if(br.hasErrors()){
			model.addAttribute("categories", categoryService.findAll());
			model.addAttribute("unites", uniteService.findAll());
			model.addAttribute("product", new Product());
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "product/add_prod";		
		}
		
		Product prod = productService.prodByDesignDescrip(product.getDesignation(), product.getDescription(), product.getIdProd());
		if(prod != null){
			model.addAttribute("categories", categoryService.findAll());
			model.addAttribute("unites", uniteService.findAll());
			model.addAttribute("product", new Product());
			model.addAttribute("existProd", "This porduct already exist.");
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "product/add_prod";
		}				
		
		productService.save(product);
		System.out.println(product.getIdProd());
		String key = "";
		if(product.getIdProd() < 10){
			key = "000"+product.getIdProd();			
		}else if(product.getIdProd() < 100){
			key = "00"+product.getIdProd();
		}else if(product.getIdProd() < 1000){
			key = "0"+product.getIdProd();
		}else{
			key = ""+product.getIdProd();
		}
		product.setDesignation(product.getDesignation().replace(product.getDesignation().substring(0, 1), product.getDesignation().substring(0, 1).toUpperCase()));
			
		if(product.getDesignation().length()<3){
			String cp = "["+product.getDesignation().toUpperCase()+"] - "+key;
			product.setCodeProduct(cp);
		}else{
			String cp = "["+product.getDesignation().substring(0, 3).toUpperCase()+"] - "+key;
			product.setCodeProduct(cp);
		}			
		productService.save(product);
		Mouvement mouvement = new Mouvement();
		mouvement.setDateMouv(new Date());
		mouvement.setProduct(product.getCodeProduct());
		mouvement.setQte(product.getQteStock());
		mouvement.setTypeMouv("in after adding a new product by "+userService.getOne(new Home().curentUser()).getNom());
		return "redirect:/utilisateur/product";
	}
	
	//                               PRINT PRODUCT                               //
	
	@RequestMapping(value="/utilisateur/printProd", method = RequestMethod.GET)
	public String printProd(Model model){
		int articles = 0, items = 0;
		double amount = 0.0;
		
		for (Product prod : productService.findAll()) {
			items++;
			articles += prod.getQteStock();
			amount += prod.getPrice() * prod.getQteStock();
		}
		model.addAttribute("items", items);
		model.addAttribute("articles", articles);
		model.addAttribute("amount", amount);
		model.addAttribute("products", productService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "product/elements_prod";
	}
	
	//                                    edit product                            //
	
	@RequestMapping(value="/utilisateur/modifyProd", method = RequestMethod.GET)
	public String modifyProd(Model model, int idProd){
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("unites", uniteService.findAll());
		model.addAttribute("product", productService.getOne(idProd));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "product/edit_prod";
	}	
	
	//                               delete product                             //
	
	@RequestMapping(value="/utilisateur/deleteProd", method = RequestMethod.GET)
	public String deleteProduct(int idProd){
		Activity act = new Activity();
		act.setAction("Supression of product");
		act.setDateAct(new Date());
		act.setUser(userService.getOne(new Home().curentUser()));
		activityService.save(act);
		productService.deleteById(idProd);
		return "redirect:product";
	}
	
	//                                Add stock                             //
	
	@RequestMapping(value="/utilisateur/addStock", method = RequestMethod.GET)
	public String addstock(Model model, int idProd){
		model.addAttribute("product", productService.getOne(idProd));
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "product/add_stock";
	}
	
	//                                    Save stock                           //
	
	@RequestMapping(value="/utilisateur/saveStock", method = RequestMethod.POST)
	public String savestock(Model model, int idProd, int quantity){
		Product product = productService.getOne(idProd);
		product.setQteStock(product.getQteStock() + quantity);
		productService.save(product);
		
		Mouvement mouv = new Mouvement();
		mouv.setDateMouv(new Date());
		mouv.setProduct(product.getCodeProduct());
		mouv.setQte(quantity);
		mouv.setTypeMouv("in after adding product by "+userService.getOne(new Home().curentUser()).getNom());
		mouvementService.save(mouv);
		return "redirect:/utilisateur/product";
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
////////////////////////////////////////////      CATEGORY CATEGORY       ///////////////////////////////////////////////////////////////
	
	// Categories list
	@Autowired
	public CategoryDAO categoryService;
	// category list
	@RequestMapping(value="/utilisateur/category", method = RequestMethod.GET)
	public String categories(Model model){
		model.addAttribute("category", new Categorie());
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "category/categorie";
	}
	
	//Save category
	@RequestMapping(value="/utilisateur/saveCat", method = RequestMethod.POST)
	public String saveCat(@Valid Categorie category, BindingResult br, Model model){
		model.addAttribute("category", new Categorie());
		model.addAttribute("categories", categoryService.findAll());
		Categorie categorie = categoryService.existCat(category.getLibelleCat());
		if(categorie != null){
			model.addAttribute("existCat", "This is already exist");
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "category/categorie";
		}
		if(br.hasErrors()){
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "category/categorie";
		}
		categoryService.save(category);
		return "redirect:/utilisateur/category";
	}
	// modify category
	@RequestMapping(value="/utilisateur/editCat", method = RequestMethod.GET)
	public String modifyCat(Model model, int idCat){
		model.addAttribute("categorie", categoryService.getOne(idCat));
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "category/categorie";
	}
	@Autowired
	public ActivityDAO actService;
	// delete category
	@RequestMapping(value="/utilisateur/deleteCat", method = RequestMethod.GET)
	public String deleteCat(int idCat){
		Activity act = new Activity();
		act.setAction("Supression of Category");
		act.setUser(userService.getOne(new Home().curentUser()));
		act.setDateAct(new Date());
		activityService.save(act);
		categoryService.deleteById(idCat);
		return "redirect:/utilisateur/category";
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////
///////////////////////////////////////////////        UNITE         //////////////////////////////////////////////////////////////////////

	@Autowired
	public UniteDAO uniteService;
	// unite list
	@RequestMapping(value="/utilisateur/unite", method = RequestMethod.GET)
	public String unites(Model model){
		model.addAttribute("unite", new Unite());
		model.addAttribute("measures", uniteService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "category/measure";
	}
	// save unite
	@RequestMapping(value="/utilisateur/saveUnite", method = RequestMethod.POST)
	public String saveUnit(@Valid Unite unite, BindingResult br, Model model){
		
		Unite unit = uniteService.existUnite(unite.getLibelleUnit());
		if(unit != null){
			model.addAttribute("existUnit", "This is already exist");
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "category/measure";
		}
		if(br.hasErrors()){
			model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
			return "category/measure";
		}
		uniteService.save(unite);
		return "redirect:/utilisateur/unite";
	}
	// modify unite
	@RequestMapping(value="/utilisateur/editUnite", method = RequestMethod.GET)
	public String modifyUnit(Model model, int idUnit){
		model.addAttribute("measure", uniteService.getOne(idUnit));
		model.addAttribute("measures", uniteService.findAll());
		model.addAttribute("usercon", userService.getOne(new Home().curentUser()));
		return "category/measure";
	}
	// delete unite
	@RequestMapping(value="/utilisateur/deleteUnite", method = RequestMethod.GET)
	public String deleteUnit(int idUnit){
		Activity act = new Activity();
		act.setAction("Supression of measure");
		act.setDateAct(new Date());
		act.setUser(userService.getOne(new Home().curentUser()));
		activityService.save(act);
		uniteService.deleteById(idUnit);
		return "redirect:/utilisateur/unite";
	}
}
