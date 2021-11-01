const AdminAuthenticate = (req, res, next) => {
	//check for session
	
	if(req.session.email && req.session.role == "admin"){
		next()
	}else{
		res.send(JSON.parse('{"error":[{"message":"admin not logged in! please login again"}]}'))
		
	}
	//if session exists call next else send "bad response login again"
}

module.exports = {AdminAuthenticate}


