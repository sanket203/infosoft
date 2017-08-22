package com.infosoft.ats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infosoft.ats.model.UserDetails;
import com.infosoft.ats.service.EmailExtractorService;
import com.infosoft.ats.utils.ResponseData;

@Controller
public class EmailExtractorController {
	
	@Autowired
	EmailExtractorService emailExtractorService;
	
	@RequestMapping(value="/getEmailData", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody ResponseEntity<ResponseData> registerMobile(@RequestBody UserDetails user){
		ResponseEntity<ResponseData> response = emailExtractorService.saveEmail(user);
		return response;
	}

}
