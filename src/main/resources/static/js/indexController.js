var app = angular.module('bezbednost');

app.run([ 'ngNotify', function(ngNotify) {

	ngNotify.config({
		position : 'bottom',
		duration : 1000,
		theme : 'pitchy',
		sticky : false,
	});
} ]);

app.config([ '$qProvider', function($qProvider) {
	$qProvider.errorOnUnhandledRejections(false);
} ]);


app.controller('appController',['$rootScope','$scope','$location',function($rootScope,$scope,$location){


}]);

app.controller('generateController',['$rootScope','ngNotify','$scope','$location', 'ServiceGenerate', function($rootScope,ngNotify,$scope,$location, serviceGenerate){

	$scope.submitLogin = function() {
		serviceGenerate
				.logIn($scope.logovanje)
				.then(
						function(response) {
							if (response.status == 200)
								if (response.data.uloga === "Klijent")
									serviceGenerate
											.preuzmiKlijenta()
											.then(
													function(
															response) {
														if (response.status == 200) {
															ngNotify.set('Uspjesno logovanje' , {
																type : 'success'
															});
															$rootScope.korisnik = response.data;
															$location
																	.path('/Klijent/klijent');
														}
													});
								else
									serviceGenerate
											.preuzmiZaposlenog()
											.then(
													function(
															response) {
														if (response.status == 200) {
															ngNotify.set('Uspjesno logovanje' , {
																type : 'success'
															});
															$rootScope.korisnik = response.data;
															if (response.data.ulogaZ === "Salterusa")
																$location
																		.path('/obicnaSalterusa/registracijaKlijenataObicna');
															else if (response.data.ulogaZ === "Super_salterusa")
																$location
																		.path('/salterusa/registracijaKlijenta');
															else
																$location
																		.path('/admin');
															
														}
													});
							

						}).catch(function(response) {
							ngNotify.set('Korisnik ne postoji' , {
								type : 'error',
							    sticky: true
							});
							console.error('Gists error', response.status, response.data)
						  });
	}
	
	$scope.changePassword = function() {
		if($scope.lozinka.stara == $rootScope.korisnik.lozinka){
			serviceGenerate.changePassword($scope.lozinka.nova).then(function(response){
				$location.path('/index');
			});
		} else {
			lozinka.nova = '';
			lozinka.stara = '';
			$location.path('/changePassword')
		}
	}
	
	$scope.logout = function(){
		serviceGenerate.logout().then(function(response){
			$rootScope.korisnik = '';
			$location.path('/login');
			$scope.logovanje = null;
		});
	}
	
	
	
	$scope.caCertificate = function() {
	    serviceGenerate.caCertificate().then(
	      function(response) {
	       $scope.caCertificate = response.data;
	      });
	   }
	
	
	$scope.generateCertificate= function(){
		
    	serviceGenerate.generateCertificate($scope.cert).then(function(response){
    		if(response.status == 200){
    			ngNotify.set('Uspesno generisanje' , {
					type : 'success'
				});
    			$scope.error = false;
    			//$location.path('/index');
    			$scope.cert = null;
    		} else {
    			$scope.error = true;
    		}
    	});
	}

	
    $scope.generateCRSCertificate= function(){
		
    	serviceGenerate.generateCRSCertificate($scope.ccrs).then(function(response){
    		if(response.status == 'OK'){
    			ngNotify.set('Uspesno generisanje' , {
					type : 'success'
				});
    			$scope.error = false;
    			$location.path('/index')
    		} else {
    			$scope.error = true;
    		}
    	});
	}
	
	
    $scope.getCertificate = function(){
		
    	serviceGenerate.getCertificate($scope.serial).then(function(response){
    		
    	});
	}	
	
    $scope.delCertificate = function(){
		
    	serviceGenerate.delCertificate($scope.serial).then(function(response){
    		
    	});
	}
  
    $scope.statusCertificate = function() {
	    serviceGenerate.statusCertificate($scope.serial).then(
	      function(response) {
	      $scope.status = response.data; 
	      });
	   }
	
}]);
