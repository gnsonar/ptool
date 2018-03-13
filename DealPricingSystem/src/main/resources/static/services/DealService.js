priceToolServices.factory("DealService", function() {
	
	var isSave = {};
	isSave.setter = function(newValue) {
		isSave.value = newValue;
    }
	isSave.getter = function() {
        return isSave.value;
    }
    return isSave;
	
	
})