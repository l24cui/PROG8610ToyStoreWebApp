package com.toystorewithdb.toystore;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Controller
@RequestMapping("/")
public class ToystoreController implements CommandLineRunner {

  @Autowired
  private ToyRepository toyRepo;

  @Autowired
  private CartRepository cartRepo;

  @Autowired
  private UserRepository userRepo;
  private User sessionUser;

 @GetMapping("/login")
  public String login(@ModelAttribute("user") User user) {
    return "login";
  }

  @PostMapping("/login")
  public String checkUser(@Valid User user, BindingResult result) {
    // TODO: Real Authentication Needed! (Compare with database results)
    if (result.hasErrors()) {
      return "login";
    }
    // (!userRepo.findByUsername(user.getUsername()).isPresent() || (userRepo.findByUsername(user.getUsername()).get().getPassword() != user.getPassword()))
    sessionUser = user;
    return "redirect:/toys";
  }

  @GetMapping(value = "/signup")
  public String signup(@ModelAttribute("user") User user) {
    return "signup";
  }

  @PostMapping("/signup")
  public String checkRegister(@Validated(SignupChecks.class) User user, BindingResult result) {
    // TODO: Check Existence!
    if (result.hasErrors()) {
      return "signup";
    }
    userRepo.save(user);
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

    long cartcount = cartRepo.countByUsername(sessionUser.getUsername());
    model.addAttribute("cartcount", cartcount);
  }

  @GetMapping("/toys/addToCart")
  public String addToCart(@RequestParam int toyid, HttpServletRequest request){
    CartRecord rec = new CartRecord();
    rec.setToyid(toyid);
    rec.setUsername(sessionUser.getUsername());
    Optional<Toy> optionalToy = toyRepo.findById(toyid);
    if (optionalToy.isPresent()) {
      rec.setName(optionalToy.get().getName());
      rec.setDescription(optionalToy.get().getDescription());
    } else {
      request.getSession().setAttribute("MSG", "Toy No More Exists!");
      return "redirect:/toys";
    }

    Optional<CartRecord> optionalCRec = cartRepo.findByUsernameAndToyid(sessionUser.getUsername(), toyid);
    if (optionalCRec.isPresent()) {
      request.getSession().setAttribute("MSG", "The toy is already in your Shopping Cart!");
    } else {
      request.getSession().setAttribute("MSG", "Added to Cart!");
      cartRepo.save(rec);
    }
    return "redirect:/toys";
  }

  @GetMapping("/cart") 
  public void viewCart(Model model){
    List<CartRecord> cart = cartRepo.findAllByUsername(sessionUser.getUsername());
    model.addAttribute("cart", cart);
 }

  @GetMapping("/cart/removeFromCart")
  public String removeFromCart(@RequestParam int id, Model model){
    List<CartRecord> cart = cartRepo.findAllByUsername(sessionUser.getUsername());
    // TODO: pop up an alert before removing the item
    if (cart != null){
      cartRepo.deleteByCartid(id);
    }
    return "redirect:/cart";
  }
   
  @GetMapping("/signout")
  public String destroySession(HttpServletRequest request) {
    request.getSession().invalidate();
    return "redirect:/";
  }

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
