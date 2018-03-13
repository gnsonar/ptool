priceToolServices.factory("AppsService", function() {
	var appDetails = {};
	appDetails.offshoreReq = '';
	appDetails.levelOfService = '';

	appDetails.appsYearlyDataInfoDtos = [];
	

	/*Percentages*/
	appDetails.simplePerc = '';
	appDetails.mediumPerc = '';
	appDetails.complexPerc = '';
	appDetails.vcomplexPerc = '';

	/*Binded with controller on UI*/
	appDetails.apps = '';
	appDetails.nodeList = [];
	


	return {
		getAppDetails : function(){
			return appDetails;
		},

		initAppDetails : function(dealInfo){
			var children = [];
			
			var apps = createAppNode("Application details",null);
			apps.id = "1";
			apps.distributedVolume = null;
			appDetails.nodeList.push(apps);
			
			var total = createAppNode("Number of Applications",apps.id);
			total.id = "1.1";
			total.distributedVolume = createAppVolDistributionList(dealInfo);
			appDetails.nodeList.push(total);
			
			var simple = createAppNode("Simple Apps",total.id);
			simple.id = "1.1.1";
			simple.tooltip='Architecture is simple and traditional. Application Services has significant experience in this type of architecture. Limited and well established technology components. Integration is not an issue. Application Services is familiar with the technology';
			simple.distributedVolume = createAppVolDistributionList(dealInfo);
			appDetails.nodeList.push(simple);
			
			var medium = createAppNode("Medium Apps",total.id);
			medium.id = "1.1.2";
			medium.tooltip='Architecture is of simple or average complexity. Application Services has limited experience in this type of architecture. Multiple technology components. Application Services has proven expertise with this technology mix.';
			medium.distributedVolume = createAppVolDistributionList(dealInfo);
			appDetails.nodeList.push(medium);
			
			var complex = createAppNode("Complex Apps",total.id);
			complex.id = "1.1.3";
			complex.tooltip='Architecture is complex / distributed. Multiple technology components that must all be integrated. Some of these technologies (or their integration) are new and yet unproven by Application Services. Architecture is highly complex and distributed.';
			complex.distributedVolume = createAppVolDistributionList(dealInfo);
			appDetails.nodeList.push(complex);
			
			var vcomplex = createAppNode("Very Complex Apps",total.id);
			vcomplex.id = "1.1.4";
			vcomplex.tooltip='Multiple technology components that must all be integrated. Many of these technologies (or their integration) are new and yet unproven by Application Services, E.g. Complex engineering / scientific applications';
			vcomplex.distributedVolume = createAppVolDistributionList(dealInfo);
			appDetails.nodeList.push(vcomplex);
			
			children.push(simple);
			children.push(medium);
			children.push(complex);
			children.push(vcomplex);
			total.children = children;
			
			children = [];
			children.push(total);
			apps.children = children;
			
			appDetails.total = apps;

			return appDetails;
		}
	}
});

function createAppVolDistributionList(dealInfo)
{
	vdList=[];
	for (var i = 0; i < dealInfo/12; i++){
		var vd = {};
		vd.year = i+1;
		vd.volume = '';
		vd.revenue = '';
		vd.unit = '';
		vd.benchLowRevenue = '';
		vd.benchTargetRevenue = '';
		vd.benchLowUnit = '';
		vd.benchTargetUnit = '';
		vdList.push(vd);
	}
	return vdList;
}

function createAppNode(levelName,parentId) {
	var node = {};
	node.id = '';
	node.levelName = levelName;
	node.totalVolume = '';
	node.totalRevenue = '';
	node.percentage = '';
	node.parentId = parentId;
	node.children = [];
	node.distributedVolume = [];
	node.open = false;
	node.benchLow = '';
	node.benchTarget = '';
	node.pastAvg = '';
	node.compAvg = '';
	return node;
}