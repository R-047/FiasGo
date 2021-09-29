const Authenticate = (req, res, next) => {
	//check for session
	if(req.session.email){
		next()
	}else{
		res.send("please login again")
		
	}
	//if session exists call next else send "bad response login again"
}

module.exports = {Authenticate}


