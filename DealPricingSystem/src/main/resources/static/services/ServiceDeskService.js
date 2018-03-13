priceToolServices.factory("ServiceDeskService", function() {
	var serviceDeskDetails = {};
	serviceDeskDetails.offshoreReq = '';
	serviceDeskDetails.tooling = '';
	serviceDeskDetails.levelOfService = '';
	serviceDeskDetails.multiLingual = '';
	serviceDeskDetails.contactRatio = '';
	
	serviceDeskDetails.serviceDeskYearlyDataInfoDtos = [];
	
	/*Percentages*/
	serviceDeskDetails.voicePerc = '';
	serviceDeskDetails.contactPerc = '';
	serviceDeskDetails.chatPerc = '';
	serviceDeskDetails.portalPerc = '';

	/*Binded with controller on UI*/
	serviceDeskDetails.contactDetail = '';
	serviceDeskDetails.nodeList = [];


	return {
		getServiceDeskDetails : function(){
			return serviceDeskDetails;
		},
		
		initServiceDeskDetails : function(dealInfo){
			var children = [];
			
			var contactDetail = createServiceDeskNode("Contact Details",null);
			contactDetail.id = "1";
			contactDetail.distributedVolume = null;
			serviceDeskDetails.nodeList.push(contactDetail);
			
			var contact = createServiceDeskNode("Contacts",contactDetail.id);
			contact.id = "1.1";
			contact.distributedVolume = createServiceDeskVolDistributionList(dealInfo);
			serviceDeskDetails.nodeList.push(contact);
			
			var voice = createServiceDeskNode("Voice",contact.id);
			voice.id = "1.1.1";
			voice.distributedVolume = createServiceDeskVolDistributionList(dealInfo);
			serviceDeskDetails.nodeList.push(voice);
			
			var mail = createServiceDeskNode("Mail",contact.id);
			mail.id = "1.1.2";
			mail.distributedVolume = createServiceDeskVolDistributionList(dealInfo);
			serviceDeskDetails.nodeList.push(mail);
			
			var chat = createServiceDeskNode("Chat",contact.id);
			chat.id = "1.1.3";
			chat.distributedVolume = createServiceDeskVolDistributionList(dealInfo);
			serviceDeskDetails.nodeList.push(chat);
			
			var portal = createServiceDeskNode("Portal",contact.id);
			portal.id = "1.1.4";
			portal.tooltip='These calls are primarily calls logged via a self-service portal and are assumed to be solved leveraging heavily on automation, significantly reducing the pricing';
			portal.distributedVolume = createServiceDeskVolDistributionList(dealInfo);
			serviceDeskDetails.nodeList.push(portal);
			
			
			children.push(voice);
			children.push(mail);
			children.push(chat);
			children.push(portal);
			contact.children = children;
			
			children = [];
			children.push(contact);
			contactDetail.children = children;
			
			serviceDeskDetails.contact = contactDetail;
			return serviceDeskDetails;
		}
	}
});

function createServiceDeskVolDistributionList(dealInfo)
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

function createServiceDeskNode(levelName,parentId) {
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