const checkForUser = (email, password) => {
	return (email === "alby@gmail.com" && password === "alby123") ? true : false;
	//use the correspoding model and return true if user exist else false

}

module.exports = {checkForUser};