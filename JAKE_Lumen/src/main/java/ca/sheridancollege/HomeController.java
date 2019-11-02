package ca.sheridancollege;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.bean.*;
import ca.sheridancollege.dao.*;

@Controller
public class HomeController {
	//DAO Call
	DAO dao=new DAO();
	String username;
	
	private FileUploadDAO fileUploadDao;
	
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
	
	String getUsername(){
		String getMe;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			getMe= ((UserDetails)principal).getUsername();
		} else {
			getMe = principal.toString();
		}
		return getMe;
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
		List<Project> projects=dao.getAllProjectsByUser(dao.getUserByUsername(username));
		model.addAttribute("projects",projects);

		return "user/homeUser";
	}
	
	@RequestMapping("/user/createProject") 
	public String goCreateProject(Model model)
	{
		
		model.addAttribute("username",getUsername());
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
	    model.addAttribute("username",getUsername());
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
		model.addAttribute("username",getUsername());
	    model.addAttribute("user",p2);
		return "register";
	}
	@GetMapping("/admin/registrationRequests") 
	public String goRegistrationRequestPage(Model model)
	{
		model.addAttribute("username",getUsername());
		model.addAttribute("users",dao.getDisabledUsers());
		return "admin/registrationRequests";
	}
	@GetMapping("/admin/viewUsers") 
	public String goViewUsersPage(Model model)
	{
		model.addAttribute("username",getUsername());
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
		model.addAttribute("username",getUsername());
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
		model.addAttribute("username",getUsername());
		return "admin/registrationRequests";
	}
	
	@RequestMapping("/EditUser")
	public String editUser2(@ModelAttribute User user, Model model, @RequestParam int userid) {
		
		dao.editUser(userid, user);
		
		List<User> users = dao.getAllUsers();
		model.addAttribute("users",users);
		model.addAttribute("username",getUsername());
		return "admin/viewUsers";

	}
	@RequestMapping("/admin/editUser/{userid}") 
	public String goEditUser(Model model, @PathVariable int userid) { 

		User user = dao.getUserById(userid);
		model.addAttribute("username",getUsername());
		model.addAttribute("user",user);
		return "admin/EditMovie";
	}
	@RequestMapping("/admin/enableUser/{userid}") 
	public String goEnableUsers(Model model, @PathVariable int userid) { 

		User user = dao.getUserById(userid);
		dao.enableUser(userid, user);
		model.addAttribute("username",getUsername());
		model.addAttribute("users",dao.getDisabledUsers());
		return "admin/registrationRequests";
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
		model.addAttribute("username", username);
		model.addAttribute("error",warning);	
	    
		return "user/createProject";
	}
	
	@RequestMapping("/user/saveProject") 
	public String saveProject(Model model) { 
		model.addAttribute("username",getUsername());
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
		model.addAttribute("username",getUsername());
		
		List<Integer> ResultList = dao.getReportResults(dao.getUserByUsername(username));
		//List<ArrayList<Project>> ReportList = dao.getReportLists(dao.getUserByUsername(username));
		List<Project> overdue = new ArrayList<Project>();
		ArrayList<Project> projectListAR = new ArrayList<Project>();
		ArrayList<Project> projectListCCS = new ArrayList<Project>();
		ArrayList<Project> projectListSSAR = new ArrayList<Project>();
		ArrayList<Project> projectListSS = new ArrayList<Project>();
	
			Date date = new Date();
		   DateFormat df = new SimpleDateFormat("dd/MM/yy");
	       System.out.println(df.format(date));
	       
	       List<ArrayList<Project>> ReportList = dao.getReportLists(dao.getUserByUsername(username));
			
			ReportList.forEach((List<Project> temp) -> {
				temp.forEach((Project temp2) ->{
					Date test;
					try {
						test = new SimpleDateFormat("dd/MM/yyyy").parse(temp2.getEndDate());
					
					System.out.println("");
					System.out.println(temp2.getProjectName());
					//projectListAR.add(temp2);
					System.out.println("");
					
					if (temp2.getType().contentEquals("Afforrestation & Reforrestation")) {
						projectListAR.add(temp2);
					
					}else if (temp2.getType().contentEquals("Carbon Capture and Storage")) {
						projectListCCS.add(temp2);
					
					}else if (temp2.getType().contentEquals("Small Scale with AR & RF")) {
						projectListSSAR.add(temp2);
						
					}else if (temp2.getType().contentEquals("Small Scale")) {
						projectListSS.add(temp2);
				
					}else {
						System.out.println("Project Type Not Found");
					}
					
					
					if (test.before(date)) {
						System.out.println(date);
						System.out.println(temp2.getProjectName());
						overdue.add(temp2);
					}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			});
			
		ResultList.forEach((Integer temp) -> {
			System.out.print(temp);
		});
		
		model.addAttribute("ResultList", ResultList);
		model.addAttribute("projectListSS", projectListSS);
		model.addAttribute("projectListAR", projectListAR);
		model.addAttribute("projectListSSAR", projectListSSAR);
		model.addAttribute("projectListCCS", projectListCCS);
		model.addAttribute("overDue", overdue);
		
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
	
	//view a specific project
	@RequestMapping("/user/viewProject/{projectId}") 
	public String viewProject(Model model, @PathVariable int projectId) { 
		Project proj=dao.getProjectById(projectId);
		model.addAttribute("project",proj);
		model.addAttribute("projectID",projectId);
		model.addAttribute("projForms",dao.getAllFormsByProject(proj));
		model.addAttribute("username",getUsername());
		return "user/viewProject";
	}
	
	@RequestMapping("/user/doUpload/{projectId}/{formid}")
    public String handleFileUpload(Model model, @RequestParam MultipartFile[] fileUpload, @PathVariable int projectId, @PathVariable int formid) throws Exception {
          Project x=dao.getProjectById(projectId);
          List<Form> xForms=dao.getAllFormsByProject(x);
          		if (fileUpload != null && fileUpload.length > 0) {
            for (MultipartFile aFile : fileUpload){
            	
                Form newForm = new Form();
                newForm.setFormName(aFile.getOriginalFilename());
                newForm.setContent(aFile.getBytes());
                newForm.setUrlPath("Placeholder");
                
                dao.editUploadForm(formid,newForm);           
            }
            
            Project proj=dao.getProjectById(projectId);
    		model.addAttribute("project",proj);
    		model.addAttribute("projectID",projectId);
    		model.addAttribute("projForms",dao.getAllFormsByProject(proj));
    		model.addAttribute("username",getUsername());
        }
  
        return "/user/viewProject";
    }
	
	@RequestMapping("/admin/disableUser/{userid}") 
	public String goDisableUsers(Model model, @PathVariable int userid) { 

		User user = dao.getUserById(userid);
		dao.disableUser(userid, user);
		model.addAttribute("username",getUsername());
		model.addAttribute("users",dao.getDisabledUsers());
		return "admin/registrationRequests";
	}
	
	@RequestMapping("/user/editUser") 
	public String goEditMyProfile(Model model) { 
		String username;
		User x = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
			x=dao.getUserByUsername(username);
		} else {
			username = principal.toString();
		}
		User user = dao.getUserByUsername(username);
		model.addAttribute("username",getUsername());
		user.setPassword("New Password");
		model.addAttribute("user",user);
		return "user/EditProfile";
	}
	
	@RequestMapping("/EditUser/MyProfile")
	public String editMyProfile(@ModelAttribute User user, Model model) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		
		dao.editMyProfile(dao.getUserByUsername(username).getUserid(), user);
		
		model.addAttribute("username",getUsername());
		
		model.addAttribute("user",user);
		
		return "user/EditProfile";
	}
	
	//view a additional forms-Kyle
		/*@RequestMapping("/user/additionalForms/{projectId}") 
		public String additionalForms(Model model, @PathVariable int projectId) { 
			Project proj=dao.getProjectById(projectId);
			model.addAttribute("project",proj);
			model.addAttribute("projectID",projectId);
			model.addAttribute("projForms",dao.getAllFormsByProject(proj));
			model.addAttribute("username",getUsername());
			return "user/viewProject";
		}*/
	
//	
//	@PostMapping("/up")
//	public String saveFile(@RequestParam MultipartFile pic,@RequestParam String author) {
//	    System.out.println("osgn jsm ojmpdn");
//	    System.out.println(pic);
//	    System.out.println(author);
//	    return "ok";
//	}
//	
//	@RequestMapping("/user/editProject/{projectId}/{formName}") 
//	public String goEditProject(Model model, @PathVariable int projectId, @PathVariable String formName) { 
//
//		Project project = dao.getProjectById(projectId);
//		model.addAttribute("username",getUsername());
//		model.addAttribute("project",project);
//		model.addAttribute("projForms",dao.getAllFormsByProject(project));
//		return "user/Upload";
//	}
	
}