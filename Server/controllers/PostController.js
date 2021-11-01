const {getVictimsFeed, getServicesFeeds, uploadServicesPost, uploadVictimsPost, upvoteVictimsPost, upvoteServicesPost, downvoteVictimsPost, downvoteServicePost, PostComment, getPostComment, deletePostComment} = require("../models/PostModel");
const {PostUpload} = require("../Utils/configStorageEngine");
const path = require("path");
const {getRootDir} = require("../Utils/getRootDir");
const {UseHereMaps} = require("../Utils/hereMapsApiConf");
const {getTime} = require('../Utils/getPostgresTime');

const setServicesPostController = (req, res) => {
		PostUpload(req, res, async (err) =>  {
			if (err) {
				return res.end("Error uploading file.");
			}else{
				const post_images = []
				req.files.forEach(element => {
					post_images.push(element.filename)
				});
				const user_id = req.body.user_id;
				const contact_info = req.body.contact_info;
				const post_desc = req.body.post_desc;
				const post_lat = req.body.post_lat;
				const post_long = req.body.post_long;
				const address_result = await UseHereMaps(`https://revgeocode.search.hereapi.com/v1/revgeocode?at=${post_lat}%2C${post_long}&lang=en-US`)
				const location_name = address_result.items[0].address.county +","+address_result.items[0].address.state
				await uploadServicesPost(user_id, contact_info, post_desc, post_images, post_lat, post_long, location_name,getTime()) ? res.json(JSON.parse('{"status":"file uploaded successfuly","error":"null"}')) : res.json(JSON.parse('{"status":"file not uploaded","error":"FileNotUploaded"}'))
				
			}	
		});
}

const setVictimsPostController = (req, res) => {
	PostUpload(req, res, async (err) =>  {
		if (err) {
			return res.end("Error uploading file.");
		}else{
			const post_images = []
				req.files.forEach(element => {
					post_images.push(element.filename)
				});
			const user_id = req.body.user_id;
			const post_desc = req.body.post_desc;
			const needys_items = JSON.parse(req.body.needys_list);
			let location_name = null;
			if(req.body.post_lat){
				const post_lat = req.body.post_lat;
				const post_long = req.body.post_long;
				const address_result = await UseHereMaps(`https://revgeocode.search.hereapi.com/v1/revgeocode?at=${post_lat}%2C${post_long}&lang=en-US`)
				location_name = address_result.items[0].address.county +","+address_result.items[0].address.state
			}
			console.log(needys_items);
			await uploadVictimsPost(user_id, post_desc, post_images,location_name, needys_items,getTime()) ? res.json(JSON.parse('{"status":"file uploaded successfuly","error":"null"}')) : res.json(JSON.parse('{"status":"file not uploaded","error":"FileNotUploaded"}'))
			
		}	
	});
}


const getServicesFeedsController = async (req, res) => {
	const user_lat = req.query.user_lat;
	const user_long = req.query.user_long;
	res.json(JSON.parse(JSON.stringify(await getServicesFeeds(user_lat, user_long))));

}

const getVictimsFeedsController = async (req, res) => {
	res.json(JSON.parse(JSON.stringify(await getVictimsFeed())));

}

const getPostsImages = (req, res) => {
	const imgFile = req.query.imageName;
	res.sendFile(path.join(getRootDir(), "Uploads", "Posts", imgFile));
}

//const result = await upvoteVictimsPost(1, 55);
//const result = await upvoteServicesPost(1, 62);
//const result = await downvoteVictimsPost(1, 55);
//const result = await downvoteServicePost(1, 62);
//const result = await PostComment(1, 62, "helloooooooooooooo", "2021-10-27", "21:16");
//const result = await getPostComment(62);
//const result = await deletePostComment(2, 62);

const updateUpvotesVictimsPostController = async (req, res) => {
	const choice = req.body.choice;
	const user_id = req.body.user_id;
	const v_post_id = req.body.v_post_id;
	if(choice === "upvote"){
		await upvoteVictimsPost(user_id, v_post_id) ? res.json(JSON.parse('{"status":"up voted","error":"null"}')) : res.json(JSON.parse('{"status":"some error occured","error":"transaction not completed"}'))
	}else if(choice === "downvote"){
		
		await downvoteVictimsPost(user_id, v_post_id) ? res.json(JSON.parse('{"status":"down voted","error":"null"}')) : res.json(JSON.parse('{"status":"some error occured","error":"transaction not completed"}'))
	}
}

const updateUpvotesServicesPostController = async (req, res) => {
	const choice = req.body.choice;
	const user_id = req.body.user_id;
	const s_post_id = req.body.s_post_id;
	if(choice == 'upvote'){
		await upvoteServicesPost(user_id, s_post_id) ? res.json(JSON.parse('{"status":"up voted","error":"null"}')) : res.json(JSON.parse('{"status":"some error occured","error":"transaction not completed"}'))
	}else if(choice == 'downvote'){
		
		await downvoteServicePost(user_id, s_post_id) ? res.json(JSON.parse('{"status":"up voted","error":"null"}')) : res.json(JSON.parse('{"status":"some error occured","error":"transaction not completed"}'))
	}
}

const postCommentsController = async (req, res) => {
	const user_id = req.body.user_id;
	const s_post_id = req.body.s_post_id;
	const comment = req.body.comment;
	const date = req.body.date;
	const time = req.body.time;
	await PostComment(user_id, s_post_id, comment, date, time) ? res.json(JSON.parse('{"status":"comments posted","error":"null"}')) : res.json(JSON.parse('{"status":"some error occured","error":"transaction not completed"}'))
}

const getPostCommentsController = async (req, res) => {
	const s_post_id = req.query.s_post_id;
	res.json(JSON.parse(JSON.stringify(await getPostComment(s_post_id))));
}

const deletePostCommentsController = async (req, res) => {
	const s_post_id = req.body.s_post_id;
	const comment_id = req.body.comment_id;
	await deletePostComment(comment_id, s_post_id) ? res.json(JSON.parse('{"status":"comment deleted","error":"null"}')) : res.json(JSON.parse('{"status":"some error occured","error":"transaction not completed"}'))
}






module.exports = {setServicesPostController, getServicesFeedsController, setVictimsPostController, getVictimsFeedsController, getPostsImages, updateUpvotesVictimsPostController, updateUpvotesServicesPostController, postCommentsController, getPostCommentsController, deletePostCommentsController}