var app = angular.module('bezbednost');
app.factory('ServiceGenerate', function serviceGenerate($http) {
	
	serviceGenerate.logIn = function(logovanje) {
		return $http.get("/korisnik/logovanje/"+logovanje.korisnickoIme+"/"+logovanje.lozinka);
	}

	serviceGenerate.changePassword = function(lozinka){
		return $http.put("korisnik/promenaLozinke/"+lozinka);
	}
	
	serviceGenerate.logout = function(){
		return $http.get("/korisnik/logout");
	}
	
	serviceGenerate.preuzmiKlijenta = function() {
		return $http.get("/klijent/preuzmiKlijenta/");
	}
	
	serviceGenerate.preuzmiZaposlenog = function() {
		return $http.get("/zaposleni/preuzmiZaposlenog/");
	}
	
	
	serviceGenerate.generateCertificate = function(cert) {
		return $http.post( "/certificate/generateCertificate" ,cert);
	}
	
	serviceGenerate.generateCRSCertificate = function(ccrs) {
		return $http.post( "/certificate/generateCRSCertificate" ,ccrs);
	}
	
	serviceGenerate.caCertificate = function() {
		return $http.get( "/certificate/getValidCertificates");
	}
	
	serviceGenerate.getCertificate = function(serial) {
		return $http.get( "/certificate/getCertificate/" + serial);
	}
	
	serviceGenerate.delCertificate = function(serial) {
		return $http.delete( "/certificate/deleteCertificate/" + serial);
	}
	
	serviceGenerate.statusCertificate = function(serial) {
		return $http.get( "/certificate/statusCertificate/" + serial);
	}
	
	return serviceGenerate;
});
