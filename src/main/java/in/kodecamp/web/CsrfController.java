package in.kodecamp.web;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.UriRef;
import javax.mvc.binding.BindingResult;
import javax.mvc.binding.MvcBinding;
import javax.mvc.binding.ParamError;
import javax.mvc.security.CsrfProtected;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author hantsy
 */
@Path("csrf")
@Controller
@RequestScoped
public class CsrfController {

    @Inject
    BindingResult bindingResult;

    @Inject
    Models models;

    @Inject
    AlertMessage flashMessage;

    @Inject
    Logger log;

    @GET
    public String get() {
        return "csrf.xhtml";
    }

    @POST
    @CsrfProtected
    public String post(@FormParam("greeting") @MvcBinding @NotBlank String greeting) {
        if (bindingResult.isFailed()) {
            AlertMessage alert = AlertMessage.danger("Validation voilations!");
            bindingResult.getAllErrors().stream().forEach((ParamError t) -> {
                alert.addError(t.getParamName(), "", t.getMessage());
            });
            models.put("errors", alert);
            log.info("mvc binding failed.");
            return "csrf.xhtml";
        }

        log.info("redirect to greeting page.");
        flashMessage.notify(AlertMessage.Type.success, "Message:" + greeting);
        return "redirect:csrf";
    }

}
