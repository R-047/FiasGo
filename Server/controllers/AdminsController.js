const {updateUserStatus, getUsers} = require("../models/UserModel")
const {getCampInfo} = require("../models/MapsModel")
const {updateSupportStatus, getSupports} = require("../models/SupportsModel")
const {getReportsServicesPost, getReportsVictimsPost} = require("../models/ReportsModel")
const {takeDownServicesPost, takeDownVictimsPost, getServicePost, getVictimsPost,  getServicesFeedsForAdmin}  = require("../models/PostModel")
const {adminAuth} = require("../models/adminModel")

const adminAuthController = async (req, res) => {
	const email = req.body.email;
	const pwd = req.body.password;
	const result = await adminAuth(email, pwd)
	if(result.rows.length != 0){
		req.session.email = email
		req.session.role = "admin"
		const user_details = JSON.stringify(result.rows[0]);
		res.json(JSON.parse(`{"error":null,"message":"login successful", "admin_details":${user_details}}`))
	}else{
		res.json(JSON.parse('{"error":{"message":"invalid credentials"}}')) }
}


const updateUserStatusController = async (req, res) => {
	const user_id = req.body.user_id;
	const user_status = req.body.user_status;
	await updateUserStatus(user_id, user_status) ? res.json(JSON.parse('{"status":"successfully updated user status","error":"null"}')) : res.json(JSON.parse('{"status":"could not update user status","error":"error occured"}'))
}


//before this getCamps
const getCampInfoController = async (req, res) => {
	const camp_id = req.query.camp_id
	const result = await getCampInfo(camp_id)
	res.json(JSON.parse(JSON.stringify(result.rows)))
}

const updateSupportStatusController = async (req, res) => {
	const support_status = req.body.support_status;
	const support_id = req.body.support_id;
	await updateSupportStatus(support_status, support_id) ? res.json(JSON.parse('{"status":"successfully updated support status","error":"null"}')) : res.json(JSON.parse('{"status":"could not update support status","error":"error occured"}'))
}

const getSupportsController = async (req, res) => {
	const result = await getSupports()
	res.json(JSON.parse(JSON.stringify(result.rows)))	
}

const getReportServicePostController = async (req, res) => {
	const s_post_id = req.query.s_post_id;
	const result = await getReportsServicesPost(s_post_id)
	res.json(JSON.parse(JSON.stringify(result.rows)))
}

const getReportVictimsPostController = async (req, res) => {
	const v_post_id = req.query.v_post_id;
	const result = await getReportsVictimsPost(v_post_id)
	res.json(JSON.parse(JSON.stringify(result.rows)))
}

const takedownVictimsPostController = async (req, res) => {
	const v_post_id = req.body.v_post_id
	await takeDownVictimsPost(v_post_id) ? res.json(JSON.parse('{"status":"victims post removed successfully","error":"null"}')) : res.json(JSON.parse('{"status":"could not remove victims post","error":"error occured"}'))
}

const takedownServicePostController = async (req, res) => {
	const s_post_id = req.body.s_post_id
	await takeDownServicesPost(s_post_id) ? res.json(JSON.parse('{"status":"service post removed successfully","error":"null"}')) : res.json(JSON.parse('{"status":"could not remove service post","error":"error occured"}'))
}

const getVictimsPostController = async (req, res) => {
	const v_post_id = req.query.v_post_id;
	const result = await getVictimsPost(v_post_id)
	res.json(JSON.parse(JSON.stringify(result.rows)))
}

const getServicePostController = async (req, res) => {
	const s_post_id = req.query.s_post_id;
	const result = await getServicePost(s_post_id)
	res.json(JSON.parse(JSON.stringify(result.rows)))

}


const getUsersController = async (req, res) => {
	const result = await getUsers()
	res.json(JSON.parse(JSON.stringify(result)))
}

const getServiceFeedsAdminsController = async (req, res) => {
	const result = await getServicesFeedsForAdmin()
	res.json(JSON.parse(JSON.stringify(result)))
}



module.exports = {updateUserStatusController, getCampInfoController, updateSupportStatusController, getSupportsController, getReportServicePostController, getReportVictimsPostController, takedownServicePostController, takedownVictimsPostController, getVictimsPostController, getServicePostController, adminAuthController, getUsersController,  getServiceFeedsAdminsController}