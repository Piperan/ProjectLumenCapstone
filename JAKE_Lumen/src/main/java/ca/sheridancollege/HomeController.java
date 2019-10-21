package ca.sheridancollege;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ca.sheridancollege.bean.Form;
import ca.sheridancollege.bean.Project;
import ca.sheridancollege.bean.User;
import ca.sheridancollege.dao.DAO;

@Controller
public class HomeController {
	//DAO Call
	DAO dao=new DAO();
	String username;
	
	//Initial Mapping
	@GetMapping("/") 
	public String goindex(Model model)
	{
		return "th_landing";
	}
	
	//Generate Users
	@GetMapping("/gen") 
	public String goindex2(Model model)
	{
		//DAO Method to generate user
		dao.generateDummyUser();
		return "th_landing";
	}
	
	//Login Error Page
	@GetMapping("/error") 
	public String goError()
	{
		return "error/th_error";
	}
	
	@RequestMapping("/user") 
	public String goSecuredHome(Model model)
	{
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		System.out.print("The Current user is " + username);
		model.addAttribute("username",username);
		User user = dao.getUserByUsername(username);
		List<Project> projects=dao.getAllProjectsByUser(dao.getUserByUsername(username));
		model.addAttribute("projects",projects);
//		projects.forEach((Project project) ->{
//			System.out.println(project.getUsers().toString());
//		});
		return "user/homeUser";
	}
	
	@RequestMapping("/user/createProject") 
	public String goCreateProject(Model model)
	{
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		System.out.print("The Current user is " + username);
		//User x= dao.findUserByName(username);
		//System.out.println(x);
		model.addAttribute("projects",dao.getAllProjects());
		return "user/homeUser";
	}
	@RequestMapping("/generateProj") 
	public String goGenProj(@ModelAttribute User user, Model model)
	{
		dao.generateProjects();
		return "user/homeUser";
	}
	@GetMapping("/admin") 
	public String goAdminHome()
	{
		
		return "admin/th_home";
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "th_login";
    }
	
    @RequestMapping("/success")
    public void loginPageRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {

        String role =  authResult.getAuthorities().toString();

        if(role.contains("ROLE_ADMIN")){
         response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/admin"));                            
         }
         else if(role.contains("ROLE_USER")) {
             response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/user"));
         }
    }
	@RequestMapping("/Edit/{username}") 
	public String editMovie(Model model, @PathVariable String username) {
		return "user/th_home"; 
	}
	@RequestMapping("/register") 
	public String goregister(Model model, @ModelAttribute User user)
	{
		User p2=new User();
	    model.addAttribute("user",p2);
		return "register";
	}
	@RequestMapping("/registerSend") 
	public String goregisterSend(Model model, @ModelAttribute User user)
	{
		synchronized(User.class) { //prevents thread interfererance
			user.setEnabled(false);
			dao.addUser(user);
		}
		User p2=new User();
	    model.addAttribute("user",p2);
		return "register";
	}
	@GetMapping("/admin/registrationRequests") 
	public String goRegistrationRequestPage(Model model)
	{
		model.addAttribute("users",dao.getDisabledUsers());
		return "admin/registrationRequests";
	}
	@GetMapping("/admin/viewUsers") 
	public String goViewUsersPage(Model model)
	{
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		System.out.print("The Current user is " + username);
		model.addAttribute("username",username);
		model.addAttribute("users",dao.getEnabledUsers());
		return "admin/viewUsers";
	}
	@RequestMapping("/admin/deleteEnabledUser/{userid}") 
	public String goDeleteEnabledVoter(Model model, @PathVariable int userid) { 
		User user = dao.getUserById(userid);
		if(user != null) {
			System.out.println(userid);
			dao.deleteUserById(userid);
		}else {
			user = new User();
		}
		model.addAttribute("users",dao.getEnabledUsers());
		return "admin/viewUsers";
	}
	@RequestMapping("/admin/deleteDisabledUser/{userid}") 
	public String goDeleteDisabledVoter(Model model, @PathVariable int userid) { 
		User user = dao.getUserById(userid);
		if(user != null) {
			System.out.println(userid);
			dao.deleteUserById(userid);
		}else {
			user = new User();
		}
		model.addAttribute("users",dao.getDisabledUsers());
		return "admin/registrationRequests";
	}
	
	@RequestMapping("/EditUser")
	public String editUser2(@ModelAttribute User user, Model model, @RequestParam int userid) {

		dao.editUser(userid, user);
		
		List<User> users = dao.getAllUsers();
		model.addAttribute("users",users);

		return "admin/viewUsers";

	}
	@RequestMapping("/admin/editUser/{userid}") 
	public String goEditUser(Model model, @PathVariable int userid) { 

		User user = dao.getUserById(userid);
		
		model.addAttribute("user",user);
		return "admin/EditMovie";
	}
	@RequestMapping("/admin/enableUser/{userid}") 
	public String goEnableUsers(Model model, @PathVariable int userid) { 

		User user = dao.getUserById(userid);
		dao.enableUser(userid, user);
		
		model.addAttribute("users",dao.getDisabledUsers());
		return "admin/registrationRequests";
	}
	//Never Used?
	@RequestMapping("/user/projectAssign") 
	public String assignProject()
	{
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		User x= dao.findUserByName("username");
		List<Project> prjectList =new ArrayList<Project>();
		prjectList.add(dao.getProjectById(1));
		prjectList.add(dao.getProjectById(2));
		prjectList.add(dao.getProjectById(3));
		dao.projectUser(x, prjectList);
		return "user/homeUser";
	}
	
	@RequestMapping("/user/projectForms") 
	public String formsProject(@ModelAttribute Project project, Model model)
	{
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		User x= dao.findUserByName("username");
		List<Project> prjectList =new ArrayList<Project>();
		prjectList.add(dao.getProjectById(1));
		prjectList.add(dao.getProjectById(2));
		prjectList.add(dao.getProjectById(3));
		dao.projectUser(x, prjectList);
		return "user/homeUser";
	}
	//Page that adds a project
	@RequestMapping("/user/addProject") 
	public String addProject(@ModelAttribute Project project, Model model) { 
		String username;
		String warning = "";
		List<Form> formsProj = new ArrayList<Form>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		User x= dao.findUserByName("username");
		
		List<Project> prjectList =new ArrayList<Project>();
		
		
		 Form form1 = new Form("F-CDM-A","https://cdm.unfccc.int/Reference/PDDs_Forms/Accreditation/accr_form01.pdf");
		 Form form2 = new Form("CDM-DOO-FORM","https://cdm.unfccc.int/sunsetcms/storage/contents/stored-file-20181001095253054/AccrForm31_DOO.pdf");
		 Form form3 = new Form("F-CDM-SCC","https://cdm.unfccc.int/sunsetcms/storage/contents/stored-file-20181001095213533/AccrForm22_SCC.pdf");
		 Form form4 = new Form("F-CDM-W","https://cdm.unfccc.int/Reference/PDDs_Forms/Accreditation/accr_form25.pdf");
		 Form form5 = new Form("F-CDM-FPM","https://cdm.unfccc.int/Reference/PDDs_Forms/Accreditation/accr_form13.pdf");
		 formsProj.add(form1);
		 formsProj.add(form2);
		 formsProj.add(form3);
		 formsProj.add(form4);
		 formsProj.add(form5);
		 switch(project.getType()) {
		  case "Afforrestation & Reforrestation":
			 Form form6 = new Form("F-CDM-AR-AM-Rev","https://cdm.unfccc.int/Reference/PDDs_Forms/Methodologies/methAR_form08.pdf");
			 Form form7 = new Form("F-CDM-AR-AM-Subm","https://cdm.unfccc.int/Reference/PDDs_Forms/Methodologies/methAR_form09.pdf");
			 formsProj.add(form6);
			 formsProj.add(form7);
		    break;
		  case "Carbon Capture and Storage":
			
		    break;
		  case "Small Scale":
			  break;
		  case "Small Scale with AR & RF":
			  break;
		  default:
		}
		 project.setForms(formsProj);
		synchronized(Project.class) {
			if(dao.validateProject(project).isEmpty()) {
				if(dao.addProject(project,dao.getUserByUsername(username))) {
						warning="Project Added";
						model.addAttribute("project",new Project());
						prjectList.add(project);
						
				}
			
				else {
					warning="Project not Added";
				}
			}
			else {
				model.addAttribute("project",project);
				model.addAttribute("errorList",dao.validateProject(project));
			}
			
		}
		
		
		model.addAttribute("formsList", project.getForms());
		model.addAttribute("error",warning);	
	    
		return "user/createProject";
	}
	
	@RequestMapping("/user/saveProject") 
	public String saveProject(Model model) { 
		model.addAttribute("project",new Project());
		return "user/createProject";
	}
	@RequestMapping("/user/myReports") 
	public String myReport(Model model) { 
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		User x= dao.findUserByName(username);
		List<Project> projList=x.getProjects();
		model.addAttribute("username",username);
		model.addAttribute("projects",projList);
		return "user/viewReports";
	}
	
	@RequestMapping("/user/myOverdue") 
	public String tryMe(Model model) { 
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		User x= dao.findUserByName(username);
		List<Project> projList=x.getProjects();
		List<Project> overList=new ArrayList<Project>();
		for (Project ccc :projList) 
		{ 
			String endDate = ccc.getEndDate();     //input string
			String lastFourDigits = "";     //substring containing last 4 characters
			 
			if (endDate.length() > 4)
			{
			    lastFourDigits = endDate.substring(endDate.length() - 4);
			    try
			    {
			      int i = Integer.parseInt(lastFourDigits.trim());
			      if(i<2019)
			      {
			      overList.add(ccc);
			      }
			    }
			    catch (NumberFormatException nfe)
			    {
			      System.out.println("NumberFormatException: " + nfe.getMessage());
			    }
			}
			else
			{
			    lastFourDigits = endDate;
			}
			 
		}
		model.addAttribute("username",username+"   ---   Projects that are: OVERDUE");
		model.addAttribute("projects",overList);
		return "user/viewReports";
	}
	@RequestMapping("/user/viewProject") 
	public String viewProject(Model model) { 
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		model.addAttribute("username",username);
		
		return "user/viewProject";
	}
}