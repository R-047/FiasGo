// select camp_id, camp_location_name, ST_Distance(ST_SetSRID(ST_MakePoint(12.3375, 75.8069), 4326), camps.camp_location) as distance_from_coorg from camps where ST_Distance(ST_SetSRID(ST_MakePoint(12.3375, 75.8069), 4326), camps.camp_location) < 1000;
// insert into camps (camp_name,id_num,closing_date,active_timings,images,host_id,description,camp_location) values('test_camp1', 1234567890, '2021-10-23', '10:36', null, 1, null, 'point(-81 30)');
const {getClient} = require('../Utils/DBConfig');

const getCampsModel = (user_lat, user_long) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select camps.*, users.user_name, ST_Distance(ST_SetSRID(ST_MakePoint(${user_lat}, ${user_long}), 4326), camps.camp_location) as distance_from_user from camps join users on (camps.host_id = users.user_id) where ST_Distance(ST_SetSRID(ST_MakePoint(${user_lat}, ${user_long}), 4326), camps.camp_location) < 30000`);
		let final_result = [];
		for(const element of result.rows){
			const camp_id = element.camp_id;
			const images_result = await client.query(`select camp_image_id, camp_image_name from camp_images group by camp_image_id having camp_id = ${camp_id};`);
			element.images = images_result.rows;
			final_result.push(element);
		}
		await client.end();
		res(final_result)		
	})
}


const postCampsModel = (camp_name, person_gov_id, openin_date, closing_date, time_from, time_till, user_id, desc, camp_lat, camp_long, camp_location_name,contact,camp_images_names) => {
	return new Promise(async(res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('BEGIN');
		console.log("started trans")
		console.log("inserting camp")
		await client.query(`insert into camps (camp_name,id_num,closing_date,host_id,description,camp_location,camp_location_name,opening_date,available_from,available_till,contact,camp_lat,camp_long) values('${camp_name}', ${person_gov_id}, '${closing_date}', ${user_id}, '${desc}', 'point(${camp_lat} ${camp_long})', '${camp_location_name}', '${openin_date}', '${time_from}', '${time_till}', '${contact}',${camp_lat}, ${camp_long})`);
		console.log("getting the camp_id")
		const result = await client.query(`SELECT camp_id FROM camps WHERE host_id=${user_id} ORDER BY camp_id DESC lIMIT 1`);
		console.log("inserting camp images")
		const camp_id = result.rows[0].camp_id;
		for(const element of camp_images_names){
			await client.query(`insert into camp_images(camp_id,camp_image_name) values(${camp_id},'${element}')`);
		}
		await client.query('COMMIT');
		console.log("commited trans")
		res(true)
		} catch (error) {
			console.log("rolling back"+error)
			await client.query("ROLLBACK");
			res(false)
		} finally {
			await client.end();
		}		
	})
}

const takeDownCamp = (camp_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`delete from camps where camp_id = ${camp_id}`);
		await client.end();
		res(result)		
	})
}

const getCampInfo = (camp_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select camps.*, users.user_name, users.user_email, users.ph_num from camps join users on camps.host_id = users.user_id where camps.camp_id = ${camp_id}`);
		const images_result = await client.query(`select camp_image_id, camp_image_name from camp_images group by camp_image_id having camp_id = ${camp_id}`);
		if(result.rows[0]){
			result.rows[0].images_result = images_result.rows;
			await client.end();
		}
		
		res(result)		
	})
}

// select * from camps where host_id = 1;
const getUsersCamps = (host_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select * from camps where host_id = ${host_id}`);
		let final_result = [];
		for(const element of result.rows){
			const camp_id = element.camp_id;
			const images_result = await client.query(`select camp_image_id, camp_image_name from camp_images group by camp_image_id having camp_id = ${camp_id};`);
			element.images = images_result.rows;
			final_result.push(element);
		}
		await client.end();
		res(final_result)		
	})
}

module.exports = {getCampsModel, postCampsModel, takeDownCamp, getCampInfo, getUsersCamps};