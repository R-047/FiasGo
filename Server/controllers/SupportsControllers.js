const {supportVictim, getVictimsItems, getPostSupports} = require("../models/SupportsModel")
const {getTime} = require("../Utils/getPostgresTime")

const supportVictimController = async (req, res) => {
	const user_id = req.body.user_id;
	const desc = req.body.desc;
	const post_id = req.body.post_id;
	const list_item_id = req.body.list_item_id;
	const quantity = req.body.quantity;
	await supportVictim(user_id,desc, post_id,list_item_id,quantity,getTime()) ? res.json(JSON.parse('{"status":"supported victim successfuly","error":"null"}')) : res.json(JSON.parse('{"status":"support didnt work","error":"error occured"}'))
}

const getVictimsItemsController = async (req, res) => {
	const v_post_id = req.query.v_post_id
	const result = await getVictimsItems(v_post_id)
	res.json(JSON.parse(JSON.stringify(result)));
}

const getPostSupportsControllers = async (req, res) => {
	const v_post_id = req.query.v_post_id
	const result = await getPostSupports(v_post_id)
	res.json(JSON.parse(JSON.stringify(result)));
}


module.exports = {supportVictimController,getVictimsItemsController,getPostSupportsControllers}