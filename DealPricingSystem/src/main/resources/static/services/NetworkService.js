priceToolServices.factory("NetworkService", function() {
	var networkDetails = {};
	networkDetails.offshoreAllowed = '';
	networkDetails.includeHardware = '';
	networkDetails.levelOfService = '';

	networkDetails.networkingYearlyDataInfoDto = [];

	/*Percentages*/
	networkDetails.wanSmPerc = '';
	networkDetails.wanMdPerc = '';
	networkDetails.wanLgPerc = '';
	networkDetails.lanSmPerc = '';
	networkDetails.lanMdPerc = '';
	networkDetails.lanLgPerc = '';

	/*Binded with controller on UI*/
	networkDetails.wan = '';
	networkDetails.lan = '';
	networkDetails.wlanCon = '';
	networkDetails.wlanAccess = '';
	networkDetails.loadBalance = '';
	networkDetails.vpnIp = '';
	networkDetails.dnsService = '';
	networkDetails.firewalls = '';
	networkDetails.reservePro = '';

	networkDetails.nodeList = [];

	return {
		getNetworkDetails : function(){
			return networkDetails;
		},

		initNetworkDetails : function(dealInfo){
			var children = [];

			/*WAN*/
			var wan = createNetworkNode("WAN",null);
			wan.id = "1";
			wan.distributedVolume = null;
			networkDetails.nodeList.push(wan);

			var wanDevice = createNetworkNode("WAN Devices",wan.id);
			wanDevice.id = "1.1";
			wanDevice.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(wanDevice);

			var wanSm = createNetworkNode("Small Routers",wanDevice.id);
			wanSm.id = "1.1.1";
			wanSm.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(wanSm);

			var wanMd = createNetworkNode("Medium Routers",wanDevice.id);
			wanMd.id = "1.1.2";
			wanMd.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(wanMd);

			var wanLg = createNetworkNode("Large/Complex Routers",wanDevice.id);
			wanLg.id = "1.1.3";
			wanLg.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(wanLg);

			children.push(wanSm);
			children.push(wanMd);
			children.push(wanLg);
			wanDevice.children = children;

			children = [];
			children.push(wanDevice);
			wan.children = children;

			networkDetails.wanDevice = wan;

			/*LAN*/
			children = [];
			var lan = createNetworkNode("LAN",null);
			lan.id = "2";
			lan.distributedVolume = null;
			networkDetails.nodeList.push(lan);

			var lanDevice = createNetworkNode("LAN Devices",lan.id);
			lanDevice.id = "2.1";
			lanDevice.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(lanDevice);

			var lanSm = createNetworkNode("Small Switches",lanDevice.id);
			lanSm.id = "2.1.1";
			lanSm.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(lanSm);

			var lanMd = createNetworkNode("Medium Switches",lanDevice.id);
			lanMd.id = "2.1.2";
			lanMd.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(lanMd);

			var lanLg = createNetworkNode("Large/Complex Switches",lanDevice.id);
			lanLg.id = "2.1.3";
			lanLg.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(lanLg);

			children.push(lanSm);
			children.push(lanMd);
			children.push(lanLg);
			lanDevice.children = children;

			children = [];
			children.push(lanDevice);
			lan.children = children;

			networkDetails.lanDevice = lan;

			/*WLAN Controller*/
			var wlanCon = createNetworkNode("WLAN Controllers",null);
			wlanCon.id = "3";
			wlanCon.distributedVolume = null;
			networkDetails.nodeList.push(wlanCon);

			var controller = createNetworkNode("WLAN Controllers",wlanCon.id);
			controller.id = "3.1";
			controller.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(controller);

			children = [];
			children.push(controller);
			wlanCon.children = children;

			networkDetails.wlanCon = wlanCon;

			/*WLAN Access Point*/
			var wlanAccess = createNetworkNode("WLAN Access Points",null);
			wlanAccess.id = "4";
			wlanAccess.distributedVolume = null;
			networkDetails.nodeList.push(wlanAccess);

			var accessPoint = createNetworkNode(" WLAN Access Points",wlanAccess.id);
			accessPoint.id = "4.1";
			accessPoint.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(accessPoint);

			children = [];
			children.push(accessPoint);
			wlanAccess.children = children;

			networkDetails.wlanAccess = wlanAccess;

			/*Load Balancerss*/
			var loadBalance = createNetworkNode("Load Balancers",null);
			loadBalance.id = "5";
			loadBalance.distributedVolume = null;
			networkDetails.nodeList.push(loadBalance);

			var loadBal = createNetworkNode("Load Balancers",loadBalance.id);
			loadBal.id = "5.1";
			loadBal.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(loadBal);

			children = [];
			children.push(loadBal);
			loadBalance.children = children;

			networkDetails.loadBalance = loadBalance;

			/*VPN/IPSec*/
			var vpnIp = createNetworkNode("VPN/IPSec",null);
			vpnIp.id = "6";
			vpnIp.distributedVolume = null;
			networkDetails.nodeList.push(vpnIp);

			var vpn = createNetworkNode("VPN/IPSec",vpnIp.id);
			vpn.id = "6.1";
			vpn.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(vpn);

			children = [];
			children.push(vpn);
			vpnIp.children = children;

			networkDetails.vpnIp = vpnIp;

			/* DNS/DHCP Service */
			var dnsService = createNetworkNode("DNS/DHCP Service",null);
			dnsService.id = "7";
			dnsService.distributedVolume = null;
			networkDetails.nodeList.push(dnsService);

			var dns = createNetworkNode("DNS/DHCP Service",dnsService.id);
			dns.id = "7.1";
			dns.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(dns);

			children = [];
			children.push(dns);
			dnsService.children = children;

			networkDetails.dnsService = dnsService;

			/* Firewalls */
			var firewalls = createNetworkNode("Firewalls",null);
			firewalls.id = "8";
			firewalls.distributedVolume = null;
			networkDetails.nodeList.push(firewalls);

			var firewall = createNetworkNode("Firewalls",firewalls.id);
			firewall.id = "8.1";
			firewall.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(firewall);

			children = [];
			children.push(firewall);
			firewalls.children = children;

			networkDetails.firewalls = firewalls;

			/* Reverse Proxies */
			var reservePro = createNetworkNode("Reverse Proxies",null);
			reservePro.id = "9";
			reservePro.distributedVolume = null;
			networkDetails.nodeList.push(reservePro);

			var proxy = createNetworkNode("Proxies (reverse)",reservePro.id);
			proxy.id = "9.1";
			proxy.distributedVolume = createNetworkVolDistributionList(dealInfo);
			networkDetails.nodeList.push(proxy);

			children = [];
			children.push(proxy);
			reservePro.children = children;

			networkDetails.reservePro = reservePro;

			return networkDetails;
		}

	}
});

function createNetworkVolDistributionList(dealInfo)
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

function createNetworkNode(levelName,parentId) {
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