package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import com.venus.restapp.service.MyAccountService;


@Controller
@RequestMapping(value="/ravi")
public class MyController {
	@Autowired
	private MyAccountService myAccountService;

	private Map<Long, MyAccount> accounts = new ConcurrentHashMap<Long, MyAccount>();
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
	        model.addAttribute(new MyAccount());
		System.out.println("\n\n----------------I am returning the view---------------------\n");
		return "ravi/createForm";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid MyAccount myAccount, BindingResult result) {
		if (result.hasErrors()) {
			return "ravi/createForm";
		}
		System.out.println("\n\n----------------Adding accountId to list: ---------------------\n" + myAccount.getId());
		this.accounts.put(myAccount.assignId(), myAccount);
		/* dummy function call to test authorization */
		myAccountService.createAccount(myAccount);
		return "redirect:/ravi/" + myAccount.getId();
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getView(@PathVariable Long id, Model model) {
		MyAccount account = this.accounts.get(id);
		if (account == null) {
			throw new ResourceNotFoundException(id);
		}
		System.out.println("\n\n----------------I am going to show the account for id: ---------------------\n" + account.getId());
		model.addAttribute("myAccount", account);
		return "ravi/view";
	}

	@RequestMapping(value="all", method=RequestMethod.GET)
	public String viewAll(Model model) {
	  List<MyAccount> myAccounts = myAccountService.findAll();
	  if (myAccounts == null) {
	    throw new ResourceNotFoundException(1L);
	  }
	  System.out.println("\n\n----------------I am going to show all accounts: ---------------------\n" + myAccounts.size());
	  /* sending all account details  */
	  model.addAttribute("allAcounts", this.accounts.values());
	  return "ravi/viewAll";
	}

}
