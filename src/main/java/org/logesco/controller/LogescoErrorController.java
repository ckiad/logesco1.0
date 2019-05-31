/**
 * 
 */
package org.logesco.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cedrickiadjeu
 *
 */
@Controller
public class LogescoErrorController implements ErrorController {

	/**
	 * 
	 */
	@RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        //do something like logging
	 
	 	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	 	if (status != null) {
	 		Integer statusCode = Integer.valueOf(status.toString());
	 		 if(statusCode == HttpStatus.NOT_FOUND.value()) {
	             return "errors/404";
	         }
	         else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	             return "errors/500";
	         }
	         else if(statusCode == HttpStatus.FORBIDDEN.value()) {
	             return "errors/403";
	         }
	 		 System.err.println("Le code de l'erreur rencontrer est "+statusCode);
	 	}
        return "errors/inconnue";
    }

	/* (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.web.ErrorController#getErrorPath()
	 */
	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		String path_error = "/error";
		return path_error;
	}

}
