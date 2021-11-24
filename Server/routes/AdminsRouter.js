const express = require("express");
const updateUserStatusRoute = express.Router();
const getCampInfoRoute = express.Router();
const updateSupportStatusRoute = express.Router();
const getSupportsRouter = express.Router();
const getReports = express.Router();
const takeDownPosts = express.Router();
const getSinglePost = express.Router();
const adminLogin = express.Router();
const getUsersRouter = express.Router();
const getServicePostRouter = express.Router();
const {updateUserStatusController, getCampInfoController, updateSupportStatusController, getSupportsController, getReportServicePostController, getReportVictimsPostController, takedownServicePostController, takedownVictimsPostController, getVictimsPostController, getServicePostController, adminAuthController, getUsersController, getServiceFeedsAdminsController} = require("../controllers/AdminsController");
const { getUsers } = require("../models/UserModel");

adminLogin.post("/", (req, res) => {
	adminAuthController(req, res)
})


updateUserStatusRoute.put("/", (req, res) => {
	updateUserStatusController(req, res);
})

getCampInfoRoute.get("/", (req, res) => {
	getCampInfoController(req, res);
})

updateSupportStatusRoute.put("/", (req, res) => {
	updateSupportStatusController(req, res);
})

getSupportsRouter.get("/", (req, res) => {
	getSupportsController(req, res)
})

getReports.get("/", (req, res) => {
	const post_type = req.query.post_type
	if(post_type == "RFH"){
		getReportVictimsPostController(req, res)
	}else if(post_type == "SERVICE"){
		getReportServicePostController(req, res)
	}
})

takeDownPosts.delete("/", (req, res) => {
	const post_type = req.body.post_type
	if(post_type == "RFH"){
		takedownVictimsPostController(req, res)
	}else if(post_type == "SERVICE"){
		takedownServicePostController(req, res)
	}
})

getSinglePost.get("/", (req, res) => {
	const post_type = req.query.post_type;
	if(post_type == "RFH"){
		getVictimsPostController(req, res)
	}else if(post_type == "SERVICE"){
		getServicePostController(req, res)
	}
})

getUsersRouter.get("/", (req, res) => {
	getUsersController(req, res)
})

getServicePostRouter.get("/", (req, res) => {
	getServiceFeedsAdminsController(req, res)
})






module.exports = {getCampInfoRoute, adminLogin, updateUserStatusRoute, getSupportsRouter, getReports, takeDownPosts, getSinglePost, updateSupportStatusRoute, getUsersRouter, getServicePostRouter};
