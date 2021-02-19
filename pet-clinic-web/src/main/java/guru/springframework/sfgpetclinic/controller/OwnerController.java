package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping({"/owners"})
@Controller
public class OwnerController {

    private OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String listOwners(Model model) {
        model.addAttribute("owners", ownerService.findAll());
        return "owners/index";
    }
    @GetMapping({"/find"})
    public String findOwners(){
        return "notimplemented";
    }

    @GetMapping("/{ownerId}")
    public String showOwners(@PathVariable String ownerId, Model model) {
        model.addAttribute("owner", ownerService.findById(Long.valueOf(ownerId)));
        return "owners/ownerDetails";
    }
}
