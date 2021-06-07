package com.loanscrefia.admin.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loanscrefia.admin.edu.service.EduService;

@Controller
@RequestMapping(value="/admin")
public class EduController {

	@Autowired private EduService eduService;
}
