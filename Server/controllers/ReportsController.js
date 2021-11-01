const {reportServicePost, reportVictimsPost} = require("../models/ReportsModel");


const reportPostController = async (req, res) => {
	const post_type = req.body.post_type;
	if(post_type == "RFH"){
		const v_post_id = req.body.v_post_id;
		const user_id = req.body.user_id;
		const report_desc = req.body.report_desc;
		await reportVictimsPost(v_post_id, user_id, report_desc) ? res.json(JSON.parse('{"status":"successfuly reported for rfh post","error":"null"}')) : res.json(JSON.parse('{"status":"could not upload for rfh post","error":"error occured"}'))
	}else if(post_type == "SERVICE"){
		const s_post_id = req.body.s_post_id;
		const user_id = req.body.user_id;
		const report_desc = req.body.report_desc;
		await reportServicePost(s_post_id, user_id, report_desc) ? res.json(JSON.parse('{"status":"successfuly reported for service post","error":"null"}')) : res.json(JSON.parse('{"status":"could not upload for service post","error":"error occured"}'))
	}
}

module.exports = {reportPostController}