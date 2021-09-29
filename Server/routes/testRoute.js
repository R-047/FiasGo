const express = require("express");
const testRouter = express.Router();

testRouter.get("/", (req,res)=>{
	console.log("logging request params ",req.query);
	if(req.session.email){
		res.json({"content":"test content","email": req.session.email});
	}else{
		res.send("oops not error, user not authenticated");
	}
})

module.exports = testRouter;