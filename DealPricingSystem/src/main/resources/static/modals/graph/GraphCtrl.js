//Controller for modal(dialog box) service call
priceToolcontrollers.controller('GraphCtrl', function($scope,dialog,customInterceptor,$window,endpointsurls,$stateParams,GraphService) {

	
	 $scope.show1=function()
	 { var pptx=new PptxGenJS(); pptx.addSlidesForTable('tabAutoPaging'); pptx.save(); }
	

        $scope.show = function() {
        	$scope.labels = ['YEAR1', 'YEAR2','YEAR3', 'YEAR4', 'YEAR5', 'YEAR6', 'YEAR7'];
        	  $scope.series = ['Series A', 'Series B','Series C'];
        	  $scope.colors = ['#8B0000', '#0000FF', '#006400'];

        	  $scope.data = [
        	    [65, 59, 80, 81, 56, 55, 40],
        	    [8, 5, 6, 6,9, 19, 12],
        	    [20, 0, 0, 0, 0, 0, 0]
        	  ];
            $scope.yFunction = function(){
                return function(d){
                    return d.y;
                };
            };
        	var graphOnfo=GraphService.getter();
        	$scope.graphinfo=GraphService.getter();
        	var storage=graphOnfo.storage;
        	var service=graphOnfo.service;
        	var retail=graphOnfo.retail;
        	var apps=graphOnfo.apps;
        	var network=graphOnfo.netWork;
        	var endUser=graphOnfo.endUser;
        	var hosting=graphOnfo.hosting;
        	$scope.exampleData = [
	                            	{ key: "One", y:1 },
	                                { key: "Two", y: 2 },
	                                { key: "Three", y: 9 },
	                                { key: "Four", y: 7 },
	                                { key: "Five", y: 1 },
	                                { key: "Six", y: 13 },
	                                { key: "Seven", y: 1 }
	                          ];
        	
        	   google.charts.load('current', {'packages':['corechart']});
        	    
        	    google.charts.setOnLoadCallback(drawChart);

        	    function drawChart() {

        	      var data = google.visualization.arrayToDataTable([
        	        ['Task', ''],
        	        ['Storage('+storage.toFixed(2)+')',     storage],
        	        ['Service('+service+')',    service],
        	        ['Apps('+apps.toFixed(2)+')',    apps],
        	        ['Retail('+retail.toFixed(2)+')',    retail],
        	        ['Network('+network.toFixed(2)+')',    network],
        	        ['EndUser('+endUser.toFixed(2)+')',    endUser],
        	        ['Hosting('+hosting.toFixed(2)+')',    hosting],
        	       
        	      ]);

        	      var options = {
        	        title: 'Total:'+graphOnfo.total.toFixed(2) +' in k '+$scope.graphinfo.currency
        	      };
        	      var options1 = {
              	        title: 'Total:'+graphOnfo.total.toFixed(2) + ' in k '+ $scope.graphinfo.currency
              	      };
        	      var options2 = {
              	        title: 'Total:'+graphOnfo.total.toFixed(2)+' in k '+$scope.graphinfo.currency
              	      };

        	      var chart = new google.visualization.PieChart(document.getElementById('piechart'));
        	    // var chart1 = new google.visualization.PieChart(document.getElementById('piechart1'));
        	     // var chart2 = new google.visualization.PieChart(document.getElementById('piechart2'));

        	      chart.draw(data, options);
        	     // chart1.draw(data, options1);
        	     // chart2.draw(data, options2);
        	    }
    	  };
    		 $scope.show();
    	  $scope.close = function(){
    	    dialog.close(undefined);
    	  };
});