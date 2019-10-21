<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style>
#logo{
    width:2.5%;
    height: 2.5%;
    float: left;
   
}
header{
    background-color: lightgreen;
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
 }
 #name{
    position: relative;
    font-family:  Impact;
    font-size: 1.5em;
    margin-top: 1.2%;

}
#myMain{
    clear:both;
    margin: auto;
    text-align: center;
    margin-top: 8%;
    background-color: rgba(255,255,255, 0.7);
}

footer{
   position: fixed;
   left: 0;
   bottom: 0;
   width: 100%;
   height: 4%;
   background-color: lightgreen;
   text-align: right;
}

.foot{
font-size: 1.5em;
font-family: 'Times New Roman', Times, serif;
margin-right: 2%;
}
table, td, th {  
    border: 1px solid #ddd;
    text-align: left;
  }
  
  table {
    border-collapse: collapse;
    width: 100%;
  }
  
  th, td {
    padding: 15px;
  }
  .myTables{
      margin: 2%;
      padding:2%;
  }
  button{
      font-family: 'Courier New', Courier, monospace;
      font-size: 1.5em;
      width: 35%;
      height: 30%;
  }
  
</style>
<meta charset="ISO-8859-1">
<title>Home</title>
</head>
<body>
    
    <header>
        <img id="logo" src="../images/lightbulb.png" alt="log">
        <p id="name"> Project Lumen</p>
        </header>
   <div id="myMain">
     <h1> Carbon Credit Management System Reports</h1> <br>
    <button><a class="link" href="<c:url value="/user"/>">Home</a></button>
    <button><a class="link" href="<c:url value="/user/reports"/>">Back to Reports</a></button>
   
    
</div>
<div class="myTables">
<h2>My Projects</h2>
<c:set var = "result" scope = "session" value = "${username}"/>
<h3> Report for: ${result}</h3>
<table>
    <tr>
    <th>Project Id:</th>
    <th>Project Name:</th>
    <th>Project End Date:</th>
    <th>Host Country:</th>
    <th>Project Description:</th>
    <th>Project Scale:</th>
    </tr>
    <tr>
       <c:forEach var="project" items="${projects}">
	<tr>
		<td>${project.projectId}</td> 
		<td>${project.projectName}</td>
		<td>${project.endDate}</td>
		<td>${project.hostCountry}</td>
		<td>${project.projectDesc}</td>
		<td>${project.projectScale}</td>
		
		
	</tr>
	</c:forEach>
</table>
</div>

<footer>
        <a class="foot" href="">Email </a>
        <a class="foot" href="">Feedback </a>
        <a class="foot" href="">Contact us!</a>
</footer>


</body>
</html>