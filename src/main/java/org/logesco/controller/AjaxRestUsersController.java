/**
 * 
 */
package org.logesco.controller;

import javax.servlet.http.HttpServletRequest;

import org.logesco.entities.Eleves;
import org.logesco.entities.Evaluations;
import org.logesco.entities.Sequences;
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
	
	public String mention(double note){
		if(note>=0 && note<5) return "NULL";
		else if(note>=5 && note<7) return "TRES FAIBLE";
		else if(note>=7 && note<8) return "FAIBLE";
		else if(note>=8 && note<9) return "INSUFFISANT";
		else if(note>=9 && note<10) return "MEDIOCRE";
		else if(note>=10 && note<12) return "PASSABLE";
		else if(note>=12 && note<14) return "ASSEZ BIEN";
		else if(note>=14 && note<16) return "BIEN";
		else if(note>=16 && note<18) return "TRES BIEN";
		else if(note>=18 && note<20) return "EXCELLENT";
		else if(note==20) return "PARFAIT";
		else  return "IMPOSSIBLE"; 
	}
	
	@GetMapping(path="/enregNote/{idEleve}/{idEval}/{proportionEval}/{noteSaisi}", produces=MimeTypeUtils.TEXT_PLAIN_VALUE)
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
				
				String mention = this.mention(valNoteSaisi);
				ResponseEntity<String> responseEntity = new ResponseEntity<String>(new String(""+mention), HttpStatus.OK);
				return responseEntity;
			}
			catch(Exception e){
				reponse = -3;
				String mention = "IMPOSSIBLE";
				ResponseEntity<String> responseEntity = new ResponseEntity<String>(new String(""+mention), HttpStatus.OK);
				return responseEntity;
			}
			
		}
		catch(Exception e){
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(path="/getNumeroEleveSuivant/{idEleve}/{idEval}", produces=MimeTypeUtils.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> testAjax(HttpServletRequest request,
			@PathVariable(name="idEleve") Long idEleve,
			@PathVariable(name="idEval") Long idEval){
		long reponse = 0;
		Evaluations evalConcerne = usersService.findEvaluations(idEval);
		Sequences seq = evalConcerne.getSequence();
		Eleves elvSvt = usersService.findElevesSuivant(idEleve, seq.getIdPeriodes());
		if(elvSvt == null) {
			reponse = -1;
		}
		else{
			reponse = elvSvt.getIdEleves();
		}
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(new String(""+reponse), HttpStatus.OK);
		return responseEntity;
	}

}
