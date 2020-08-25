package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.Composition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
public class CompositionController {

    @GetMapping("/admin/chem")
    public String chem(Model model) {
        model.addAttribute("C", "-");
        model.addAttribute("Si", "-");
        model.addAttribute("Mn", "-");
        model.addAttribute("Cr", "-");
        model.addAttribute("Ni", "-");
        model.addAttribute("Cu", "-");
        return "chem";
    }

    @PostMapping("/admin/chem")
    public String chemPost(@RequestParam String OD, @RequestParam String OT, @RequestParam String OC,
                           @RequestParam String OSi, @RequestParam String OMn, @RequestParam String OCr,
                           @RequestParam String ONi, @RequestParam String OCu, @RequestParam String IC,
                           @RequestParam String ISi, @RequestParam String IMn, @RequestParam String ICr,
                           @RequestParam String INi, @RequestParam String ICu, Model model) throws IOException {
        String[] inputs = {OD, OT, OC, OSi, OMn, OCr, ONi, OCu, IC, ISi, IMn, ICr, INi, ICu};
        Map<String, Double> map = Composition.calculate(inputs);
        model.addAttribute("C", map.get("C"));
        model.addAttribute("Si", map.get("Si"));
        model.addAttribute("Mn", map.get("Mn"));
        model.addAttribute("Cr", map.get("Cr"));
        model.addAttribute("Ni", map.get("Ni"));
        model.addAttribute("Cu", map.get("Cu"));


        return "chem";
    }
}
