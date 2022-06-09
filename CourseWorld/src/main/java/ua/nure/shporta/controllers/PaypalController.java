package ua.nure.shporta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import ua.nure.shporta.model.User;
import ua.nure.shporta.service.SubscriptionService;
import ua.nure.shporta.service.UserService;
import ua.nure.shporta.service.impl.PaypalService;

@Controller
@RequestMapping("/pay")
public class PaypalController {

    @Autowired
    private PaypalService service;
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionService subscriptionService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "courses/";
    private static final String BASE_URL = "http://localhost:8080/CourseWorld/";

    @PostMapping
    public String payment(@RequestParam Integer courseId, @RequestParam Double price) {
        try {

            Payment payment = service.createPayment(price, BASE_URL + CANCEL_URL + courseId,
                    BASE_URL + SUCCESS_URL + "?courseId=" + courseId);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam Integer courseId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                User currentUser = userService.getCurrentUser();
                subscriptionService.subscribe(currentUser.getId(), courseId);
                return "redirect:/courses/" + courseId;
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

}
