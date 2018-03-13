priceToolServices.factory("StorageService", function() {
	var storageDetails = {};
	storageDetails.dealTerm = '';
	storageDetails.offshoreReq = '';
	storageDetails.includeHardware = '';
	storageDetails.toolingMonitoring = '';
	storageDetails.coLocation = '';
	storageDetails.serviceOrch = '';
	storageDetails.totalServers = '';
	storageDetails.averagestorageVolume = '';
	storageDetails.serviceWindowSla = '';
	storageDetails.storageYearlyDataInfoDtos = [];

	/*Percentages*/
	storageDetails.performancePerc = '';
	storageDetails.standardPerc = '';

	storageDetails.backupVolumePerc = '';

	/*Binded with controller on UI*/
	storageDetails.storage = '';
	storageDetails.backup = '';
	storageDetails.nodeList = [];


	return {

		getStorageDetails : function(){
			return storageDetails;
		},

		initStorageDetails : function(dealInfo){
			var children = [];

			var storage = createStorageNode("Storage",null);
			storage.id = "1";
			storage.distributedVolume = null;
			storageDetails.nodeList.push(storage);
			
			var storageVol = createStorageNode("Storage Volume (in TB)",storage.id);
			storageVol.id = "1.1";
			storageVol.distributedVolume = createStorageVolDistributionList(dealInfo);
			storageDetails.nodeList.push(storageVol);

			var performance = createStorageNode("Performance Storage Volume (in TB)",storageVol.id);
			performance.id = "1.1.1";
			performance.tooltip='This storage type builds around high bandwidth, rapid accessibility. Primarily used for more critical data. From a benchmarking point of view, this is a blend between Tier 0 and Tier 1 storage types.';
			performance.distributedVolume = createStorageVolDistributionList(dealInfo);
			storageDetails.nodeList.push(performance);

			var standard = createStorageNode("Standard Storage Volume (in TB)",storageVol.id);
			standard.id = "1.1.2";
			standard.tooltip='This storage type builds around normal bandwidth and accessibility, used for the majority of data storage. From a benchmarking point of view, this is a blend between Tier 2 and Tier 3 storage types.';
			standard.distributedVolume = createStorageVolDistributionList(dealInfo);
			storageDetails.nodeList.push(standard);

			children.push(performance);
			children.push(standard);
			storageVol.children = children;
			
			children = [];
			children.push(storageVol);
			storage.children = children;

			//storageDetails.storage = storageVol;
			storageDetails.storage = storage;

			var backup = createStorageNode("Backup",null);
			backup.id = "2";
			backup.distributedVolume = null;
			storageDetails.nodeList.push(backup);
			
			var backupVolume = createStorageNode("Backup Volume (in TB)",backup.id);
			backupVolume.id = "2.1";
			backupVolume.distributedVolume = createStorageVolDistributionList(dealInfo);
			storageDetails.nodeList.push(backupVolume);
			
			children = [];
			children.push(backupVolume);
			backup.children = children;
			
			storageDetails.backup = backup;


			return storageDetails;
		}

	}
});

function createStorageVolDistributionList(dealInfo)
{
	vdList=[];
	for (var i = 0; i < dealInfo/12; i++){
		var vd = {};
		vd.year = i+1;
		vd.volume = '';
		vd.revenue = '';
		vd.unit = '';
		vdList.push(vd);
	}
	return vdList;
}

function createStorageNode(levelName,parentId) {
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