package com.cusbee.kiosk.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */

@Controller
@RequestMapping(value = "/mail")
public class AddsController {

  /*  @Autowired
    private AdsService adsService;*/

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    /*@RequestMapping(value = "/ads-return", method = RequestMethod.GET)
    @ResponseBody
    //@Cacheable(value="movieFindCache2", key="#page")
    public ResponseContainer<Map<String,String>> adsReturn(@RequestParam(value = "page", required = true) Integer page){

        return adsService.adsReturn(page);
    }*/

    @RequestMapping(value = "/greetings", method = RequestMethod.POST)
    @ResponseBody
    //@Cacheable(value="movieFindCache2", key="#page")
    public void adsReturn1(@RequestParam(value = "email", required = true) String email, Locale locale, HttpServletRequest request, HttpServletResponse response) throws MessagingException {
        sendSimpleMail("Alex", email, locale, request, response);

    }

    public void sendSimpleMail(
            final String recipientName, final String recipientEmail, final Locale locale, HttpServletRequest request, HttpServletResponse response)
            throws MessagingException {

        // Prepare the evaluation context
        final WebContext ctx = new WebContext(request, response, request.getServletContext());
        //final Context ctx = new Context(LocaleContextHolder.getLocale());
        /*ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
*/
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Subscription email");
        message.setFrom("noreply.tritipgrill@gmail.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process("mail/test-mail", ctx);
        //final String htmlContent = this.htmlTemplateEngine.process("mail/test-mail.html", ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        this.javaMailSender.send(mimeMessage);
    }

}
