priceToolServices.factory("HostingService", function() {
	var hostingDetails = {};
	hostingDetails.offshoreReq = '';
	hostingDetails.includeHardware = '';
	hostingDetails.toolingMonitoring = '';
	hostingDetails.coLocation = '';
	hostingDetails.serviceOrch = '';
	hostingDetails.totalServers = '';
	hostingDetails.averageServerVolume = '';
	hostingDetails.serviceWindowSla = '';
	hostingDetails.dealType = '';
	hostingDetails.hostingYearlyDataInfoDtos = [];
	
	/*Percentages*/
	hostingDetails.physicalServerPerc = '';

	hostingDetails.physicalWinLinuxPerc = '';
	hostingDetails.physicalWinLinuxSmallPerc = '';
	hostingDetails.physicalWinLinuxMediumPerc = '';
	hostingDetails.physicalWinLinuxLargePerc = '';

	hostingDetails.physicalUnixPerc = '';
	hostingDetails.physicalUnixSmallPerc = '';
	hostingDetails.physicalUnixMediumPerc = '';
	hostingDetails.physicalUnixLargePerc = '';

	hostingDetails.virtualServerPerc = '';

	hostingDetails.virtualPublicPerc = '';

	hostingDetails.virtualPublicWinLinuxPerc = '';
	hostingDetails.virtualPublicWinLinuxSmallPerc = '';
	hostingDetails.virtualPublicWinLinuxMediumPerc = '';
	hostingDetails.virtualPublicWinLinuxLargePerc = '';

	hostingDetails.virtualPublicUnixPerc = '';
	hostingDetails.virtualPublicUnixSmallPerc = '';
	hostingDetails.virtualPublicUnixMediumPerc = '';
	hostingDetails.virtualPublicUnixLargePerc = '';

	hostingDetails.virtualPrivatePerc = '';

	hostingDetails.virtualPrivateWinLinuxPerc = '';
	hostingDetails.virtualPrivateWinLinuxSmallPerc = '';
	hostingDetails.virtualPrivateWinLinuxMediumPerc = '';
	hostingDetails.virtualPrivateWinLinuxLargePerc = '';

	hostingDetails.virtualPrivateUnixPerc = '';
	hostingDetails.virtualPrivateUnixSmallPerc = '';
	hostingDetails.virtualPrivateUnixMediumPerc = '';
	hostingDetails.virtualPrivateUnixLargePerc = '';

	/*Binded with controller on UI*/
	hostingDetails.serverHosting = {};
	hostingDetails.platformHosting = [];
	hostingDetails.nodeList = [];
	

	return {
		getHostingDetails : function(){
			return hostingDetails;
		},
		
		initHostingDetails : function(dealInfo){
			var children = [];
			
			var serverHosting = createNode("Server Hosting",null);
			serverHosting.id = "1";
			serverHosting.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(serverHosting);
			
			var serverVol = createNode("Server Volume",serverHosting.id);
			serverVol.id = "1.1";
			serverVol.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(serverVol);
			
			children.push(serverVol);
			serverHosting.children = children;
			
			
			var physicalServer = createNode("Physical Server",serverVol.id);
			physicalServer.id = "1.1.1";
			physicalServer.tooltip='Servers having only one image per server';
			physicalServer.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(physicalServer);
			
			var virtualServer = createNode("Virtual Server",serverVol.id);
			virtualServer.id = "1.1.2";
			virtualServer.tooltip='Servers having images operating in an environment with multiple OS instances running on single server';
			virtualServer.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(virtualServer);
			
			children = [];
			children.push(physicalServer);
			children.push(virtualServer);
			serverVol.children = children;
			
			var winLinux = createNode("Windows/Linux",physicalServer.id);
			winLinux.id = "1.1.1.1";
			winLinux.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(winLinux);
			
			var unix = createNode("Unix",physicalServer.id);
			unix.id = "1.1.1.2";
			unix.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(unix);
			
			children = [];
			children.push(winLinux);
			children.push(unix);
			physicalServer.children = children;
			
			var winLinuxSm = createNode("Small",winLinux.id);
			winLinuxSm.id = "1.1.1.1.1";
			winLinuxSm.tooltip='Servers in the range of 1-2 (v)CPUs and 2-8 GB RAM';
			winLinuxSm.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(winLinuxSm);
			
			var winLinuxMd = createNode("Medium",winLinux.id);
			winLinuxMd.id = "1.1.1.1.2";
			winLinuxMd.tooltip='Servers in the range of 4-8 (v)CPUs and 8-32 GB RAM';
			winLinuxMd.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(winLinuxMd);
			
			var winLinuxLg = createNode("Large",winLinux.id);
			winLinuxLg.id = "1.1.1.1.3";
			winLinuxLg.tooltip='Servers in the range of 16-32 (v)CPUs and 16-128 GB RAM';
			winLinuxLg.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(winLinuxLg);
			
			children = [];
			children.push(winLinuxSm);
			children.push(winLinuxMd);
			children.push(winLinuxLg);
			winLinux.children = children;
			
			var unixSm = createNode("Small",unix.id);
			unixSm.id = "1.1.1.2.1";
			unixSm.tooltip='Servers in the range of 1-2 (v)CPUs and 2-8 GB RAM';
			unixSm.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(unixSm);
			
			var unixMd = createNode("Medium",unix.id);
			unixMd.id = "1.1.1.2.2";
			unixMd.tooltip='Servers in the range of 4-8 (v)CPUs and 8-32 GB RAM';
			unixMd.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(unixMd);
			
			var unixLg = createNode("Large",unix.id);
			unixLg.id = "1.1.1.2.3";
			unixLg.tooltip='Servers in the range of 16-32 (v)CPUs and 16-128 GB RAM';
			unixLg.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(unixLg);
			
			children = [];
			children.push(unixSm);
			children.push(unixMd);
			children.push(unixLg);
			unix.children = children;
			
			var vPublic = createNode("Public Cloud Server",virtualServer.id);
			vPublic.id = "1.1.2.1";
			vPublic.tooltip='The public cloud is defined as computing services offered by third-party providers over the public Internet, making them available to anyone who wants to use or purchase them';
			vPublic.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublic);
			
			var vPrivate = createNode("Private Cloud Server",virtualServer.id);
			vPrivate.id = "1.1.2.2";
			vPrivate.tooltip='Private cloud refers to a model of cloud computing where IT services are provisioned over private IT infrastructure for the dedicated use of a single organization.';
			vPrivate.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivate);
			
			children = [];
			children.push(vPublic);
			children.push(vPrivate);
			virtualServer.children = children;
			
			var vPublicWinLinux = createNode("Windows/Linux",vPublic.id);
			vPublicWinLinux.id = "1.1.2.1.1";
			vPublicWinLinux.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicWinLinux);
			
			var vPublicUnix = createNode("Unix",vPublic.id);
			vPublicUnix.id = "1.1.2.1.2";
			vPublicUnix.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicUnix);
			
			children = [];
			children.push(vPublicWinLinux);
			children.push(vPublicUnix);
			vPublic.children = children;
			
			var vPublicWinLinuxSm = createNode("Small",vPublicWinLinux.id);
			vPublicWinLinuxSm.id = "1.1.2.1.1.1";
			vPublicWinLinuxSm.tooltip='Servers in the range of 1-2 (v)CPUs and 2-8 GB RAM';
			vPublicWinLinuxSm.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicWinLinuxSm);
			
			var vPublicWinLinuxMd = createNode("Medium",vPublicWinLinux.id);
			vPublicWinLinuxMd.id = "1.1.2.1.1.2";
			vPublicWinLinuxMd.tooltip='Servers in the range of 4-8 (v)CPUs and 8-32 GB RAM';
			vPublicWinLinuxMd.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicWinLinuxMd);
			
			var vPublicWinLinuxLg = createNode("Large",vPublicWinLinux.id);
			vPublicWinLinuxLg.id = "1.1.2.1.1.3";
			vPublicWinLinuxLg.tooltip='Servers in the range of 16-32 (v)CPUs and 16-128 GB RAM';
			vPublicWinLinuxLg.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicWinLinuxLg);
			
			children = [];
			children.push(vPublicWinLinuxSm);
			children.push(vPublicWinLinuxMd);
			children.push(vPublicWinLinuxLg);
			vPublicWinLinux.children = children;
			
			var vPublicUnixSm = createNode("Small",vPublicUnix.id);
			vPublicUnixSm.id = "1.1.2.1.2.1";
			vPublicUnixSm.tooltip='Servers in the range of 1-2 (v)CPUs and 2-8 GB RAM';
			vPublicUnixSm.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicUnixSm);
			
			var vPublicUnixMd = createNode("Medium",vPublicUnix.id);
			vPublicUnixMd.id = "1.1.2.1.2.2";
			vPublicUnixMd.tooltip='Servers in the range of 4-8 (v)CPUs and 8-32 GB RAM';
			vPublicUnixMd.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicUnixMd);
			
			var vPublicUnixLg = createNode("Large",vPublicUnix.id);
			vPublicUnixLg.id = "1.1.2.1.2.3";
			vPublicUnixLg.tooltip='Servers in the range of 16-32 (v)CPUs and 16-128 GB RAM';
			vPublicUnixLg.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPublicUnixLg);
			
			children = [];
			children.push(vPublicUnixSm);
			children.push(vPublicUnixMd);
			children.push(vPublicUnixLg);
			vPublicUnix.children = children;
			
			var vPrivateWinLinux = createNode("Windows/Linux",vPrivate.id);
			vPrivateWinLinux.id = "1.1.2.2.1";
			vPrivateWinLinux.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateWinLinux);
			
			var vPrivateUnix = createNode("Unix",vPrivate.id);
			vPrivateUnix.id = "1.1.2.2.2";
			vPrivateUnix.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateUnix);
			
			children = [];
			children.push(vPrivateWinLinux);
			children.push(vPrivateUnix);
			vPrivate.children = children;
			
			var vPrivateWinLinuxSm = createNode("Small",vPrivateWinLinux.id);
			vPrivateWinLinuxSm.id = "1.1.2.2.1.1";
			vPrivateWinLinuxSm.tooltip='Servers in the range of 1-2 (v)CPUs and 2-8 GB RAM';
			vPrivateWinLinuxSm.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateWinLinuxSm);
			
			var vPrivateWinLinuxMd = createNode("Medium",vPrivateWinLinux.id);
			vPrivateWinLinuxMd.id = "1.1.2.2.1.2";
			vPrivateWinLinuxMd.tooltip='Servers in the range of 4-8 (v)CPUs and 8-32 GB RAM';
			vPrivateWinLinuxMd.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateWinLinuxMd);
			
			var vPrivateWinLinuxLg = createNode("Large",vPrivateWinLinux.id);
			vPrivateWinLinuxLg.id = "1.1.2.2.1.3";
			vPrivateWinLinuxLg.tooltip='Servers in the range of 16-32 (v)CPUs and 16-128 GB RAM';
			vPrivateWinLinuxLg.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateWinLinuxLg);
			
			children = [];
			children.push(vPrivateWinLinuxSm);
			children.push(vPrivateWinLinuxMd);
			children.push(vPrivateWinLinuxLg);
			vPrivateWinLinux.children = children;
			
			var vPrivateUnixSm = createNode("Small",vPrivateUnix.id);
			vPrivateUnixSm.id = "1.1.2.2.2.1";
			vPrivateUnixSm.tooltip='Servers in the range of 1-2 (v)CPUs and 2-8 GB RAM';
			vPrivateUnixSm.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateUnixSm);
			
			var vPrivateUnixMd = createNode("Medium",vPrivateUnix.id);
			vPrivateUnixMd.id = "1.1.2.2.2.2";
			vPrivateUnixMd.tooltip='Servers in the range of 4-8 (v)CPUs and 8-32 GB RAM';
			vPrivateUnixMd.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateUnixMd);
			
			var vPrivateUnixLg = createNode("Large",vPrivateUnix.id);
			vPrivateUnixLg.id = "1.1.2.2.2.3";
			vPrivateUnixLg.tooltip='Servers in the range of 16-32 (v)CPUs and 16-128 GB RAM';
			vPrivateUnixLg.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(vPrivateUnixLg);
			
			children = [];
			children.push(vPrivateUnixSm);
			children.push(vPrivateUnixMd);
			children.push(vPrivateUnixLg);
			vPrivateUnix.children = children;
			
			hostingDetails.serverHosting = serverHosting;
			
			var platformHosting = createNode("Platform Hosting",null);
			platformHosting.id = "2";
			platformHosting.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(platformHosting);
			
			var sql = createNode("SQL/Open Source Instances",platformHosting.id);
			sql.id = "2.1";
			sql.tooltip='The number of supported SQL/Open Source instances';
			sql.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(sql);
			
			var cots = createNode("COTS Installations/Licenses",platformHosting.id);
			cots.id = "2.2";
			cots.tooltip='The number of supported COTS installations/licenses (SAP,Oracle,Siebel). 1 License on a server = 1 database';
			cots.distributedVolume = createVolDistributionList(dealInfo);
			hostingDetails.nodeList.push(cots);
			
			children = [];
			children.push(sql);
			children.push(cots);
			platformHosting.children = children;
			
			hostingDetails.platformHosting = platformHosting;
			
			return hostingDetails;
		}
		
	}
});

function createVolDistributionList(dealInfo)
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

function createNode(levelName,parentId) {
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