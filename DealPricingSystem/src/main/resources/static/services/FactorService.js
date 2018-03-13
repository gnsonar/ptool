priceToolServices.factory("FactorService", function() {
	
	var NetworkDetails = {};
	NetworkDetails.setter = function(newValue) {
		NetworkDetails.value = newValue;
    }
	NetworkDetails.getter = function() {
        return NetworkDetails.value;
    }
    return NetworkDetails;
	
	
})