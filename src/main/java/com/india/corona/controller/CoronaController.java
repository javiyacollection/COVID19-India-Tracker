package com.india.corona.controller;

import com.india.corona.dao.CoronaDAO;
import com.india.corona.entity.Corona;
import com.india.corona.service.CoronaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/cases")
public class CoronaController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoronaController.class);
	
	
	@Autowired
	private CoronaService service;
	
	@Autowired
	private CoronaDAO dao;
	
	@GetMapping("/view")
	public String viewCases(Model theModel) throws InterruptedException{
		
		LOGGER.info("VIEW CORONA CASES IN INDIA");
		
		// get customers from the service
		service.updateCases();
//		Thread.sleep(3000);
		List<Corona> cases = dao.findAll();
		System.out.println("corona result size: " +cases.size());
		System.out.println("corona result: " +cases.toString());
		// add the customers to the model
		theModel.addAttribute("cases", cases);
		
		return "view-cases";
	}
	
	@GetMapping("/load")
	public String updateCases(Model theModel) throws InterruptedException {
		service.updateCases();
		Thread.sleep(10000);
		return "redirect:/cases/view";
	}

}
