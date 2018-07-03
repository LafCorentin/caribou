package com.bdd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.caribou.LogController;
import com.caribou.LogsRepository;
import com.mongodb.Mongo;

@Component
@EnableMongoRepositories(basePackageClasses = com.caribou.LogsRepository.class)
public class Recherche{
	// Une classe destin�e � traiter les requetes de filtrage des Logs
	// Va probablement utiliser JQUERY
	public LogsRepository logsRepository;
	@Autowired Mongo mongo;
	@Autowired MongoDbFactory mongoDbFactory;
	public String Filtre;
	
	public Recherche(LogsRepository input) {
		logsRepository=input;
	}
	
	public ModelAndView noFilter(ModelAndView mav) {
		mav.addObject("logs", logsRepository.findAll());
		return mav;
	}
	
	public int conversionDate(String date) {
		// Pour des dates de type heure:minute:sec.millisec
		return 0;
	}
	
	public ModelAndView filterBySeverityLvl(String severitylvl, ModelAndView mav) {
		mav.addObject("logs", logsRepository.findLogsBySeveritylvl(severitylvl));
		return mav;
	}
	
	public ModelAndView filterByDate(String datebeginning,String dateend, ModelAndView mav) {
		return mav;
	}
	
	public ModelAndView filterById(int idbeginning, int idend, ModelAndView mav) {
		return mav;
	}
	
	public ModelAndView filter(String filter, ModelAndView mav) {
		mav.addObject("filter", filter);
		System.out.println(filter);
		if (filter.equals("severityLvlFilter")) {
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
			String severitylvl = "DEBUG"; // A changer pour r�cup�rer le parametre de Corentin
			return filterBySeverityLvl(severitylvl,mav);
		}
		else {
			return noFilter(mav);
		}
	}
}