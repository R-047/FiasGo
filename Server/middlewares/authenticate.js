const Authenticate = (req, res, next) => {
	//check for session
	
	if(req.session.email && req.session.role == "user"){
		next()
	}else{
		res.send(JSON.parse('{"error":[{"message":"please login again"}]}'))
		
	}
	//if session exists call next else send "bad response login again"
}

module.exports = {Authenticate}


