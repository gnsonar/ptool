priceToolcontrollers.controller('TotalsCtrl', function($scope,$rootScope,DealService,customInterceptor,endpointsurls,$stateParams,Excel,$timeout,$dialog,GraphService,$alert) {
	var scope;
	$scope.tabs=[{id:"tab1", class:'active', ScenerioID: 0, tabname:"Active Scenerio"}];
	$scope.exportToExcel=function(tableId){
	// ex: '#my-table'
        var exportHref=Excel.tableToExcel(tableId,'TOTAL');
       $timeout(function(){location.href=exportHref;},100); // trigger download
    }
	$scope.graphView = false;
	$scope.numberArr = [];
	$scope.subTotals = [];
	$scope.addSubTotals = [];
	$scope.serviceGovList = [];
	$scope.transitionFeeList = [];
	$scope.totalRevenueList = [];
	$scope.migrationCost=[];
	$scope.appYearlyRevenue=[];
	$scope.serviceDeskYearlyRevenue=[];
	$scope.reatailInfo=[];
	$scope.endUserYearlyRevenue=[];
	$scope.networkYearlyRevenue=[];
	$scope.hostingYearlyRevenue=[];
	$scope.storageInfo = [];
	$scope.retailInfo = [];
	$scope.ShowCopy = true;
	$scope.disableCopy = false;
	$scope.graphinfo= {storage:0,apps:0,service:0,total:0,currency:0,serviceWindowSla:0,includeHardware:0,offshoreAllowed:0,retail:0,netWork:0,endUser:0,hosting:0};


	$scope.exportToExcel=function(tableId){
	// ex: '#my-table'
		var exportHref=Excel.tableToExcel(tableId,'TOTAL');
       $timeout(function(){location.href=exportHref;},100); // trigger download
    }

	// function for reseting tab and select the index which is selected by user.
	$scope.tabReset=function(index)
	{
		for(var i=0;i< $scope.tabs.length;i++){
			$scope.tabs[i].class="Inactive";
			if(i==index){
    			$scope.tabs[i].class="active";
    		}
		}
	}

	//function for init
	$scope.init = function(){
		$scope.disableCopy = false;
		mapActiveScenerio();
	}

	function mapActiveScenerio() {
		var url =  endpointsurls.TOTAL_REVENUE+$stateParams.dealId;
        customInterceptor.getrequest(url).then(function successCallback(response) {
        	$scope.yearlyRevenue=response.data;
        	$scope.dealInfoDto=response.data.dealInfoDto;
        	$scope.submissionIndicator=response.data.dealInfoDto.submissionIndicator;
        	$scope.assessmentIndicator=response.data.dealInfoDto.assessmentIndicator.split(',');
        	var isSave={'genericA':($scope.assessmentIndicator[0]==1)?true:false,
        			'hostingA':($scope.assessmentIndicator[1]==1)?true:false,
    		     	'storageA':($scope.assessmentIndicator[2]==1)?true:false,
    		     	'endUserA':($scope.assessmentIndicator[3]==1)?true:false,
    		     	'networkA':($scope.assessmentIndicator[4]==1)?true:false,
    		     	'serviceDeskA':($scope.assessmentIndicator[5]==1)?true:false,
    		     	'appsA':($scope.assessmentIndicator[6]==1)?true:false,
    		     	'retailA':($scope.assessmentIndicator[7]==1)?true:false
    		};
        	$scope.isSaveStorage= DealService.getter() || isSave;
            DealService.setter($scope.isSaveStorage);
        	//$scope.yearlyRevenue['scenarioDesc']="Active";
        	$scope.migrationCostApplicable = $scope.dealInfoDto.migrationCost;
        	$scope.dealCurrency=$scope.dealInfoDto.currency;
        	$scope.serviceGovernance=$scope.dealInfoDto.serviceGovernance;
        	$scope.transitionFees=$scope.dealInfoDto.transitionFees;
        	$scope.dealTermInYears = $scope.dealInfoDto.dealTerm;
        	$scope.scenarioList = response.data.scenarioList;
        	scope=$scope.scenarioList;
        	if($scope.scenarioList.length > 0 && $scope.scenarioList != null) {
        		for(var i=0;i < $scope.scenarioList.length; i++){
        			$scope.scenarioId = $scope.scenarioList[i].scenarioId;
        			$scope.AddScenerio($scope.scenarioList[i].scenarioName);
        		}
        	}

        	for(var i=0; i< $scope.dealTermInYears/12; i++){
        		$scope.numberArr.push(i);

        		// Hosting Yearly Revenue
        		if($scope.yearlyRevenue.hostingYearlyRevenue[i] != null){
        			$scope.hostingYearlyRevenue.push($scope.yearlyRevenue.hostingYearlyRevenue[i]);
        		}
        		else{
        			$scope.hostingYearlyRevenue.push({totalRevenue: 0});
        		}

        		// Storage Yearly Revenue
        		if($scope.yearlyRevenue.storageYearlyRevenue[i] != null){
        			$scope.storageInfo.push($scope.yearlyRevenue.storageYearlyRevenue[i]);
        		}
        		else{
        			$scope.storageInfo.push({totalRevenue: 0});
        		}

        		// End User Yearly Revenue
        		if($scope.yearlyRevenue.endUserYearlyRevenue[i] != null){
        			$scope.endUserYearlyRevenue.push($scope.yearlyRevenue.endUserYearlyRevenue[i]);
        		}
        		else{
        			$scope.endUserYearlyRevenue.push({totalRevenue: 0});
        		}

        		// Network Yearly Revenue
        		if($scope.yearlyRevenue.networkYearlyRevenue[i] != null){
        			$scope.networkYearlyRevenue.push($scope.yearlyRevenue.networkYearlyRevenue[i]);
        		}
        		else{
        			$scope.networkYearlyRevenue.push({totalRevenue: 0});
        		}

        		// Service Desk Yearly Revenue
        		if($scope.yearlyRevenue.serviceDeskYearlyRevenue[i] != null){
        			$scope.serviceDeskYearlyRevenue.push($scope.yearlyRevenue.serviceDeskYearlyRevenue[i]);
        		}
        		else{
        			$scope.serviceDeskYearlyRevenue.push({totalContactsRevenue: 0});
        		}

        		// Application Yearly Revenue
        		if($scope.yearlyRevenue.appYearlyRevenue[i] != null){
        			$scope.appYearlyRevenue.push($scope.yearlyRevenue.appYearlyRevenue[i]);
        		}
        		else{
        			$scope.appYearlyRevenue.push({totalAppsRevenue: 0});
        		}

        		// Retail Yearly Revenue
        		if($scope.yearlyRevenue.retailYearlyRevenue[i] != null){
        			$scope.retailInfo.push($scope.yearlyRevenue.retailYearlyRevenue[i]);
        		}
        		else{
        			$scope.retailInfo.push({noOfShops: 0});
        		}

        		// Retail Yearly Revenue
        		if($scope.yearlyRevenue.totalMigrationCost[i] != null){
        			$scope.migrationCost.push($scope.yearlyRevenue.totalMigrationCost[i]);
        		}
        		else{
        			$scope.migrationCost.push({cost: 0.00});
        		}
        	}

        	$scope.setRevenue();
        	})
	}

	// set revenue year wise
    $scope.setRevenue=function()
    {
    	$scope.graphTotalCost = 0;
    	$scope.subTotals = [];
    	$scope.transitionFeeList = [];
    	$scope.serviceGovList = [];
    	$scope.addSubTotals = [];
    	$scope.totalRevenueList = [];
		for(var i=0;i<$scope.dealTermInYears/12;i++)
	    {
			$scope.subTotals.push(parseFloat($scope.storageInfo[i].totalRevenue)+
			parseFloat($scope.appYearlyRevenue[i].totalAppsRevenue+
		    parseFloat($scope.retailInfo[i].noOfShops)+
		    parseFloat($scope.networkYearlyRevenue[i].totalRevenue)+
		    parseFloat($scope.serviceDeskYearlyRevenue[i].totalContactsRevenue)+
		    parseFloat($scope.hostingYearlyRevenue[i].totalRevenue)+
			parseFloat($scope.endUserYearlyRevenue[i].totalRevenue)));

			//graphs
			$scope.graphinfo.storage=$scope.graphinfo.storage+$scope.storageInfo[i].totalRevenue;
			$scope.graphinfo.apps=$scope.graphinfo.apps+$scope.appYearlyRevenue[i].totalAppsRevenue;
			$scope.graphinfo.service=$scope.graphinfo.service+$scope.serviceDeskYearlyRevenue[i].totalContactsRevenue;
			$scope.graphinfo.total=$scope.graphinfo.total+$scope.subTotals[i];
			$scope.graphinfo.offshoreAllowed=$scope.dealInfoDto.offshoreAllowed;
			$scope.graphinfo.includeHardware=$scope.dealInfoDto.includeHardware;
			$scope.graphinfo.serviceWindowSla=$scope.dealInfoDto.serviceWindowSla;
			$scope.graphinfo.retail=$scope.graphinfo.retail+$scope.retailInfo[i].noOfShops;
			$scope.graphinfo.netWork=$scope.graphinfo.netWork+$scope.networkYearlyRevenue[i].totalRevenue;
			$scope.graphinfo.currency=$scope.dealInfoDto.currency;
			$scope.graphinfo.endUser=$scope.graphinfo.endUser+$scope.endUserYearlyRevenue[i].totalRevenue;
			$scope.graphinfo.hosting=$scope.graphinfo.hosting+$scope.hostingYearlyRevenue[i].totalRevenue;
			GraphService.setter($scope.graphinfo);

			//serviceSub
			var serviceSub=($scope.subTotals[i]*$scope.serviceGovernance)/100;
			$scope.serviceGovList.push(serviceSub.toFixed(2));
			if(i==0){
				var transitionFee=($scope.subTotals[i]*$scope.transitionFees)/100;
				$scope.transitionFeeList.push(transitionFee.toFixed(2));
			}else{
				$scope.transitionFeeList.push(0);
			}

			$scope.addSubTotals.push(parseFloat($scope.transitionFeeList[i])+parseFloat($scope.serviceGovList[i])+parseFloat($scope.migrationCost[i].cost));
			//$scope.migrationCost.push(0);
			$scope.totalRevenueList.push($scope.addSubTotals[i]+$scope.subTotals[i]);
			}
			for(var i=0; i<$scope.totalRevenueList.length;i++)
				{
				$scope.graphTotalCost = $scope.graphTotalCost+$scope.totalRevenueList[i];
				}
    	}

    	$scope.GetScenerio = function(id,index){
        	if(id == 0){
        		$scope.disableCopy = false;
        		$scope.hostingYearlyRevenue = [];
    			$scope.storageInfo = [];
    			$scope.endUserYearlyRevenue = [];
    			$scope.networkYearlyRevenue = [];
    			$scope.serviceDeskYearlyRevenue = [];
    			$scope.appYearlyRevenue = [];
    			$scope.retailInfo = [];
    			$scope.subTotals = [];
    			$scope.serviceGovList = [];
    			$scope.transitionFeeList = [];
    			$scope.migrationCost = [];
    			$scope.addSubTotals = [];
    			$scope.totalRevenueList = [];
    			$scope.numberArr = [];
    			$scope.tabReset(index);
        		mapActiveScenerio();
        	}
        	else{
        		$scope.disableCopy = true;
        		$scope.scenarioId = id;
        		$scope.tabReset(index);
        		mapCopiedScenerio();

        	}
        }

        function mapCopiedScenerio() {
        	var url =  endpointsurls.SAVE_ACTIVE_SCENERIO+$stateParams.dealId+'/'+$scope.scenarioId;
            customInterceptor.getrequest(url).then(function successCallback(response) {
            	$scope.scenarioId = response.data.scenarioId;
            	$scope.copiedDataList = response.data;
            	$scope.yearlyRevenue = response.data;
            	if($scope.copiedDataList != null && $scope.scenarioId != null){
            	mapCopiedScenerioData();
            	}
            });
        }

        function mapCopiedScenerioData() {
        	$scope.graphTotalCost=0;
    		if($scope.copiedDataList.scenarioYearlyInfoDtoList.length > 0){
    			$scope.hostingYearlyRevenue = [];
    			$scope.storageInfo = [];
    			$scope.endUserYearlyRevenue = [];
    			$scope.networkYearlyRevenue = [];
    			$scope.serviceDeskYearlyRevenue = [];
    			$scope.appYearlyRevenue = [];
    			$scope.retailInfo = [];
    			$scope.subTotals = [];
    			$scope.serviceGovList = [];
    			$scope.transitionFeeList = [];
    			$scope.migrationCost = [];
    			$scope.addSubTotals = [];
    			$scope.totalRevenueList = [];
    			for(var i=0;i<$scope.copiedDataList.scenarioYearlyInfoDtoList.length;i++){
    				$scope.hostingYearlyRevenue.push({totalRevenue: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].hosting});
    				$scope.storageInfo.push({totalRevenue: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].storage});
    				$scope.endUserYearlyRevenue.push({totalRevenue: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].endUser});
    				$scope.networkYearlyRevenue.push({totalRevenue: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].network});
    				$scope.serviceDeskYearlyRevenue.push({totalContactsRevenue: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].serviceDesk});
    				$scope.appYearlyRevenue.push({totalAppsRevenue: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].application});
    				$scope.retailInfo.push({noOfShops: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].retail});
    				$scope.subTotals.push($scope.copiedDataList.scenarioYearlyInfoDtoList[i].towerSubtotal);
    				$scope.serviceGovList.push($scope.copiedDataList.scenarioYearlyInfoDtoList[i].serviceGov);
    				$scope.transitionFeeList.push($scope.copiedDataList.scenarioYearlyInfoDtoList[i].transitionFees);
    				$scope.migrationCost.push({cost: $scope.copiedDataList.scenarioYearlyInfoDtoList[i].migrationCost});
    				$scope.addSubTotals.push($scope.copiedDataList.scenarioYearlyInfoDtoList[i].additionalSubtotal);
    				$scope.totalRevenueList.push($scope.copiedDataList.scenarioYearlyInfoDtoList[i].totalPrice);

    				//graph
    				$scope.graphinfo.storage=$scope.graphinfo.storage+$scope.storageInfo[i].totalRevenue;
    				$scope.graphinfo.apps=$scope.graphinfo.apps+$scope.appYearlyRevenue[i].totalAppsRevenue;
    				$scope.graphinfo.service=$scope.graphinfo.service+$scope.serviceDeskYearlyRevenue[i].totalContactsRevenue;
    				$scope.graphinfo.total=$scope.graphinfo.total+$scope.subTotals[i];
    				$scope.graphinfo.offshoreAllowed=$scope.dealInfoDto.offshoreAllowed;
    				$scope.graphinfo.includeHardware=$scope.dealInfoDto.includeHardware;
    				$scope.graphinfo.serviceWindowSla=$scope.dealInfoDto.serviceWindowSla;
    				$scope.graphinfo.retail=$scope.graphinfo.retail+$scope.retailInfo[i].noOfShops;
    				$scope.graphinfo.netWork=$scope.graphinfo.netWork+$scope.networkYearlyRevenue[i].totalRevenue;
    				$scope.graphinfo.currency=$scope.dealInfoDto.currency;
    				$scope.graphinfo.endUser=$scope.graphinfo.endUser+$scope.endUserYearlyRevenue[i].totalRevenue;
    				$scope.graphinfo.hosting=$scope.graphinfo.hosting+$scope.hostingYearlyRevenue[i].totalRevenue;
    				GraphService.setter($scope.graphinfo);
    			}
    			for(var i=0; i<$scope.totalRevenueList.length;i++)
				{
				$scope.graphTotalCost = $scope.graphTotalCost+$scope.totalRevenueList[i];
				}
    		}
        }

        $scope.AddScenerio=function(name){
        	$scope.tabname = name;
        	var flag = '';
        	var len = $scope.tabs.length;
        	if(len == 1){
        		$scope.tabs.push({id:"tab2", class:'Inactive', ScenerioID: $scope.scenarioId, tabname: $scope.tabname});
        		$scope.ShowCopy = true;
        	}
        	if(len == 2){
        		for(var i=0;i < len;i++){
        			if($scope.scenarioId == $scope.tabs[i].ScenerioID){
        				flag = 1;
        			}
        			else{
        				flag = 0;
        			}
        		}
        		if(flag == 0){
        			$scope.tabs.push({id:"tab3", class:'Inactive', ScenerioID: $scope.scenarioId, tabname: $scope.tabname});
        			$scope.ShowCopy = false;
        		}
        	}
        }

        //show message for remove scenario
        $scope.deleteMsg = false;
        $scope.tabScenarioName = '';



        //remove scenario function
        $scope.removeTab = function(index){
        	 $rootScope.spinner=true;
        	var scenerioid = $scope.tabs[index].ScenerioID;
        	$scope.tabScenarioName = $scope.tabs[index].tabname;
        	var url =  endpointsurls.DELETE_SCENERIO+scenerioid;

        	customInterceptor.deleterequest(url,scenerioid).then(function successCallback(response) {
        		$rootScope.spinner=false;
        		$scope.tabs.splice(index, 1);
        		$scope.GetScenerio(0,0);
        		$scope.deleteMsg = true;

        		window.setTimeout(function() {
                    $(".alert").fadeTo(500, 0).slideUp(500, function(){
                        $(this).remove();
                    });
                }, 1500);

            	console.log(response.data);
            });
        	$scope.deleteMsg = false;
        	$scope.ShowCopy = true;
        }

        function saveActiveScenerio(name,description) {
        	saveInfo();
        	$scope.totalInfoDto = {
        			dealId : $stateParams.dealId,
        			scenarioName : name,
        			scenarioDesc :  description,
        			transitionFees : $scope.transitionFees,
        			serviceGovernance : $scope.serviceGovernance,
        			migrationCostApplicable : $scope.migrationCostApplicable == "Yes" ? true : false,
        			scenarioYearlyInfoDtoList : $scope.totalYearlyInfoList
	    	}

        	var url = endpointsurls.SAVE_ACTIVE_SCENERIO+$stateParams.dealId;
        	customInterceptor.postrequest(url,$scope.totalInfoDto).then(function successCallback(response) {
        		$scope.scenarioId = response.data.scenarioId;
        		$scope.AddScenerio(name);
        		$scope.GetScenerio(0,0);
        	});
        }

        function saveInfo(){
        	var yearlyInfoList = [];
        	for (var y = 0; y < $scope.dealTermInYears/12; y++) {
        		var yearlyData = {};
        		yearlyData.year = y+1;
        		yearlyData.hosting = $scope.hostingYearlyRevenue[y].totalRevenue;
    			yearlyData.storage = $scope.storageInfo[y].totalRevenue;
    			yearlyData.endUser = $scope.endUserYearlyRevenue[y].totalRevenue;
    			yearlyData.network = $scope.networkYearlyRevenue[y].totalRevenue;
    			yearlyData.serviceDesk = $scope.serviceDeskYearlyRevenue[y].totalContactsRevenue;
    			yearlyData.application = $scope.appYearlyRevenue[y].totalAppsRevenue;
    			yearlyData.retail = $scope.retailInfo[y].noOfShops;
    			yearlyData.towerSubtotal = $scope.subTotals[y];
    			yearlyData.additionalSubtotal = $scope.addSubTotals[y];
    			yearlyData.totalPrice = $scope.totalRevenueList[y];
    			yearlyData.serviceGov = $scope.serviceGovList[y];
    			yearlyData.transitionFees = $scope.transitionFeeList[y];
    			yearlyData.migrationCost = $scope.migrationCost[y].cost;
    			yearlyInfoList.push(yearlyData);
        	}
        	$scope.totalYearlyInfoList = yearlyInfoList;
        }



	        //modal for change setting section
			var dialogOptionsforCopy = {
		    controller: 'CopyCtrl',
		    templateUrl: '/modals/copy/copy.html'
		  };



			$scope.copy = function(searchModel,modelName){
		    $dialog.dialog(angular.extend(dialogOptionsforCopy, {resolve: {searchModel: searchModel,scope:scope,modelName:modelName}}))
		    .open()
		    .then(function(result) {
		    	if(result) {
		    		//angular.copy(result, searchModel);
	        	saveActiveScenerio(result.nickname,result.description);
	        }
	        searchModel = undefined;
	    });
	  };

	  //graph view
	  $scope.viewGraphs = function(){
		  $scope.graphView = true;
		  $scope.show();
		  $scope.showBarChart();
	  };

	  $scope.init();

	  $scope.show = function() {
      	var graphOnfo=GraphService.getter();
      	$scope.graphinfo=GraphService.getter();
      	var storage=graphOnfo.storage;
      	var service=graphOnfo.service;
      	var retail=graphOnfo.retail;
      	var apps=graphOnfo.apps;
      	var network=graphOnfo.netWork;
      	var endUser=graphOnfo.endUser;
      	var hosting=graphOnfo.hosting;
      	   google.charts.load('current', {'packages':['corechart']});
      	   google.charts.setOnLoadCallback(drawChart);
      	   function drawChart() {
      	      var data = google.visualization.arrayToDataTable([
      	        ['Task', ''],
      	        ['Storage('+storage.toFixed(2)+')',     storage],
      	        ['Service('+service.toFixed(2)+')',    service],
      	        ['Apps('+apps.toFixed(2)+')',    apps],
      	        ['Retail('+retail.toFixed(2)+')',    retail],
      	        ['Network('+network.toFixed(2)+')',    network],
      	        ['EndUser('+endUser.toFixed(2)+')',    endUser],
      	        ['Hosting('+hosting.toFixed(2)+')',    hosting],
      	      ]);

      	      var options = {
      	    	title: 'Overall Deal Price Breakdown',
      	    	chartArea:{left:10},
      	    	sliceVisibilityThreshold: .0001
      	      };

      	      var chart = new google.visualization.PieChart(document.getElementById('piechart'));
      	      chart.draw(data, options);
      	    }
  	  };

  	  	$scope.showBarChart = function() {
  	  		$scope.labels=[];
  	  			for (var y = 1; y <= 10; y++) {
  	  				if(y<=$scope.dealTermInYears/12){
  	  					$scope.labels.push('Year '+(y));
  	  				}else{
  	  					$scope.labels.push('');
  	  				}
 		 }


  	  		if($scope.subTotals.length==1)
  	  			{
  	  				$scope.subTotals.push(0);
  	  				$scope.serviceGovList.push(0);
  	  				$scope.transitionFeeList.push(0);
  	  			}

  	  	 for (var i = 0; i <$scope.dealTermInYears/12; i++) {
        	 $scope.subTotals[i]=parseFloat($scope.subTotals[i]).toFixed(2);
        	 $scope.serviceGovList[i]=parseFloat($scope.serviceGovList[i]).toFixed(2);
        	 $scope.transitionFeeList[i]=parseFloat($scope.transitionFeeList[i]).toFixed(2);
  			 }
  	  			$scope.series = ['Tower Pricing', 'Service','Transition'];
  	  			$scope.colors = ['#ff0000', '#797676', '#c1c1c1'];
  	  			$scope.data = [
  		                  $scope.subTotals,
  		                  $scope.serviceGovList,
  		                  $scope.transitionFeeList
  		                  ];
          }
	});

priceToolcontrollers.factory('Excel',function($window){
    var uri='data:application/vnd.ms-excel;base64,',
        template='<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
        base64=function(s){return $window.btoa(unescape(encodeURIComponent(s)));},
        format=function(s,c){return s.replace(/{(\w+)}/g,function(m,p){return c[p];})};
    return {
        tableToExcel:function(tableId,worksheetName){
            var table=$(tableId),
                ctx={worksheet:worksheetName,table:table.html()},
                href=uri+base64(format(template,ctx));
            return href;
        }
    };
})
