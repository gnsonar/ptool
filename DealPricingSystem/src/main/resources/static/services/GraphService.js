priceToolServices.factory("GraphService", function() {
	
	var garphDeatls = {};
	garphDeatls.setter = function(newValue) {
		garphDeatls.value = newValue;
    }
	garphDeatls.getter = function() {
        return garphDeatls.value;
    }
    return garphDeatls;
	
	
})