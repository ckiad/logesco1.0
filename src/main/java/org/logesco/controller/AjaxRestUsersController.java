/**
 * 
 */
package org.logesco.controller;

import javax.servlet.http.HttpServletRequest;

import org.logesco.entities.Evaluations;
import org.logesco.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cedrickiadjeu
 *
 */
@RestController
@RequestMapping(path="/logesco/users/ajaxrestusers")
public class AjaxRestUsersController {

	@Autowired
	private IUsersService usersService;
	
	@GetMapping(path="/testAjax/{idEleve}/{idEval}/{proportionEval}/{noteSaisi}", produces=MimeTypeUtils.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> testAjax(HttpServletRequest request,
			@PathVariable(name="idEleve") Long idEleve,
			@PathVariable(name="idEval") Long idEval,
			@PathVariable(name="proportionEval") String proportionEval,
			@PathVariable(name="noteSaisi") String noteSaisi){
		try{
			
			//String texteReponse = "**** les parametres passes a la requete sont: idEleves="+idEleve+" idEval="+idEval+" noteSaisi="+noteSaisi;
			
			int reponse = 0;
			
			//Eleves elv = usersService.findEleves(idEleve);
			
			/*
			 * Recuperer l'évaluation
			 */
			Evaluations evalConcerne = usersService.findEvaluations(idEval);
			if(evalConcerne == null) System.err.println("yyyyyyyyyyyyyyyyy evalConcerne non trouve");
			
			try{
				int newproportionEval = Integer.parseInt(proportionEval);
				double valNoteSaisi = Double.parseDouble(noteSaisi);
				/*
				 * On met à jour la nouvelle proportion de l'évaluation
				 */
				evalConcerne.setProportionEval(newproportionEval);
				
				int r = usersService.saveEvaluation(evalConcerne.getContenuEval(), evalConcerne.getCours(), evalConcerne.getDateenregEval(), 
						evalConcerne.getProportionEval(), evalConcerne.getSequence(), evalConcerne.getTypeEval());
				
				System.err.println("le r de saveEvalsssAjax == "+r);
				if(r == 0) reponse = -1;
				
				/*
				 * On peut donc enregistrer la note de l'élève
				 */
				int ret = usersService.saveNoteEvalEleve(idEval, idEleve, valNoteSaisi);
				reponse = ret;
				System.err.println("le r de saveNoteEvalAjax == "+reponse);
				if(ret == 0) reponse = -2;
				
				ResponseEntity<String> responseEntity = new ResponseEntity<String>(new String(""+reponse), HttpStatus.OK);
				return responseEntity;
			}
			catch(Exception e){
				reponse = -3;
				ResponseEntity<String> responseEntity = new ResponseEntity<String>(new String(""+reponse), HttpStatus.OK);
				return responseEntity;
			}
			
		}
		catch(Exception e){
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

}
