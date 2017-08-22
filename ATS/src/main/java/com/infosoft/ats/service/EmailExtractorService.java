package com.infosoft.ats.service;

import org.springframework.http.ResponseEntity;

import com.infosoft.ats.model.UserDetails;
import com.infosoft.ats.utils.ResponseData;



public interface EmailExtractorService {
	
	public ResponseEntity<ResponseData> saveEmail(UserDetails user);

}
