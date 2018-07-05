package com.caribou;

import java.util.Arrays;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bdd.ParametresRecherche;
import com.bdd.Recherche;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.Mongo;

@RestController
@Controller
@Component
@EnableMongoRepositories(basePackageClasses = com.caribou.LogsRepository.class)
@Repository
public class LogController {
	@Autowired
	ParametresRecherche param;
	@Autowired
	Gson gson;
	@Autowired
	String regexAgent;
	@Autowired
	LogsRepository logsRepository;
	@Autowired
	Mongo mongo;
	@Autowired
	MongoDbFactory mongoDbFactory;
	@Autowired
	ModelAndView mav;
	@Autowired
	Recherche recherche;

	// Certains Logs apparaissent en double, il faut checker si c'est un probleme
	// dID ou d'enregistrement

	@RequestMapping(value = "/logIncome", method = RequestMethod.POST)
	@ResponseBody
	void logIncome(@RequestBody String newlog) {
		// Fonction qui convertit le json en objet java pour sauvegarder les r�sulats
		// dans la BDD
		System.out.println("je reçois :");
		Queue<Queue<String>> logs = gson.fromJson(newlog, new TypeToken<Queue<Queue<String>>>() {
		}.getType());
		for (Queue<String> log : logs) {
			String res = "";
			int i =0;
			for (String tmp : log) {
				res += tmp;
				i++;
			}
			if (i>1)
				System.out.println("Nb d'�lements dans la queue" +i);
			logsRepository.save(new Logs(res));
		}
	}

	@RequestMapping(value = "/regexOutput", method = RequestMethod.GET)
	String regexOutcome() {
		return regexAgent;
	}

	@RequestMapping(value = "/getParamAgent", method = RequestMethod.GET)
	String paramOutcome() {
		// TODO
		return "";
	}

	public void LogsResource(LogsRepository logsRepository) {
		this.logsRepository = logsRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView index() {
		// Retourne � l'acceuil
		mav.setViewName("index");
		return mav;
	}

	@RequestMapping(value = "/listeLogs", method = RequestMethod.GET)
	@ResponseBody
	ModelAndView listeLogs(ModelAndView mav,
			@RequestParam(value = "selectedfilters", required = false,defaultValue="") String selectedfilters,
			@RequestParam(value = "selectedseveritylvls", required = false,defaultValue="") String selectedseveritylvls,
			@RequestParam(value = "selectedregexsps", required = false,defaultValue="") String selectedregexps,
			@RequestParam(value = "detectiondate", required = false,defaultValue="") String detectiondate,
			@RequestParam(value = "detectionidlog", required = false,defaultValue="") String detectionidlog,
			@RequestParam(value = "agent", required = false,defaultValue="") String agent,
			@RequestParam(value = "datebeginning", required = false,defaultValue="defaultbeginning") String datebeginning,
			@RequestParam(value = "dateend", required = false,defaultValue="defaultend") String dateend) {
		// TROUVEZ UN MOYEN DE COMPARER LES DATES
		// Fonction qui affiche tous les logs de la base de donn�es, � terme elle devra
		// afficher seuelement selon les filtres
		// On veut afficher une liste de logs pour l'instant on affiche uniquement les
		// ID et les messages
		mav.clear();
		param.setSelectedfilters(Arrays.asList(selectedfilters.split("\\,")));
		param.setSelectedregexps(Arrays.asList(selectedregexps.split("\\,")));
		for (String filt : param.getSelectedfilters()) {
			if (filt.equals("regexpFilter")) {
				for (String regexp : param.getSelectedregexps()){
					recherche.filterByRegex(regexp, mav);
				}
			} else {
				mav = recherche.filter(filt, mav);
			}
		}
		mav.addObject("datebeginning", datebeginning);
		mav.addObject("dateend", dateend);
		mav.setViewName("listeLogs");
		return mav;
	}

	@GetMapping(value = "/listeLogs/afficherunLog")
	ModelAndView afficherUnLog(ModelAndView mav, @RequestParam(value = "idlog") int idlog) {
		mav.addObject("logs", logsRepository.findOneByIdlog(idlog));
		mav.setViewName("listeLogs");
		return mav;
	}

	@RequestMapping(value = "/gestionBdd", method = RequestMethod.GET)
	ModelAndView gestionBdd(ModelAndView mav) {
		// Fait acc�der � l'�cran de gestion de la BDD
		mav.setViewName("gestionBdd");
		return mav;
	}

	@RequestMapping(value = "/gestionBdd/viderBdd", method = RequestMethod.GET)
	ModelAndView viderBdd(ModelAndView mav) {
		// EN un clic sur le bouton vide toute la BDD
		mongo.dropDatabase(mongoDbFactory.getDb().getName());
		mav.setViewName("gestionBdd");
		return mav;
	}

	@RequestMapping(value = "/parametres_recherche", method = RequestMethod.GET)
	ModelAndView parametresRecherche(ModelAndView mav)
	{
		mav.setViewName("parametres_recherche");
		return mav;
	}
	
}
