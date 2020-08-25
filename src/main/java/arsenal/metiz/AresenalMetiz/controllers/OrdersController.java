package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.models.Order;
import arsenal.metiz.AresenalMetiz.repo.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrdersController {

    @Autowired
    OrdersRepository ordersRepository;

    @GetMapping("/admin/orders")
    public String orders(Model model) {
        Iterable<Order> orders = ordersRepository.findAll();
        model.addAttribute("ord", orders);
        return "orders";
    }

    @GetMapping("/admin/orders/add")
    public String addOrderGet(Model model) {
        return "addOrder";
    }

    @PostMapping("/admin/orders/add")
    public String addOrderPost(@RequestParam String mark, @RequestParam String diameter,
                               @RequestParam String packing, @RequestParam String mass,
                               @RequestParam String date) {
        String massF = mass.replaceAll(",", ".");
        Order order = new Order(mark, diameter, packing, date, Float.parseFloat(massF));
        ordersRepository.save(order);
        return "redirect:/admin/orders";
    }
}
