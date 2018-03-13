priceToolServices.factory("EndUserService", function() {
	var endUserDetails = {};
	endUserDetails.offshoreAllowed = '';
	endUserDetails.includeHardware = '';
	endUserDetails.incluseBreakFix = '';
	endUserDetails.resolutionTime = '';
	endUserDetails.solutionName ='';
	endUserDetails.endUserYearlyDataInfoDtos = [];
	
	/*Percentages*/
	endUserDetails.laptop = '';
	endUserDetails.vdi = '';
	endUserDetails.desktop = '';
	endUserDetails.mobile = '';
	endUserDetails.lapHigh = '';
	endUserDetails.lapAvg = '';

	/*Binded with controller on UI*/
	endUserDetails.user = '';
	endUserDetails.userImac = '';
	endUserDetails.nodeList = [];

	return {
		getEndUserDetails : function(){
			return endUserDetails;
		},
		
		initEndUserDetails : function(dealInfo){
			var children = [];
			
			var user = createEndUserNode("End user",null);
			user.id = "1";
			user.distributedVolume = null;
			endUserDetails.nodeList.push(user);
			
			
			var userVol = createEndUserNode("End user Devices",user.id);
			userVol.id = "1.1";
			userVol.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(userVol);
			
			var laptop = createEndUserNode("Laptops",userVol.id);
			laptop.id = "1.1.1";
			laptop.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(laptop);

			var desktop = createEndUserNode("Desktops",userVol.id);
			desktop.id = "1.1.2";
			desktop.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(desktop);

			
			var vdi = createEndUserNode("Thin Clients(VDI)",userVol.id);
			vdi.id = "1.1.3";
			vdi.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(vdi);

			
			var mobile = createEndUserNode("Mobile devices",userVol.id);
			mobile.id = "1.1.4";
			mobile.tooltip='These are on top of the number of end-user devices. These devices are always without hardware pricing and are a combination of mobile (smart) phones and tablets';
			mobile.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(mobile);

			var lapHigh = createEndUserNode("High-end laptops",laptop.id);
			lapHigh.id = "1.1.1.1";
			lapHigh.tooltip='These laptops are for specific use, like CAD laptops or mobile workstations. This distinction primarily affects the hardware price of the device';
			lapHigh.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(lapHigh);

			var lapAvg = createEndUserNode("Standard laptops",laptop.id);
			lapAvg.id = "1.1.1.2";
			lapAvg.tooltip='These are the main-stream most common laptops. This distinction primarily affects the hardware price of the system.';
			lapAvg.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(lapAvg);

			children.push(lapHigh);
			children.push(lapAvg);
			laptop.children = children;
			
			children = [];
			children.push(laptop);
			children.push(desktop);
			children.push(vdi);
			children.push(mobile);
			userVol.children = children;
			
			children = [];
			children.push(userVol);
			user.children = children;
			
			endUserDetails.user = user;
			
			var userImac = createEndUserNode("IMAC",null);
			userImac.id = "2";
			userImac.distributedVolume = null;
			endUserDetails.nodeList.push(userImac);
			
			var imac = createEndUserNode("Number of IMACs",userImac.id);
			imac.id = "2.1";
			imac.distributedVolume = createEndUserVolDistributionList(dealInfo);
			endUserDetails.nodeList.push(imac);
			
			children = [];
			children.push(imac);
			userImac.children = children;
			
			endUserDetails.userImac = userImac;
			

			return endUserDetails;
		}
		

		}
});

function createEndUserVolDistributionList(dealInfo)
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

function createEndUserNode(levelName,parentId) {
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