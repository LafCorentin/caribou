package com.caribou;

import java.util.ArrayDeque;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bdd.RemplirBdd;
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
	
	@RequestMapping(value = "/logIncome", method = RequestMethod.POST)
	@ResponseBody
	void logIncome(@RequestBody String newlog) {
		System.out.println("je reçois :");
		int id = 0;
		Logs tmp = new Logs(id,"");
		Queue<Queue<String>> logs = gson.fromJson(newlog, new TypeToken<Queue<Queue<String>>>() {
		}.getType());
		
		for(Queue<String> log : logs ) {
			for(String line : log) {
				tmp = new Logs(id, line);
//				id = tmp.getId();
//				tmp.setId(id);
				id++;
				logsRepository.save(tmp);
			}
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
	ModelAndView

			index() {
		mav.setViewName("index");
		return mav;
	}

	@RequestMapping(value = "/listeLogs", method = RequestMethod.GET)
	@ResponseBody
	ModelAndView listeLogs(ModelAndView mav,
			@RequestParam(value = "filter", required = true/* , defaultValue="nofilter" */) String filter,
			@RequestParam(value = "datebeginning", required = false) String datebeginning,
			@RequestParam(value = "dateend", required = false) String dateend) {
		// On veut afficher une liste de logs pour l'instant on affiche uniquement les
		// ID et les messages
		// On constate qu'une m�thode get sur chaque log suffit � afficher le bon truc
		RemplirBdd objremplirbdd = new RemplirBdd();
		ArrayDeque<String> ad = new ArrayDeque<String>(1000); // De base l'array deque en contient 16
		ad.addFirst(
				"10:55:47,539 INFO  [org.apache.cxf.interceptor.LoggingInInterceptor] (default task-158) Inbound Message\r\n"
						+ "----------------------------\r\n" + "ID: 1041\r\n"
						+ "--------------------------------------\r\n"
						+ "---------------------------\r\n" + "ID: 1041\r\n" + "Response-Code: 200\r\n"
						+ "    \"identifier\" : 640007027\r\n" + "  }\r\n" + "}" + "---------------------------");
		ad.addFirst(
				"10:55:47,539 DEBUG  [org.apache.cxf.interceptor.LoggingInInterceptor] (default task-158) Inbound Message\r\n"
						+ "----------------------------\r\n" + "ID: 1041\r\n"
						+ "--------------------------------------\r\n"
						+ "---------------------------\r\n" + "ID: 1041\r\n" + "Response-Code: 200\r\n"
						+ "    \"identifier\" : 640007027\r\n" + "  }\r\n" + "}" + "---------------------------");
		ad.addFirst("Log du controller � mettre au nouvel endroit");
//		MongoClient mongoClient = new MongoClient("localhost", 27017);
//		@SuppressWarnings("deprecation")
//		DB db = mongoClient.getDB("logsRepository");
//		db.logsRepository.save(new Logs(25,"Logs issus de db.logs.save"));
		objremplirbdd.remplir(ad, logsRepository);
		mav.addObject("logs", logsRepository.findAll());
		mav.addObject("filter", filter);
		mav.addObject("datebeginning", datebeginning);
		mav.addObject("dateend", dateend);
		mav.setViewName("listeLogs");
		return mav;
	}

	@RequestMapping(value = "/gestionBdd", method = RequestMethod.GET)
	ModelAndView gestionBdd(ModelAndView mav) {
		mav.setViewName("gestionBdd");
		return mav;
	}

	@RequestMapping(value = "/gestionBdd/viderBdd", method = RequestMethod.GET)
	ModelAndView viderBdd(ModelAndView mav) {
		mongo.dropDatabase(mongoDbFactory.getDb().getName());
		mav.setViewName("gestionBdd");
		return mav;
	}

//	@RequestMapping(value = "/gestionBdd/creerBdd", method = RequestMethod.GET)
//	ModelAndView viderBdd(ModelAndView mav,
//			@RequestParam(value = "bddname", required = false, defaultValue = "logsRepository") String bddname) {
//		// CHECKER SI CA SUPPRIME PAS SI Y EN A DEJA UNE
//		mongo.getCollection(bddname);
//		mav.setViewName("gestionBdd");
//		return mav;
//	}

}


