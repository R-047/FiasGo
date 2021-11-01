const multer = require("multer");
const path = require('path');
const crypto = require('crypto');



function writeImage(req, file, callback){
		const user_id = req.body.user_id
		const org_file_name = file.originalname.split('.')[0];
		const time = Date.now()
		const uniqueText = user_id+org_file_name+time;
		const uniqueFileName = crypto.createHash('sha256').update(uniqueText).digest('hex')
		callback(null, uniqueFileName+path.extname(file.originalname));
}

const PostStorage = multer.diskStorage({
	destination: (req, file, callback) => {
		callback(null, "./Uploads/Posts")
		
	},
	filename: (req, file, callback) => {
		writeImage(req, file, callback);
	 }

});

const DPStorage = multer.diskStorage({
	destination: (req, file, callback) => {
		callback(null, "./Uploads/ProfilePics")
	},
	filename: (req, file, callback) => {
		writeImage(req, file, callback);
	}

});


const CampImageStorage = multer.diskStorage({
	destination: (req, file, callback) => {
		callback(null, "./Uploads/CampImages")
		
	},
	filename: (req, file, callback) => {
		writeImage(req, file, callback);
	}

});



var PostUpload = multer({ storage: PostStorage }).array('post_images', 10);
var DPUpload = multer({ storage: DPStorage }).single('profile_pic');
var CampImageUpload = multer({ storage: CampImageStorage }).array('camp_images', 10);

module.exports = {PostUpload, DPUpload, CampImageUpload};