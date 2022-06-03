baseRestURL = "https://34.132.85.42:8080/MicroStrategyLibrary";
projectID   = "FE1BF18D4A3E6C7376EFD69ED8BD79C4";
dossierID   = "38C204614524304BEE191B88257D348F";
/* since in this case we want dossier to load automatically for everyone we will use a demo user */
username    = "desarrollo";
password    = "plataforma2020";
/* End of configuration parameters  ------------------------------------------------------------- */

/* Generate the complete URL for the Dossier */
var dossierUrl = baseRestURL + '/app/' + projectID + '/' + dossierID + '/K53--K46';

/* Populate div with Dossier: */
microstrategy.dossier.create({
	/* This is the document's <div> container where the Dossier should be placed. */
	placeholder: document.getElementById("dossierContainer"),
	url: dossierUrl,

	/* The following parameters define the appearance of the Dossier.
	E.g. is the navigation or collaboration bar displayed, do right-click actions work, etc. */
	disableNotification: true,
	enableResponsive: true,

	/* And parameters for the user authentication. */
	/* In case we didn't want the dossier to load automatically for everyone */
	/* and wanted the user to log in we would skip that part. */
	enableCustomAuthentication: true,
	customAuthenticationType: microstrategy.dossier.CustomAuthenticationType.AUTH_TOKEN,
	getLoginToken: login

}).then(function(dossier) {
	/* Code to execute after the Dossier has finished loading... */
});

function login() {
	/* Prepare some parameters for login request */
	var options = {
		method: 'POST',
		credentials: 'include', /* include cookie */
		mode: 'cors', /* set as CORS mode for cross origin resource sharing */
		headers: {'Content-Type': 'application/json'},
		body: JSON.stringify({
		/* loginMode: 8 */ /* Login as guest user. */
		"loginMode": 1, /* standard login mode */
		"username": username,
		"password": password
		})
	};

	/* The actual login takes place here */
	return fetch(baseRestURL + '/api/auth/login', options).then(function (response) {
		if (response.ok) {
		return response.headers.get('x-mstr-authToken');
		} else {
		response.json().then(function(json) {
			console.log(json);
		});
		}
	}).catch(function (error) {
		console.log(error);
	});
};