priceToolServices.factory("RetailService", function() {
	var retailDetails = {};
	retailDetails.offshoreReq = '';
	retailDetails.levelOfService = '';
	retailDetails.tooling = '';
	retailDetails.multiLingual = '';
	
	retailDetails.retailYearlyDataInfoDtos = [];

	/*Percentages*/
	retailDetails.shopsPerc = '';

	/*Binded with controller on UI*/
	retailDetails.retail = '';
	retailDetails.nodeList = [];
	


	return {
		getRetailDetails : function(){
			return retailDetails;
		},

		initRetailDetails : function(dealInfo){
			var children = [];
			
			var retail = createRetailNode("Retail Details",null);
			retail.id = "1";
			retail.distributedVolume = null;
			retailDetails.nodeList.push(retail);
			
			var shops = createRetailNode("Number of lanes",retail.id);
			shops.id = "1.1";
			shops.distributedVolume = createRetailVolDistributionList(dealInfo);
			retailDetails.nodeList.push(shops);
			
			children.push(shops);
			retail.children = children;
			
			retailDetails.shops = retail;

			return retailDetails;
		}
	}
});

function createRetailVolDistributionList(dealInfo)
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

function createRetailNode(levelName,parentId) {
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