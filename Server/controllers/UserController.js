const {authuser, registerUser,updateUserDpImageName, getUserDpPath, getUserActivites} = require("../models/UserModel");
const {DPUpload} = require("../Utils/configStorageEngine")
const path = require("path");
const {getRootDir} = require("../Utils/getRootDir");

const userAuth = async (req, res) => {
	const email = req.body.email;
	const pwd = req.body.password;
	const result = await authuser(email, pwd)
	if(result.rows.length != 0 && result.rows[0].user_status=='active'){
		req.session.email = email
		req.session.role = "user"
		const user_details = JSON.stringify(result.rows[0]);
		res.json(JSON.parse(`{"error":null,"message":"login successful", "user_details":${user_details}}`))
	}else{
		res.json(JSON.parse('{"error":{"message":"user not found"}}')) }
}

const userRegister = async (req, res) => {
	const username = req.body.name;
	const email = req.body.email;
	const password = req.body.password;
	const phone_number = req.body.ph_number;
	const join_time = req.body.join_date_time;
	//check if user already exist
	const result = await authuser(email, password);
	if(result.rows.length != 0){
		res.json(JSON.parse('{"status":"user already exist","error":"user_exist_error"}'));
	}else{
		await registerUser(username, email, password, phone_number, join_time, null)
		res.json(JSON.parse('{"status":"successfully registered","error":"null"}'));
	}
}


const uploadDpImageController =  (req, res) => {
	DPUpload(req, res, async (err) => {
		if (err) {
			return res.end("Error uploading file.");
		}else{
			const user_id = req.body.user_id;
			const image_name = req.file.filename;
			await updateUserDpImageName(user_id, image_name);
			res.json(JSON.parse('{"status":"successfully uploaded","error":"null"}'))
		}
	})
}

const getDpController = async (req, res) => {
	const result = await getUserDpPath(req.body.user_id)
	const image_name = result.rows[0].profile_pic
	try{
		res.sendFile(path.join(getRootDir(), "Uploads", "ProfilePics", image_name));
	}catch(error){
		console.log("image not found");
		res.json(JSON.parse('{"status":"image not found","image_error":"image not found"}'))
	}
	
}

const getUserActivitiesController = async (req, res) => {
	const user_id = req.query.user_id;
	const user_activities = await getUserActivites(user_id);
	res.json(JSON.parse(JSON.stringify(user_activities)));
}




module.exports = {userAuth, userRegister, uploadDpImageController, getDpController, getUserActivitiesController}

