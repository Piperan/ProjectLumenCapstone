<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
 <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <meta charset="ISO-8859-1">
  <title>Reports</title>

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
  <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load("current", {packages:["corechart"]});
      google.charts.setOnLoadCallback(drawChart);
      function drawChart() {
    	
    	var type = [];
    	for (var i = 0; i < ${ResultList}.length; i++){ 
    		type[i] = ${ResultList}[i];
    	}
    	
        var data = google.visualization.arrayToDataTable([
          ['ProjectType', 'NumberOfProjects'],
          ['Afforrestation & Reforrestation', type[0] ],
          ['Carbon Capture and Storage', type[1] ],
          ['Small Scale with AR & RF', type[2] ],
          ['Small Scale', type[3] ]
        ]);

        var options = {
          title: 'Project Types',
          pieHole: 0.4,
        };

        var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
        chart.draw(data, options);
      }
    </script>
<style type="text/css">
footer {
  bottom: 0;
  width: 100%;
  background-color: #333;
  color:#fff;
}
</style>
</head>
<body>

 <!-- Navigation -->
  <nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
  	 <a class="navbar-brand" href="/"><img src="../images/pl_logo_j.png" width="50" height="50"/></a> 
      <a class="navbar-brand" href="/">PROJECT LUMEN</a>
      <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item">
           <a class="nav-link" href="<c:url value="/user"/>">Home</a>
          </li>
          <li class="nav-item">
          <a class="nav-link" href="<c:url value="user/myReports"/>">Reports</a>
          </li>
          <c:set var = "result" scope = "session" value = "${username}"/>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownPortfolio" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              ${username}
            </a>
            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownPortfolio">
              <a class="dropdown-item" href="portfolio-1-col.html">My Profile</a>
              <a class="dropdown-item" href="mailto:bayanie@sheridancollege.ca">Issues</a>
              <a class="dropdown-item" href="<c:url value="/logout"/>">Logout</a>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <!-- end of navigation -->
  <br><br><br>
<div id="donutchart" style="width: 900px; height: 500px;"></div>
 <div class="container">

 <ul class="nav nav-tabs">
  <li class="nav-item">
    <a class="nav-link active" href="#myReportPage">Overdue Project</a>
  </li>
  <li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Project Type</a>
    <div class="dropdown-menu">
      <a class="dropdown-item" href="#myReportPage">Afforrestation & Reforrestation</a>
      <a class="dropdown-item" href="#myReportPage">Carbon Capture and Storage</a>
      <a class="dropdown-item" href="#myReportPage">Small Scale</a>
      <a class="dropdown-item" href="#myReportPage">Small Scale with AR & RF</a>
      <div class="dropdown-divider"></div>
    </div>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="#">Link</a>
  </li>
  
</ul>
</div>
<div id="myReportPage">
     <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Username</th>
					<th>Address</th>
					<th>Edit</th>
					<!-- <th>Delete</th>  -->
				</tr>
			</thead>
			  <tfoot>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Username</th>
					<th>Address</th>
					<th>Edit</th>
				</tr>
			</tfoot>
			<tbody>
<tr>
	
</tr>
</tbody>
</table>
</div>
  <!-- Footer -->
  <footer class="py-5 bg-dark">
    <div class="container">
      <p class="m-0 text-center text-white">Copyright &copy; Project Lumen</p>
    </div>
  </footer>
</body>
</html>