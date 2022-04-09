package com.toystorewithdb.toystore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@Controller
public class ToystoreController implements CommandLineRunner {

  @Autowired
  private ToyRepository toyRepo;

  @Autowired
  private CartRepository cartRepo;

 @GetMapping("/login")
  public String login(@ModelAttribute("user") User user, Model model, HttpServletRequest request) {
    return "login";
  }

  @PostMapping("/login")
  public String checkUser(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
    // TODO: Real Authentication Needed! (Compare with database results)
    if (result.hasErrors()) {
      return "login";
    }
    request.getSession().setAttribute("USERNAME", user.getUsername());
    model.addAttribute("username", user.getUsername());
    return "redirect:/toys";
  }

  @GetMapping(value = "/signup")
  public String signup(@ModelAttribute("user") User user) {
    return "signup";
  }

  @PostMapping("/signup")
  public String checkRegister(@Validated(SignupChecks.class) User user, BindingResult result) {
    // TODO: Search in database and insert non-existing new user to database
    if (result.hasErrors()) {
      return "signup";
    }
    return "signupS";
  }
  
  @GetMapping("/toys/details")
  public String showMore(@RequestParam int id, Model model) {
    Toy errorToy = new Toy(); 
    errorToy.setId(0);
    errorToy.setName("Empty");
    errorToy.setDescription("Your requested Toy does not exist");
    Toy result = toyRepo.findById(id).orElse(errorToy);
    model.addAttribute("name", result.getName());
    model.addAttribute("description", result.getDescription());
    return "details";
  }
  
  @GetMapping("/toys") 
  public void getToys(Model model, HttpSession session){
    String msg = (String) session.getAttribute("MSG");
    model.addAttribute("msg", msg);

    long toyscount = toyRepo.count();
    model.addAttribute("toyscount", toyscount);

    List<Toy> toys = (List<Toy>) toyRepo.findAll();
    model.addAttribute("toys", toys);

    long cartcount = cartRepo.countByUsername((String) session.getAttribute("USERNAME"));
    model.addAttribute("cartcount", cartcount);
  }

  @GetMapping("/toys/addToCart")
  public String addToCart(@RequestParam int toyid, Model model, HttpServletRequest request){
    CartRecord rec = new CartRecord();
    rec.setToyid(toyid);
    rec.setUsername((String) request.getSession().getAttribute("USERNAME"));
    Optional<Toy> optionalToy = toyRepo.findById(toyid);
    if (optionalToy.isPresent()) {
      rec.setName(optionalToy.get().getName());
      rec.setDescription(optionalToy.get().getDescription());
    } else {
      request.getSession().setAttribute("MSG", "Toy No More Exists!");
      return "redirect:/toys";
    }

    Optional<CartRecord> optionalCRec = cartRepo.findByUsernameAndToyid((String) request.getSession().getAttribute("USERNAME"), toyid);
    if (optionalCRec.isPresent()) {
      request.getSession().setAttribute("MSG", "The toy is already in your Shopping Cart!");
    } else {
      request.getSession().setAttribute("MSG", "Added to Cart!");
      cartRepo.save(rec);
    }
    model.addAttribute("cart", cartRepo.findAllByUsername((String) request.getSession().getAttribute("USERNAME")));
    return "redirect:/toys";
  }

  @GetMapping("/cart") 
  public void viewCart(Model model, HttpSession session){
    List<CartRecord> cart = cartRepo.findAllByUsername((String) session.getAttribute("USERNAME"));
    model.addAttribute("cart", cart);
 }

  @GetMapping("/cart/removeFromCart")
  public String removeFromCart(@RequestParam int id, Model model, HttpServletRequest request){
    @SuppressWarnings("unchecked")
    List<CartRecord> cart = cartRepo.findAllByUsername((String) request.getSession().getAttribute("USERNAME"));
    if (cart != null){
      cartRepo.deleteByCartid(id);
    }
    model.addAttribute("cart", cart);
    return "redirect:/cart";
  }
   
  @GetMapping("/signout")
  public String destroySession(HttpServletRequest request) {
    request.getSession().invalidate();
    return "redirect:/";
  }   

/*
   @PostMapping("/toys/add")
  public ResponseEntity<Toy> addToy(@RequestBody Toy newToy) throws Exception {
    Toy toy = toyRepo.save(newToy);
    if (toy == null) {
      throw new Exception();
    } else {
      return new ResponseEntity<>(toy, HttpStatus.CREATED);
    }
  }
 */

  @Override
  public void run(String... args) throws Exception {

    toyRepo.deleteAll();
    cartRepo.deleteAll();

    Toy puzzle = new Toy();
    puzzle.setName("puzzle");
    puzzle.setDescription("a puzzle");

    Toy game = new Toy();
    game.setName("game");
    game.setDescription("a game");
    
    Toy doll = new Toy();
    doll.setName("doll");
    doll.setDescription("a doll");

    toyRepo.save(puzzle);
    toyRepo.save(doll);
    toyRepo.save(game);

  }
}
