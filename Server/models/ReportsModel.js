const {getClient} = require('../Utils/DBConfig');


// victim_post_report_id | v_post_id | user_id | report_desc
// insert into victims_post_reports(v_post_id, user_id, report_desc) values(${v_post_id}, ${user_id}, '${report_desc}')
const reportVictimsPost = async(v_post_id, user_id, report_desc) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("inserting reports")
		await client.query(`insert into victims_post_reports(v_post_id, user_id, report_desc) values(${v_post_id}, ${user_id}, '${report_desc}')`);
		console.log("updating reports count")
		await client.query(`update victims_post set v_reports_count = (select v_reports_count from victims_post where v_post_id = ${v_post_id}) + 1 where v_post_id = ${v_post_id}`);
		await client.query('commit');
		console.log("commited trans")
		res(true)
		} catch (error) {
			console.log("rolling back"+error)
			await client.query("rollback");
			res(false)
		} finally {
			await client.end();
		}			
	})
}

// insert into service_post_reports(s_post_id, user_id, report_desc) values(62, 1, 'not good')
const reportServicePost = async(s_post_id, user_id, report_desc) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("inserting reports")
		await client.query(`insert into service_post_reports(s_post_id, user_id, report_desc) values(${s_post_id}, ${user_id}, '${report_desc}')`);
		console.log("updating reports count")
		await client.query(`update service_post set s_reports_count = (select s_reports_count from service_post where s_post_id = ${s_post_id} ) + 1 where s_post_id = ${s_post_id};`);
		await client.query('commit');
		console.log("commited trans")
		res(true)
		} catch (error) {
			console.log("rolling back"+error)
			await client.query("rollback");
			res(false)
		} finally {
			await client.end();
		}			
	})
}


// select * from victims_post_reports where v_post_id = 55;
const getReportsVictimsPost = async(v_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select victims_post_reports.*, users.user_name, users.user_email from victims_post_reports join users using(user_id) where v_post_id = ${v_post_id}`);
		await client.end();
		res(result)		
	})
}


// select * from service_post_reports where s_post_id = 62;
const getReportsServicesPost = async(s_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select service_post_reports.*, users.user_name, users.user_email from service_post_reports join users using(user_id) where s_post_id = ${s_post_id}`);
		await client.end();
		res(result)		
	})
}




module.exports = {reportVictimsPost,reportServicePost,getReportsVictimsPost,getReportsServicesPost}