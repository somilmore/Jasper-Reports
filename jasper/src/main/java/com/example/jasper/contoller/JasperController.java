package com.example.jasper.contoller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jasper.service.JasperService;

import net.sf.jasperreports.engine.JRException;


@RestController
public class JasperController {

	@Autowired
    private JasperService jasperService;

	@GetMapping("/generateReport")
	public String generateReport(@RequestParam(value = "columns") List<String> columns) throws JRException, IOException {
	    
		return jasperService.exportReport(columns);
	}
}