package com.toystorewithdb.toystore;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/backend")
public class BackendController {
  
  @Autowired
  private ToyRepository toyRepo;

  @GetMapping("/list")
  public void index(Model model) {
    model.addAttribute("toylist", toyRepo.findAll());
  }

  @GetMapping("/delete")
  public String delete(@RequestParam int toyid) {
      toyRepo.deleteById(toyid);
      return "redirect:/backend/list";
  }

  @GetMapping("/add")
  public String goAdd(@ModelAttribute("toy") Toy toy) {
    return "/backend/add";
  }

  @PostMapping("/add")
  public String addToy(@Valid Toy toy, BindingResult result) {
    if (result.hasErrors()) {
      return "/backend/add";
    }
    toyRepo.save(toy);
    return "redirect:/backend/list";
  }

  @GetMapping("/update")
  public String goUpdate(@RequestParam int id, @RequestParam String name, Model model, @ModelAttribute("toy") Toy toy) {
    model.addAttribute("id", id);
    model.addAttribute("name", name);
    toy.setId(id);
    return "/backend/update";
  }

  @PostMapping("/update")
  public String updateToy(@Valid Toy toy, BindingResult result, Model model) {
    if (result.hasErrors()) {
        return "/backend/update";
    }
    if (toyRepo.findById(toy.getId()).isPresent()) {
      toyRepo.save(toy);
    }
    return "redirect:/backend/list";
  }

}
