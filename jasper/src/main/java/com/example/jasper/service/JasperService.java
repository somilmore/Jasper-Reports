package com.example.jasper.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jasper.model.Jasper;
import com.example.jasper.repository.JasperRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

@Service
public class JasperService {

	@Autowired
	private JasperRepository jasperRepository;

	public String exportReport(List<String> columns) throws FileNotFoundException, JRException {
		
		List<Jasper> jasperData = jasperRepository.findAll();

	    // Create JasperDesign
	    JasperDesign jasperDesign = new JasperDesign();
	    jasperDesign.setName("dynamicReport");

	    // Create fields dynamically based on user-requested columns
	    for (String column : columns) {
	        JRDesignField field = new JRDesignField();
	        field.setName(column);
	        field.setValueClass(Object.class);
	        jasperDesign.addField(field);
	    }

	    // Create query
	    JRDesignQuery query = new JRDesignQuery();
	    query.setText("SELECT * FROM JASPER");
	    jasperDesign.setQuery(query);
	    
	 // Create header band
	    JRDesignBand headerBand = new JRDesignBand();
	    headerBand.setHeight(20);

	    // Initialize X position
	    int xPos = 0;

	 // Add column headers to header band
	    for (String column : columns) {
	        JRDesignTextField headerField = new JRDesignTextField();
	        headerField.setX(xPos); // Set X position
	        headerField.setY(0);
	        headerField.setWidth(100);
	        headerField.setHeight(20);
	        
	        JRDesignExpression expression = new JRDesignExpression();
	        expression.setText("\"" + column + "\""); // Set text using expression
	        headerField.setExpression(expression);
	        
	        headerBand.addElement(headerField);

	        // Increase X position for the next header field
	        xPos += 80; // Increase by desired spacing
	    }
	    // Add header band to design
	    jasperDesign.setColumnHeader(headerBand);

	 // Create band
	    JRDesignBand band = new JRDesignBand();
	    band.setHeight(20);

	    // Initialize X position
	    xPos = 0;

	    // Add fields to band
	    for (String column : columns) {
	        JRDesignField field = new JRDesignField();
	        field.setName(column);
	        JRDesignTextField textField = new JRDesignTextField();
	        textField.setX(xPos); // Set X position
	        textField.setY(0);
	        textField.setWidth(100);
	        textField.setHeight(20);
	        textField.setExpression(new JRDesignExpression("$F{" + column + "}"));
	        band.addElement(textField);

	        // Increase X position for the next field
	        xPos += 80; // Increase by desired spacing
	    }

	    // Add band to design
	    ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);

	    // Compile the JasperDesign
	    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

	    // Populate data
	    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jasperData);

	    // Fill the report
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

	    // Export to PDF
	    String filePath = "D:\\Somil\\jasperReport.pdf";
	    JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);

	    return "Report generated successfully and stored at " + filePath;
    }
}
