	priceToolConstants.factory('endpointsurls', function() {
    var endPoint = {
    		HOST_DETAILS_URL:'/getHostDetails',
    		LOGIN_URL:'/login',
    		LOGOUT_URL:'/resources/logout',
    		REGISTRATION_URL:'/register',
    		
    		//SideHeaderSubmission
    		SUBMISSION_DEAL_WORKFLOW_URL:'/resources/generic/workflow/',

    		//Home
    		HOME_RECENT_DEAL_URL:'/resources/home/getRecentDeals',
    		HOME_GET_LATEST_UPDATE_URL :'/resources/home/getLatestUpdates',
    		HOME_FIND_DEAL_URL :'/resources/home/findDeal?clientName=',

    		//Generic
    		GENERIC_DEAL_INFO_URL:'/resources/generic/getGenericDropDownDetails',
    		GENERIC_SAVE_URL:'/resources/generic/saveGenericDealDetails',
			GENERIC_DEAL_DETAILS_URL:'/resources/generic/getGenericDetailsByDealId?dealId=',

			GENERIC_SUBMISSION_SAVE_URL:'/resources/generic/saveGenericDealDetails',
			GENERIC_SUBMISSION_DEAL_INFO_URL:'/resources/generic/getGenericDropDownDetails',
			GENERIC_SUBMISSION_DEAL_DETAILS_URL:'/resources/generic/getGenericDetailsByDealId?dealId=',
			GENERIC_SUBMISSION_CONTACT_INFORMATION_URL:'/resources/common/ldapUsers?name=',


			//Admin
			ADMIN_ACCESS_REQUEST_URL :'/resources/admin/getPendingAccessRequests',
			ADMIN_ROLES_URL :'/resources/admin/getRoles',
			ADMIN_UPDATE_ACCESS_REQUEST_URL :'/resources/admin/updateAccessRequest',
			ADMIN_SAVE_MESSAGE_URL :'/resources/admin/saveAdminMessage',
			ADMIN_GET_CURRENCY_URL : '/resources/admin/currency',
			ADMIN_UPDATE_CURRENCY_URL : '/resources/admin/currency',

		   // storage
			STORAGE_DROPDOWNS:'/resources/storage/dropdowns/',
			STORAGE_DETAILS_URL:'/resources/storage/volumes/',
			STORAGE_SAVE_DETAILS_URL:'/resources/storage/volumes/',
			STORAGE_CALCULATOR:'/resources/storage/revenues/',
			STORAGE_RESULT:'/resources/storage/revenues/',
			STORAGE_NEAREST_DEAL:'/resources/storage/results/',
			STORAGE_RECALCULATE:'/resources/storage/solution/',


			//Total
			TOTAL_REVENUE:'/resources/totalRevenues/',
			ASSUMPTIONS:'/resources/assumptions',
			SAVE_ACTIVE_SCENERIO:'/resources/scenario/',
			DELETE_SCENERIO:'/resources/scenario/',

			//Apps
		    APP_DROPDOWN:'/resources/apps/dropdowns/',
		    SAVE_APP_INFO:'/resources/apps/volumes/',
		    CALCULATE_APP_INFO:'/resources/apps/revenues/',
		    APP_NEAREST_DEAL:'/resources/apps/results/',
		    APP_REVENUE:'/resources/apps/revenues/',
		    APP_RECALCULATE:'/resources/apps/solution/',

		    //Service Desk
		    SERVICE_DROPDOWN:'/resources/serviceDesk/dropdowns/',
		    SAVE_SERVICE_INFO:'/resources/serviceDesk/volumes/',
		    SERVICE_CALCULATE_INFO:'/resources/serviceDesk/revenues/',
		    SERVICE_NEAREST_DEAL:'/resources/serviceDesk/results/',
		    SERVICE_REVENUE:'/resources/serviceDesk/revenues/',
		    SERVICE_RECALCULATE:'/resources/serviceDesk/solution/',

		    //Network
		    NETWORK_DROPDOWN:'/resources/network/dropdowns/',
		    SAVE_NETWORK_INFO:'/resources/network/volumes/',
		    NETWORK_CALCULATE_INFO: '/resources/network/revenues/',
		    NETOWK_REVENUE:'/resources/network/revenues/',
		    NETWORK_RECALCULATE:'/resources/network/solution/',
		    NETWORK_NEAREST_DEAL:'/resources/network/results/',

		    //Retail
		    RETAIL_DROPDOWN:'/resources/retail/dropdowns/',
		    SAVE_RETAIL_INFO:'/resources/retail/volumes/',
		    RETAIL_NEAREST_DEAL:'/resources/retail/results/',
		    CALCULATE_RETAIL_INFO:'/resources/retail/revenues/',
		    RETAIL_RECALCULATE:'/resources/retail/solution/',
		    RETAIL_REVENUE:'/resources/retail/revenues/',

		    //End User
		    ENDUSER_DROPDOWN:'/resources/enduser/dropdowns/',
		    SAVE_ENDUSER_INFO:'/resources/enduser/volumes/',
		    ENDUSER_CALCULATE_INFO: '/resources/enduser/revenues/',
		    ENDUSER_RECALCULATE:'/resources/enduser/solution/',
		    ENDUSER_NEAREST_DEAL:'/resources/enduser/results/',
		    ENDUSER_REVENUE:'/resources/enduser/revenues/',

		    //Hosting
		    HOSTING_DROPDOWN:'/resources/hosting/dropdowns/',
		    SAVE_HOSTING_INFO:'/resources/hosting/volumes/',
		    CALCULATE_HOSTING_INFO:'/resources/hosting/revenues/',
		    HOSTING_NEAREST_DEAL:'/resources/hosting/results/',
		    HOSTING_REVENUE:'/resources/hosting/revenues/',
		    HOSTING_RECALCULATE:'/resources/hosting/solution/',
		   
		    //indicator 
		    GENERic_INDICATOR:'/resources/generic/towerIndicators/'
    };
    return endPoint;
});