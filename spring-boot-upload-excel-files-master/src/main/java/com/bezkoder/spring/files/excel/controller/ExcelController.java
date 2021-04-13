package com.bezkoder.spring.files.excel.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.files.excel.helper.ExcelHelper;
import com.bezkoder.spring.files.excel.message.ResponseMessage;
import com.bezkoder.spring.files.excel.model.Tutorial;
import com.bezkoder.spring.files.excel.service.ExcelService;
import com.example.springbootloggingexample.controller.MessageController;

import TutorialDto.TutorialDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@CrossOrigin("http://localhost:8081")
@Api(value = "ExcelController", description = "REST Apis related to tutorial Entity!!!!")
@Controller
@RequestMapping("/api/excel")
public class ExcelController {

  @Autowired
  ExcelService fileService;
@Autowired
 ModelMapper modelMapper;
Logger logger = LoggerFactory.getLogger(ExcelController.class);
  
  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if(file.isEmpty()) {
    	throw new IllegalArgumentException();
    }
    
    if(file.equals("String")) {
    	throw new IllegalStateException();
    }
    
    
    
    if (ExcelHelper.hasExcelFormat(file)) {
      try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        logger.debug("hello, debug in root level");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload an excel file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }
  
  @ExceptionHandler(value = { IllegalStateException.class})
	protected ResponseEntity<Object> handleException(IllegalStateException e) {
		return new ResponseEntity<Object>("illegal state exception in controller",  HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleException(IllegalArgumentException e){
		
		return new ResponseEntity<Object>("illegal arg exception in controller", HttpStatus.BAD_REQUEST);
	}
  
  
  
  @ApiOperation(value = "Get list of tutorials in the System ", response = Iterable.class, tags = "tutorials")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })

  @GetMapping("/tutorials")
  public ResponseEntity<TutorialDto> getAllTutorials() {
	  List<Tutorial> tutorial = fileService.getAllTutorials();
	  TutorialDto tutorialdto = modelMapper.map(tutorial, TutorialDto.class);
    //  return  fileService.getAllTutorials().stream().map(tutorial -> modelMapper.map(tutorial, TutorialDto.class)).collect(Collectors.toList());
	  return new ResponseEntity<TutorialDto>(tutorialdto, HttpStatus.OK);
  }
  @ApiOperation(value = "Download the excel file ", response = Tutorial.class, tags = "download")
  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "tutorials.xlsx";
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(file);
  }

}
