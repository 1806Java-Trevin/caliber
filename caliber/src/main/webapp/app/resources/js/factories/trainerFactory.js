/**
 * API for making trainer related AJAX calls to the backend
 */
angular.module("api").factory("trainerFactory", function($log, $http) {
	$log.debug("Booted Trainer API");
	var trainer = {};

	// test connection to factory -- remove on release
	trainer.test = function() {
		return "Trainer factory test successful.";
	};

	// Grab all batches
	trainer.getAllBatches = function() {
		var data = [];
		$http({
			url : "/trainer/batch/all",
			method : "GET",
		}).then(function(response) {
			$log.debug(response);
			// copy array
			angular.copy(response.data, data);
		}, function(response) {
			data = null;
			$log.error("There was an error: " + response.status);
		});
		return data;
	};

	// Grab current batch
	trainer.getCurrentBatch = function(){
		var data = {};
		$http({
			url: "/trainer/batch/current",
			method: "GET"
		}).then(function(response){
			$log.debug(response);
			// copy object
			angular.copy(response.data, data);
		}, function(response){
			data = null;
			$log.error("There was an error: " + response.status);
		});
		return data;
	};
	
	// Grab batch
	trainer.getBatch = function(id){
		var data = {};
		$http({
			url: "/trainer/batch/" + id,
			method: "GET"
		}).then(function(response){
			$log.debug(response);
			// copy object
			angular.copy(response.data, data);
		}, function(response){
			data = null;
			$log.error("There was an error: " + response.status);
		});
		return data;
	};
	
	return trainer;
});